package com.example.te_scheduler_c196.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Note;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    void insertCourse(Course course);
    @Insert
    void popCourses(List<Course> courses);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("DELETE FROM course_table")
    void deleteAllCourses();

    @Query("SELECT * FROM course_table ORDER BY course_start DESC")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM course_table WHERE fk_term_id = :termId")
    LiveData<List<Course>> getCoursesForTerm(final int termId);

    @Query("SELECT course_id, course_title FROM course_table where fk_mentor_id = :mentorId")
    List<Course> getCourseTitleOnMentor(final int mentorId);

    @Query("SELECT COUNT(*) FROM course_table")
    LiveData<Integer> getCourseCount();
}
