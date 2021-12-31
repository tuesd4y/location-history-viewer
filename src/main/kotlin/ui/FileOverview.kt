import androidx.compose.runtime.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import data.LocationHistoryTakeout
import data.toLocalDateTime
import okio.buffer
import okio.source
import java.io.File
import java.time.LocalDate
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Composable
fun FileOverview(file: File, onFileError: () -> Unit) {
    var selectedDayIndex by remember { mutableStateOf(0) }
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }

    val loadSuccessful = loadLocationHistory(file)
    if (!loadSuccessful) onFileError()

    if (LoadedData.locationHistory != null) {
        val locationsPerDay = LoadedData.locationHistory!!
            .locations
            .groupingBy { it.timestampMs.toLocalDateTime().toLocalDate() }
            .eachCount()

        selectedDay = locationsPerDay.keys.toList()[selectedDayIndex]
        LoadedData.locationsOnSelectedDay = LoadedData.locationHistory?.locations?.filter {
            it.timestampMs.toLocalDateTime().toLocalDate() == selectedDay
        }

        DaySelection(locationsPerDay.map { (day, count) -> "$day - $count locations logged" }) {
            selectedDayIndex = it
        }
    }


    if (LoadedData.locationsOnSelectedDay != null) {
        MapOfDailyActivities(selectedDay)
    }
}

const val locationHistoryFile = "Takeout/Location History/Location History.json"

val moshi: Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()
val adapter = moshi.adapter(LocationHistoryTakeout::class.java)

fun loadLocationHistory(file: File): Boolean {
    ZipInputStream(file.inputStream()).use { zis ->

        var entry: ZipEntry? = zis.nextEntry
        // find entry with location_history file
        while (entry != null) {
            if (entry.name == locationHistoryFile)
                break
            entry = zis.nextEntry
        }

        if (entry != null) {
            val data = adapter.fromJson(zis.source().buffer())
            LoadedData.locationHistory = data
            zis.closeEntry()
            return true
        }

        // file not found
        zis.closeEntry()
    }
    return false
}