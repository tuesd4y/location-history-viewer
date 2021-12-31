package data

import java.time.Instant
import java.time.ZoneOffset

data class LocationHistoryTakeout(val locations: List<Location>)

data class Location(
    val timestampMs: String,
    val latitudeE7: Long,
    val longitudeE7: Long,
    val accuracy: Long,
    val altitude: Long? = null,
    val verticalAccuracy: Long? = null,
    val source: Source,
    val deviceTag: Long,
    val platformType: PlatformType? = null,
    val activity: List<LocationActivity>? = null,
    val locationMetadata: List<LocationMetadatum>? = null
)

fun String.toLocalDateTime() = Instant
    .ofEpochMilli(toLong())
    .atOffset(ZoneOffset.UTC)
    .toLocalDateTime()

fun Long.toRealCoords() = toDouble() / 1e7

data class LocationActivity(
    val timestampMs: String,

    val activity: List<ActivityActivity>
)

data class ActivityActivity(
    val type: Type,
    val confidence: Long
)

enum class Type {
    EXITING_VEHICLE,
    IN_VEHICLE,
    ON_BICYCLE,
    ON_FOOT,
    RUNNING,
    STILL,
    TILTING,
    UNKNOWN,
    WALKING;
}

data class LocationMetadatum(
    val timestampMs: String,

    val wifiScan: WifiScan
)

data class WifiScan(
    val accessPoints: List<AccessPoint>
)

data class AccessPoint(
    val mac: String,
    val strength: Long
)

enum class PlatformType {
    ANDROID
}

enum class Source {
    CELL,
    GPS,
    WIFI;
}