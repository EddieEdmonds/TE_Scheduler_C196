package com.example.te_scheduler_c196.DB_Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "mentor_table")

public class Mentor {
    @PrimaryKey(autoGenerate = true)
    private int mentor_id;

    private String mentor_name;
    private String mentor_phone;
    private String mentor_email;

    public Mentor(String mentor_name, String mentor_phone, String mentor_email) {
        this.mentor_name = mentor_name;
        this.mentor_phone = mentor_phone;
        this.mentor_email = mentor_email;
    }

    public void setMentor_id(int mentor_id) {

        this.mentor_id = mentor_id;
    }

    public int getMentor_id() {
        return mentor_id;
    }

    public String getMentor_name() {
        return mentor_name;
    }

    public String getMentor_phone() {
        return mentor_phone;
    }

    public String getMentor_email() {
        return mentor_email;
    }

}
