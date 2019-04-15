package com.hour24.tb.utils;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixplicity.htmlcompat.HtmlCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class BindingAdapterUtils {

    public static String TAG = BindingAdapterUtils.class.getName();

    /**
     * 특정 텍스트 컬러 변경
     *
     * @param view
     * @param search      - 엑센트를 부여할 텍스트
     * @param text        - 원본 텍스트
     * @param accentColor - 컬러
     */
    @BindingAdapter({"accentSearch", "accentText", "accentColor"})
    public static void setAccentTextColor(final TextView view, final String search, final String text, final int accentColor) {
        try {


            int start = 0;
            int end = 0;

            if (search != null && text != null) {
                start = text.indexOf(search);
                end = start + search.length();

//                        Logger.e(TAG, "start : " + start);
//                        Logger.e(TAG, "end : " + end);

                if (start > -1) {
                    SpannableStringBuilder ssb = new SpannableStringBuilder(text);
                    ssb.setSpan(new ForegroundColorSpan(accentColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.setText(ssb);
                } else {
                    view.setText(text);
                }
            } else {
                view.setText(search);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 특정 폰트 변경 (굵게)
     *
     * @param view
     * @param search
     * @param text
     */
    @BindingAdapter({"accentSearch", "accentText"})
    public static void setAccentTextFont(final TextView view, final String search, final String text) {
        try {

            int start = 0;
            int end = 0;

            if (search != null && text != null) {
                start = text.indexOf(search);
                end = start + search.length();
                Logger.e(TAG, "start : " + start);
                Logger.e(TAG, "end : " + end);

                if (start > -1) {

                    SpannableStringBuilder ssb = new SpannableStringBuilder(text);
                    ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    view.setText(ssb);

                } else {
                    view.setText(text);
                }
            } else {
                view.setText(search);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 배경 색상 변경
     */
    @BindingAdapter({"backgroundColor"})
    public static void setAccentTextColor(final View view, final int color) {
        try {

            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setBackgroundColor(color);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 배경 색상 String 변경
     */
    @BindingAdapter({"backgroundColor"})
    public static void setBackgroundColor(final View view, final String color) {
        try {

            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setBackgroundColor(Color.parseColor(color));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 다른 형태의 포맷으로 날짜 타입 변경
     *
     * @param view
     * @param date
     * @param originalFormat
     * @param convertFormat
     */
    @BindingAdapter({"date", "originalFormat", "convertFormat"})
    public static void setConvertDateFormat(final TextView view, final String date, final String originalFormat, final String convertFormat) {
        try {

            view.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (TextUtils.isEmpty(date)) {
                            return;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat(originalFormat, Locale.KOREA);
                        Date d = sdf.parse(date);

                        sdf = new SimpleDateFormat(convertFormat, Locale.KOREA);
                        String result = sdf.format(d);

                        view.setText(result);

                    } catch (Exception e) {
                        e.printStackTrace();

                        view.setText(date);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 다른 형태의 포맷으로 날짜 타입 변경
     *
     * @param view
     * @param date
     * @param convertFormat
     */
    @BindingAdapter({"date", "convertFormat"})
    public static void setConvertDateFormat(final TextView view, final long date, final String convertFormat) {
        try {

            view.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat(convertFormat, Locale.KOREA);
                        view.setText(sdf.format(date));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 새로고침 컬러 변경
     *
     * @param view
     * @param color
     */
    @BindingAdapter({"refreshColor"})
    public static void setColorSchemeResources(final SwipeRefreshLayout view, final int color) {
        try {
            view.setColorSchemeColors(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 이미지뷰 이미지 세팅
     *
     * @param view
     * @param resId
     */
    @BindingAdapter({"backgroundResource"})
    public static void setBackgroundResource(final ImageView view, final int resId) {
        try {
            view.setBackgroundResource(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 리사이클러뷰 애니메이션 적용 여부
     *
     * @param view
     * @param isItemAnimator
     */
    @BindingAdapter({"itemAnimator"})
    public static void setItemAnimator(final RecyclerView view, final boolean isItemAnimator) {
        try {

            if (isItemAnimator) {
                view.setItemAnimator(new DefaultItemAnimator());
            } else {
                view.setItemAnimator(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * html format
     *
     * @param view
     * @param html
     */
    @BindingAdapter({"htmlFormat"})
    public static void setHtmlFormat(final TextView view, final String html) {
        try {
            Spanned fromHtml = HtmlCompat.fromHtml(view.getContext(), html, 0);
            view.setText(fromHtml);
        } catch (Exception e) {
            e.printStackTrace();
            view.setText(html);
        }
    }

}
