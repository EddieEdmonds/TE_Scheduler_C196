package com.example.te_scheduler_c196.DB_Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table",
        foreignKeys = @ForeignKey(entity = Course.class, childColumns = "fk_course_id", parentColumns = "course_id"))

public class Note {
    @PrimaryKey(autoGenerate = true)
    private int note_id;
    private int fk_course_id;



    private String title;
    private String note_body;

    public Note(String title, String note_body, int fk_course_id) {
        this.title = title;
        this.note_body = note_body;
        this.fk_course_id = fk_course_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }


    public int getNote_id() {
        return note_id;
    }

    public String getTitle() {
        return title;
    }

    public String getNote_body() {
        return note_body;
    }

    public int getFk_course_id() {
        return fk_course_id;
    }
}
