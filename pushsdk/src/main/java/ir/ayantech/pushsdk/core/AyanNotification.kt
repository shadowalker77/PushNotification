package ir.ayantech.pushsdk.core

import android.content.Context
import android.content.Intent
import ir.ayantech.pushsdk.activity.IncomeMessageActivity
import ir.ayantech.pushsdk.helper.NotificationUtils
import ir.ayantech.pushsdk.model.Message
import ir.ayantech.pushsdk.model.MessageDeserializer
import ir.ayantech.pushsdk.model.api.NotificationObject
import ir.ayantech.pushsdk.networking.BooleanCallBack
import ir.ayantech.pushsdk.networking.NotificationObjectsCallBack
import ir.ayantech.pushsdk.networking.PushNotificationNetworking
import ir.ayantech.pushsdk.networking.SimpleCallBack
import ir.ayantech.pushsdk.storage.Constants
import ir.ayantech.pushsdk.storage.PreferencesManager
import ir.ayantech.pushsdk.storage.PushNotificationUser

object AyanNotification {

    fun initialize(context: Context) {
        PreferencesManager.initialize(context)
        PushNotificationNetworking.initialize(context)
        if (isServerNotifiedToken() && isServerNotifiedMobile()) return
        if (PushNotificationUser.getPushNotificationToken().isEmpty()) return
        if (!isServerNotifiedToken() && PushNotificationUser.getPushNotificationToken()
                .isNotEmpty()
        )
            PushNotificationNetworking.reportNewDevice(PushNotificationUser.getPushNotificationToken())
        if (!isServerNotifiedMobile() && PushNotificationUser.getUserMobile().isNotEmpty())
            PushNotificationNetworking.reportDeviceMobileNumber(PushNotificationUser.getUserMobile())
    }

    fun isServerNotifiedToken(): Boolean {
        return PreferencesManager.readBooleanFromSharedPreferences(Constants.SERVER_NOTIFIED_TOKEN)
    }

    fun isServerNotifiedMobile(): Boolean {
        return PreferencesManager.readBooleanFromSharedPreferences(Constants.SERVER_NOTIFIED_MOBILE)
    }

    fun performMessageLogic(context: Context, message: Message<*>) {
        if (message.notificationToShow == null && message.action != null) {
            message.action.context = context
            message.action.doAction()
            PushNotificationNetworking.reportDeviceReceivedNotificationStatus(
                message.messageId,
                "action_done"
            )
        }
        if (message.notificationToShow != null && message.action == null) {
            NotificationUtils.showNotificationMessageWithBigImage(
                context,
                message.notificationToShow.title,
                message.notificationToShow.message,
                Intent(),
                message.notificationToShow.imageUrl,
                message.notificationToShow.buttons,
                message.notificationToShow.iconUrl,
                message.notificationToShow.isUseCustomView
            )
        }
        if (message.notificationToShow != null && message.action != null) {
            NotificationUtils.showNotificationMessageWithBigImage(
                context,
                message.notificationToShow.title,
                message.notificationToShow.message,
                IncomeMessageActivity.getIntentByMessage(context, message),
                message.notificationToShow.imageUrl,
                message.notificationToShow.buttons,
                message.notificationToShow.iconUrl,
                message.notificationToShow.isUseCustomView
            )
        }
    }

    fun <T> reportExtraInfo(extraInfo: T) {
        PushNotificationUser.setPushNotificationExtraInfo(extraInfo)
        if (isServerNotifiedToken())
            PushNotificationNetworking.reportNewDevice(
                PushNotificationUser.getPushNotificationToken(),
                extraInfo
            )
    }

    fun reportDeviceMobileNumber(mobileNumber: String) {
        PushNotificationNetworking.reportDeviceMobileNumber(mobileNumber)
    }

    fun getNotificationList(
        itemCount: Long,
        notificationObjectsCallBack: NotificationObjectsCallBack
    ) {
        getNotificationList(itemCount, 0, notificationObjectsCallBack)
    }

    fun removeAllNotifications(success: BooleanCallBack) {
        PushNotificationNetworking.removeAllNotifications(success)
    }

    private fun getNotificationList(
        itemCount: Long,
        offset: Long,
        notificationObjectsCallBack: NotificationObjectsCallBack
    ) {
        PushNotificationNetworking.getNotificationsList(itemCount, offset) { success, output ->
            var nextPageClosure: SimpleCallBack? = null
            output?.let {
                if (!success) {
                    notificationObjectsCallBack(success, 0L, 0L, listOf(), null)
                    return@let
                }
                if (it.HasMore) {
                    nextPageClosure = {
                        getNotificationList(itemCount, it.NextOffset, notificationObjectsCallBack)
                    }
                }
                notificationObjectsCallBack(
                    success,
                    it.TotalCount,
                    it.UnSeenCount,
                    it.Notifications?.map {
                        NotificationObject(
                            it.NotificationID,
                            MessageDeserializer.stringToMessage(
                                it.Notification.Data.message,
                                it.NotificationID.toString()
                            ),
                            it.Seen,
                            it.SendDateTime
                        )
                    },
                    nextPageClosure
                )
            }
        }
    }
}