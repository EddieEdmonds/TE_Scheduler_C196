package com.example.te_scheduler_c196.DB_Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "term_table")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int term_id;

    private String term_title;
    private Date term_start_date;
    private Date term_end_date;

    public Term(String term_title, Date term_start_date, Date term_end_date) {
        this.term_title = term_title;
        this.term_start_date = term_start_date;
        this.term_end_date = term_end_date;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    public int getTerm_id() {
        return term_id;
    }

    public String getTerm_title() {
        return term_title;
    }

    public Date getTerm_start_date() {
        return term_start_date;
    }

    public Date getTerm_end_date() {
        return term_end_date;
    }
}
