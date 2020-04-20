package ir.ayantech.pushsdk.model.api

data class GetNotificationsSummeryInput(
    val RegistrationToken: String
)

data class GetNotificationsSummeryOutput(
    val TotalCount: Long,
    val UnSeenCount: Long
)