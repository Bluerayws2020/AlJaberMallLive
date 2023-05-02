package com.example.aljabermall

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aljabermall.databinding.ActivityForgetPasswordBinding
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.inVisible
import com.example.aljabermall.helpers.ViewUtils.isInputEmpty
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.UserViewModel

import java.util.*

const val FORGET_PASS_OTP = 2

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private val userVM by viewModels<UserViewModel>()
    protected fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userVM.getOtpForForget().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    showMessage(result.data.msg)
                    if (result.data.status == 200) {
                        val otpIntent = Intent(this, Otp::class.java)
//                        val otpInfo = Otp.ForgetPassOTP(
//                            phoneNumber = binding.phoneNumberEt.text.toString(),
//                            otpType = FORGET_PASS_OTP,
//                        )
                        otpIntent.putExtra(PHONENUMEBR, binding.phoneNumberEt.text.toString());

                        otpIntent.putExtra(OTP_TYPE, "2")
                        val uid = result.data.data?.uid
                        otpIntent.putExtra(CHANGE_PHONE_UID, uid.toString())

                        startActivity(otpIntent)
                    } else {
                        binding.forgetPassBtn.show()
                        binding.progressRequestPassword.hide()
                    }
                }
                is NetworkResults.Error -> {
                    showMessage(getString(R.string.error_occurred))
                    result.exception.printStackTrace()
                }
            }
        }

        binding.forgetPassBtn.setOnClickListener {
            if (binding.phoneNumberEt.isInputEmpty()) {
                binding.phoneNumberEt.error = getString(R.string.required)
            } else {
                it.inVisible()
                binding.progressRequestPassword.show()
                userVM.otpForForget(binding.phoneNumberEt.text.toString())
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


}