package ir.ayantech.pushsdk.storage

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson

@SuppressLint("StaticFieldLeak")
object PushUser {

    private const val PUSH_NOTIFICATION_EXTRA_INFO = "pushNotificationExtraInfo"
    private const val PUSH_NOTIFICATION_EXTRA_INFO_CLASS = "pushNotificationExtraInfoClass"

    lateinit var context: Context

    var pushNotificationToken: String
        get() = PreferencesManager.getInstance(context).read("pushNotificationToken")
        set(value) = PreferencesManager.getInstance(context).save("pushNotificationToken", value)

    var userMobile: String
        get() = PreferencesManager.getInstance(context).read("userMobile")
        set(value) = PreferencesManager.getInstance(context).save("userMobile", value)

    @Throws(ClassNotFoundException::class)
    fun <T> getPushNotificationExtraInfo(): T {
        return Gson().fromJson(
            PreferencesManager.getInstance(context).readStringFromSharedPreferences(PUSH_NOTIFICATION_EXTRA_INFO),
            Class.forName(getPushNotificationExtraInfoClass())
        ) as T
    }

    private fun getPushNotificationExtraInfoClass(): String {
        return PreferencesManager.getInstance(context).readStringFromSharedPreferences(PUSH_NOTIFICATION_EXTRA_INFO_CLASS)
    }

    fun <T> setPushNotificationExtraInfo(pushNotificationExtraInfo: T) {
        setPushNotificationExtraInfoClass(pushNotificationExtraInfo)
        PreferencesManager.getInstance(context).save(
            PUSH_NOTIFICATION_EXTRA_INFO,
            Gson().toJson(pushNotificationExtraInfo)
        )
    }

    private fun <T> setPushNotificationExtraInfoClass(pushNotificationExtraInfo: T) {
        PreferencesManager.getInstance(context).save(
            PUSH_NOTIFICATION_EXTRA_INFO_CLASS,
            pushNotificationExtraInfo!!::class.java.name
        )
    }
}