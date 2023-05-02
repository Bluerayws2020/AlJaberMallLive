package com.example.aljabermall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.aljabermall.databinding.ActivityHomeBinding
import com.example.aljabermall.fragments.BRANCH_DIALOG
import com.example.aljabermall.fragments.BRANCH_ID
import com.example.aljabermall.fragments.BranchesDialog
import com.example.aljabermall.fragments.CANCELABLE
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.BranchViewModel
import com.example.aljabermall.viewmodels.CartViewModel
import com.example.aljabermall.viewmodels.ProfileViewModel
import com.google.android.material.navigation.NavigationView
import java.util.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val branchViewModel by viewModels<BranchViewModel>()
    private val cartVM by viewModels<CartViewModel>()
    private val profileVM by viewModels<ProfileViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")

if (HelperUtils.getUID(this) == "0"){

}else {
    getUserInfo()

}


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        NavigationUI.setupWithNavController(binding.navMenu, navController)

        binding.navMenu.setNavigationItemSelectedListener(this)

        //check if user is guest to open login activity
        if (HelperUtils.getUID(this) == "0") {
            binding.bottomNavigationView.menu.findItem(R.id.favouriteFragment)
                .setOnMenuItemClickListener {
                    HelperUtils.openLoginActivity(this)
                    return@setOnMenuItemClickListener true
                }
            binding.bottomNavigationView.menu.findItem(R.id.cartFragment)
                .setOnMenuItemClickListener {
                    HelperUtils.openLoginActivity(this)
                    return@setOnMenuItemClickListener true
                }
            binding.navMenu.menu.findItem(R.id.logout).isVisible = true

        }


        cartVM.getCart().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.msg .isNullOrEmpty()) {
                        showBranchesDialog(isCancelEnabled = false)
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }
        cartVM.retrieveCart()

//        branchViewModel.getUpdateBranchesResponse().observe(this) { result ->
//            when (result) {
//                is NetworkResults.Success -> {
//                    if (result.data.status == 1) {
////                        val branchId = result.data.user.user_branch_id
////                        saveBranchId(branchId)
////                        navController.popBackStack()
////                        navController.navigate(R.id.homeFragment)
//
//                    } else
//                        showClearCartDialog()
//                }
//                is NetworkResults.Error -> {
//                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
//                    result.exception.printStackTrace()
//                }
//            }
//        }
    }

    private fun showBranchesDialog(isCancelEnabled: Boolean) {
        val branchesDialog = BranchesDialog()
        val bundleArg = Bundle()
        bundleArg.putBoolean(CANCELABLE, isCancelEnabled)
        branchesDialog.arguments = bundleArg
        branchesDialog.show(supportFragmentManager, "DIALOG")
        branchesDialog.setFragmentResultListener(BRANCH_DIALOG) { _, bundle ->
            val branchId = bundle.getInt(BRANCH_ID)
            branchViewModel.updateBranches(branchId.toString())
        }
    }

    private fun saveBranchId(branchId: Int) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putInt("branch_id", branchId)
        }.apply()
    }

    private fun showClearCartDialog() {
        val builderDialog = AlertDialog.Builder(this)
        builderDialog.setMessage(getString(R.string.clear_cart_message))
        builderDialog.setPositiveButton(getString(R.string.open_cart)) { dialog, _ ->
            dialog.dismiss()
            navController.navigate(R.id.cartFragment)
        }
        builderDialog.setNegativeButton(getString(R.string.close)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialogCart = builderDialog.create()
        dialogCart.show()
    }

    fun openDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {

            R.id.profile -> {
                if (!HelperUtils.isGuest(this)) {
                    val profileIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(profileIntent)
                }
                return true
            }
            R.id.about_us -> {
//                val aboutUrl = if (HelperUtils.getLang(this) == "en")
//                    HelperUtils.BASE_URL_EN + HelperUtils.ABOUT_US_URL
//                else
//                    HelperUtils.BASE_URL_AR + HelperUtils.ABOUT_US_URL
//                val intentWeb = Intent(this, InfoWebActivity::class.java)
//                val bundle = Bundle()
//                bundle.putString("web_url", aboutUrl)
//                bundle.putString("title", getString(R.string.about_us))
//                intentWeb.putExtras(bundle)
//                startActivity(intentWeb)


                val about_us = Intent(this, AboutUs::class.java)
                startActivity(about_us)
                return true
            }
            R.id.contact_us -> {
//                val aboutUrl = if (HelperUtils.getLang(this) == "en")
//                    HelperUtils.BASE_URL_EN + HelperUtils.CONTACT_US_URL
//                else
//                    HelperUtils.BASE_URL_AR + HelperUtils.CONTACT_US_URL
//                val intentWeb = Intent(this, InfoWebActivity::class.java)
//                val bundle = Bundle()
//                bundle.putString("web_url", aboutUrl)
//                bundle.putString("title", getString(R.string.contact_us))
//                intentWeb.putExtras(bundle)
//                startActivity(intentWeb)
                val contaact_us = Intent(this, ContactUs::class.java)
                startActivity(contaact_us)
                return true
            }
            R.id.language -> {
//                val languageIntent = Intent(this, LanguageActivity::class.java)
//                startActivity(languageIntent)

                Toast.makeText(applicationContext, "سوف يتم توّفر اللغة الإنجليزية لاحقًا", Toast.LENGTH_SHORT).show()
                return true
            }
//            R.id.change_branch -> {
//                showBranchesDialog(isCancelEnabled = true)
//                return true
//            }
            R.id.logout -> {
                logout()
                return true
            }
            R.id.my_orders -> {
                if (!HelperUtils.isGuest(this)) {
                    val intentOrders = Intent(this, OrdersActivity::class.java)
                    startActivity(intentOrders)
                }
                return true
            }
            R.id.saved_address -> {
                if (!HelperUtils.isGuest(this)) {
                    val intentAddress = Intent(this, AddressCreationActivity::class.java)
                    val bundle = Bundle()
                    bundle.putBoolean(IS_ADDRESS_UPDATE_KEY, true)
                    intentAddress.putExtras(bundle)
                    startActivity(intentAddress)
                }
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        val intentSplash = Intent(this, MainActivity::class.java)
        startActivity(intentSplash)
        finish()
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }



    private fun getUserInfo(){

        profileVM.getUserInfo().observe(this) { result ->

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {

                        val navHeader = binding.navMenu.getHeaderView(0)
                        val homeId = navHeader.findViewById<TextView>(R.id.name)
                        homeId.text = result.data.userProfile.user_name.toString()
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}