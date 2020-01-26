package com.example.te_scheduler_c196.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.te_scheduler_c196.DB_Entities.Term;

import java.util.List;

@Dao
public interface TermDao {
    @Insert
    void insertTerm(Term term);

    @Insert
    void popTerms(List<Term> terms);

    @Update
    void updateTerm(Term term);

    @Delete
    void deleteTerm(Term term);

    @Query("DELETE FROM term_table")
    void deleteAllTerms();

    @Query("SELECT * FROM term_table ORDER BY term_id ASC")
    LiveData<List<Term>> getAllTerms();

    @Query("SELECT * FROM term_table WHERE term_id = :termId")
    LiveData<List<Term>> getTermById(final int termId);

    @Query("SELECT COUNT(*) FROM term_table")
    LiveData<Integer> getTermCount();

}
