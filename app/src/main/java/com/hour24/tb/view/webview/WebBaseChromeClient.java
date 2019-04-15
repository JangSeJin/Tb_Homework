package com.hour24.tb.view.webview;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.hour24.tb.interfaces.WebViewListener;

public class WebBaseChromeClient extends WebChromeClient {

    // Log Tag
    private static final String TAG = WebBaseChromeClient.class.getName();

    // Variable
    private Activity mActivity;

    private WebViewListener.OnReceivedTitleListener mTitleListener;
    private WebViewListener.OnProgressChangedListener mOnProgressChangedListener;

    public WebBaseChromeClient(Activity activity) {
        this.mActivity = activity;
    }


    public void setOnReceivedTitleListener(WebViewListener.OnReceivedTitleListener listener) {
        this.mTitleListener = listener;
    }

    public void setOnProgressChangedListener(WebViewListener.OnProgressChangedListener listener) {
        this.mOnProgressChangedListener = listener;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        if (mActivity != null && !mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity)
                    .setTitle(message)
                    .setPositiveButton(mActivity.getString(android.R.string.ok), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            result.confirm();
                        }
                    })
                    .show();
        }
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        if (mActivity != null && !mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity)
                    .setMessage(message)
                    .setPositiveButton(mActivity.getString(android.R.string.ok), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            result.confirm();
                        }
                    })
                    .setNegativeButton(mActivity.getString(android.R.string.cancel), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            result.cancel();
                        }
                    })
                    .show();
        }
        return true;
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, final JsResult result) {
        if (mActivity != null && !mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity)
                    .setMessage(message)
                    .setPositiveButton(mActivity.getString(android.R.string.ok), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            result.confirm();
                        }
                    })
                    .setNegativeButton(mActivity.getString(android.R.string.cancel), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            result.cancel();
                        }
                    })
                    .show();
        }
        return true;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onProgressChanged(newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (mTitleListener != null) {
            mTitleListener.onReceivedTitle(title);
        }
    }
}