package ir.ayantech.pushsdk.model.action;

import android.content.Context;

import java.io.Serializable;

public abstract class PushNotificationAction implements Serializable {
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public abstract void doAction();
}
