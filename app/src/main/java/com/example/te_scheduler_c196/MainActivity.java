package com.example.te_scheduler_c196;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.Utility.DateUtil;
import com.example.te_scheduler_c196.Utility.MyNotification;
import com.example.te_scheduler_c196.ViewModels.AssessmentViewModel;
import com.example.te_scheduler_c196.ViewModels.CourseViewModel;
import com.example.te_scheduler_c196.ViewModels.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MainViewModel mainViewModel;

    PendingIntent myPendingIntent;
    PendingIntent myPendingIntent2;


    //Grabs the count of each table from the DB. There is a getNoteCount()
    // in the appRepository if needed elsewhere.
    private LiveData<Integer> termCount;
    private LiveData<Integer> courseCount;
    private LiveData<Integer> mentorCount;
    private LiveData<Integer> assCount;

    private List<Course> myCourseList = new ArrayList<>();
    private List<Assessment> myAssList = new ArrayList<>();

    ArrayList<MyNotification> nList1 = new ArrayList<>();
    ArrayList<MyNotification> nList2 = new ArrayList<>();
    ArrayList<MyNotification> nList3 = new ArrayList<>();

    private TextView termCountView, courseCountView, assCountView, mentorCountView;


    AlarmManager alarmManager;
    AlarmManager alarmManager2;
    NotificationBroadcast myBroadcastReceiver;
    NotificationBroadcast2 myBroadcastReceiver2;
    Calendar firingCal;

    CourseViewModel courseViewModel;
    AssessmentViewModel assViewModel;
    private AlertDialog alertDialog;
    private NotificationManagerCompat notificationManager;
    int notificationId = 1;

    Bundle bundle = new Bundle();
    Bundle bundle2 = new Bundle();
    Bundle bundle3 = new Bundle();

    private ImageButton btnMentor;

    private View.OnClickListener emptyDbListener = new View.OnClickListener() {
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
        btnMentor = findViewById(R.id.btn_mentors);

        if (emptyDbButton != null) {
            emptyDbButton.setOnClickListener(emptyDbListener);
        }


        assViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assViewModel.getAllAssessments().observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(@Nullable List<Assessment> assessmentList) {
                assert assessmentList != null;
                myAssList.addAll(assessmentList);
                handleAlertsForAss();
            }
        });

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courseList) {
                assert courseList != null;
                myCourseList.addAll(courseList);
                handleAlertsForCourses();
            }
        });

        ObserveItems();


    }

    private void handleAlertsForAss() {
        firingCal = Calendar.getInstance();
        firingCal.set(Calendar.HOUR_OF_DAY, 13); //hour of day (24 hour clock)
        firingCal.set(Calendar.MINUTE, 27); //alarm minute
        firingCal.set(Calendar.SECOND, 30); //seconds
        if(Calendar.getInstance().after(firingCal)){
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        long intendedTime = firingCal.getTimeInMillis();

        //NotificationBroadcast2 myBroadcastReceiver2 = new NotificationBroadcast2();
        //registerReceiver(myBroadcastReceiver2, new IntentFilter());
        Intent intent = new Intent().setClass(getBaseContext(), NotificationBroadcast2.class);

        Date currentDate = DateUtil.getCurrentDate();

        for (Assessment a : myAssList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
            Log.i(TAG, "Ass goal date: " + sdf.format(a.getAss_goal_date()));
            Log.i(TAG, "Today's date: " + sdf.format(currentDate));

            if (DateUtil.compareDates(a.getAss_goal_date(), currentDate)) {
                MyNotification myNotification;
                String assTitle = a.getAss_title();
                String message = assTitle + " due today";
                int id = notificationId;
                myNotification = new MyNotification(assTitle, message, id);
                nList3.add(myNotification);
                notificationId++;
                Log.i(TAG, "Assessment goal date today.");
            } else {
                Log.i(TAG, "No assessment goal dates today.");
            }
        }

        if (nList3.size() > 0) {
            bundle3.putSerializable("N_LIST_3", nList3);
            intent.putExtra("BUNDLE3", bundle3);
            //myPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            Log.i(TAG, "nList3 Main Activity: " + nList3.size());
        }


        alarmManager2 = (AlarmManager) (this.getSystemService(ALARM_SERVICE));
        myPendingIntent2 = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        assert alarmManager2 != null;
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, myPendingIntent2);

    }

    private void handleAlertsForCourses() {
        firingCal = Calendar.getInstance();
        firingCal.set(Calendar.HOUR_OF_DAY, 13); //hour of day (24 hour clock)
        firingCal.set(Calendar.MINUTE, 27); //alarm minute
        firingCal.set(Calendar.SECOND, 0); //seconds
        if(Calendar.getInstance().after(firingCal)){
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        long intendedTime = firingCal.getTimeInMillis();

//        NotificationBroadcast myBroadcastReceiver = new NotificationBroadcast();
//        registerReceiver(myBroadcastReceiver, new IntentFilter());
        Intent intent = new Intent().setClass(getBaseContext(), NotificationBroadcast.class);

        Date currentDate = DateUtil.getCurrentDate();


        for (Course c : myCourseList) {
            if (DateUtil.compareDates(c.getCourse_start(), currentDate)) {
                MyNotification myNotification;
                String courseTitle = c.getCourse_title();
                String message = courseTitle + " starts today";
                int id = notificationId;
                myNotification = new MyNotification(courseTitle, message, id);
                nList1.add(myNotification);
                notificationId++;
            } else {
                Log.i(TAG, "No courses start today.");
            }
        }
        for (Course c : myCourseList) {
            if (DateUtil.compareDates(c.getCourse_end(), currentDate)) {
                MyNotification myNotification;
                String courseTitle = c.getCourse_title();
                String message = courseTitle + " ends today";
                int id = notificationId;
                myNotification = new MyNotification(courseTitle, message, id);
                nList2.add(myNotification);
                notificationId++;
            } else {
                Log.i(TAG, "No courses due today.");
            }
        }



        if (nList1.size() > 0) {
            bundle.putSerializable("N_LIST_1", nList1);
            intent.putExtra("BUNDLE", bundle);
            Log.i(TAG, "nList1 Main Activity: " + nList1.size());
        }
        if (nList2.size() > 0) {
            bundle2.putSerializable("N_LIST_2", nList2);
            intent.putExtra("BUNDLE2", bundle2);
            Log.i(TAG, "nList2 Main Activity: " + nList2.size());
        }


        alarmManager = (AlarmManager) (this.getSystemService(ALARM_SERVICE));
        myPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, myPendingIntent);

    }


    private void ObserveItems() {
        if (termCount != null) {
            termCount.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer != null) {
                        String string = integer.toString();
                        termCountView.setText(string);
                        Log.i("MainActivity", "termCount success = " + string);
                    } else {
                        termCountView.setText("0");
                        Log.e("MainActivity", "Something BROKE! termCount not NULL, but wasn't able to set termCountView");
                    }
                }
            });
        } else {
            termCountView.setText("0");
            Log.e("MainActivity", "termCount null");
        }

        if (courseCount != null) {
            courseCount.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer != null) {
                        String string = integer.toString();
                        courseCountView.setText(string);
                        Log.i("MainActivity", "courseCount success = " + string);
                    } else {
                        courseCountView.setText("0");
                        Log.e("MainActivity", "Something BROKE! courseCount not NULL, but wasn't able to set courseCountView");
                    }
                }
            });
        } else {
            courseCountView.setText("0");
            Log.e("MainActivity", "courseCount null ");
        }

        if (assCount != null) {
            assCount.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer != null) {
                        String string = integer.toString();
                        assCountView.setText(string);
                        Log.i("MainActivity", "assCount success = " + string);
                    } else {
                        assCountView.setText("0");
                        Log.e("MainActivity", "Something BROKE! assCount not NULL, but wasn't able to set assCountView");
                    }
                }
            });
        } else {
            assCountView.setText("0");
            Log.e("MainActivity", "assCount null ");
        }

        if (mentorCount != null) {
            mentorCount.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer != null) {
                        String string = integer.toString();
                        mentorCountView.setText(string);
                        Log.i("MainActivity", "mentorCount success = " + string);
                    } else {
                        mentorCountView.setText("0");
                        Log.e("MainActivity", "Something BROKE! mentorCount not NULL, but wasn't able to set mentorCountView");
                    }
                }
            });
        } else {
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

    public void launchCourses(View view) {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);

        Toast.makeText(this, "Courses Clicked", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Course clicked");
    }

    public void launchAss(View view) {
        Intent intent = new Intent(this, AssessmentActivity.class);
        startActivity(intent);

        Toast.makeText(this, "Assessments Clicked", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Assessments clicked");
    }

    public void launchMentors(View view) {
        Intent intent = new Intent(this, MentorActivity.class);
        startActivity(intent);

        Toast.makeText(this, "Mentors Clicked", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Mentors clicked");
    }
}
