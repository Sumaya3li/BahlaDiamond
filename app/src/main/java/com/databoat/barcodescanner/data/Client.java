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
     String idst_type;

    public String getIdst_type() {
        return idst_type;
    }

    public void setIdst_type(String idst_type) {
        this.idst_type = idst_type;
    }

    public Client(String idts, String name,String idst_type) {
        this.idts = idts;
        this.name = name;
        this.idst_type = idst_type;
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
