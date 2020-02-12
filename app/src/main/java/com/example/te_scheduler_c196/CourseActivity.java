package com.example.te_scheduler_c196;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_scheduler_c196.Adapters.CourseAdapter;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.Utility.DateUtil;
import com.example.te_scheduler_c196.ViewModels.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.te_scheduler_c196.App.CHANNEL_1_ID;
import static com.example.te_scheduler_c196.App.CHANNEL_2_ID;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_ID;

public class CourseActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    CourseViewModel courseViewModel;

    private AlertDialog alertDialog;

    private NotificationManagerCompat notificationManager;

    //private LiveData<List<Course>> allCourses;
    public static final int ADD_COURSE_REQUEST = 3;
    public static final int EDIT_COURSE_REQUEST = 4;
    //This id gets updated each time a notification is sent out so they're always unique.
//    int notificationId = 1;

    private List<Course> myCourseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        notificationManager = NotificationManagerCompat.from(this);

        RecyclerView courseRecyclerView = findViewById(R.id.course_recycler_view);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseRecyclerView.setHasFixedSize(true);

        //Sets the adapter being used to grab data from the ViewModel.
        final CourseAdapter courseAdapter = new CourseAdapter();
        courseRecyclerView.setAdapter(courseAdapter);

        //Loads the full list of courses for the student.
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courseList) {
                courseAdapter.setCourseList(courseList);
                assert courseList != null;
                myCourseList.addAll(courseList);
            }
        });

        //+ button on main Course page. Opens the "Add Course" activity.
        FloatingActionButton btnAddTerm = findViewById(R.id.btn_add_course);
        btnAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            //New term onClick method
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this, CourseAddActivity.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST);

            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final Course swipedCourse = courseAdapter.getCourseAt(viewHolder.getAdapterPosition());

                //We pop a dialog letting users know that Notes and Assessments will be deleted as well.
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage("All Notes and Assessments associated with this course will be deleted!");

                //If they click positive button: course, note, and assessments deleted
                // Course deleted via the below deleteCourse method.
                // Notes and Assessments deleted through "on delete CASCADE" in the respective DAOs.
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // Your action
                        courseViewModel.deleteCourse(swipedCourse);
                        dialog.cancel();
                    }
                });
                //If you click cancel, view is refreshed and course is returned.
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        courseAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    }
                });
                alertDialog = builder.show();
                builder.show();
            }
        }).attachToRecyclerView(courseRecyclerView);

////////////////////ClickListener for main area of recycler view item (course detail) and edit button (edit course)///////////////////
        courseAdapter.setOnCourseClickListener(new CourseAdapter.onCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent intent = new Intent(CourseActivity.this, CourseDetailActivity.class);
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_TITLE, course.getCourse_title());
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_START_DATE, DateUtil.dateToString(course.getCourse_start()));
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_END_DATE, DateUtil.dateToString(course.getCourse_end()));
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_STATUS, course.getCourse_status());

                intent.putExtra(EXTRA_COURSE_ID, course.getCourse_id());
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_MENTOR_ID, course.getFk_mentor_id());
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_TERM_ID, course.getFk_term_id());

                startActivity(intent);
            }

            @Override
            public void onEditClick(Course course) {
                Intent intent = new Intent(CourseActivity.this, CourseEditActivity.class);
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_TITLE, course.getCourse_title());
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_START_DATE, DateUtil.dateToString(course.getCourse_start()));
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_END_DATE, DateUtil.dateToString(course.getCourse_end()));
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_STATUS, course.getCourse_status());

                intent.putExtra(EXTRA_COURSE_ID, course.getCourse_id());
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_MENTOR_ID, course.getFk_mentor_id());
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_TERM_ID, course.getFk_term_id());

                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }

            @Override
            public void onDetailClick(Course course) {
                Intent intent = new Intent(CourseActivity.this, CourseDetailActivity.class);
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_TITLE, course.getCourse_title());
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_START_DATE, DateUtil.dateToString(course.getCourse_start()));
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_END_DATE, DateUtil.dateToString(course.getCourse_end()));
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_STATUS, course.getCourse_status());

                intent.putExtra(EXTRA_COURSE_ID, course.getCourse_id());
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_MENTOR_ID, course.getFk_mentor_id());
                intent.putExtra(CourseAddActivity.EXTRA_COURSE_TERM_ID, course.getFk_term_id());

//                Course courseTest = myCourseList.get(0);
//                Date test = courseTest.getCourse_start();
//                Log.i(TAG, "Current date: " + DateUtil.getCurrentDate());
//                Log.i(TAG, "Course Date: "+test);

//                sendOnChannel1();
                startActivity(intent);

            }
        });


    }

//    public void sendOnChannel1(){
//        Date currentDate = DateUtil.getCurrentDate();
//        for(Course c:myCourseList){
//            if(c.getCourse_start().equals(currentDate)){
//                String courseTitle = c.getCourse_title();
//                String message = courseTitle+" starts today";
//
//                Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
//                        .setSmallIcon(R.drawable.ic_course_icon)
//                        .setContentTitle(courseTitle)
//                        .setContentText(message)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .build();
//
//                notificationManager.notify(notificationId, notification);
//                notificationId++;
//            }
//        }
//    }


//    public void sendOnChannel2(View v){
//        Date currentDate = DateUtil.getCurrentDate();
//        for(Course c:myCourseList){
//            if(c.getCourse_end().equals(currentDate)){
//                String courseTitle = c.getCourse_title();
//                String message = courseTitle+" ends today";
//
//                Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
//                        .setSmallIcon(R.drawable.ic_course_icon)
//                        .setContentTitle(courseTitle)
//                        .setContentText(message)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .build();
//                notificationManager.notify(notificationId, notification);
//                notificationId++;
//            }
//        }
//    }

    @Override
    public void onPause(){
        super.onPause();
        if ( alertDialog!=null && alertDialog.isShowing() ){
            alertDialog.cancel();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        String courseTitle = data.getStringExtra(CourseAddActivity.EXTRA_COURSE_TITLE);
        String courseStartDate = data.getStringExtra(CourseAddActivity.EXTRA_COURSE_START_DATE);
        String courseEndDate = data.getStringExtra(CourseAddActivity.EXTRA_COURSE_END_DATE);
        String courseStatus = data.getStringExtra(CourseAddActivity.EXTRA_COURSE_STATUS);
        int courseTermId = data.getIntExtra(CourseAddActivity.EXTRA_COURSE_TERM_ID, -1);
        int fk_courseMentorId = data.getIntExtra(CourseAddActivity.EXTRA_COURSE_MENTOR_ID, -1);


        Date courseStartDateConverted = DateUtil.stringToDateConverter(courseStartDate);                        //Dates received from CourseAddActivity are in String format.
        Date courseEndDateConverted = DateUtil.stringToDateConverter(courseEndDate);                            //These two lines convert to Date format.

        Course course;

        if (requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            try {
                course = new Course(courseTitle, courseStartDateConverted, courseEndDateConverted, courseStatus, courseTermId, fk_courseMentorId);
                courseViewModel.insertCourse(course);
                Log.i(TAG, "Created new course");
                Toast.makeText(this, "Course Saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.i(TAG, "Something went wrong saving new Course");
                e.printStackTrace();
            }
        }else if(requestCode==EDIT_COURSE_REQUEST && resultCode == RESULT_OK){
            int courseId = data.getIntExtra(EXTRA_COURSE_ID, -1);
            try {
                if(courseId != -1){
                    course = new Course(courseTitle, courseStartDateConverted, courseEndDateConverted, courseStatus, courseTermId, fk_courseMentorId);
                    course.setCourse_id(courseId);
                    courseViewModel.updateCourse(course);
                    Log.i(TAG, "Updated course");
                    Toast.makeText(this, "Course Saved!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.i(TAG, "Something went wrong updating Course");
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Course not saved!", Toast.LENGTH_SHORT).show();
        }
    }
}
