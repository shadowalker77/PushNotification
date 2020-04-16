package ir.ayantech.pushsdk.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageHelper {

    public static void downloadImage(String url, OnBitmapDownloaded onBitmapDownloaded) {
        new DownloadImage(onBitmapDownloaded).execute(url);
    }

    private static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        private OnBitmapDownloaded onBitmapDownloaded;

        public DownloadImage(OnBitmapDownloaded onBitmapDownloaded) {
            this.onBitmapDownloaded = onBitmapDownloaded;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getBitmapFromURL(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            onBitmapDownloaded.onBitmapDownloaded(bitmap);
        }
    }

    public static Bitmap convertBase64ToBitmap(String base64) {
        return BitmapFactory.decodeStream(new ByteArrayInputStream(
                        Base64.decode(base64, Base64.DEFAULT)
                )
        );
    }

    public interface OnBitmapDownloaded {
        void onBitmapDownloaded(Bitmap bitmap);
    }
}
