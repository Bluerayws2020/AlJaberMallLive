package com.example.aljabermall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.aljabermall.databinding.ActivityLanguageBinding
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import java.util.*

class LanguageActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.title = getString(R.string.language)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.buttonArabicLanguage.setOnClickListener(this)
        binding.buttonEnglishLanguage.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        var language = ""
        when (v?.id) {
            R.id.button_english_language -> language = "en"

            R.id.button_arabic_language -> language = "ar"
        }
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().putString("lang", language).apply()
        val intentSplash = Intent(this, MainActivity::class.java)
        startActivity(intentSplash)
        finishAffinity()
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}