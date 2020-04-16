package ir.ayantech.pushsdk.model.api

data class ReportDeviceReceivedNotificationStatusInput(
    val ExtraInfo: Any?,
    val MessageID: String,
    val Status: String
)