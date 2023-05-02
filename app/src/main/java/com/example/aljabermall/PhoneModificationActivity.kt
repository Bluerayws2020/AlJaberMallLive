package com.example.aljabermall

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aljabermall.databinding.ActivityPhoneModificationBinding
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.inVisible
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.ProfileViewModel
import com.example.aljabermall.viewmodels.UserViewModel





class PhoneModificationActivity : AppCompatActivity() {
    protected fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private lateinit var binding: ActivityPhoneModificationBinding
    private val otpViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneModificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = intent.getIntExtra(CHANGE_PHONE_UID, 0)
        val oldPhoneNumber = intent.getStringExtra(MODIFIED_PHONE_NUMBER)

        binding.phoneNumberEt.setText(oldPhoneNumber)

        otpViewModel.getResndOtp().observe(this) { results ->
            when (results) {
                is NetworkResults.Success -> {
                    showMessage(results.data.msg)

                    if (results.data.status == 200) {

                        val changePhoneIntent = Intent()
                        changePhoneIntent.putExtra(
                            MODIFIED_PHONE_NUMBER,
                            binding.phoneNumberEt.text.toString(),
                        )
                        setResult(RESULT_OK, changePhoneIntent)
                        finish()
                    } else {
                        binding.changePhoneBtn.show()
                        binding.progressChangePhone.hide()
                    }
                }
                is NetworkResults.Error -> {
                    binding.changePhoneBtn.show()
                    binding.progressChangePhone.hide()
                    showMessage(getString(R.string.error_occurred))
                    results.exception.printStackTrace()
                }
            }
        }

        binding.changePhoneBtn.setOnClickListener {
            val phoneNumber = binding.phoneNumberEt.text.trim().toString()
            if (phoneNumber.isNotEmpty()) {
                it.inVisible()
                binding.progressChangePhone.show()
                if (phoneNumber == oldPhoneNumber) {
                    finish()
                } else {
                    otpViewModel.resendOtp(
        binding.phoneNumberEt.text.toString(),
                   "2"
                        ,
                    )
                }
            } else {
                binding.phoneNumberEt.error = getString(R.string.required)
            }
        }
     }
}