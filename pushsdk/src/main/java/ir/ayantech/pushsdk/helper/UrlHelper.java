package ir.ayantech.pushsdk.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by S on 3/29/2017.
 */

public class UrlHelper {
    public static void openUrl(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(browserIntent);
    }
}
