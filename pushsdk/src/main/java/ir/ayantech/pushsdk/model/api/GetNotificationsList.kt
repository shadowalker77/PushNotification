package ir.ayantech.pushsdk.model.api

data class GetNotificationsListInput(
    val Limit: Long,
    val Offset: Long,
    val RegistrationToken: String
)

data class GetNotificationsListOutput(
    val HasMore: Boolean,
    val NextOffset: Long,
    val Notifications: List<GetNotificationDetailOutput>?,
    val TotalCount: Long,
    val UnSeenCount: Long
)