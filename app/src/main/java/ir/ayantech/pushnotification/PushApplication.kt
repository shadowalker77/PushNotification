package ir.ayantech.pushnotification

import android.app.Application
import ir.ayantech.pushsdk.core.AyanNotification

class PushApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AyanNotification.initialize(this)
    }
}