package ir.ayantech.pushsdk.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ir.ayantech.pushsdk.model.Config;
import ir.ayantech.pushsdk.model.Message;
import ir.ayantech.pushsdk.networking.PushNotificationNetworking;

public class IncomeMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message message = (Message) getIntent().getSerializableExtra("messageTag");
        if (message != null) {
            message.getAction().setContext(this);
            message.getAction().doAction();
            PushNotificationNetworking.INSTANCE.reportDeviceReceivedNotificationStatus(message.getMessageId(), "clicked", null);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            notificationManager.cancel(Config.NOTIFICATION_ID);
        } catch (Exception e) {
        }
        try {
            notificationManager.cancel(Config.NOTIFICATION_ID_BIG_IMAGE);
        } catch (Exception e) {
        }
        try {
            notificationManager.cancel(Config.NOTIFICATION_ID_CUSTOM);
        } catch (Exception e) {
        }
        finish();
    }

    public static Intent getIntentByMessage(Context context, Message message) {
        Intent intent = new Intent(context, IncomeMessageActivity.class);
        intent.putExtra("messageTag", message);
        return intent;
    }
}
