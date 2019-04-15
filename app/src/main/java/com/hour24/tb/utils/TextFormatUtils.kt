package com.hour24.tb.utils

import android.app.Activity
import com.hour24.tb.R
import com.hour24.tb.const.APIConst
import java.text.SimpleDateFormat
import java.util.*

object TextFormatUtils {

    /**
     * blog, cafe 추출
     *
     * @return
     */
    fun getTypeName(activity: Activity, type: String): String {

        try {

            return if (APIConst.TYPE_BLOG == type) {
                activity.getString(R.string.main_type_blog)
            } else {
                activity.getString(R.string.main_type_cafe)
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
    fun getDate(activity: Activity, dateTime: String ): String {

        try {

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSX", Locale.KOREA)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")

            val nowDate: Date = Date() // 현재
            val apiDate: Date = simpleDateFormat.parse(dateTime) // api 에서 받아온 날짜

            // 날자비교
            val calDate = nowDate.time - apiDate.time
            val calDateDays = (calDate / (24 * 60 * 60 * 1000)).toInt()

            return when (calDateDays) {
                0 -> activity.getString(R.string.main_today)
                1 -> activity.getString(R.string.main_yesterday)
                else -> {
                    val format = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
                    format.format(apiDate)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

}
