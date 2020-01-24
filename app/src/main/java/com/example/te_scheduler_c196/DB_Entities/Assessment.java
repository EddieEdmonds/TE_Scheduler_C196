package com.example.te_scheduler_c196.DB_Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "ass_table",
        foreignKeys = @ForeignKey(entity = Course.class, childColumns = "fk_course_id", parentColumns = "course_id"))

public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int ass_id;
    private int fk_course_id;

    private String ass_type;
    private String ass_title;
    private Date ass_due_date;
    private Date ass_goal_date;

    public Assessment(String ass_type, String ass_title, Date ass_due_date, Date ass_goal_date, int fk_course_id) {
        this.ass_type = ass_type;
        this.ass_title = ass_title;
        this.ass_due_date = ass_due_date;
        this.ass_goal_date = ass_goal_date;
        this.fk_course_id = fk_course_id;
    }

    public void setAss_id(int ass_id) {
        this.ass_id = ass_id;
    }

    public int getAss_id() {
        return ass_id;
    }

    public String getAss_type() {
        return ass_type;
    }

    public String getAss_title() {
        return ass_title;
    }

    public Date getAss_due_date() {
        return ass_due_date;
    }

    public Date getAss_goal_date() {
        return ass_goal_date;
    }

    public int getFk_course_id() {
        return fk_course_id;
    }
}
