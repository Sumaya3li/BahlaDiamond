package com.databoat.barcodescanner.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "current")
public class Current {

    @PrimaryKey()
    @NonNull
    private String idst;
    private String perusal;
    private String note;

    public Current(@NonNull String idst, String perusal, String note) {
        this.idst = idst;
        this.perusal = perusal;
        this.note = note;
    }

    @NonNull
    public String getIdst() {
        return idst;
    }

    public void setIdst(@NonNull String idst) {
        this.idst = idst;
    }

    public String getPerusal() {
        return perusal;
    }

    public void setPerusal(String perusal) {
        this.perusal = perusal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
