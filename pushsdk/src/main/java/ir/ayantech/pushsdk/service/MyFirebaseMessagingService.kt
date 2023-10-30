package ir.ayantech.pushsdk.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ir.ayantech.pushsdk.core.AyanNotification
import ir.ayantech.pushsdk.core.AyanNotification.performMessageLogic
import ir.ayantech.pushsdk.model.MessageDeserializer
import ir.ayantech.pushsdk.networking.PushNotificationNetworking
import ir.ayantech.pushsdk.storage.Constants
import ir.ayantech.pushsdk.storage.PreferencesManager
import ir.ayantech.pushsdk.storage.PushUser

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.messageId?.let {
            PushNotificationNetworking.reportDeviceReceivedNotificationStatus(it, "delivered")
        }
        if (remoteMessage.data.isNotEmpty()) {
            try {
                handleDataMessage(remoteMessage.data, remoteMessage.messageId)
            } catch (e: Exception) {
            }
        }
    }

    private fun handleDataMessage(
        arrayMap: Map<String, String>,
        messageId: String?
    ) {
        try {
            val body = arrayMap["message"]
            performMessageLogic(
                applicationContext,
                MessageDeserializer.stringToMessage(body, messageId)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("newToken", s)
        PreferencesManager.getInstance(AyanNotification.context!!).save(
            Constants.SERVER_NOTIFIED_TOKEN,
            false
        )
        if (s.isNotEmpty()) {
            PushUser.pushNotificationToken = s
            PushNotificationNetworking.reportNewDevice(s)
        }
    }
}