package com.example.aljabermall

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.aljabermall.databinding.ActivityInfoWebBinding
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import java.util.*

class InfoWebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoWebBinding
    private var phoneNumber = ""
    private var facebookUrl = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val url = intent.extras?.getString("web_url")
        val title = intent.extras?.getString("title")

        supportActionBar?.title = title

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebClient()
        binding.webView.loadUrl(url!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.webView.restoreState(savedInstanceState)
    }

    inner class WebClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            checkUrl(request?.url.toString())
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.progressBarWeb.show()
            binding.webView.hide()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.progressBarWeb.hide()
            binding.webView.show()
        }

        //handle external links
        private fun checkUrl(url: String): Boolean {
            return when {
                url.contains("tel") -> {
                    if (ActivityCompat.checkSelfPermission(
                            this@InfoWebActivity,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        phoneNumber = url
                        requestPhoneCallPermission.launch(Manifest.permission.CALL_PHONE)
                    } else
                        phoneCall(url)
                    true
                }
                url.contains("whatsapp") -> {
                    try {
                        openApp(url, "com.whatsapp")
                    } catch (e: PackageManager.NameNotFoundException) {
                        Toast.makeText(
                            this@InfoWebActivity,
                            "لا يوجد تطبيق واتساب",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    true
                }
                url.contains("mailto") -> {
                    sendEmail(url)
                    true
                }
                url.contains("facebook") -> {
                    facebookUrl = url
                    try {
                        openApp("fb://facewebmodal/f?href=${url}", "com.facebook.katana")
                    } catch (e: PackageManager.NameNotFoundException) {
                        openBrowser(url)
                    }
                    true
                }
                else -> false
            }
        }
    }

    //make call for number click
    private fun phoneCall(phone: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse(phone)
        startActivity(intent)
    }

    //open url in app
    @Throws(PackageManager.NameNotFoundException::class)
    private fun openApp(url: String, mPackage: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(mPackage)
        val applicationInfo: ApplicationInfo =
            packageManager.getApplicationInfo(mPackage, 0)
        if (applicationInfo.enabled) {
            when {
                intent.resolveActivity(packageManager) != null -> startActivity(intent)
                url.contains("facebook") -> openBrowser(url)
                else -> openBrowser(url)
            }
        }
    }

    //open external browser
    private fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (browserIntent.resolveActivity(packageManager) != null) startActivity(
            browserIntent
        ) else
            Toast.makeText(
                this@InfoWebActivity,
                getString(R.string.error),
                Toast.LENGTH_LONG
            ).show()
    }

    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$email"))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            if (e.message != null) e.printStackTrace()
            Toast.makeText(
                this@InfoWebActivity,
                getString(R.string.no_email),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Request permission phone call
    private val requestPhoneCallPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                phoneCall(phoneNumber)
            else
                Toast.makeText(this, getString(R.string.denied), Toast.LENGTH_LONG).show()
        }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}