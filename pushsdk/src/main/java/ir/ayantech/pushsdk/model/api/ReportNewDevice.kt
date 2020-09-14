package ir.ayantech.pushsdk.model.api

data class ReportNewDeviceInput(
    val ApplicationName: String?,
    val ApplicationType: String?,
    val ApplicationVersion: String?,
    val ExtraInfo: Any?,
    val OperatorName: String?,
    val Platform: String,
    val RegistrationToken: String
)