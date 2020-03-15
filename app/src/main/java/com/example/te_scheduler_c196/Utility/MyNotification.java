package com.example.te_scheduler_c196.Utility;

import java.io.Serializable;

public class MyNotification implements Serializable {
    private String nTitle;
    private String nDescription;
    private int nId;

    public MyNotification(String nTitle, String nDescription, int nId) {
        this.nTitle = nTitle;
        this.nDescription = nDescription;
        this.nId = nId;
    }

    public String getNTitle() {
        return nTitle;
    }

    public String getNDescription() {
        return nDescription;
    }
    public int getNId() {
        return nId;
    }
}
