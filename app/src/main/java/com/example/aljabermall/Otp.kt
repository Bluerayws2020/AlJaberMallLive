package com.example.aljabermall


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.aljabermall.databinding.OtpActivatyBinding
import com.example.aljabermall.fragments.ProgressDialogFragment
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.inVisible
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.UserViewModel

import java.util.*

const val OTP_TYPE = "OTP_T"
const val PROGRESS_DIALOG = "progress_dialog"
const val MODIFIED_PHONE_NUMBER = "OLD_NUMBER"
const val CHANGE_PHONE_UID = "P_UID"
const val PHONENUMEBR = "PHONE"
const val PLAYERID = "PLAYERID"

class Otp : AppCompatActivity() {

    private val otpViewModel by viewModels<UserViewModel>()
    private lateinit var binding: OtpActivatyBinding
    private var countDownTimer: CountDownTimer? = null
//    private lateinit var otpInfo: OTPInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OtpActivatyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.otpViewEt.apply {
            setAnimationEnable(true)
            setCursorVisible(true)
            setHideLineWhenFilled(false)
            setTransformationMethod(PasswordTransformationMethod())
            setPasswordHidden(false)
            setOnClickListener {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        }




        var otpCode = binding.otpViewEt.text
        val phone = intent.getStringExtra(PHONENUMEBR)
        val type = intent.getStringExtra(OTP_TYPE)
        val uid = intent.getStringExtra(CHANGE_PHONE_UID)
Log.d("TYPEEEEE",type.toString())
        val playerId = intent.getStringExtra(PLAYERID)


        otpViewModel.getOtpResponse().observe(this) { results ->
            binding.progressVerifyOtp.hide()
            when (results) {
                is NetworkResults.Success -> {
                    if (results.data.status == 200) {

//                        send user id to rest password page


if (type == "1" ){
saveUserData(uid!!)
}else {
    val intentSignIn = Intent(this, ResetPasswordActivity::class.java)
    intentSignIn.putExtra(CHANGE_PHONE_UID,uid.toString())
    startActivity(intentSignIn)
}

                        showMessage(results.data.msg.toString())

                    } else {
                        showMessage(results.data.msg.toString())

                        binding.progressVerifyOtp.hide()
                        binding.verifyOtpBtn.show()
                    }
                }
                is NetworkResults.Error -> {
                    showMessage(getString(R.string.error_occurred))

                    results.exception.printStackTrace()
                }
            }
        }

        otpViewModel.getResndOtp().observe(this) { results ->
            when (results) {
                is NetworkResults.Success -> {
                    hideDialogProgress()
                    showMessage(results.data.msg.toString())
                    if (results.data.status == 200) {
                        binding.resendCodeTv.isEnabled = false
                        countDownTimer?.start()
                    } else {
                        binding.resendCodeTv.isEnabled = true
//                        binding.resendCodeTv.hide()
                        countDownTimer?.cancel()
                    }
                }
                is NetworkResults.Error -> {
                    hideDialogProgress()
                    showMessage(getString(R.string.error_occurred))
                    results.exception.printStackTrace()
                }
            }
        }

//        binding.otpViewEt.setOtpCompletionListener { otpCode = it }

        binding.verifyOtpBtn.setOnClickListener {
            if (otpCode?.isNotEmpty()!! && otpCode.length == 6) {
                it.inVisible()
                binding.progressVerifyOtp.show()
                otpViewModel.sendOtp(
                    phone.toString(),
                    type.toString(),
                    otpCode.toString(),
                )
            } else {
                showMessage(getString(R.string.correct_otp_required))
            }
        }

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendCodeTv.text = getString(
                    R.string.resend_code_30,
                    (millisUntilFinished / 1000).toString(),

                    )
                binding.resendCodeTv.isEnabled = false
                binding.resendCodeTv.setTextColor(Color.parseColor("#eaebf0"))

            }

            override fun onFinish() {
                binding.resendCodeTv.setText(R.string.resend_code)
                binding.resendCodeTv.setTextColor(Color.parseColor("#3C6E3E"))
                binding.resendCodeTv.isEnabled = true
            }

        }

        binding.resendCodeTv.setText(R.string.resend_code)
        binding.resendCodeTv.isEnabled = true
        binding.phoneNumberTv.apply {
            text = "+962$phone"
            show()
        }
//        countDownTimer?.start()
        binding.resendCodeTv.setOnClickListener {
            showDialogProgress()
            otpViewModel.resendOtp(phone.toString(),"2")
        }
        binding.phoneNumberTv.text = phone.toString()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        countDownTimer = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        showMessage(getString(R.string.verification_required))
    }

    override fun attachBaseContext(newBase: Context?) {
        val language = "ar"
        val configuration = Configuration()
        configuration.setLocale(Locale.forLanguageTag(language))
        val context = newBase?.createConfigurationContext(configuration)
        super.attachBaseContext(context)
    }
    fun Activity.showMessage(message: String, gravity: Int? = null) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        gravity?.let { toast.setGravity(it, 0, 0) }
        toast.show()
    }
    fun FragmentActivity.hideDialogProgress() {
        supportFragmentManager.findFragmentByTag(PROGRESS_DIALOG)?.let { fragment ->
            if (fragment.isAdded)
                (fragment as ProgressDialogFragment).dismiss()
        }
    }
    fun FragmentActivity.showDialogProgress() {
        ProgressDialogFragment().show(supportFragmentManager, PROGRESS_DIALOG)
    }
    private fun saveUserData(uid:String) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("uid",uid)

        }.apply()

        val intentSignIn = Intent(this, HomeActivity::class.java)
        startActivity(intentSignIn)
        finishAffinity()

    }
}