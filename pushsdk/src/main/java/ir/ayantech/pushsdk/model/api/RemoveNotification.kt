package ir.ayantech.pushsdk.model.api

data class RemoveNotificationInput(
    val NotificationID: Long,
    val RegistrationToken: String
)