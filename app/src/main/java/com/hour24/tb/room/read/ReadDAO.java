package com.hour24.tb.room.read;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ReadDAO {

    /**
     * 저장된 URL 체크
     * @param url
     * @return
     */
    @Query("SELECT COUNT(url) FROM Read WHERE url = :url")
    int selectCount(String url);

    /**
     * URL 저장
     * @param model
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Read... model);

}
