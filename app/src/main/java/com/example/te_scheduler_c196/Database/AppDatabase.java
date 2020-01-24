package com.example.te_scheduler_c196.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Note;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Utility.PopulateDb;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Database(entities = {Assessment.class, Course.class, Mentor.class, Note.class, Term.class}, version = 2)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract AssDao assDao();
    public abstract CourseDao courseDao();
    public abstract MentorDao mentorDao();
    public abstract NoteDao noteDao();
    public abstract TermDao termDao();

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db){
          super.onCreate(db);
          new PopulateDbAsyncTask(instance).execute();
      }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private AssDao assDao;
        private CourseDao courseDao;
        private MentorDao mentorDao;
        private NoteDao noteDao;
        private TermDao termDao;

        private PopulateDbAsyncTask(AppDatabase db){
            termDao = db.termDao();
            mentorDao = db.mentorDao();
            courseDao = db.courseDao();
            assDao = db.assDao();
            noteDao = db.noteDao();

        }

        private static Date getDate(int diff){
            GregorianCalendar date = new GregorianCalendar();
            date.add(Calendar.MILLISECOND, diff);
            return date.getTime();
        }

        @Override
        protected Void doInBackground(Void... voids){
            //Pre-populate database with test data.
            //@Insert default conflict strategy is to ABORT.
            //If you clear the database and fill it in with your own information,
            //this should not overwrite that.
            termDao.popTerms(PopulateDb.popTerms());
            mentorDao.popMentors(PopulateDb.popMentors());
            courseDao.popCourses(PopulateDb.popCourses());
            noteDao.popNotes(PopulateDb.popNotes());
            assDao.popAssessments(PopulateDb.popAssessments());

            return null;
        }
    }





}
