package com.example.aljabermall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.aljabermall.adapters.AddressAdapter
import com.example.aljabermall.databinding.ActivityAddressCreationBinding
import com.example.aljabermall.fragments.ProgressDialogFragment
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.isInputEmpty
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.LocationModel
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.LocationsViewModel
import java.util.*

const val IS_ADDRESS_UPDATE_KEY = "is_update"

class AddressCreationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddressCreationBinding
    private val locationVM by viewModels<LocationsViewModel>()
    private var latitude: String? = null
    private var longitude: String? = null
    private var locationName = ""
    private var selectedLocationId = 0
    private var selectedLocationName = ""
    private var isAddressUpdate = false
    private var updateAddressId = 0
    private var progressDialog = ProgressDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.title = getString(R.string.add_location)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isAddressUpdate = intent.extras?.getBoolean(IS_ADDRESS_UPDATE_KEY) == true
        HelperUtils.setDefaultLanguage(this,"ar")

//        binding.cityLocation.inputType = InputType.TYPE_NULL

        locationVM.getAddLocationMessage().observe(this) { result ->
            binding.progressSaveLocation.hide()
            binding.saveLocation.show()
            when (result) {
                is NetworkResults.Success -> {
                    locationVM.retrieveUserLocations()
                    binding.progressLocation.show()
                    clearFields()
                }
                is NetworkResults.Error -> {
                    result.exception.toString()
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            }
        }

        locationVM.getUserLocations().observe(this) { result ->
            binding.progressLocation.hide()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        setupLocations(result.data.user_locations)
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

        locationVM.retrieveUserLocations()

        if (isAddressUpdate) {
            binding.continueShopping.hide()
            subscribeUpdateLiveData()
        }
        binding.saveLocation.setOnClickListener(this)
        binding.selectLocationMap.setOnClickListener(this)
        binding.continueShopping.setOnClickListener(this)
    }

    private fun clearFields() {
        binding.streetLocation.text.clear()
//        binding.apartmentLocation.text.clear()
        binding.buildingLocation.text.clear()
        binding.areaLocation.text.clear()
        binding.cityLocation.text.clear()
        locationName = ""
        longitude = ""
        latitude = ""
        updateAddressId = 0
    }

    private fun subscribeUpdateLiveData() {
        locationVM.getUpdateLocationMessage().observe(this) { result ->
            binding.saveLocation.show()
            binding.progressSaveLocation.hide()
            when (result) {
                is NetworkResults.Success -> {
                    Toast.makeText(this, result.data.msg, Toast.LENGTH_SHORT).show()
                    if (result.data.status == 1) {
                        clearFields()
                        locationVM.retrieveUserLocations()
                    }
                }
                is NetworkResults.Error -> {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
                    val error = result.exception
                    error.printStackTrace()
                }
            }
        }

        locationVM.getDeleteLocationMessage().observe(this) { result ->
            if (progressDialog.isAdded)
                progressDialog.dismiss()
            when (result) {
                is NetworkResults.Success -> {
//                    Toast.makeText(this, result.data.msg, Toast.LENGTH_SHORT).show()
                    if (result.data.status == 1) {
                        locationVM.retrieveUserLocations()
                        binding.addressesRecycler.adapter = null
                    }
                }
                is NetworkResults.Error -> {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
                    val error = result.exception
                    error.printStackTrace()
                }
            }
        }
    }
    private fun setupLocations(userLocations: List<LocationModel>) {
        val addressAdapter = AddressAdapter(
            userLocations,
            object : AddressAdapter.OnAddressListener {
                override fun updateAddress(position: Int) {
                    val address = userLocations[position]
                    binding.cityLocation.setText(address.fullAddress.locality)
                    binding.areaLocation.setText(address.fullAddress.address_line1)
                    binding.buildingLocation.setText(address.fullAddress.given_name)
                    binding.streetLocation.setText(address.fullAddress.family_name)
                    updateAddressId = address.user_id
                }

                override fun deleteAddress(position: Int) {
                    progressDialog.show(supportFragmentManager, "address_activity")
                    val locationId = userLocations[position].user_id
                    locationVM.deleteUserLocation(locationId.toString(),"1")
                }

                override fun selectAddress(position: Int) {
                    val model = userLocations[position]
                    selectedLocationName = model.fullAddress.toString()
                    selectedLocationId = model.user_id
                }

            }, isAddressUpdate, applicationContext)
        binding.addressesRecycler.show()
        binding.addressesRecycler.adapter = addressAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private val mapLocationResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                //val city = result.data?.getStringExtra("city")
                val street = result.data?.getStringExtra("street")
                latitude = result.data?.getStringExtra("latitude")!!
                longitude = result.data?.getStringExtra("longitude")!!
                locationName = result.data?.getStringExtra("location_name")!!
                //binding.cityLocation.setText(city)
                binding.streetLocation.setText(street)
            }
        }

    private fun isInputValid(): Boolean {
        var status = true

        if (binding.cityLocation.isInputEmpty()) {
            status = false
            binding.cityLocation.error = getString(R.string.required)
        }

        if (binding.areaLocation.isInputEmpty()) {
            status = false
            binding.areaLocation.error = getString(R.string.required)
        }

        if (binding.buildingLocation.isInputEmpty()) {
            status = false
            binding.buildingLocation.error = getString(R.string.required)
        }

//        if (binding.apartmentLocation.isInputEmpty()) {
//            status = false
//            binding.apartmentLocation.error = getString(R.string.required)
//        }

        if (binding.streetLocation.isInputEmpty()) {
            status = false
            binding.streetLocation.error = getString(R.string.required)
        }

/*        if (latitude.isEmpty() && longitude.isEmpty()) {
            status = false
            Toast.makeText(
                this,
                getString(R.string.select_location_message), Toast.LENGTH_LONG
            ).show()
        }*/

        return status
    }

//    Click
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.save_location -> {
//                HelperUtils.hideKeyBoard(this)
                if (isInputValid()) {
                    binding.saveLocation.hide()
                    binding.progressSaveLocation.show()
                    if (updateAddressId != 0) {
                        locationVM.updateUserLocation(
                           HelperUtils.getUID(applicationContext),
                            updateAddressId.toString(),
                            binding.cityLocation.text.toString(),
                            binding.areaLocation.text.toString(),
                            binding.buildingLocation.text.toString(),
                            binding.streetLocation.text.toString(),
                            language = "ar"
                        )

                    } else {
                        locationVM.addUserLocation(
                            language = "ar",
                            HelperUtils.getUID(applicationContext),
//                            locationName,
                            binding.cityLocation.text.toString(),
                            binding.areaLocation.text.toString(),
                            binding.streetLocation.text.toString(),
                            binding.buildingLocation.text.toString()
//                            binding.apartmentLocation.text.toString()
                        )
                    }
                }
            }
            R.id.select_location_map -> {
                val intentMap = Intent(this, MapsActivity::class.java)
                mapLocationResults.launch(intentMap)
            }
            R.id.continue_shopping -> {
                val intentResult = Intent()
                intentResult.putExtra("location_id", selectedLocationId.toString())
                intentResult.putExtra("location_name", selectedLocationName)
                setResult(RESULT_OK, intentResult)
                finish()
            }
        }
    }




//    this mathod call while we need to use fragment
    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}