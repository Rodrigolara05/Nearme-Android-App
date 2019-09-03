package resasoftware.com.pe.nearme.controllers.fragments


import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import resasoftware.com.pe.nearme.R
import resasoftware.com.pe.nearme.controllers.activities.EnterpriseDetailsActivity
import resasoftware.com.pe.nearme.models.Enterprise
import resasoftware.com.pe.nearme.models.Preferences
import resasoftware.com.pe.nearme.models.User
import resasoftware.com.pe.nearme.network.NearmeApi
import com.google.android.gms.maps.model.Marker



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

var map: String = ""
var range: Int = 0

/**
 * A simple [Fragment] subclass.
 *
 */

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MapFragment() : Fragment(), OnMapReadyCallback, LocationListener {

    var user: User = User()

    internal var TAG = "Near Me Maps:"
    private var gMap: GoogleMap? = null

    private lateinit var locationManager: LocationManager
    private var latitud = 0.0
    private var longitud = 0.0
    private val zoom = 15f

    private var location: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onResume() {
        super.onResume()
        loadPreference()
        activity?.intent?.extras?.apply {
            user = getSerializable("user") as User
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapViewFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    fun changeMapStyle(){
        var style = R.raw.mapnormalstyle
        if(map=="Normal") {
            style=R.raw.mapnormalstyle
        }
        else {
            if (map=="Satelital"){ //Nigth fake
                style=R.raw.mapnightstyle
            }
        }
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = gMap!!.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, style
                )
            )
            if (!success) {
                Log.e(TAG, "Maps Fragment,Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Maps Fragment,Can't find style. Error: ", e)
        }
        val lima = LatLng(-12.0431800, -77.0282400)
        gMap!!.moveCamera(CameraUpdateFactory.newLatLng(lima))

    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Add a marker in Sydney, Australia, and move the camera.

        NearmeApi.getEnterprises(
            {
                it as ArrayList<Enterprise>
                for(item in it){
                    if(Preferences.ExitsPreferences(item.categoryId.name)) {
                        val point = LatLng(item.latitude.toDouble(), item.longitude.toDouble())
                        gMap!!.addMarker(MarkerOptions().position(point).title(item.name).snippet(item.id.toString()))
                    }
                }
            },{
                Log.d("Nerame Api Error: ", it.message)
            },
            getString(R.string.nearme_api_key)
        )

        changeMapStyle()

        locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                activity!!.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "No hay permisos de geolocalizacion")
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        } else {

            location = locationManager.getLastKnownLocation(NETWORK_PROVIDER)
            if (location != null) {

                latitud = location!!.latitude
                longitud = location!!.longitude
                Log.d(TAG, "Las coordenadas actuales son :: Lat:$latitud Lon:$longitud")
            } else {
                Log.e(TAG, "Location is null")
                return
            }

        }


        goToLocationInMap(latitud, longitud, zoom)
        val uiSettings = gMap!!.uiSettings
        uiSettings.isZoomControlsEnabled = true


       gMap!!.setOnMarkerClickListener {

           val intent = Intent(this.activity, EnterpriseDetailsActivity::class.java)
           var id = it.snippet.toInt()
           intent.putExtra("enterpriceID", id)
           intent.putExtra("user", user)
           startActivity(intent)
            false
        }
    }

    private fun goToLocationInMap(lat: Double, lng: Double, zoom: Float) {

        val latitudLongitud = LatLng(lat, lng)
        drawUserInMap(latitudLongitud)
        val update = CameraUpdateFactory.newLatLngZoom(latitudLongitud, zoom)
        gMap?.moveCamera(update)

    }
    private fun drawUserInMap(point: LatLng) {
        // Creating an instance of MarkerOptions and define a icon

        val markerOptions = MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.perfil))
        // Setting latitude and longitude for the marker
        markerOptions.position(point)
        // Adding marker on the Google Map
        gMap?.addMarker(markerOptions)
    }

    override fun onLocationChanged(location: Location) {

    }

    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

    }

    override fun onProviderEnabled(s: String) {

    }

    override fun onProviderDisabled(s: String) {

    }

    private fun loadPreference(){
        val preference: SharedPreferences? = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)

        range = preference?.getInt("range",5) as Int
        map = preference?.getString("mapStyle","Normal") as String

    }
}
