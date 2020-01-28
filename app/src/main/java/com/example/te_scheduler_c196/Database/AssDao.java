package com.example.te_scheduler_c196.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.te_scheduler_c196.DB_Entities.Assessment;

import java.util.List;

@Dao
public interface AssDao {
    @Insert
    void insertAssessment(Assessment assessment);
    @Insert
    void popAssessments(List<Assessment> assessments);

    @Update
    void updateAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("DELETE FROM ass_table")
    void deleteAllAssessments();

    @Query("SELECT * FROM ass_table ORDER BY ass_due_date ASC")
    LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * FROM ass_table WHERE fk_course_id = :courseId")
    LiveData<List<Assessment>>getAssessmentsForCourse(final int courseId);

    @Query("SELECT course_title FROM course_table WHERE course_id = :fk_course_id")
    LiveData<List<String>> getCourseTitleForAssessment(final int fk_course_id);

    @Query("SELECT COUNT(*) FROM ass_table")
    LiveData<Integer> getAssessmentCount();

}
