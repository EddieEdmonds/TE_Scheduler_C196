package com.example.te_scheduler_c196.Database;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Note;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.MainActivity;
import com.example.te_scheduler_c196.Utility.PopulateDb;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static final String TAG = AppRepository.class.getSimpleName();

    private AssDao assDao;
    private CourseDao courseDao;
    private MentorDao mentorDao;
    private NoteDao noteDao;
    private TermDao termDao;

    private LiveData<Integer> assCount;
    private LiveData<Integer> courseCount;
    private LiveData<Integer> mentorCount;
    private LiveData<Integer> noteCount;
    private LiveData<Integer> termCount;
    private AppDatabase database;


    private LiveData<List<Assessment>> allAssessments;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Mentor>> allMentors;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Term>> allTerms;

    private String courseTitle;


    public AppRepository(Application application) {
        database = AppDatabase.getInstance(application);
        assDao = database.assDao();
        courseDao = database.courseDao();
        mentorDao = database.mentorDao();
        noteDao = database.noteDao();
        termDao = database.termDao();

        //getting all entries for each table.
        allAssessments = assDao.getAllAssessments();
        allCourses = courseDao.getAllCourses();
        allMentors = mentorDao.getAllMentors();
        allNotes = noteDao.getAllNotes();
        allTerms = termDao.getAllTerms();

        //getting the count of entries in each table
        assCount = assDao.getAssessmentCount();
        courseCount = courseDao.getCourseCount();
        mentorCount = mentorDao.getMentorCount();
        noteCount = noteDao.getNoteCount();
        termCount = termDao.getTermCount();
    }

//////Public facing "API" to be used outside the repository and ROOM setup.
    //These are running as AsyncTask since they are small, quick queries.  If this was a much larger app,
    //using a library like "Executor" would be a better option.

    //Fill database
//    public void populateDb(){
//        executor.execute(new termDao.popTerms(););
//    }


    //Clear database
    public void emptyDatabase() {
        //theoretically, calling each of the "deleteAll" AsyncTasks at one time should empty the DB.
        try {
            new DeleteAllAssessmentsAsyncTask(assDao).execute();
            new DeleteAllNotesAsyncTask(noteDao).execute();
            new DeleteAllMentorsAsyncTask(mentorDao).execute();
            new DeleteAllTermsAsyncTask(termDao).execute();
            new DeleteAllCoursesAsyncTask(courseDao).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "test catch");
        }
    }

    //////Assessment modifications accessible by the rest of the app.
    public void insertAssessment(Assessment assessment) {
        new InsertAssessmentAsyncTask(assDao).execute(assessment);
    }

    public void updateAssessment(Assessment assessment) {
        new UpdateAssessmentAsyncTask(assDao).execute(assessment);
    }

    public void deleteAssessment(Assessment assessment) {
        new DeleteAssessmentAsyncTask(assDao).execute(assessment);
    }

    public void deleteAllAssessments() {
        new DeleteAllAssessmentsAsyncTask(assDao).execute();
    }


    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }

    public LiveData<Integer> getAssCount() {
        return assCount;
    }

    public LiveData<List<String>> getCourseTitleForAssessment(int courseId){
        return assDao.getCourseTitleForAssessment(courseId);
    }


    //////Course modifications accessible by rest of the app
    public void insertCourse(Course course) {
        new InsertCourseAsyncTask(courseDao).execute(course);
    }

    public void updateCourse(Course course) {
        new UpdateCourseAsyncTask(courseDao).execute(course);
    }

    public void deleteCourse(Course course) {
        new DeleteCourseAsyncTask(courseDao).execute(course);
    }

    public void deleteAllCourses() {
        new DeleteAllCoursesAsyncTask(courseDao).execute();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<Integer> getCourseCount() {
        return courseCount;
    }

    public LiveData<List<Course>> getAllCoursesByTerm(int termId){
        return courseDao.getAllCoursesByTerm(termId);
    }


//    public List<Course> getCourseTitleOnMentor(int mentorId){
//        return courseDao.getCourseTitleOnMentor(mentorId);
//    }

    ///////Mentor modifications accessible by rest of the app
    public void insertMentor(Mentor mentor) {
        new InsertMentorAsyncTask(mentorDao).execute(mentor);
    }

    public void updateMentor(Mentor mentor) {
        new UpdateMentorAsyncTask(mentorDao).execute(mentor);
    }

    public void deleteMentor(Mentor mentor) {
        new DeleteMentorAsyncTask(mentorDao).execute(mentor);
    }

    public void deleteAllMentors() {
        new DeleteAllMentorsAsyncTask(mentorDao).execute();
    }

    public LiveData<List<Mentor>> getAllMentors() {
        return allMentors;
    }

    public LiveData<Integer> getMentorCount() {
        return mentorCount;
    }

    ///////Note modifications accessible by rest of the app
    public void insertNote(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void updateNote(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteNote(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<Integer> getNoteCount() {
        return noteCount;
    }

    //////Term modifications accessible by rest of the app
    public void insertTerm(Term term) {
        new InsertTermAsyncTask(termDao).execute(term);
    }

    public void updateTerm(Term term) {
        new UpdateTermAsyncTask(termDao).execute(term);
    }

    public void deleteTerm(Term term) {
        new DeleteTermAsyncTask(termDao).execute(term);
    }

    public void deleteAllTerms() {
        new DeleteAllTermsAsyncTask(termDao).execute();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<Integer> getTermCount() {
        return termCount;
    }
////******************************************************************************************************


    //Async tasks for the above public interface.
//////Async tasks for Assessment modifications.
    private static class InsertAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssDao assDao;

        private InsertAssessmentAsyncTask(AssDao assDao) {
            this.assDao = assDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assDao.insertAssessment(assessments[0]);
            return null;
        }
    }

    private static class UpdateAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        //Because the class is static, we can't access the dao from our repository directly.
        private AssDao assDao;

        //We need to pass it in over a constructor which we set up below.
        private UpdateAssessmentAsyncTask(AssDao assDao) {
            this.assDao = assDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            //now we call the assDao that we passed in with the constructor above.
            //we access the var args at index 0 since we are only passing in one assessment to this method.
            assDao.updateAssessment(assessments[0]);
            return null;
        }
    }

    private static class DeleteAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssDao assDao;

        private DeleteAssessmentAsyncTask(AssDao assDao) {
            this.assDao = assDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assDao.deleteAssessment(assessments[0]);
            return null;
        }
    }

    public static class DeleteAllAssessmentsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AssDao assDao;

        private DeleteAllAssessmentsAsyncTask(AssDao assDao) {
            this.assDao = assDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                assDao.deleteAllAssessments();
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Log.e(TAG, "DeleteAllAssessments failed");
            }
            return null;
        }
    }

/////Async tasks for Course modifications
    private static class InsertCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private InsertCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... Courses) {
            courseDao.insertCourse(Courses[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private UpdateCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... Courses) {
            courseDao.updateCourse(Courses[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private DeleteCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... Courses) {
            courseDao.deleteCourse(Courses[0]);
            return null;
        }
    }

    public static class DeleteAllCoursesAsyncTask extends AsyncTask<Void, Void, Void> {
        private CourseDao courseDao;

        private DeleteAllCoursesAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                courseDao.deleteAllCourses();
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Log.e(TAG, "DeleteAllCourses failed");
            }
            return null;
        }
    }

/////Async tasks for Mentor modifications
    private static class InsertMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private InsertMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... Mentors) {
            mentorDao.insertMentor(Mentors[0]);
            return null;
        }
    }

    private static class UpdateMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private UpdateMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... Mentors) {
            mentorDao.updateMentor(Mentors[0]);
            return null;
        }
    }

    private static class DeleteMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private DeleteMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... Mentors) {
            mentorDao.deleteMentor(Mentors[0]);
            return null;
        }
    }

    public static class DeleteAllMentorsAsyncTask extends AsyncTask<Void, Void, Void> {
        private MentorDao mentorDao;

        private DeleteAllMentorsAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mentorDao.deleteAllMentors();
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Log.e(TAG, "DeleteAllMentors failed");
            }
            return null;
        }
    }

/////Async tasks for Note modifications
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... Notes) {
            noteDao.insertNote(Notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... Notes) {
            noteDao.updateNote(Notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... Notes) {
            noteDao.deleteNote(Notes[0]);
            return null;
        }
    }

    public static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                noteDao.deleteAllNotes();
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Log.e(TAG, "DeleteAllNotes failed");
            }
            return null;
        }
    }

/////Async tasks for Term modifications
    private static class CountTermsAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;

        private CountTermsAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.getTermCount();
            return null;
        }
    }

    private static class InsertTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private InsertTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... Terms) {
            termDao.insertTerm(Terms[0]);
            return null;
        }
    }

    private static class UpdateTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private UpdateTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... Terms) {
            termDao.updateTerm(Terms[0]);
            return null;
        }
    }

    private static class DeleteTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private DeleteTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... Terms) {
            termDao.deleteTerm(Terms[0]);
            return null;
        }
    }



    public static class DeleteAllTermsAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;
        private DeleteAllTermsAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                termDao.deleteAllTerms();
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Log.e(TAG, "Delete Failed");
            }
            return null;
        }
    }


}
