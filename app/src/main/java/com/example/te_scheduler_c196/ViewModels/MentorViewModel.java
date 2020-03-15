package com.example.te_scheduler_c196.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Database.AppRepository;

import java.util.ArrayList;
import java.util.List;

public class MentorViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<Mentor>> allMentors;
    private LiveData<List<Mentor>> allMentorById;

    public MentorViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allMentors = repository.getAllMentors();
    }

    public void insertMentor(Mentor mentor){
        repository.insertMentor(mentor);
    }

    public void deleteMentor(Mentor mentor){
        repository.deleteMentor(mentor);
    }

    public void updateMentor(Mentor mentor){
        repository.updateMentor(mentor);
    }

    public LiveData<List<Mentor>> getAllMentors(){
        return allMentors;
    }

    public LiveData<List<Mentor>> getMentorById(int mentorId){
        return allMentorById = repository.getMentorById(mentorId);
    }

}
