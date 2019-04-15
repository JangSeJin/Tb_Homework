package com.hour24.tb.view.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hour24.tb.interfaces.WebViewListener;
import com.hour24.tb.utils.Logger;

public class WebBaseViewClient extends WebViewClient {

    // Log Tag
    private static final String TAG = WebBaseViewClient.class.getName();

    private Activity mActivity;

    // 페이지 상태
    private WebViewListener.OnPageStatusListener mPageStatusListener;

    public WebBaseViewClient(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onPageStarted(final WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Logger.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + url);

        try {

            if (mPageStatusListener != null) {
                mPageStatusListener.onPageStarted(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Logger.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + url);

        try {

            // Cookie Sync Manager - Sync
            CookieSyncManager.getInstance().sync();

            if (mPageStatusListener != null) {
                mPageStatusListener.onPageFinished(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        Logger.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        resend.sendToTarget();
    }

    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        try {
            if (mActivity != null && mActivity.isFinishing()) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("이 사이트의 보안 인증서는 신뢰할 수 없습니다.\n계속 진행하시겠습니까?")
                        .setPositiveButton(mActivity.getString(android.R.string.ok), new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                handler.proceed();
                            }
                        })
                        .setNegativeButton(mActivity.getString(android.R.string.cancel), new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                handler.cancel();
                            }
                        })
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnPageStatusListener(WebViewListener.OnPageStatusListener listener) {
        this.mPageStatusListener = listener;
    }

}
