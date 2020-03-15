package com.example.te_scheduler_c196.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Database.AppRepository;

import java.util.List;

public class TermViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<Term>> allTerms;
    private LiveData<List<Course>> allCoursesForTerm;
    private LiveData<List<Term>> termByid;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allTerms = repository.getAllTerms();
        allCoursesForTerm = repository.getAllCoursesByTerm(1);
        termByid = repository.getTermById(-1);
    }

    public void insertTerm(Term term) {
        repository.insertTerm(term);
    }

    public void deleteTerm(Term term) {
        repository.deleteTerm(term);
    }

    public void updateTerm(Term term) {
        repository.updateTerm(term);
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<Course>> getAllCoursesForTerm(int termId) {
        return allCoursesForTerm = repository.getAllCoursesByTerm(termId);
    }

    public LiveData<List<Term>> getTermById(int termId) {
        return termByid = repository.getTermById(termId);
    }


}
