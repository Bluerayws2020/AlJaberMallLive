package com.example.aljabermall

import android.app.Application
import com.example.aljabermall.helpers.HelperUtils
import com.onesignal.OneSignal

private const val ONESIGNAL_APP_ID = "8aaa660c-30fc-4629-b282-3394551472d0"

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        // Enable verbose OneSignal logging to debug issues if needed.
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        HelperUtils.setDefaultLanguage(this,"ar")
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}