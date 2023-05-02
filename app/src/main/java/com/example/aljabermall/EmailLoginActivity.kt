package com.example.aljabermall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.aljabermall.databinding.ActivityEmailLoginBinding
import com.example.aljabermall.fragments.PasswordDialogFragment
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.inVisible
import com.example.aljabermall.helpers.ViewUtils.isInputEmpty
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.UserModel
import com.example.aljabermall.viewmodels.UserViewModel
import retrofit2.HttpException
import java.util.*

class EmailLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailLoginBinding
    private val userVM by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")




        supportActionBar?.title = getString(R.string.login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)





        binding.resetPassTv.setOnClickListener{
            val intentHome = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intentHome)
        }


        userVM.getLoginResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {

                        if (!result.data.user.verified.isNullOrEmpty() ) {
                            saveUserData(
                                result.data.user,

                                )
                            val intentHome = Intent(this, HomeActivity::class.java)
                            startActivity(intentHome)
                            finishAffinity()
//
                        }else {
                            val intentHome = Intent(this, Otp::class.java)
                            val phone = result.data.user.phone
                            val uid = result.data.user.id

                            intentHome.putExtra(PHONENUMEBR, phone.toString());
                            intentHome.putExtra(OTP_TYPE,"1");
                            intentHome.putExtra(CHANGE_PHONE_UID,uid.toString());

                            startActivity(intentHome)
                            finishAffinity()
                        }

                    } else {
                        showMessage(result.data.status.msg)
                        hideProgress()
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    hideProgress()
                    if (result.exception is HttpException)
                        showMessage(result.exception.message())
                    else
                        showMessage(getString(R.string.error))
                }
            }
        }

        binding.signInBtn.setOnClickListener {
            HelperUtils.hideKeyBoard(this)
            if (isInputValid()) {
                binding.progressBarLogin.show()
                it.inVisible()
                userVM.userLogin(
                    binding.emailSignIn.text.toString(),
                    binding.passwordEt.text.toString(),
                    HelperUtils.getAndroidID(this)
                )
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

    private fun isInputValid(): Boolean {
        var status = true

        if (binding.emailSignIn.isInputEmpty()) {
            status = false
            binding.emailSignIn.error = getString(R.string.required)
        }

        if (binding.passwordEt.isInputEmpty()) {
            status = false
            binding.passwordEt.error = getString(R.string.required)
        }

        return status
    }

    private fun saveUserData(user: UserModel) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("uid", user.id.toString())
            putString("user_type", user.user_type)

        }.apply()
    }

    private fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun hideProgress() {
        binding.signInBtn.show()
        binding.progressBarLogin.hide()
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}