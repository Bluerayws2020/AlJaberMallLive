package com.example.aljabermall

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.aljabermall.databinding.ActivityMainBinding
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.HelperUtils.setDefaultLanguage
import com.example.aljabermall.helpers.ViewUtils.show
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.hide()

        binding.constraintLayout.layoutTransition.enableTransitionType(LayoutTransition.APPEARING)

        binding.guestContinueBtn.setOnClickListener(this)
        binding.signInButton.setOnClickListener(this)
        binding.signUpManualBtn.setOnClickListener(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (HelperUtils.getUID(this) != "0") {
                openHome()
            } else
                showViews()
        }, 2000)

        HelperUtils.setDefaultLanguage(this,"ar")
    }

    private fun openHome() {
        val intentHome = Intent(this, HomeActivity::class.java)
        startActivity(intentHome)
        finish()
    }

    private fun showViews() {
        binding.signInButton.show()
        binding.guestContinueBtn.show()
        binding.signUpManualBtn.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.guest_continue_btn -> {
                val intentHome = Intent(this, HomeActivity::class.java)
                startActivity(intentHome)
              Log.d("IDEEEES",  HelperUtils.getUID(this))
                finish()
            }
            R.id.sign_in_button -> {
                val intentLogin = Intent(this, EmailLoginActivity::class.java)
                startActivity(intentLogin)
            }
            R.id.sign_up_manual_btn -> {
                val intentSignUp = Intent(this, SignUpActivity::class.java)
                startActivity(intentSignUp)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }


}