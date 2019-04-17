package com.hour24.tb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hour24.tb.BuildConfig;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
     * @author 장세진
     * @description Date class 로 변환
     */
    public static Date getFormatToDate(String format, final String dateTime) {
        try {
            // Date 처리
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.KOREA);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat.parse(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 다른 형태의 포맷으로 날짜 타입 변경
     */
    public static String getDateToFormat(final Date date, final String format) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
            return sdf.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
