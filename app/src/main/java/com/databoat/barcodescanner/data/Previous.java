package com.databoat.barcodescanner.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "previous")
public class Previous {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String idst;
    private String nameId;
    private String reading;
    private String idstType;
    private String consumption;
    private String note;
    private String dateDo;

    public Previous(String idst, String nameId, String reading, String idstType, String consumption,
                    String note, String dateDo) {
        this.idst = idst;
        this.nameId = nameId;
        this.reading = reading;
        this.idstType = idstType;
        this.consumption = consumption;
        this.note = note;
        this.dateDo = dateDo;
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

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getIdstType() {
        return idstType;
    }

    public void setIdstType(String idstType) {
        this.idstType = idstType;
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

    public String getDateDo() {
        return dateDo;
    }

    public void setDateDo(String dateDo) {
        this.dateDo = dateDo;
    }
}
