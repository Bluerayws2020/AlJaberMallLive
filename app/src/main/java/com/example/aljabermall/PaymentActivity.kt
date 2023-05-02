package com.example.aljabermall

import android.R
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.aljabermall.databinding.PaymentActivityBinding
import com.example.aljabermall.fragments.CartFragment
import com.example.aljabermall.helpers.HelperUtils


class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: PaymentActivityBinding
    private var phoneNumber: String? = null
    private var facebookUrl: String? = null
    private var uploadCallback: ValueCallback<Array<Uri>>? = null
    private var mFileChooserParams: WebChromeClient.FileChooserParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaymentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "الدفع"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        HelperUtils.setDefaultLanguage(this,"ar")


        val bundle = Bundle()
        val orderIdBundle = bundle.getString("orderid")
        Log.d("Order ID -  Bundle", "onCreate: " + orderIdBundle)
        Log.d("Order ID -  Bundl\ne", "onCreate: " + HelperUtils.getUID(applicationContext))
        val orderId = CartFragment.orderId
//        binding.webView.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl("https://jabermall.com/app/redirectToCheckout?uid=${HelperUtils.getUID(applicationContext)}&order_id=${CartFragment.orderId}")
//
//                return true
//            }
//        }
        gotoPage()


        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.allowContentAccess = true

    }

    private fun gotoPage() {
        val url = "https://jabermall.com/app/redirectToCheckout?uid=${HelperUtils.getUID(applicationContext)}&order_id=${CartFragment.orderId}"

        val webSettings: WebSettings = binding.webView.getSettings()
        webSettings.builtInZoomControls = true
        binding.webView.setWebViewClient(Callback()) //HERE IS THE MAIN CHANGE
        binding.webView.loadUrl(url)


    }

    private class Callback : WebViewClient() {
        //HERE IS THE MAIN CHANGE.
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }
    }


}