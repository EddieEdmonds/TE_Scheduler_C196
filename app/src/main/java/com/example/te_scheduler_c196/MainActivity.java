package com.example.te_scheduler_c196;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.DB_Entities.Note;
import com.example.te_scheduler_c196.ViewModels.MainViewModel;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MainViewModel mainViewModel;

    //Grabs the count of each table from the DB. There is a getNoteCount()
    // in the appRepository if needed elsewhere.
    private LiveData<Integer> termCount;
    private LiveData<Integer> courseCount;
    private LiveData<Integer> mentorCount;
    private LiveData<Integer> assCount;

    private List<Term> termList;
    private List<Course> courseList;
    private List<Mentor> mentorList;
    private List<Assessment> assList;
    private List<Note> noteList;

    private TextView termCountView, courseCountView, assCountView, mentorCountView;


    private View.OnClickListener emptyDbListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {
                mainViewModel.emptyDatabase();
                Toast.makeText(MainActivity.this, "Database Emptied", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "test catch");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        termCount = mainViewModel.getTermCount();
        courseCount = mainViewModel.getCourseCount();
        assCount = mainViewModel.getAssCount();
        mentorCount = mainViewModel.getMentorCount();

        //Initialize "count" text views on main screen.
        termCountView = findViewById(R.id.term_count);
        courseCountView = findViewById(R.id.course_count);
        assCountView = findViewById(R.id.ass_count);
        mentorCountView = findViewById(R.id.mentor_count);

        ImageButton emptyDbButton = findViewById(R.id.btn_emptyDatabase);

        if(emptyDbButton !=null){
            emptyDbButton.setOnClickListener(emptyDbListener);
        }


        ObserveItems();
    }


    private void ObserveItems() {
        if(termCount!=null){
            termCount.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if(integer!=null){
                        String string = integer.toString();
                        termCountView.setText(string);
                        Log.i("MainActivity", "termCount success = " + string);
                    }else{
                        termCountView.setText("0");
                        Log.e("MainActivity", "Something BROKE! termCount not NULL, but wasn't able to set termCountView");
                    }
                }
            });
        }else{
            termCountView.setText("0");
            Log.e("MainActivity", "termCount null");
        }

        if(courseCount!=null){
            courseCount.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if(integer!=null){
                        String string = integer.toString();
                        courseCountView.setText(string);
                        Log.i("MainActivity", "courseCount success = " + string);
                    }else{
                        courseCountView.setText("0");
                        Log.e("MainActivity", "Something BROKE! courseCount not NULL, but wasn't able to set courseCountView");
                    }
                }
            });
        }else{
            courseCountView.setText("0");
            Log.e("MainActivity", "courseCount null ");
        }

        if(assCount!=null){
            assCount.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if(integer!=null){
                        String string = integer.toString();
                        assCountView.setText(string);
                        Log.i("MainActivity", "assCount success = " + string);
                    }else{
                        assCountView.setText("0");
                        Log.e("MainActivity", "Something BROKE! assCount not NULL, but wasn't able to set assCountView");
                    }
                }
            });
        }else{
            assCountView.setText("0");
            Log.e("MainActivity", "assCount null ");
        }

        if(mentorCount!=null){
            mentorCount.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if(integer!=null){
                        String string = integer.toString();
                        mentorCountView.setText(string);
                        Log.i("MainActivity", "mentorCount success = " + string);
                    }else{
                        mentorCountView.setText("0");
                        Log.e("MainActivity", "Something BROKE! mentorCount not NULL, but wasn't able to set mentorCountView");
                    }
                }
            });
        }else{
            mentorCountView.setText("0");
            Log.e("MainActivity", "mentorCount null ");
        }


    }


    public void launchTerms(View view) {
        Intent intent = new Intent(this, TermActivity.class);
        startActivity(intent);


        Toast.makeText(this, "Term Clicked", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Term clicked");
    }

    public void launchCourses(View view){
        Intent intent = new Intent (this, CourseActivity.class);
        startActivity(intent);

        Toast.makeText(this, "Courses Clicked", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Course clicked");
    }

    public void launchAss(View view){
        Intent intent = new Intent(this, AssessmentActivity.class);
        startActivity(intent);

        Toast.makeText(this, "Assessments Clicked", Toast.LENGTH_SHORT).show();
    }

    public void launchMentors(View view){
        Intent intent = new Intent(this, MentorActivity.class);
        startActivity(intent);

        Toast.makeText(this, "Mentors Clicked", Toast.LENGTH_SHORT).show();
    }
}
