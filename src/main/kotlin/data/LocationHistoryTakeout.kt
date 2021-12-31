package data

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

data class LocationHistoryTakeout(val locations: List<Location>)

data class Location(
    val timestampMs: String,
    val latitudeE7: Long,
    val longitudeE7: Long,
    val accuracy: Long,
    val altitude: Long? = null,
    val verticalAccuracy: Long? = null,
    val source: Source?,
    val deviceTag: Long,
    val platformType: PlatformType? = null,
    val activity: List<LocationActivity>? = null,
    val locationMetadata: List<LocationMetadatum>? = null
)

fun String.toLocalDateTime(): LocalDateTime = Instant
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
    IN_CAR,
    IN_BUS,
    ON_FOOT,
    RUNNING,
    STILL,
    TILTING,
    UNKNOWN,
    WALKING,
    IN_RAIL_VEHICLE,
    IN_ROAD_VEHICLE,
    IN_TWO_WHEELER_VEHICLE,
    IN_FOUR_WHEELER_VEHICLE
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
    WIFI,
    UNKNOWN;
}