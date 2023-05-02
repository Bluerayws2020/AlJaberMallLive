package com.example.aljabermall

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.aljabermall.databinding.ActivityMapsBinding
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.*

private const val SETTING_CODE = 100

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
    GoogleMap.OnMyLocationButtonClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationRequest: LocationRequest
    private var latitude = ""
    private var longitude = ""
    private var locationName: String? = null
    private var city: String? = null
    private var street: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")

        supportActionBar?.title = getString(R.string.location)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationSettings()

        binding.selectLocation.setOnClickListener {
            if (latitude.isEmpty() || longitude.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.select_location),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val intentResult = Intent()
                intentResult.putExtra("latitude", latitude)
                intentResult.putExtra("longitude", longitude)
                intentResult.putExtra("location_name", locationName)
                intentResult.putExtra("city", city)
                intentResult.putExtra("street", street)
                setResult(RESULT_OK, intentResult)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        //mMap.setOnMyLocationButtonClickListener(this)
        //enable user location view
        val ammanLocation = LatLng(31.8360368, 35.6674341)
        mMap.addMarker(MarkerOptions().position(ammanLocation).title("Amman"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ammanLocation))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)

        enableUserLocationUI()
    }

    // Request location permissions
    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isEnabled = false
            permissions.entries.forEach {
                isEnabled = it.value
            }
            if (isEnabled) {
                enableUserLocationUI()
            }
        }

    private fun enableUserLocationUI() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
    }


    private fun locationSettings() {
        locationRequest = LocationRequest.create()
        locationRequest.apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 2 * 1000
            fastestInterval = 1 * 1000
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {

        }

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(this, SETTING_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getAddress(lat: Double, lng: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            val obj = addresses[0]
            val address = obj.getAddressLine(0)
            city = obj.locality
            street = obj.thoroughfare
            locationName = address
            binding.locationName.text = address
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapClick(p0: LatLng) {
        latitude = p0.latitude.toString()
        longitude = p0.longitude.toString()
        getAddress(p0.latitude, p0.longitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0).title("Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p0))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)

    }

    override fun onMyLocationButtonClick(): Boolean {
        //get current location
        locationSettings()
        return false
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}