package com.databoat.barcodescanner.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "data")
public class Form {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String idst;
    private String name_id;
    private String perusal_previous;
    private String perusal_current;
    private String idst_type;
    private String consumption;
    private String note;
    private String date_do;

    public Form(String idst, String name_id, String perusal_previous, String perusal_current, String idst_type, String consumption, String note, String date_do) {
        this.idst = idst;
        this.name_id = name_id;
        this.perusal_previous = perusal_previous;
        this.perusal_current = perusal_current;
        this.idst_type = idst_type;
        this.consumption = consumption;
        this.note = note;
        this.date_do = date_do;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdst() {
        return idst;
    }

    public void setIdst(String idst) {
        this.idst = idst;
    }

    public String getName_id() {
        return name_id;
    }

    public void setName_id(String name_id) {
        this.name_id = name_id;
    }

    public String getPerusal_previous() {
        return perusal_previous;
    }

    public void setPerusal_previous(String perusal_previous) {
        this.perusal_previous = perusal_previous;
    }

    public String getPerusal_current() {
        return perusal_current;
    }

    public void setPerusal_current(String perusal_current) {
        this.perusal_current = perusal_current;
    }

    public String getIdst_type() {
        return idst_type;
    }

    public void setIdst_type(String idst_type) {
        this.idst_type = idst_type;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate_do() {
        return date_do;
    }

    public void setDate_do(String date_do) {
        this.date_do = date_do;
    }
}
