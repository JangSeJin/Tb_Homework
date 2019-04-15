package com.hour24.tb.retrofit


import com.hour24.tb.model.SearchModel

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KakaoService {

    /**
     * 블로그 검색 - https://developers.kakao.com/docs/restapi/search#블로그-검색
     * 카페 검색 - https://developers.kakao.com/docs/restapi/search#카페-검색
     *
     * @return
     */
    @GET("/v2/search/{type}")
    fun reqKakaoSearch(
            @Path("type") type: String,
            @Query("query") query: String,
            @Query("sort") sort: String,
            @Query("page") page: Int,
            @Query("size") size: Int
    ): Call<SearchModel>


}
