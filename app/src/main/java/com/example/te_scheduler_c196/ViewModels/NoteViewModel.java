package com.example.te_scheduler_c196.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Note;
import com.example.te_scheduler_c196.Database.AppRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> allNotesByCourse;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allNotes = repository.getAllNotes();
        allNotesByCourse = repository.getNotesByCourse(1);
    }

    public void insertNote(Note note){
        repository.insertNote(note);
    }
    public void updateNote(Note note){
        repository.updateNote(note);
    }

    public void deleteNote(Note note){
        repository.deleteNote(note);
    }

    public LiveData<List<Note>> getNotesByCourse(int courseId){
        return allNotesByCourse = repository.getNotesByCourse(courseId);
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
}
