package ir.ayantech.pushsdk.model.api

import android.graphics.Bitmap
import ir.ayantech.pushsdk.helper.ImageHelper
import ir.ayantech.pushsdk.model.Message
import ir.ayantech.pushsdk.model.MessageDeserializer
import ir.ayantech.pushsdk.model.action.PushNotificationAction
import ir.ayantech.pushsdk.networking.PushNotificationNetworking
import ir.ayantech.pushsdk.networking.SimpleCallBack

class NotificationObject<T : PushNotificationAction>(
    private val notificationId: Long?,
    private val message: Message<T>?,
    val seen: Boolean,
    val sendDateTime: String,
    val title: String? = message?.notificationToShow?.title
) {

    val body = message?.notificationToShow?.message
    val buttons = message?.notificationToShow?.buttons?.map {
        NotificationObject(
            null,
            it.message,
            seen,
            sendDateTime,
            it.text
        )
    }

    fun performAction() {
        message?.action?.doAction()
    }

    fun getNotificationDetail(callback: (NotificationObject<*>) -> Unit) {
        if (notificationId == null) return
        PushNotificationNetworking.getNotificationDetail(notificationId) {
            callback(
                NotificationObject(
                    it.NotificationID,
                    MessageDeserializer.stringToMessage(
                        it.Notification.Data.message,
                        ""
                    ),
                    it.Seen,
                    it.SendDateTime
                )
            )
        }
    }

    fun remove(success: SimpleCallBack) {
        if (notificationId == null) return
        PushNotificationNetworking.removeNotification(notificationId, success)
    }

    fun getImage(callback: (Bitmap?) -> Unit) {
        if (message?.notificationToShow?.imageUrl == null) {
            callback(null)
            return
        }
        ImageHelper.downloadImage(message.notificationToShow?.imageUrl) {
            callback(it)
        }
    }
}