package com.hour24.tb.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hour24.tb.BuildConfig;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String TAG = Utils.class.getName();

    /**
     * Get Test
     */
    public static boolean isTest() {
        return BuildConfig.DEBUG;
    }

    /**
     * Get Hash Key
     *
     * @param activity
     */
    public static void getHashKey(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash", "" + Base64.encodeToString(md.digest(), Base64.DEFAULT).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context context
     * @param dp      dp
     * @author 장세진
     * @description Get DP
     */
    public static float getDpFromPx(Context context, float dp) {
        try {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dp;
    }

    /**
     * @author 장세진
     * @description 키보드 보임/숨김
     */
    public static void setKeyboardShowHide(final Activity activity, final View view, final boolean isShow) {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (isShow) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    } else {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 다른 형태의 포맷으로 날짜 타입 변경
     */
    public static String getConvertDateFormat(final long time, final String format) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
            return sdf.format(time);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 멀티라인 제한
     */
    public static String getMultiLineDisable(String content, String enter) {
        try {

            // 세줄이상 잘라버리기
            String block = content;
            String a = enter;
            for (int i = 0; i < 100; i++) {
                a += "\n";
                block = block.replaceAll(enter, "\n\n");
            }
            return block;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * 다른 형태의 포맷으로 날짜 타입 변경
     */
    public static String getConvertDateFormat(final String date, final String originalFormat, final String convertFormat) {
        try {

            if (TextUtils.isEmpty(date)) {
                return date;
            }

            SimpleDateFormat sdf = new SimpleDateFormat(originalFormat, Locale.KOREA);
            Date d = sdf.parse(date);

            sdf = new SimpleDateFormat(convertFormat, Locale.KOREA);

            return sdf.format(d);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 클립보드 복사
     *
     * @param context
     * @param text
     */
    public static void setClipBoard(Context context, String text) {

        try {

            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("label", text);
            clipboardManager.setPrimaryClip(clipData);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 파일 확장자 가져오기
     *
     * @param fileStr 경로나 파일이름
     * @return
     */
    public static String getExtension(String fileStr) {
        String fileExtension = fileStr.substring(fileStr.lastIndexOf(".") + 1, fileStr.length());
        return TextUtils.isEmpty(fileExtension) ? null : fileExtension;
    }
}
