package com.hour24.tb.room.recent;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Recent {

    @PrimaryKey
    @NonNull
    public String search;

    public Recent(String search) {
        this.search = search;
    }
}
