package com.example.te_scheduler_c196.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.te_scheduler_c196.DB_Entities.Course;

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

    @Query("SELECT * FROM course_table ORDER BY fk_term_id, course_start")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM course_table WHERE fk_term_id = :termId")
    LiveData<List<Course>> getAllCoursesByTerm(final int termId);

    @Query("SELECT COUNT(*) FROM course_table WHERE fk_term_id = :termId")
    int getCourseCountByTerm(final int termId);

    @Query("SELECT COUNT(*) FROM course_table")
    LiveData<Integer> getCourseCount();

    @Query("SELECT * FROM course_table")
    List<Course> getCourseList();

}
