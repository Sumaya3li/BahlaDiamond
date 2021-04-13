package com.databoat.barcodescanner.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clients")
public class Client {

    @PrimaryKey()
    @NonNull
    String idts;
    String name;

    public Client(String idts, String name) {
        this.idts = idts;
        this.name = name;
    }

    public String getIdts() {
        return idts;
    }

    public void setIdts(String idts) {
        this.idts = idts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
