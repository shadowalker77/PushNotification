package ir.ayantech.pushsdk.helper

import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager

fun getOperatorName(context: Context): String? {
    val manager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return manager.networkOperatorName
}

fun getApplicationVersion(context: Context): String {
    try {
        return context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}