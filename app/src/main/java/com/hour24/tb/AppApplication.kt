package com.hour24.tb

import android.app.Application
import com.hour24.tb.room.AppDatabase

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 최근검색 Instance 생성
        AppDatabase.getInstance(this)

    }
}