package com.example.aljabermall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.aljabermall.databinding.ActivityLoginBinding
import com.example.aljabermall.fragments.ProgressDialogFragment

import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.User
import com.example.aljabermall.models.UserResponse
import com.example.aljabermall.viewmodels.UserViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val RC_SIGN_IN = 105

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private val userVM by viewModels<UserViewModel>()
    private var progressDialog = ProgressDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.title = getString(R.string.login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        HelperUtils.setDefaultLanguage(this,"ar")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            //.requestIdToken(getString(R.string.client_server_id))
            .requestId()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult) {
                    val request =
                        GraphRequest.newMeRequest(result.accessToken) { _, response ->
                            lifecycleScope.launch {
                                serializeJson(response)
                            }
                        }
                    val parameters = Bundle()
                    parameters.putString(
                        "fields",
                        "id,name,link,email,birthday,gender,picture.type(large),location"
                    )
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    dismissProgress()
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.ops_cancel),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onError(error: FacebookException) {
                    dismissProgress()
                    Toast.makeText(this@LoginActivity, getString(R.string.error), Toast.LENGTH_LONG)
                        .show()
                    error.printStackTrace()
                }
            })

        userVM.getSignUpResponse().observe(this) { result ->
            dismissProgress()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg?.status == 1) {
                        Toast.makeText(this, result.data.msg.msg, Toast.LENGTH_LONG).show()

                        saveUserData(result.data.user!!)
                        val intentHome = Intent(this, HomeActivity::class.java)
                        startActivity(intentHome)
                        finishAffinity()

                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.emailSignInButton.setOnClickListener(this)
        binding.googleSignInButton.setOnClickListener(this)
        binding.facebookSignInButton.setOnClickListener(this)
    }

    private fun dismissProgress() {
        if (progressDialog.isAdded)
            progressDialog.dismiss()
    }

    private suspend fun serializeJson(response: GraphResponse?) = withContext(Dispatchers.Default) {
        val gson = Gson()
        val user = gson.fromJson(response?.rawResponse, UserResponse::class.java)
        signUp(
            user.email,
            user.name,
            user.picture.data.url,
            HelperUtils.FACEBOOK
        )
    }

    private fun signUp(
        email: String?,
        fullName: String?,
        userImage: String,
        loginProvider: String
    ) {
        lifecycleScope.launch(Dispatchers.Main) {
            progressDialog.show(supportFragmentManager, "login")
        }
        userVM.userSignUpProvider(email, fullName, userImage, loginProvider)
    }

    private fun saveUserData(user: User) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("uid", user.id.toString())
            putString("user_name", user.user_name)
            putString("user_type", user.user_type)
        }.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.email_sign_in_button -> {
                val intentEmailLogin = Intent(this, EmailLoginActivity::class.java)
                startActivity(intentEmailLogin)
            }
            R.id.facebook_sign_in_button -> {
                LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    listOf(
                        "email", "public_profile", "user_friends",
                        "user_birthday", "user_location", "user_gender", "user_link"
                    )
                )
            }
            R.id.google_sign_in_button -> {
                val signInIntent: Intent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            signUp(
                account.email,
                account.displayName,
                account.photoUrl.toString(),
                HelperUtils.GOOGLE
            )




        } catch (e: ApiException) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //updateUI(null)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}