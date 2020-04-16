package ir.ayantech.pushsdk.helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.core.app.ShareCompat;

/**
 * Created by Administrator on 7/17/2017.
 */

public class ShareHelper {
    public static void share(Context context, String shareBody) {
        ShareCompat.IntentBuilder.from((Activity) context)
                .setText(shareBody)
                .setType("text/plain")
                .setChooserTitle("به اشتراک گذاری از طریق:")
                .startChooser();
    }

    public static void copyToClipBoard(Context context, String string) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("ReferralCode", string);
        clipboard.setPrimaryClip(clip);
    }
}
