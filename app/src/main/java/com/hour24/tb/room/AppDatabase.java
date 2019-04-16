package com.hour24.tb.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import com.hour24.tb.room.recent.Recent;
import com.hour24.tb.room.recent.RecentDAO;
import com.hour24.tb.view.viewmodel.RecentViewModel;

import java.util.List;

@Database(entities = {Recent.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public interface OnSelectRecentListener {
        public void recent(List<Recent> list);
    }

    private static final String TAG = AppDatabase.class.getName();

    private static volatile AppDatabase mInstance;

    public abstract RecentDAO recentDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = create(context);
        }
        return mInstance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "tb_homework.db").build();
    }

    /**
     * 최근검색 저장
     *
     * @param search
     */
    public static void insert(final String search) {

        try {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    Recent recent = new Recent(search);
                    mInstance.recentDao().insert(recent);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 최근검색 검색
     *
     * @param search
     */
    public static void select(final OnSelectRecentListener listener) {

        try {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    listener.recent(mInstance.recentDao().selectAll());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}