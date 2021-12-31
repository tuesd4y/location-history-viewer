import data.Location
import data.LocationHistoryTakeout
import data.toLocalDateTime
import data.toRealCoords
import org.geotools.data.FeatureReader
import org.geotools.data.Query
import org.geotools.data.store.ContentDataStore
import org.geotools.data.store.ContentEntry
import org.geotools.data.store.ContentFeatureSource
import org.geotools.data.store.ContentState
import org.geotools.feature.NameImpl
import org.geotools.feature.simple.SimpleFeatureBuilder
import org.geotools.feature.simple.SimpleFeatureTypeBuilder
import org.geotools.geometry.jts.JTSFactoryFinder
import org.geotools.geometry.jts.ReferencedEnvelope
import org.geotools.referencing.crs.DefaultGeographicCRS
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Point
import org.opengis.feature.simple.SimpleFeature
import org.opengis.feature.simple.SimpleFeatureType
import org.opengis.feature.type.Name
import java.time.LocalDate

enum class UiState {
    FileSelection,
    FileViewing
}

object LoadedData {
    fun dailyDataStore(day: LocalDate): ContentDataStore {
        return LocationListDataStore(day)
    }

    var locationHistory: LocationHistoryTakeout? = null

    var locationsOnSelectedDay: List<Location>? = null
}

class LocationListDataStore(private val day: LocalDate) : ContentDataStore() {
    override fun createTypeNames(): List<Name> {
        return listOf(NameImpl("visited-$day"))
    }

    override fun createFeatureSource(entry: ContentEntry): ContentFeatureSource {
        return LocationListFeatureSource(entry, Query.ALL)
    }
}

class LocationListFeatureSource(entry: ContentEntry, query: Query) : ContentFeatureSource(
    entry,
    query
) {
    override fun getBoundsInternal(query: Query): ReferencedEnvelope {
        return ReferencedEnvelope(
            LoadedData.locationsOnSelectedDay?.minOf { it.longitudeE7.toRealCoords() } ?: 0.0,
            LoadedData.locationsOnSelectedDay?.maxOf { it.longitudeE7.toRealCoords() } ?: 0.0,
            LoadedData.locationsOnSelectedDay?.minOf { it.latitudeE7.toRealCoords() } ?: 0.0,
            LoadedData.locationsOnSelectedDay?.maxOf { it.latitudeE7.toRealCoords() } ?: 0.0,
            DefaultGeographicCRS.WGS84
        )
    }

    override fun getCountInternal(query: Query): Int {
        return LoadedData.locationsOnSelectedDay?.size ?: -1
    }

    override fun getReaderInternal(query: Query): FeatureReader<SimpleFeatureType, SimpleFeature> {
        return LocationListFeatureReader(state, query)
    }

    override fun buildFeatureType(): SimpleFeatureType {
        return SimpleFeatureTypeBuilder()
            .crs(DefaultGeographicCRS.WGS84)
            .apply {
                add("Location", Point::class.java)
                add("time", String::class.java)
                name = entry.name.toString()
            }
            .buildFeatureType()
    }
}

class LocationListFeatureReader(contentState: ContentState, query: Query) :
    FeatureReader<SimpleFeatureType, SimpleFeature> {
    private val builder = SimpleFeatureBuilder(contentState.featureType)
    private val geomFactory = JTSFactoryFinder.getGeometryFactory(null)

    private var i = 0

    override fun close() {}

    override fun getFeatureType(): SimpleFeatureType {
        return builder.featureType
    }

    override fun next(): SimpleFeature? {
        return LoadedData.locationsOnSelectedDay?.get(i++)?.let { toFeature(it) }
    }

    override fun hasNext(): Boolean = i < (LoadedData.locationsOnSelectedDay?.size ?: -1)

    private fun toFeature(location: Location): SimpleFeature {
        builder.set("Location", geomFactory.createPoint(Coordinate().apply {
            x = location.longitudeE7.toRealCoords()
            y = location.latitudeE7.toRealCoords()
        }))
        builder.set("time", location.timestampMs.toLocalDateTime().toString())
        return builder.buildFeature(location.timestampMs)
    }
}
