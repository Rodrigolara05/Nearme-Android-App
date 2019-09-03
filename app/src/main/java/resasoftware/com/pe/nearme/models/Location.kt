package resasoftware.com.pe.nearme.models

class Location(
    private var id_location: Int,
    private var latitude: Float,
    private var longitude: Float,
    private var id_store: Int
) {

    fun Location(id_location: Int, latitude: Float, longitude: Float, id_store: Int) {
        this.id_location = id_location
        this.latitude = latitude
        this.longitude = longitude
        this.id_store = id_store
    }
}