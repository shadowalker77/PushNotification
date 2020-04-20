package ir.ayantech.pushsdk.model.action;

import android.content.Intent;
import android.content.pm.PackageManager;

import java.io.Serializable;

public class OpenApplicationAction extends PushNotificationAction implements Serializable {
    @Override
    public void doAction() {
        PackageManager pm = getContext().getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(getContext().getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }
}
