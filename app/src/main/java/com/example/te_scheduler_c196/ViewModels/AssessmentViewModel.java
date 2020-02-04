package com.example.te_scheduler_c196.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.Database.AppRepository;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allAssessments = repository.getAllAssessments();
    }

    public void insertAssessment(Assessment assessment){
        repository.insertAssessment(assessment);
    }

    public void deleteAssessment(Assessment assessment){ repository.deleteAssessment(assessment); }

    public void updateAssessment(Assessment assessment) { repository.updateAssessment(assessment); }

    public LiveData<List<Assessment>> getAllAssessments(){
        return allAssessments;
    }

    public LiveData<List<Assessment>> getAllAssessmentsByCourse(int courseId){
        return repository.getAllAssessmentsByCourse(courseId);
    }
}
