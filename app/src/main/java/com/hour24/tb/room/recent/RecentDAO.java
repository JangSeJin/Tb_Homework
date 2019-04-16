package com.hour24.tb.room.recent;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecentDAO {

    @Query("SELECT * FROM Recent")
    List<Recent> selectAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recent... model);

}
