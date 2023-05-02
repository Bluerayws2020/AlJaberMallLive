package com.example.aljabermall

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aljabermall.databinding.ActivityProfileBinding
import com.example.aljabermall.fragments.ProgressDialogFragment
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.UserProfile
import com.example.aljabermall.viewmodels.ProfileViewModel
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProfileBinding
    private val profileVM by viewModels<ProfileViewModel>()
    private var progressDialog = ProgressDialogFragment()
    private var birthDate: String? = null
    private var gender: String? = null
    private val userID by lazy { HelperUtils.getUID(applicationContext) }


    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    private var mediaPath: String? = null
    private var postPath: String? = null
    private var imageFile: File? = null
    private var compressedImageFile: File? = null


    private var fullName: String ?= null
    private var email: String ?= null
    private var phoneNumebr: String ?= null

    private val oldPassword: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")

        supportActionBar?.title = getString(R.string.profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialogFragment()
        progressDialog.show(supportFragmentManager, "profile")


        profileVM.getUserInfo().observe(this) { result ->
            if (progressDialog.isAdded)
                progressDialog.dismiss()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        setupUserInfo(result.data.userProfile)

                        fullName = result.data.userProfile.user_name
                        email = result.data.userProfile.email
                       phoneNumebr = result.data.userProfile.phone.toString()
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            }
        }

        profileVM.getEditProfileMessage().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 1) {
                        Toast.makeText(this@ProfileActivity, result.data.msg, Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.editInfoBtn.setOnClickListener(this)
        binding.birthDateProfile.setOnClickListener(this)
//        binding.profileImageProfile.setOnClickListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUserInfo(profile: UserProfile) {
//        birthDate = profile.birth_date
//        gender = profile.gender
        gender?.let {
            binding.genderSpinner.setSelection(it.toInt())
        }
        binding.fullNameEt.setText(profile.user_name.toString())
        binding.emailEt.text = profile.email
        binding.phoneNumber.setText(profile.phone.toString())

//        binding.phoneNumber.setText(profile.phone.toString())
//        binding.userImage.(profile.image.toString())
//        binding.birthDateProfile.text = profile.birth_date
        Log.i("image", "setupUserInfo: " + profile.image.toString())
//        binding.importImage.setOnClickListener {
//        GlideApp.with(context).load(model.getOffers).apply(options).into(slideImage);

//        Glide.with(applicationContext)
//                            .load(HelperUtils.BASE_URL + profile.image)
//                            .placeholder(R.drawable.ic_profile_user)
//                            .error(R.drawable.ic_profile_user)
//                            .into(binding.userImage)


//        binding.userImage.load(HelperUtils.BASE_URL + profile.image) {
//            placeholder(R.drawable.ic_profile_user)
//            error(R.drawable.ic_profile_user)
//        }
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
            R.id.edit_info_btn -> {
                HelperUtils.hideKeyBoard(this)
                binding.editInfoBtn.hide()
                binding.progressEdit.show()


                Log.d("TAG", "old: " + fullName + "  new : " + binding.fullNameEt.text.toString().trim())
                Log.d("TAG", "old: " + email + "  new : " + binding.emailEt.text.toString().trim())
                Log.d("TAG", "old: " + binding.passs1.text.toString() + "new : " )

                if(fullName != binding.fullNameEt.text.toString().trim() &&
                    email == binding.emailEt.text.toString().trim() || binding.passs1.text.toString() ==""){

                    profileVM.editUserProfile(
                        userID,
                        binding.passs1.text.trim().toString(),
                        binding.passs1.text.trim().toString(),
                        binding.fullNameEt.text.toString().trim()
                    )
                }

                else if(email != binding.emailEt.text.toString().trim() &&
                    fullName == binding.fullNameEt.text.toString().trim() || binding.passs1.text.toString() ==""){
                    profileVM.editUserProfile(
                        userID,
                        binding.passs1.text.trim().toString(),
                        binding.passs1.text.trim().toString(),
                        fullName,
                        binding.emailEt.text.toString().trim()
                    )
                }

                else if(fullName != binding.fullNameEt.text.toString().trim()
                    && email != binding.emailEt.text.toString().trim() || binding.passs1.text.toString() ==""){
                    profileVM.editUserProfile(
                        userID,
                        binding.passs1.text.trim().toString(),
                        binding.passs1.text.trim().toString(),
                        binding.fullNameEt.text.toString().trim(),
                        binding.emailEt.text.toString().trim()
                    )
                }

                else if(binding.passs1.text.toString() != "" &&
                    fullName == binding.fullNameEt.text.toString().trim() && email == binding.emailEt.text.toString().trim()){
                    profileVM.editUserProfile(
                        userID,
                        binding.confirmPass.text.trim().toString(),
                        binding.passs1.text.trim().toString(),
                    )
                }

                else {
                    profileVM.editUserProfile(
                        userID,
                        binding.confirmPass.text.trim().toString(),
                        binding.passs1.text.trim().toString(),
                        binding.fullNameEt.text.toString().trim(),
                        binding.emailEt.text.toString().trim()
                    )

                }

                Log.i("check", "onClick: $userID")

            }
            R.id.birth_date_profile -> {
                showDateDialog {
                    birthDate = it
                    binding.birthDateProfile.text = it
                }
            }

//            R.id.profile_image_profile  -> {
//
////                pickImageGallery()
//                Toast.makeText(applicationContext, "Hello, clicked", Toast.LENGTH_SHORT).show()
//                Log.d("IamHere","1")
////                Log.i("profile image", "onClick: ")
////                pickImageGallery()
//            }

        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }

//    @SuppressLint("IntentReset")
//    private fun pickImageGallery() {
//
//        let {
//            if (ContextCompat.checkSelfPermission(it.applicationContext,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED)
//            {
//                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
//            }
//            else {
//                val pickImageIntent = Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
//                )
//                pickImageIntent.type = "image/*"
//                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
//                pickImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//                startActivityForResult(pickImageIntent, IMAGE_REQUEST_CODE)
//
//            }
//        }
//    }


//    private val PERMISSIONS_LENGTH = 2
//    override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<out String>, grantResults:IntArray) {
//        // Check whether requestCode is set to the value of CAMERA_REQ_CODE during permission application, and then check whether the permission is enabled.
//
//        if(requestCode == 1 )
//        {
//            if(grantResults.isNotEmpty() && grantResults.size
//                == PERMISSIONS_LENGTH && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED)
//            {
//                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                startActivityForResult(intent,IMAGE_REQUEST_CODE )
//                Log.i("REQUEST2CODE", "onActivityResult: $requestCode")
//            }
//        }else {
//            Toast.makeText(this@ProfileActivity,"Permission Denied",Toast.LENGTH_LONG).show()
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }

//    @SuppressLint("Recycle")
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        super.onActivityResult(requestCode, resultCode, data)
//
//        Log.i("REQUEST CODE", "onActivityResult: $requestCode")
//        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
//            if (data != null) {
//                // Get the Image from data
//                val selectedImage = data.data
////                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//
//
//                val cursor = this@ProfileActivity.contentResolver?.query(
//                    selectedImage!!,
//                    filePathColumn,
//                    null,
//                    null,
//                    null
//                )
//                assert(cursor != null)
//                cursor!!.moveToFirst()
//
//                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                mediaPath = cursor.getString(columnIndex)


//                binding.userImage.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
//                postPath = mediaPath
//
//                Log.i("postPath", "onActivityResult:  " + mediaPath.toString())
//
//                imageFile = File(mediaPath.toString())
//
//
//                lifecycleScope.launch {
//                    compressedImageFile =
//                        Compressor.compress(applicationContext, imageFile!!, Dispatchers.IO)
//
//                    Log.i("path", "onActivityResult: $compressedImageFile")
//
//                    profileVM.editUserProfile(
//                        userID,
//                        profileImage = compressedImageFile
//                    )
//
//                }
//
//            } else if (resultCode != Activity.RESULT_CANCELED) {
//                Toast.makeText(
//                    applicationContext,
//                    "Sorry, there was an error!",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//        } }
}