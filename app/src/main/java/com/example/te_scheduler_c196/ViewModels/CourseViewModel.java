package com.example.te_scheduler_c196.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.Database.AppRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<Course>> allCourses;

    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allCourses = repository.getAllCourses();

    }

    public void insertCourse(Course course){
        repository.insertCourse(course);
    }

    public void deleteCourse(Course course){ repository.deleteCourse(course); }

    public void updateCourse(Course course) { repository.updateCourse(course); }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }

    public LiveData<List<Course>> getAllCoursesByTerm(int termId){
        return repository.getAllCoursesByTerm(termId);
    }

    public int getCourseCountByTerm(int termId){
        return repository.getCourseCountByTerm(termId);
    }

    public List<Course> allCourseList(){
        return repository.getAllCourseList();
    }


}
