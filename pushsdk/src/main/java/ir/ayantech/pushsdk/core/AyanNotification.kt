package ir.ayantech.pushsdk.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import ir.ayantech.pushsdk.activity.IncomeMessageActivity
import ir.ayantech.pushsdk.helper.NotificationUtils
import ir.ayantech.pushsdk.model.Message
import ir.ayantech.pushsdk.model.MessageDeserializer
import ir.ayantech.pushsdk.model.api.NotificationObject
import ir.ayantech.pushsdk.networking.*
import ir.ayantech.pushsdk.storage.Constants
import ir.ayantech.pushsdk.storage.PreferencesManager
import ir.ayantech.pushsdk.storage.PushUser

@SuppressLint("StaticFieldLeak")
object AyanNotification {
    var context: Context? = null
    fun initialize(context: Context, baseUrl: String? = null) {
        this.context = context
        PushUser.context = context
        PushNotificationNetworking.initialize(context, baseUrl)
        if (isServerNotifiedToken() && isServerNotifiedMobile()) return
        if (PushUser.pushNotificationToken.isEmpty()) return
        if (!isServerNotifiedToken() && PushUser.pushNotificationToken.isNotEmpty())
            PushNotificationNetworking.reportNewDevice(PushUser.pushNotificationToken)
        if (!isServerNotifiedMobile() && PushUser.userMobile.isNotEmpty())
            PushNotificationNetworking.reportDeviceMobileNumber(PushUser.userMobile)
    }

    fun isServerNotifiedToken(): Boolean {
        return PreferencesManager.getInstance(context!!)
            .readBooleanFromSharedPreferences(Constants.SERVER_NOTIFIED_TOKEN)
    }

    fun isServerNotifiedMobile(): Boolean {
        return PreferencesManager.getInstance(context!!)
            .readBooleanFromSharedPreferences(Constants.SERVER_NOTIFIED_MOBILE)
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
        PushUser.setPushNotificationExtraInfo(extraInfo)
        if (isServerNotifiedToken())
            PushNotificationNetworking.reportNewDevice(
                PushUser.pushNotificationToken,
                extraInfo
            )
    }

    fun reportDeviceMobileNumber(mobileNumber: String) {
        PushUser.userMobile = mobileNumber
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

    fun getNotificationsSummery(callBack: NotificationsSummaryCallBack) {
        PushNotificationNetworking.getNotificationsSummery(callBack)
    }

    private fun getNotificationList(
        itemCount: Long,
        offset: Long,
        notificationObjectsCallBack: NotificationObjectsCallBack
    ) {
        PushNotificationNetworking.getNotificationsList(itemCount, offset) { success, output ->
            var nextPageClosure: SimpleCallBack? = null
            if (!success) {
                notificationObjectsCallBack(success, listOf(), null)
                return@getNotificationsList
            }
            output?.let {
                if (it.HasMore) {
                    nextPageClosure = {
                        getNotificationList(itemCount, it.NextOffset, notificationObjectsCallBack)
                    }
                }
                notificationObjectsCallBack(
                    success,
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
                    }?.filter { it.message != null && it.title != null },
                    nextPageClosure
                )
            }
        }
    }
}