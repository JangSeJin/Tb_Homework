package com.hour24.tb

import com.hour24.tb.const.DataConst
import com.hour24.tb.model.DocumentItem
import com.hour24.tb.utils.TextFormatUtils
import com.hour24.tb.utils.Utils
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {

    /**
     * String Format to Date Test
     */
    @Test
    fun getFormatToDateTest() {
        val date = Utils.getFormatToDate("yyyy-MM-dd'T'HH:mm:ss.SSSSX", "2019-03-09T19:32:30.000+09:00")
        assertNotNull(date)
    }

    /**
     * Date to yyyy년 MM월 dd일
     */
    @Test
    fun getDateToFormatTest() {
        val format = Utils.getDateToFormat(Date(), "yyyy년 MM월 dd일")
        assertNotNull(format)
    }

    /**
     * Filter에 따른 blogname, cafename Test
     */
    @Test
    fun getNameTest() {

        val item = DocumentItem()

        // blogname 확인
        item.filter = DataConst.FILTER_BLOG
        item.blogname = "blogNameTest"
        var name = TextFormatUtils.getName(item)
        assertNotNull(name)

        // cafename 확인
        item.filter = DataConst.FILTER_CAFE
        item.cafename = "cafeNameTest"
        name = TextFormatUtils.getName(item)
        assertNotNull(name)

    }


}
