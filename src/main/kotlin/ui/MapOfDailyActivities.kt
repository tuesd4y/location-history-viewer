import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.geotools.data.shapefile.ShapefileDataStore
import org.geotools.map.FeatureLayer
import org.geotools.map.MapContent
import org.geotools.styling.SLD
import org.geotools.swing.JMapPane
import org.geotools.swing.tool.PanTool
import org.geotools.swing.tool.ZoomInTool
import org.geotools.swing.tool.ZoomOutTool
import java.time.LocalDate
import javax.swing.BoxLayout
import javax.swing.JPanel

@Composable
fun MapOfDailyActivities(selectedDay: LocalDate?) {
    if (selectedDay == null) {
        Text(LoadedData.locationsOnSelectedDay?.toString() ?: "No date selected!")
    } else {
        ActivityMap(selectedDay)
    }
}

@Composable
fun ActivityMap(selectedDay: LocalDate) {
    Text("Showing ${LoadedData.locationsOnSelectedDay?.size} Locations visited on $selectedDay")

    val map = MapContent()
    map.title = selectedDay.toString()
    val mapPane = JMapPane(map)
    mapPane.background = java.awt.Color.LIGHT_GRAY

    val featureDataStore = LoadedData.dailyDataStore(selectedDay)
    val source = featureDataStore.getFeatureSource("visited-$selectedDay")

    val worldStore =
        ShapefileDataStore(LoadedData.javaClass.getResource("/world-administrative-boundaries/world-administrative-boundaries.shp"))
    val worldSource = worldStore.featureSource
    val worldStyle = SLD.createPolygonStyle(java.awt.Color.DARK_GRAY, null, .8f)
    val worldLayer = FeatureLayer(worldSource, worldStyle)

    val style = SLD.createPointStyle("square", java.awt.Color.RED, java.awt.Color.RED, 1.0f, 8.0f, "time", null)
    val layer = FeatureLayer(source, style)
    mapPane.mapContent.addLayer(layer)
    mapPane.mapContent.addLayer(worldLayer)

    val panTool = PanTool()
    val zoomInTool = ZoomInTool()
    val zoomOutTool = ZoomOutTool()

    mapPane.cursorTool = panTool

    Row {
        Button({ mapPane.cursorTool = panTool }, modifier = Modifier.padding(4.dp)) { Text("Pan") }
        Button({ mapPane.cursorTool = zoomInTool }, modifier = Modifier.padding(4.dp)) { Text("Zoom In") }
        Button({ mapPane.cursorTool = zoomOutTool }, modifier = Modifier.padding(4.dp)) { Text("Zoom Out") }
    }
    SwingPanel(
        modifier = Modifier.padding(20.dp).fillMaxSize(),
        background = Color.White,
        factory = {
            JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(mapPane)
            }
        }
    )
}