package com.hour24.tb.utils

import android.app.Activity
import com.hour24.tb.R
import com.hour24.tb.const.DataConst
import com.hour24.tb.model.DocumentItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

object TextFormatUtils {

    /**
     * Name 추출
     */
    fun getName(model: DocumentItem): String {

        try {

            return if (model.filter == DataConst.FILTER_BLOG) {
                model.blogname
            } else {
                model.cafename
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * yyyy-MM-dd'T'HH:mm:ssXXX 날짜 변경
     * 어제, 오늘, 그외 (YYYY년 MM월 DD일)
     */
    fun getDate(activity: Activity, date: Date): String {

        try {

            val nowDate: Date = Date() // 현재

            // 날자비교
            val calDate = nowDate.time - date.time
            val calDateDays = (calDate / (24 * 60 * 60 * 1000)).toInt()

            return when (calDateDays) {
                0 -> activity.getString(R.string.main_today)
                1 -> activity.getString(R.string.main_yesterday)
                else -> {
                    val format = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
                    format.format(date)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

}
