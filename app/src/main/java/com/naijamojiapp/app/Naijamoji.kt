package com.naijamojiapp.app

import android.content.Context
import android.os.StrictMode
import android.util.Log
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
//import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.naijamojiapp.app.roomDB.catchImage.ImagesCache
import com.naijamojiapp.app.webservice.WebService


class Naijamoji : MultiDexApplication() {

    companion object {
        var INSTANCE: Naijamoji? = null
        var mContext: Context? = null
    }

    fun getInstance(): Naijamoji {
        if (INSTANCE == null)
            INSTANCE = mContext as Naijamoji
        return INSTANCE as Naijamoji
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        val cache = ImagesCache.getInstance() //Singleton instance handled in ImagesCache class.
        cache.initializeCache()
        Log.d("Print facebook sdk version",FacebookSdk.getSdkVersion());
        FacebookSdk.sdkInitialize(mContext as Naijamoji);
        AppEventsLogger.activateApp(this)
        if (BuildConfig.DEBUG) {
           // Stetho.initializeWithDefaults(this)
        }
        FirebaseApp.initializeApp(this)
//        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
        AndroidNetworking.initialize(applicationContext, WebService.okHttpClient)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

}
