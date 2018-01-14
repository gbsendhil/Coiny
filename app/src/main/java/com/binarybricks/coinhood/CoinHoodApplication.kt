package com.binarybricks.coinhood

import android.app.Application
import android.content.Context
import android.support.annotation.NonNull
import android.util.Log
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * Created by pairan on 1/8/18.
 */

class CoinHoodApplication : Application() {

    companion object {
        private lateinit var appContext: Context

        @JvmStatic
        fun getGlobalAppContext(): Context {
            return appContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String, @NonNull message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

//            FakeCrashLibrary.log(priority, tag, message)
//
//            if (t != null) {
//                if (priority == Log.ERROR) {
//                    FakeCrashLibrary.logError(t)
//                } else if (priority == Log.WARN) {
//                    FakeCrashLibrary.logWarning(t)
//                }
//            }
        }
    }
}