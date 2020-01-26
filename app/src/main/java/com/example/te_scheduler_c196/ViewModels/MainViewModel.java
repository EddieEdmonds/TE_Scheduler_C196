package com.example.te_scheduler_c196.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Note;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Database.AppRepository;
import com.example.te_scheduler_c196.MainActivity;
import com.example.te_scheduler_c196.Utility.PopulateDb;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String LOG_TAG = MainViewModel.class.getSimpleName();

    private AppRepository repository;
    private LiveData<List<Term>> allTerms;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Mentor>> allMentors;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Assessment>> allAssessments;

    //Contains counts of items in db
    private LiveData<Integer> termCount;
    private LiveData<Integer> courseCount;
    private LiveData<Integer> assCount;
    private LiveData<Integer> mentorCount;
    private LiveData<Integer> noteCount;


    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allTerms = repository.getAllTerms();
        allCourses = repository.getAllCourses();
        allMentors = repository.getAllMentors();
        allNotes = repository.getAllNotes();
        allAssessments = repository.getAllAssessments();

        termCount = repository.getTermCount();
        courseCount = repository.getCourseCount();
        assCount = repository.getAssCount();
        mentorCount = repository.getMentorCount();
        noteCount = repository.getNoteCount();
    }

    public void emptyDatabase(){
        try {
            repository.emptyDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "test catch");
        }
    }

    public LiveData<List<Term>> getAllTerms(){
        return allTerms;
    }

    public LiveData<Integer> getTermCount() {
        return termCount;
    }

    public LiveData<Integer> getCourseCount() {
        return courseCount;
    }

    public LiveData<Integer> getAssCount() {
        return assCount;
    }

    public LiveData<Integer> getMentorCount() {
        return mentorCount;
    }

    public LiveData<Integer> getNoteCount() {
        return noteCount;
    }






}
