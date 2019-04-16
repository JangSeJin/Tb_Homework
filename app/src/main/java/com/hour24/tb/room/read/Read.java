package com.hour24.tb.room.read;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Read {

    @PrimaryKey
    @NonNull
    public String url;

    public Read(String url) {
        this.url = url;
    }
}
