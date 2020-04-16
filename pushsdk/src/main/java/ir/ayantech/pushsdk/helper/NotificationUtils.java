package ir.ayantech.pushsdk.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Random;

import ir.ayantech.pushsdk.R;
import ir.ayantech.pushsdk.activity.CustomizableDialogActivity;
import ir.ayantech.pushsdk.activity.IncomeMessageActivity;
import ir.ayantech.pushsdk.model.Config;
import ir.ayantech.pushsdk.model.Message;

public class NotificationUtils {

    public static void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, String imageUrl,
                                                            List<CustomizableDialogActivity.Button> buttonList,
                                                            final String bigIconUrl, boolean isCustom) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NotificationUtils.showNotificationMessage(context, title, message,intent, imageUrl, buttonList, bigIconUrl, isCustom);
    }

    public static void showNotificationMessage(final Context context,
                                               final String title,
                                               final String message,
                                               final Intent intent,
                                               final String imageUrl,
                                               final List<CustomizableDialogActivity.Button> buttonList,
                                               final String bigIconUrl,
                                               final boolean isCustom) {
        if (TextUtils.isEmpty(message))
            return;

        final int icon = R.drawable.pushIcon;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context, "push");

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getPackageName() + "/raw/notification");
//        if (!TextUtils.isEmpty(bigIconUrl)) {
//            if (bigIconUrl.length() > 4 && Patterns.WEB_URL.matcher(bigIconUrl).matches()) {
        ImageHelper.downloadImage(bigIconUrl, new ImageHelper.OnBitmapDownloaded() {
            @Override
            public void onBitmapDownloaded(final Bitmap bigIcon) {
                if (imageUrl != null) {
                    ImageHelper.downloadImage(imageUrl, new ImageHelper.OnBitmapDownloaded() {
                        @Override
                        public void onBitmapDownloaded(Bitmap bitmap) {
                            if (isCustom)
                                showCustomNotification(context, bitmap, mBuilder, icon, title, message, resultPendingIntent, alarmSound, buttonList, bigIcon);
                            else
                                showBigNotification(context, bitmap, mBuilder, icon, title, message, resultPendingIntent, alarmSound, buttonList, bigIcon);
                        }
                    });
                } else {
                    if (isCustom)
                        showCustomNotification(context, null, mBuilder, icon, title, message, resultPendingIntent, alarmSound, buttonList, bigIcon);
                    else
                        showSmallNotification(mBuilder, context, icon, title, message, resultPendingIntent, alarmSound, buttonList, bigIcon);
                }
            }
        });
//            }
//        }
    }


    private static void showSmallNotification(NotificationCompat.Builder mBuilder,
                                              Context context,
                                              int icon,
                                              String title,
                                              String message,
                                              PendingIntent resultPendingIntent,
                                              Uri alarmSound,
                                              List<CustomizableDialogActivity.Button> buttonList,
                                              final Bitmap bigIconBitmap) {

        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();

        inboxStyle.bigText(message);

        Notification notification;
        mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
//                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.drawable.pushIcon)
                .setLargeIcon(bigIconBitmap)
                .setContentText(message);
        if (buttonList != null)
            if (!buttonList.isEmpty()) {
                for (CustomizableDialogActivity.Button b : buttonList) {
                    mBuilder.addAction(0, b.getText(), getPendingIntentByMessage(context, b.getMessage()));
                }
                mBuilder.setColor(ContextCompat.getColor(context, R.color.colorAccent));
            }
        notification = mBuilder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "push";// The id of the channel.
            CharSequence name = context.getResources().getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        /////////////////

        if (notificationManager != null) {
            notificationManager.notify(Config.NOTIFICATION_ID, notification);
        }
    }

    private static void showBigNotification(Context context,
                                            Bitmap bitmap,
                                            NotificationCompat.Builder mBuilder,
                                            int icon,
                                            String title,
                                            String message,
                                            PendingIntent resultPendingIntent,
                                            Uri alarmSound,
                                            List<CustomizableDialogActivity.Button> buttonList,
                                            final Bitmap bigIconBitmap) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
//                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.drawable.pushIcon)
                .setLargeIcon(bigIconBitmap)
                .setContentText(message);

        if (buttonList != null)
            if (!buttonList.isEmpty()) {
                for (CustomizableDialogActivity.Button b : buttonList) {
                    mBuilder.addAction(0, b.getText(), getPendingIntentByMessage(context, b.getMessage()));
                }
                mBuilder.setColor(ContextCompat.getColor(context, R.color.colorAccent));
            }
        notification = mBuilder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "push";// The id of the channel.
            CharSequence name = context.getResources().getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        /////////////////
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    private static void showCustomNotification(Context context,
                                               Bitmap bitmap,
                                               NotificationCompat.Builder builder,
                                               int icon,
                                               String title,
                                               String message,
                                               PendingIntent resultPendingIntent,
                                               Uri alarmSound,
                                               List<CustomizableDialogActivity.Button> buttonList,
                                               final Bitmap bigIconBitmap) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "push";// The id of the channel.
            CharSequence name = context.getResources().getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        /////////////////
        builder.setSmallIcon(icon);
        builder.setSound(alarmSound);
        builder.setContentIntent(resultPendingIntent);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remote_view_custom_notification);
        remoteViews.setTextViewText(R.id.notificationTitleTv, title);
        ///////////////
        remoteViews.setTextViewText(R.id.notificationMessageTv, message);
        ///////////////
        if (bitmap != null)
            remoteViews.setImageViewBitmap(R.id.notificationBigImageIv, bitmap);
        if (bigIconBitmap != null)
            remoteViews.setImageViewBitmap(R.id.notificationBigIconIv, bigIconBitmap);
        else
            remoteViews.setViewVisibility(R.id.notificationBigIconIv, View.GONE);
        if (buttonList != null) {
            if (!buttonList.isEmpty()) {
                remoteViews.setTextViewText(R.id.notificationBtn1Tv, buttonList.get(0).getText());
                remoteViews.setOnClickPendingIntent(R.id.notificationBtn1Tv, getPendingIntentByMessage(context, buttonList.get(0).getMessage()));
            }
            if (buttonList.size() > 1) {
                remoteViews.setTextViewText(R.id.notificationBtn2Tv, buttonList.get(1).getText());
                remoteViews.setOnClickPendingIntent(R.id.notificationBtn2Tv, getPendingIntentByMessage(context, buttonList.get(1).getMessage()));
            }
            if (buttonList.size() > 2) {
                remoteViews.setTextViewText(R.id.notificationBtn3Tv, buttonList.get(2).getText());
                remoteViews.setOnClickPendingIntent(R.id.notificationBtn3Tv, getPendingIntentByMessage(context, buttonList.get(2).getMessage()));
            }
        } else {
            remoteViews.setViewVisibility(R.id.notificationButtonsLl, View.GONE);
        }
        builder.setCustomContentView(remoteViews);
        builder.setCustomBigContentView(remoteViews);
        Notification notification = builder.build();
        notificationManager.notify(Config.NOTIFICATION_ID_CUSTOM, notification);
    }

    public static void playNotificationSound(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PendingIntent getPendingIntentByMessage(Context context, Message message) {
        return PendingIntent.getActivity(
                context,
                new Random().nextInt(1000),
                IncomeMessageActivity.getIntentByMessage(context, message),
                PendingIntent.FLAG_CANCEL_CURRENT
        );
    }

}
