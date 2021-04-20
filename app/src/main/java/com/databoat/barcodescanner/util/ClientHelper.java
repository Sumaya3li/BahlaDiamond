package com.databoat.barcodescanner.util;

public class ClientHelper {

    private String idst;
    private String name_id;
    private String perusal_previous;

    public ClientHelper(String idst, String name_id, String perusal_previous) {
        this.idst = idst;
        this.name_id = name_id;
        this.perusal_previous = perusal_previous;
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
}
