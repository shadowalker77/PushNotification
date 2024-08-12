package ir.ayantech.pushsdk.networking

import android.content.Context
import android.util.Log
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.AyanCallStatus
import ir.ayantech.ayannetworking.ayanModel.LogLevel
import ir.ayantech.pushsdk.R
import ir.ayantech.pushsdk.core.AyanNotification
import ir.ayantech.pushsdk.helper.getApplicationVersion
import ir.ayantech.pushsdk.helper.getOperatorName
import ir.ayantech.pushsdk.model.api.*
import ir.ayantech.pushsdk.storage.Constants
import ir.ayantech.pushsdk.storage.EndPoint
import ir.ayantech.pushsdk.storage.PreferencesManager
import ir.ayantech.pushsdk.storage.PushUser

typealias SimpleCallBack = () -> Unit

typealias BooleanCallBack = (Boolean) -> Unit

typealias NotificationObjectsCallBack = (
    success: Boolean,
    notifications: List<NotificationObject<*>>?,
    getNextPageClosure: SimpleCallBack?
) -> Unit

typealias NotificationsSummaryCallBack = (
    success: Boolean,
    totalCount: Long,
    unSeenCount: Long
) -> Unit

object PushNotificationNetworking {
    lateinit var ayanApi: AyanApi

    private var applicationName: String? = null
    private var applicationType: String? = null
    private var applicationVersion: String? = null
    private var operatorName: String? = null

    fun initialize(context: Context, baseUrl: String? = null) {
        this.applicationName = context.resources.getString(R.string.applicationName)
        this.applicationType = context.resources.getString(R.string.applicationType)
        this.applicationVersion = getApplicationVersion(context)
        this.operatorName = getOperatorName(context)
        ayanApi = AyanApi(
            context = context,
            getUserToken = { "" },
            defaultBaseUrl = baseUrl ?: "https://pushnotification.infra.ayantech.ir/WebServices/App.svc/",
            logLevel = LogLevel.DO_NOT_LOG
        )
    }

    fun reportNewDevice(token: String, extraInfo: Any? = null) {
        ayanApi.ayanCall<Void>(
            AyanCallStatus {
                success {
                    PreferencesManager.getInstance(AyanNotification.context!!).save(
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
                applicationName,
                applicationType,
                applicationVersion,
                extraInfo ?: try {
                    PushUser.getPushNotificationExtraInfo<Any>()
                } catch (e: Exception) {
                    null
                },
                operatorName,
                "android",
                token
            ),
            identity = false
        )
    }

    fun reportDeviceMobileNumber(mobileNumber: String) {
        ayanApi.ayanCall<Void>(
            AyanCallStatus {
                success {
                    PreferencesManager.getInstance(AyanNotification.context!!).save(
                        Constants.SERVER_NOTIFIED_MOBILE,
                        true
                    )
                    Log.d(
                        "AyanPush",
                        "User mobile number successfully reported to the server."
                    )
                }
            },
            EndPoint.ReportDeviceMobileNumber,
            ReportDeviceMobileNumberInput(
                mobileNumber,
                PushUser.pushNotificationToken
            ),
            hasIdentity = false
        )
    }

    fun reportDeviceReceivedNotificationStatus(
        messageId: String?,
        status: String,
        extraInfo: Any? = null
    ) {
        if (messageId == null) return
        ayanApi.ayanCall<Void>(
            AyanCallStatus {
                success {
                    Log.d(
                        "AyanPush",
                        "Message status with $status status successfully reported to the server."
                    )
                }
            },
            EndPoint.ReportDeviceReceivedNotificationStatus,
            ReportDeviceReceivedNotificationStatusInput(extraInfo, messageId, status),
            hasIdentity = false
        )
    }

    fun getNotificationsList(
        itemCount: Long,
        offset: Long = 0L,
        callback: (success: Boolean, output: GetNotificationsListOutput?) -> Unit
    ) {
        ayanApi.ayanCall<GetNotificationsListOutput>(
            AyanCallStatus {
                success {
                    it.response?.Parameters?.let {
                        callback(true, it)
                    }
                }
                failure {
                    callback(false, null)
                }
            },
            EndPoint.GetNotificationsList,
            GetNotificationsListInput(
                itemCount,
                offset,
                PushUser.pushNotificationToken
            ),
            hasIdentity = false
        )
    }

    fun getNotificationDetail(
        notificationId: Long,
        callback: (success: Boolean, output: GetNotificationDetailOutput?) -> Unit
    ) {
        ayanApi.ayanCall<GetNotificationDetailOutput>(
            AyanCallStatus {
                success {
                    it.response?.Parameters?.let { callback(true, it) }
                }
                failure {
                    callback(false, null)
                }
            },
            EndPoint.GetNotificationDetail,
            GetNotificationDetailInput(
                notificationId,
                PushUser.pushNotificationToken
            ),
            hasIdentity = false
        )
    }

    fun removeNotification(notificationId: Long, callback: BooleanCallBack) {
        ayanApi.ayanCall<Void>(
            AyanCallStatus {
                success {
                    callback(true)
                }
                failure {
                    callback(false)
                }
            },
            EndPoint.RemoveNotification,
            RemoveNotificationInput(
                notificationId,
                PushUser.pushNotificationToken
            ),
            hasIdentity = false
        )
    }

    fun removeAllNotifications(callback: BooleanCallBack) {
        ayanApi.ayanCall<Void>(
            AyanCallStatus {
                success {
                    callback(true)
                }
                failure {
                    callback(false)
                }
            },
            EndPoint.RemoveAllNotifications,
            RemoveAllNotificationsInput(PushUser.pushNotificationToken),
            hasIdentity = false
        )
    }

    fun getNotificationsSummery(callBack: NotificationsSummaryCallBack) {
        ayanApi.ayanCall<GetNotificationsSummeryOutput>(
            AyanCallStatus {
                success {
                    callBack(
                        true,
                        it.response?.Parameters?.TotalCount ?: 0L,
                        it.response?.Parameters?.UnSeenCount ?: 0L
                    )
                }
                failure {
                    callBack(false, 0L, 0L)
                }
            },
            EndPoint.GetNotificationsSummery,
            GetNotificationsSummeryInput(PushUser.pushNotificationToken),
            hasIdentity = false
        )
    }
}