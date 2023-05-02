package com.example.aljabermall

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.aljabermall.databinding.ActivityAboutUsBinding
import com.example.aljabermall.databinding.ActivityCategoriesBinding
import com.example.aljabermall.databinding.FragmentContactUsBinding
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.AlJaberViewModel
import com.example.aljabermall.viewmodels.ProductsViewModel
import java.util.*

class AboutUs : AppCompatActivity() {


    private lateinit var binding: ActivityAboutUsBinding
    private val alJaberViewModel by viewModels<AlJaberViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.title = getString(R.string.about_us)

        binding.progressAboutUs.show()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (!HelperUtils.isNetWorkAvailable(this)) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
        }
        HelperUtils.setDefaultLanguage(this,"ar")

alJaberViewModel.retriveAboutus()

        alJaberViewModel.getLiveAboutUS().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status.status == 1) {
                        Toast.makeText(this, result.data.status.msg, Toast.LENGTH_LONG).show()
                        binding.aboutTv.text = result.data.data.body

                    } else {
                        Toast.makeText(this, result.data.status.msg, Toast.LENGTH_LONG).show()
                    }
                    Log.d("=CARTITEM", result.toString())
                    binding.progressAboutUs.hide()
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Log.d("=CARTITEMERROR", result.exception.toString())
//                    toast(getString(R.string.error))
                    binding.progressAboutUs.hide()
                }
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

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}