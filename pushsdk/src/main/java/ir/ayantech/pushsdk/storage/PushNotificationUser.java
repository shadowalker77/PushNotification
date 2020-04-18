package ir.ayantech.pushsdk.storage;

import com.google.gson.Gson;

public class PushNotificationUser {

    public static final String PUSH_NOTIFICATION_TOKEN = "pushNotificationToken";
    public static final String USER_MOBILE = "userMobile";
    public static final String PUSH_NOTIFICATION_EXTRA_INFO = "pushNotificationExtraInfo";
    private static final String PUSH_NOTIFICATION_EXTRA_INFO_CLASS = "pushNotificationExtraInfoClass";

    public static String getPushNotificationToken() {
        return PreferencesManager.readStringFromSharedPreferences(PUSH_NOTIFICATION_TOKEN);
//        return "cz5je04gj3s:APA91bHCGYdZ2rnr8YZcRq-DPWdoiFThEQwc84Qu-gMSvmoFDF3gEMcRY1N-L-O9Ot41zCKXfdJ3kv3nTB3QfKGyHvb28g5y6txhM4AYSlcpIFfm80wi-3iDy8DsME8cAylj1pUAQmPa";
    }

    public static String getUserMobile() {
        return PreferencesManager.readStringFromSharedPreferences(USER_MOBILE);
    }

    public static <T> T getPushNotificationExtraInfo() throws ClassNotFoundException {
        return ((T) new Gson().fromJson(PreferencesManager.readStringFromSharedPreferences(PUSH_NOTIFICATION_EXTRA_INFO),
                Class.forName(getPushNotificationExtraInfoClass())));
    }

    private static String getPushNotificationExtraInfoClass() {
        return PreferencesManager.readStringFromSharedPreferences(PUSH_NOTIFICATION_EXTRA_INFO_CLASS);
    }

    public static void setPushNotificationToken(String pushNotificationToken) {
        PreferencesManager.saveToSharedPreferences(PUSH_NOTIFICATION_TOKEN, pushNotificationToken);
    }

    public static void setUserMobile(String userMobile) {
        PreferencesManager.saveToSharedPreferences(USER_MOBILE, userMobile);
    }

    public static <T> void setPushNotificationExtraInfo(T pushNotificationExtraInfo) {
        setPushNotificationExtraInfoClass(pushNotificationExtraInfo);
        PreferencesManager.saveToSharedPreferences(PUSH_NOTIFICATION_EXTRA_INFO, new Gson().toJson(pushNotificationExtraInfo));
    }

    private static <T> void setPushNotificationExtraInfoClass(T pushNotificationExtraInfo) {
        PreferencesManager.saveToSharedPreferences(PUSH_NOTIFICATION_EXTRA_INFO_CLASS, pushNotificationExtraInfo.getClass().getName());
    }
}
