package ir.ayantech.pushsdk.networking

import android.content.Context
import android.util.Log
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.AyanCallStatus
import ir.ayantech.pushsdk.R
import ir.ayantech.pushsdk.helper.getApplicationVersion
import ir.ayantech.pushsdk.helper.getOperatorName
import ir.ayantech.pushsdk.model.api.*
import ir.ayantech.pushsdk.storage.Constants
import ir.ayantech.pushsdk.storage.EndPoint
import ir.ayantech.pushsdk.storage.PreferencesManager
import ir.ayantech.pushsdk.storage.PushNotificationUser

typealias SimpleCallBack = () -> Unit

typealias NotificationObjectsCallBack = (
    totalCount: Long,
    unSeenCount: Long,
    notifications: List<NotificationObject<*>>,
    getNextPageClosure: SimpleCallBack?
) -> Unit

object PushNotificationNetworking {
    private lateinit var ayanApi: AyanApi

    fun initialize(context: Context) {
        ayanApi = AyanApi(
            context,
            { "" },
            "https://pushnotification.infra.ayantech.ir/WebServices/App.svc/"
        )
    }

    fun reportNewDevice(token: String, extraInfo: Any? = null) {
        ayanApi.context?.let { context ->
            ayanApi.ayanCall<Void>(
                AyanCallStatus {
                    success {
                        PreferencesManager.saveToSharedPreferences(
                            Constants.SERVER_NOTIFIED_TOKEN,
                            true
                        )
                        Log.d(
                            "AyanPush",
                            "FCM token successfully reported to the server."
                        )
                    }
                    failure {
                        Log.e(
                            "AyanPush",
                            "FCM token not reported to the server. Did you correctly set \"properties.xml\" file?"
                        )
                    }
                },
                EndPoint.ReportNewDevice,
                ReportNewDeviceInput(
                    context.resources.getString(R.string.applicationName),
                    context.resources.getString(R.string.applicationType),
                    getApplicationVersion(context),
                    extraInfo,
                    getOperatorName(context),
                    "android",
                    token
                ),
                identity = false
            )
        }
    }

    fun reportDeviceMobileNumber(mobileNumber: String) {
        simpleCall<Void>(
            EndPoint.ReportDeviceMobileNumber,
            ReportDeviceMobileNumberInput(
                mobileNumber,
                PushNotificationUser.getPushNotificationToken()
            )
        ) {
            PreferencesManager.saveToSharedPreferences(Constants.SERVER_NOTIFIED_MOBILE, true)
            Log.d(
                "AyanPush",
                "User mobile number successfully reported to the server."
            )
        }
    }

    fun reportDeviceReceivedNotificationStatus(
        messageId: String,
        status: String,
        extraInfo: Any? = null
    ) {
        simpleCall<Void>(
            EndPoint.ReportDeviceReceivedNotificationStatus,
            ReportDeviceReceivedNotificationStatusInput(extraInfo, messageId, status)
        ) {
            Log.d(
                "AyanPush",
                "Message status with $status status successfully reported to the server."
            )
        }
    }

    fun getNotificationsList(
        itemCount: Long,
        offset: Long = 0L,
        callback: (GetNotificationsListOutput) -> Unit
    ) {
        simpleCall<GetNotificationsListOutput>(
            EndPoint.GetNotificationsList,
            GetNotificationsListInput(
                itemCount,
                offset,
                PushNotificationUser.getPushNotificationToken()
            )
        ) {
            it?.let { callback(it) }
        }
    }

    fun getNotificationDetail(notificationId: Long, callback: (GetNotificationDetailOutput) -> Unit) {
        simpleCall<GetNotificationDetailOutput>(
            EndPoint.GetNotificationDetail,
            GetNotificationDetailInput(notificationId, PushNotificationUser.getPushNotificationToken())
        ) {
            it?.let { callback(it) }
        }
    }

    fun removeNotification(notificationId: Long, success: SimpleCallBack) {
        simpleCall<Void>(
            EndPoint.RemoveNotification,
            RemoveNotificationInput(notificationId, PushNotificationUser.getPushNotificationToken())
        ) {
            success()
        }
    }

    fun removeAllNotifications(success: SimpleCallBack) {
        simpleCall<Void>(
            EndPoint.RemoveAllNotifications,
            RemoveAllNotificationsInput(PushNotificationUser.getPushNotificationToken())
        ) {
            success()
        }
    }

    private inline fun <reified GenericOutput> simpleCall(
        endPoint: String,
        input: Any? = null,
        crossinline onSuccess: (GenericOutput?) -> Unit
    ) {
        ayanApi.ayanCall<GenericOutput>(
            AyanCallStatus {
                success {
                    onSuccess(it.response?.Parameters)
                }
            },
            endPoint,
            input,
            hasIdentity = false
        )
    }
}