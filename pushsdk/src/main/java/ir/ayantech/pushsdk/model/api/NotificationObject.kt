package ir.ayantech.pushsdk.model.api

import android.content.Context
import android.graphics.Bitmap
import ir.ayantech.pushsdk.helper.ImageHelper
import ir.ayantech.pushsdk.model.Message
import ir.ayantech.pushsdk.model.MessageDeserializer
import ir.ayantech.pushsdk.model.action.PushNotificationAction
import ir.ayantech.pushsdk.networking.BooleanCallBack
import ir.ayantech.pushsdk.networking.PushNotificationNetworking

class NotificationObject<T : PushNotificationAction>(
    private val notificationId: Long?,
    internal val message: Message<T>?,
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

    fun performAction(context: Context) {
        message?.action?.context = context
        message?.action?.doAction()
    }

    fun getNotificationDetail(callback: (success: Boolean, notificationObject: NotificationObject<*>?) -> Unit) {
        if (notificationId == null) return
        PushNotificationNetworking.getNotificationDetail(notificationId) { success, output ->
            if (!success) {
                callback(success, null)
                return@getNotificationDetail
            }
            output?.let {
                callback(
                    success,
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
    }

    fun remove(success: BooleanCallBack) {
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