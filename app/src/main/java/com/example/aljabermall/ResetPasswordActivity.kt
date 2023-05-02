package com.example.aljabermall

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aljabermall.databinding.ActivityResetPasswordBinding
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.isInputEmpty
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.UserViewModel

const val FORGET_PASS_TYPE = 2

//const val RESET_PASS_TYPE = 3
const val UID_KEY = "UID"

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private val otpViewModel by viewModels<UserViewModel>()

    private fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.extras?.getString(CHANGE_PHONE_UID.toString())
//print(I,userId)
        Log.d("MesagIDe",userId.toString())
        otpViewModel.getResetPassword().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    showMessage(result.data.msg)
                    if (result.data.status == 200) {
                        val intentLogin = Intent(this, MainActivity::class.java)
                        startActivity(intentLogin)
                        finishAffinity()
                        finish()
                    } else {
                        binding.progressResetPass.hide()
                        binding.submitNewPasswordBtn.show()
                    }
                }
                is NetworkResults.Error -> {
                    showMessage(getString(R.string.error_occurred))
                    result.exception.printStackTrace()
                    Log.d("ERRRRRR",result.exception.message.toString())
                }
            }
        }

        binding.submitNewPasswordBtn.setOnClickListener {
            if (isInputValid()) {
                it.hide()
                binding.progressResetPass.show()
                otpViewModel.resetPass(
                    userId.toString(),
                    binding.newPasswordEt.text.toString()
,                            binding.newPasswordEt.text.toString()


                )
            }
        }
    }

    private fun isInputValid(): Boolean {
        var state = true

        /*if (binding.oldPasswordEt.isInputEmpty()) {
            state = false
            binding.oldPasswordEt.error = getString(R.string.required)
        }*/
        if (binding.newPasswordEt.isInputEmpty()) {
            state = false
            binding.newPasswordEt.error = getString(R.string.required)
        }
        if (binding.rePasswordEt.isInputEmpty()) {
            state = false
            binding.rePasswordEt.error = getString(R.string.required)
        }
        if (binding.newPasswordEt.text.toString().trim() !=
            binding.rePasswordEt.text.toString().trim()
        ) {
            state = false
            binding.newPasswordEt.error = "كلمة السر غير مطابقة"
            binding.rePasswordEt.error = "كلمة السر غير مطابقة"
        }

        return state
    }


}