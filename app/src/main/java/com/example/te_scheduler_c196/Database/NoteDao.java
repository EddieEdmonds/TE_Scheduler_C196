package com.example.te_scheduler_c196.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.te_scheduler_c196.DB_Entities.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insertNote(Note note);
    @Insert
    void popNotes(List<Note> notes);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY note_id")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note_table WHERE fk_course_id = :courseId")
    LiveData<List<Note>> getNotesByCourse(final int courseId);

    @Query("SELECT COUNT(*) FROM note_table")
    LiveData<Integer> getNoteCount();

}
