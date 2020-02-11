package com.example.te_scheduler_c196;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.Utility.DateUtil;
import com.example.te_scheduler_c196.ViewModels.CourseViewModel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AssessmentAddEditActivity extends AppCompatActivity {
    private static final String TAG = AssessmentAddEditActivity.class.getSimpleName();

    public static final String EXTRA_ASS_ID = "com.example.te_scheduler_c196.EXTRA_ASS_ID";
    public static final String EXTRA_ASS_TITLE = "com.example.te_scheduler_c196.EXTRA_ASS_TITLE";
    public static final String EXTRA_ASS_DUE_DATE = "com.example.te_scheduler_c196.EXTRA_ASS_DUE_DATE";
    public static final String EXTRA_ASS_GOAL_DATE = "com.example.te_scheduler_c196.EXTRA_ASS_GOAL_DATE";
    public static final String EXTRA_ASS_FK_COURSE_ID = "com.example.te_scheduler_c196.EXTRA_ASS_FK_COURSE_ID";
    public static final String EXTRA_ASS_TYPE = "com.example.te_scheduler_c196.EXTRA_ASS_TYPE";

    private String dateFormattedShort = null;
    private TextView tv_DueDate, tv_GoalDate;
    private EditText et_AssessmentTitle;
    private Spinner sp_Courses;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private List<String> spCourseList = new ArrayList<>();
    private List<Course> courseListCourse = new ArrayList<>();

    private DatePickerDialog.OnDateSetListener dueDateSetListener;
    private DatePickerDialog.OnDateSetListener goalDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_add);

        tv_DueDate = findViewById(R.id.tv_select_due_date);
        tv_GoalDate = findViewById(R.id.tv_select_goal_date);
        et_AssessmentTitle = findViewById(R.id.et_assessment_title);
        sp_Courses = findViewById(R.id.sp_course);
        radioGroup = findViewById(R.id.radio_group);

        ///////////////////////Start Date/////////////////////////////
        tv_DueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_DueDate.getText().toString().equals("")){
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            AssessmentAddEditActivity.this, R.style.DatePickerDialogTheme,
                            dueDateSetListener,
                            year, month, day);
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }else{
                    String startDateString = tv_DueDate.getText().toString();
                    Date startDate = DateUtil.stringToDateConverter(startDateString);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        int year = localStartDate.getYear();
                        int month = localStartDate.getMonthValue()-1;
                        int day = localStartDate.getDayOfMonth();
                        DatePickerDialog dialog = new DatePickerDialog(
                                AssessmentAddEditActivity.this, R.style.DatePickerDialogTheme,
                                dueDateSetListener,
                                year, month, day);
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    } else {
                        int year = startDate.getYear();
                        int month = startDate.getMonth();
                        int day = startDate.getDay();
                        DatePickerDialog dialog = new DatePickerDialog(
                                AssessmentAddEditActivity.this, R.style.DatePickerDialogTheme,
                                dueDateSetListener,
                                year, month, day);
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }

            }
        });

        dueDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateFormattedShort = DateUtil.dateConverter(year, month, dayOfMonth);
                tv_DueDate.setText(dateFormattedShort);
                Log.i(TAG, "onDateSet Start: short: " + dateFormattedShort);

            }
        };

///////////////////////End Date/////////////////////////////
        tv_GoalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_GoalDate.getText().toString().equals("")){
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            AssessmentAddEditActivity.this, R.style.DatePickerDialogTheme,
                            goalDateSetListener,
                            year, month, day);
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }else{
                    String endDateString = tv_GoalDate.getText().toString();
                    Date startDate = DateUtil.stringToDateConverter(endDateString);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        int year = localStartDate.getYear();
                        int month = localStartDate.getMonthValue()-1;
                        int day = localStartDate.getDayOfMonth();
                        DatePickerDialog dialog = new DatePickerDialog(
                                AssessmentAddEditActivity.this, R.style.DatePickerDialogTheme,
                                goalDateSetListener,
                                year, month, day);
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    } else {
                        int year = startDate.getYear();
                        int month = startDate.getMonth();
                        int day = startDate.getDay();
                        DatePickerDialog dialog = new DatePickerDialog(
                                AssessmentAddEditActivity.this, R.style.DatePickerDialogTheme,
                                goalDateSetListener,
                                year, month, day);
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }

            }
        });

        goalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateFormattedShort = DateUtil.dateConverter(year, month, dayOfMonth);
                tv_GoalDate.setText(dateFormattedShort);
                Log.i(TAG, "onDateSet End: short: " + dateFormattedShort);

            }
        };

        ///////////////////////////Gets all mentors and adds them to the mentor spinner list///////////////////////////////
        final ArrayAdapter<String> sp_CourseListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spCourseList);
        sp_CourseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Courses.setAdapter(sp_CourseListAdapter);


        CourseViewModel courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courseList) {
                if(courseList!=null){
                    for(Course c : courseList){
                        spCourseList.add(c.getCourse_title());
                        courseListCourse.add(c);
                        //termListTerm.add(m);
                    }
                }
                sp_CourseListAdapter.notifyDataSetChanged();
            }
        });

//        private String dateFormattedShort = null;

//        private Spinner sp_Courses;
//        private RadioGroup radioGroup;
//        private RadioButton radioButton;

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ASS_ID)){
            setTitle("Edit Assessment");
            et_AssessmentTitle.setText(intent.getStringExtra(EXTRA_ASS_TITLE));
            tv_DueDate.setText(intent.getStringExtra(EXTRA_ASS_DUE_DATE));
            tv_GoalDate.setText(intent.getStringExtra(EXTRA_ASS_GOAL_DATE));

            //Whatever "type" is passed in, I check it against available buttons and check the one it matches.
            //This should be sufficient because these are the only two types available and users can't enter custom text.
            String assType = intent.getStringExtra(EXTRA_ASS_TYPE);
            if(assType != null && assType.equals("Objective")){
                radioGroup.check(R.id.rb_objective);
            }else{
                radioGroup.check(R.id.rb_performance);
            }

            //The fk_course_id from the db will match directly with its location in the spinner list.
            //The spinner indexes form 0 and the ids from the db index from 1.
            //Need to subtract 1 from the courseId to get the matching index in the spinner.
            int courseIndex = intent.getIntExtra(EXTRA_ASS_FK_COURSE_ID, -1)-1;
            sp_Courses.setSelection(courseIndex);
        }else{
            setTitle("New Assessment");
        }

    }

    private void SaveNewAssessment(){
        String assTitle = et_AssessmentTitle.getText().toString();
        String assDueDate = tv_DueDate.getText().toString();
        String assGoalDate = tv_GoalDate.getText().toString();

        int selectedCoursePosition = sp_Courses.getSelectedItemPosition();              //Get selected course position in spinner
        Course course = courseListCourse.get(selectedCoursePosition);                   //Use position to select that Course.
        int courseId = course.getCourse_id();                                           //Use that Course to select Course ID

        int radioId = radioGroup.getCheckedRadioButtonId();                             //Get id of checked radio button.
        radioButton = findViewById(radioId);                                            //Set the view of the radio button so we know where it is.
        String assType = radioButton.getText().toString();                              //Get text from that radio button.

        Intent data = new Intent();
        data.putExtra(EXTRA_ASS_TITLE, assTitle);
        data.putExtra(EXTRA_ASS_DUE_DATE, assDueDate);
        data.putExtra(EXTRA_ASS_GOAL_DATE, assGoalDate);
        data.putExtra(EXTRA_ASS_FK_COURSE_ID, courseId);
        data.putExtra(EXTRA_ASS_TYPE, assType);

        int assId = getIntent().getIntExtra(EXTRA_ASS_ID, -1);
        if(assId != -1){
            data.putExtra(EXTRA_ASS_ID, assId);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //using IF statement here because we only have one button in our "menu". The save button.
        //IF we add more buttons in the future, we can use a case statement.
        if (item.getItemId() == R.id.save) {
            SaveNewAssessment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
