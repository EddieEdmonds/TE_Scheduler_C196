package com.example.te_scheduler_c196.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Mentor;

import java.util.List;

@Dao
public interface MentorDao {
    @Insert
    void insertMentor(Mentor mentor);
    @Insert
    void popMentors(List<Mentor> mentors);

    @Update
    void updateMentor(Mentor mentor);

    @Delete
    void deleteMentor(Mentor mentor);

    @Query("DELETE FROM mentor_table")
    void deleteAllMentors();

    @Query("SELECT * FROM mentor_table ORDER BY mentor_name DESC")
    LiveData<List<Mentor>> getAllMentors();

    @Query("SELECT * FROM course_table WHERE fk_mentor_id = :mentorId")
    LiveData<List<Course>> getCoursesAssignedToMentor(final int mentorId);

//    @Query("SELECT * FROM mentor_table WHERE fk_course_id = :courseId")
//    LiveData<List<Mentor>> getMentorForCourse(final int courseId);

    @Query("SELECT COUNT(*) FROM mentor_table")
    LiveData<Integer> getMentorCount();
}
