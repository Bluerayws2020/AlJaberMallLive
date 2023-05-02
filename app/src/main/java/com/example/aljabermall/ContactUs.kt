package com.example.aljabermall

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import com.example.aljabermall.databinding.FragmentContactUsBinding
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.AlJaberViewModel
import java.util.*

class ContactUs : AppCompatActivity() {
    val viewModel by viewModels<AlJaberViewModel>()

    private var navController: NavController? = null
    private lateinit var binding: FragmentContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = FragmentContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.title = getString(R.string.contact_us)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (!HelperUtils.isNetWorkAvailable(this)) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
        }
        HelperUtils.setDefaultLanguage(this,"ar")


        viewModel.getContactUsLivedaat().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status.status == 1) {
                        Toast.makeText(this, result.data.status.msg, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, result.data.status.msg, Toast.LENGTH_LONG).show()
                    }
                    Log.d("=CARTITEM", result.toString())
                    binding.progressBarContatcUs.hide()
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Log.d("=CARTITEMERROR", result.exception.toString())
//                    toast(getString(R.string.error))
                    binding.progressBarContatcUs.hide()
                }
            }
        }


    binding.contactUsSend.setOnClickListener{
        binding.progressBarContatcUs.show()
        viewModel.retriveContatcUS(
            binding.contactUsName.text.clear().toString()
            , binding.contactUsEmail.text.clear().toString()
            , binding.contactUsSubject.text.clear().toString()
            , binding.contactUsMessage.text.clear().toString())

    }

    }


    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}