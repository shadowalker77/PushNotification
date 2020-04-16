package ir.ayantech.pushsdk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.OkHttpDownloader;

import ir.ayantech.pushsdk.R;
import ir.ayantech.pushsdk.model.action.DownloadFileAction;

public class DownloadFileDialog extends AppCompatActivity {

    public static final String TITLE_TAG = "titleTag";
    public static final String MESSAGE_TAG = "messageTag";
    public static final String LINK_TAG = "linkTag";
    public static final String CANCELLABLE_TAG = "cancellableTag";

    private int id = -1;
    private DownloadManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_file);
//        this.setCancelable(false);
//        this.setCanceledOnTouchOutside(false);
        setFinishOnTouchOutside(false);

        if (!isCancellable())
            findViewById(R.id.negativeTv).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.titleTv)).setText(getDialogTitle());
        ((TextView) findViewById(R.id.messageTv)).setText(getDialogMessage());
        manager = new DownloadManager.Builder().context(this)
                .downloader(OkHttpDownloader.create())
                .threadPoolSize(2)
                .build();
        try {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            findViewById(R.id.progressTv).setVisibility(View.VISIBLE);
            id = DownloadFileAction.download(this, getFileLink(), new DownloadCallback() {
                @Override
                public void onStart(int downloadId, long totalBytes) {
                }

                @Override
                public void onRetry(int downloadId) {
                }

                @Override
                public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
                    long progressPercent = bytesWritten * 100 / totalBytes;
                    ((ProgressBar) findViewById(R.id.progressBar)).setProgress((int) progressPercent);
                    ((TextView) findViewById(R.id.progressTv)).setText(String.format("%%%s", String.valueOf(progressPercent)));
                }

                @Override
                public void onSuccess(int downloadId, String filePath) {
                    try {
                        DownloadFileAction.openFile(DownloadFileDialog.this, filePath);
                        DownloadFileDialog.this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int downloadId, int statusCode, String errMsg) {
                    DownloadFileDialog.this.finish();
                }
            });
        } catch (Exception e) {
            DownloadFileDialog.this.finish();
        }
        findViewById(R.id.negativeTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    manager.cancel(id);
                } catch (Exception e) {
                }
                DownloadFileDialog.this.finish();
            }
        });
    }

    private String getDialogTitle() {
        return getIntent().getStringExtra(TITLE_TAG);
    }

    private String getDialogMessage() {
        return getIntent().getStringExtra(MESSAGE_TAG);
    }

    private String getFileLink() {
        return getIntent().getStringExtra(LINK_TAG);
    }

    private boolean isCancellable() {
        return getIntent().getBooleanExtra(CANCELLABLE_TAG, false);
    }

    public static void createAndStartActivity(Context context, DownloadFileAction.Model model) {
        Intent intent = new Intent(context, DownloadFileDialog.class);
        intent.putExtra(TITLE_TAG, model.getTitle());
        intent.putExtra(MESSAGE_TAG, model.getMessage());
        intent.putExtra(LINK_TAG, model.getFileUrl());
        intent.putExtra(CANCELLABLE_TAG, model.isCancellable());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}