package ir.ayantech.pushsdk.model.action;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.OkHttpDownloader;
import com.coolerfall.download.Priority;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import ir.ayantech.pushsdk.activity.DownloadFileDialog;

public class DownloadFileAction extends PushNotificationAction implements Serializable {

    private Model model;

    public DownloadFileAction(String title,
                              String message,
                              String fileType,
                              String fileUrl,
                              boolean openAfterDownload,
                              boolean cancellable,
                              boolean silent) {
        this.model = new Model(title,
                message,
                fileType,
                fileUrl,
                openAfterDownload,
                cancellable,
                silent);
    }

    @Override
    public void doAction() {
        if (model.isSilent()) {
            download(getContext(), model.getFileUrl(), new DownloadCallback() {
                @Override
                public void onStart(int downloadId, long totalBytes) {
                    super.onStart(downloadId, totalBytes);
                }

                @Override
                public void onRetry(int downloadId) {
                    super.onRetry(downloadId);
                }

                @Override
                public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
                    super.onProgress(downloadId, bytesWritten, totalBytes);
                }

                @Override
                public void onSuccess(int downloadId, String filePath) {
                    super.onSuccess(downloadId, filePath);
                    DownloadFileAction.openFile(getContext(), filePath);
                }

                @Override
                public void onFailure(int downloadId, int statusCode, String errMsg) {
                    super.onFailure(downloadId, statusCode, errMsg);
                }
            });
        } else {
            DownloadFileDialog.createAndStartActivity(getContext(), model);
        }
    }

    public static int download(Context context, String fileLink, DownloadCallback callback) {
        DownloadManager manager = new DownloadManager.Builder().context(context)
                .downloader(OkHttpDownloader.create())
                .threadPoolSize(2)
                .build();
        String fileName = fileLink.split("/")[fileLink.split("/").length - 1];
        String destPath = getRootDirPath(context) + "/" + fileName;
        DownloadRequest request = new DownloadRequest.Builder()
                .url(fileLink)
                .retryTime(5)
                .retryInterval(2, TimeUnit.SECONDS)
                .progressInterval(100, TimeUnit.MILLISECONDS)
                .priority(Priority.HIGH)
                .destinationFilePath(destPath)
                .downloadCallback(callback)
                .build();

        return manager.add(request);
    }

    public static void openFile(Context context, String filePath) {
        if (Build.VERSION.SDK_INT >= 24) {
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            String mimeType = myMime.getMimeTypeFromExtension(fileExt(filePath).substring(1));
            newIntent.setDataAndType(FileProvider.getUriForFile(context, context.getPackageName() +
                    ".provider", new File(filePath)), mimeType);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                context.startActivity(newIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            String mimeType = myMime.getMimeTypeFromExtension(fileExt(filePath).substring(1));
            newIntent.setDataAndType(Uri.fromFile(new File(filePath)), mimeType);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "دستگاه شما امکان باز کردن این فایل را ندارد.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static String fileExt(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.contains("/")) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();
        }
    }

    private static String getRootDirPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return context.getFilesDir().getAbsolutePath();
        else if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return context.getExternalFilesDir(null).getAbsolutePath();
        else
            return null;
    }

    public static class Model extends ActionModel {
        private String title;
        private String message;
        private String fileType;
        private String fileUrl;
        private boolean openAfterDownload;
        private boolean cancellable;
        private boolean silent;

        public Model(String title, String message, String fileType, String fileUrl, boolean openAfterDownload, boolean cancellable, boolean silent) {
            this.title = title;
            this.message = message;
            this.fileType = fileType;
            this.fileUrl = fileUrl;
            this.openAfterDownload = openAfterDownload;
            this.cancellable = cancellable;
            this.silent = silent;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public boolean isOpenAfterDownload() {
            return openAfterDownload;
        }

        public void setOpenAfterDownload(boolean openAfterDownload) {
            this.openAfterDownload = openAfterDownload;
        }

        public boolean isCancellable() {
            return cancellable;
        }

        public void setCancellable(boolean cancellable) {
            this.cancellable = cancellable;
        }

        public boolean isSilent() {
            return silent;
        }

        public void setSilent(boolean silent) {
            this.silent = silent;
        }
    }
}
