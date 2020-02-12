package com.example.te_scheduler_c196.DB_Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.SET_NULL;

@Entity(tableName = "course_table",
        foreignKeys = {
                @ForeignKey(entity = Term.class, parentColumns = "term_id", childColumns = "fk_term_id", onDelete = SET_NULL),
                @ForeignKey(entity = Mentor.class, parentColumns = "mentor_id", childColumns = "fk_mentor_id", onDelete = SET_NULL)
        })

public class Course {
    @PrimaryKey(autoGenerate = true)
    private int course_id;

    private String course_title;
    private Date course_start;
    private Date course_end;
    private String course_status;

    private Integer fk_term_id;
    private Integer fk_mentor_id;


    public Course(String course_title, Date course_start, Date course_end, String course_status, Integer fk_term_id, Integer fk_mentor_id) {
        this.course_title = course_title;
        this.course_start = course_start;
        this.course_end = course_end;
        this.course_status = course_status;
        this.fk_term_id = fk_term_id;
        this.fk_mentor_id = fk_mentor_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public Date getCourse_start() {
        return course_start;
    }

    public Date getCourse_end() {
        return course_end;
    }

    public String getCourse_status() {
        return course_status;
    }

    public Integer getFk_term_id() {
        return fk_term_id;
    }

    public Integer getFk_mentor_id() { return fk_mentor_id; }
}
