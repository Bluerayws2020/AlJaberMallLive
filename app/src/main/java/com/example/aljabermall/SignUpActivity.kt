package com.example.aljabermall

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.aljabermall.databinding.ActivitySignUpBinding
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.isInputEmpty
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.User
import com.example.aljabermall.viewmodels.UserViewModel
import java.util.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpBinding
    private val userVM by viewModels<UserViewModel>()

    //private var imageFile: File? = null
    private var gender = ""
    private var birthDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.title = getString(R.string.create_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userVM.getSignUpResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.msg?.status == 1) {
//                        saveUserData(result.data.user!!)
//                        userSignUp
                        val intentHome = Intent(this, Otp::class.java)
                        intentHome.putExtra(PHONENUMEBR, binding.phoneNumber.text.toString());
                        intentHome.putExtra(OTP_TYPE, "1");
                        intentHome.putExtra(CHANGE_PHONE_UID, result.data.user?.id.toString());
                        Toast.makeText(this,result.data.msg?.msg.toString(),Toast.LENGTH_LONG).show()

                        startActivity(intentHome)
                        finishAffinity()
                    } else {
                        binding.progressSignUp.hide()
                        binding.signUpBtn.show()
                        if (result.data.msg?.msg.isNullOrEmpty()){
                            Toast.makeText(this, result.data.message .toString(), Toast.LENGTH_LONG)
                                .show()
                        }else {
                            Toast.makeText(this, result.data.msg?.msg.toString(), Toast.LENGTH_LONG)
                                .show()

                        }
                    }
                }
                is NetworkResults.Error -> {
                    binding.progressSignUp.hide()
                    binding.signUpBtn.show()
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
                    result.exception.printStackTrace()
                }
            }
        }

        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gender = position.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                gender = ""
            }
        }

        //binding.importImage.setOnClickListener(this)
        binding.birthDate.setOnClickListener(this)
        binding.signUpBtn.setOnClickListener(this)
    }

    /*private fun getConfig(): ImagePickerConfig {
        return ImagePickerConfig {
            mode = ImagePickerMode.SINGLE
            returnMode = ReturnMode.NONE
            isFolderMode = true
            isShowCamera = true
            theme = R.style.ImagePickerTheme
            savePath = ImagePickerSavePath(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path!!,
                isRelative = true
            )
        }
    }*/

    /*private val launcher = registerImagePicker { imageList ->
        if (imageList.isNotEmpty()) {
            imageFile = File(imageList[0].path)
            binding.profileImage.load(imageFile) {
                placeholder(R.drawable.ic_profile_user)
                error(R.drawable.ic_profile_user)
                scale(Scale.FIT)
            }
        }
    }*/

    // Request permission camera
    /*private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                launcher.launch(getConfig())
            } else
                Toast.makeText(this, getString(R.string.denied), Toast.LENGTH_LONG).show()
        }*/

    private fun saveUserData(user: User) {
//        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
//        sharedPreferences.edit().apply {
//            putString("uid", user.id.toString())
//            putString("user_name", user.user_name)
//            putString("user_type", user.user_type)
//        }.apply()
    }

    private fun isInputValid(): Boolean {
        var status = true
        var message = ""

        if (binding.fullNameEt.isInputEmpty()) {
            status = false
            binding.fullNameEt.error = getString(R.string.required)
        }

        if (binding.emailEt.isInputEmpty()) {
            status = false
            binding.emailEt.error = getString(R.string.required)
        }

//        if (binding.phoneNumber.isInputEmpty()) {
//            status = false
//            binding.emailEt.error = getString(R.string.required)
//        }

        when {
            binding.passwordEt.isInputEmpty() -> {
                status = false
                binding.passwordEt.error = getString(R.string.required)
            }
            binding.rePasswordEt.isInputEmpty() -> {
                status = false
                binding.rePasswordEt.error = getString(R.string.required)
            }
            binding.passwordEt.text.toString() != binding.passwordEt.text.toString() -> {
                status = false
                binding.passwordEt.error = getString(R.string.pass_mismatch)
                binding.rePasswordEt.error = getString(R.string.pass_mismatch)
            }
        }

//        if (birthDate.isEmpty()) {
//            status = false
//            binding.birthDate.error = getString(R.string.required)
//        }
//
//        if (gender.isEmpty() || gender == "0") {
//            status = false
//            message += getString(R.string.gender_required) + "\n"
//        }

        /*if (imageFile == null) {
            status = false
            message += getString(R.string.require_image)
        }*/

        if (message.trim().isNotEmpty())
            Toast.makeText(this, message.trim(), Toast.LENGTH_LONG).show()

        return status
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDateDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->
                onDateSelected("$year-${month + 1}-$dayOfMonth")
            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            /*R.id.import_image -> {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                } else
                    launcher.launch(getConfig())
            }*/
            R.id.birth_date -> {
                showDateDialog { date ->
                    binding.birthDate.text = date
                    birthDate = date
                }
            }
            R.id.sign_up_btn -> {
                HelperUtils.hideKeyBoard(this)
                if (isInputValid()) {
                    binding.progressSignUp.show()
                    binding.signUpBtn.hide()
                    userVM.userSignUp(
                        binding.fullNameEt.text.toString(),
                        binding.emailEt.text.toString(),
                        binding.passwordEt.text.toString(),
                        binding.phoneNumber.text.toString(),

                    )
                }
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