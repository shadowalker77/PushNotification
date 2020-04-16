package ir.ayantech.pushsdk.model.api

data class GetNotificationDetailInput(
    val NotificationID: Long,
    val RegistrationToken: String
)

data class GetNotificationDetailOutput(
    val Notification: PushNotification,
    val NotificationID: Long,
    val Seen: Boolean,
    val SendDateTime: String
)

data class PushNotification(
    val AndroidChannelID: String,
    val Badge: String,
    val Body: String,
    val BodyLocArgs: List<String>,
    val BodyLocKey: String,
    val ClickAction: String,
    val Color: String,
    val Data: NotificationData,
    val Icon: String,
    val Sound: String,
    val Tag: String,
    val Title: String,
    val TitleLocArgs: List<String>,
    val TitleLocKey: String
)

data class NotificationData(val message: String)