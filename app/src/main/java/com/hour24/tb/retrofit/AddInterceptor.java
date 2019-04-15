package com.hour24.tb.retrofit;

import android.content.Context;

import com.hour24.tb.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by N16326 on 2018. 10. 17..
 */

public class AddInterceptor implements Interceptor {

    private Context mContext;

    public AddInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Authorization", "KakaoAK " + mContext.getString(R.string.kakao_rest_app_key));
        Response response = chain.proceed(builder.build());

        return response;
    }
}