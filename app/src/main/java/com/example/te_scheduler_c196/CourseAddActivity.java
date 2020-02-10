package com.example.te_scheduler_c196;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Utility.DateUtil;
import com.example.te_scheduler_c196.ViewModels.MentorViewModel;
import com.example.te_scheduler_c196.ViewModels.TermViewModel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CourseAddActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_COURSE_TERM_ID ="com.example.te_scheduler_c196.EXTRA_COURSE_TERM_ID";
    public static final String EXTRA_COURSE_ID = "com.example.te_scheduler_c196.EXTRA_COURSE_ID";
    public static final String EXTRA_COURSE_TITLE ="com.example.te_scheduler_c196.EXTRA_COURSE_TITLE";
    public static final String EXTRA_COURSE_START_DATE ="com.example.te_scheduler_c196.EXTRA_COURSE_START_DATE";
    public static final String EXTRA_COURSE_END_DATE ="com.example.te_scheduler_c196.EXTRA_COURSE_END_DATE";
    public static final String EXTRA_COURSE_STATUS ="com.example.te_scheduler_c196.EXTRA_COURSE_STATUS";
    public static final String EXTRA_COURSE_MENTOR_ID = "com.example.te_scheduler_c196.EXTRA_COURSE_MENTOR_ID";


    private String dateFormattedShort = null;
    private TextView tv_StartDate, tv_EndDate;
    private EditText et_CourseTitle;
    private Spinner sp_CourseStatus, sp_CourseTerm, sp_CourseMentor;

    private List<String> termListTitle = new ArrayList<>();
    private List<Term> termListTerm = new ArrayList<>();
    private static List<String> spCourseStatusList = new ArrayList<>();

    private List<String> spCourseMentorList = new ArrayList<>();
    private List<Mentor> mentorListMentor = new ArrayList<>();

    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        tv_StartDate = findViewById(R.id.tv_start_date);
        tv_EndDate = findViewById(R.id.tv_end_date);
        et_CourseTitle = findViewById(R.id.et_course_title);
        sp_CourseStatus = findViewById(R.id.spinner_course_status);
        sp_CourseTerm = findViewById(R.id.spinner_course_term);
        sp_CourseMentor = findViewById(R.id.spinner_course_mentor);


        spCourseStatusList.add("In Progress");
        spCourseStatusList.add("Completed");
        spCourseStatusList.add("Dropped");
        spCourseStatusList.add("Plan to Take");


        ArrayAdapter<String> sp_StatusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spCourseStatusList);
        sp_StatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_CourseStatus.setAdapter(sp_StatusAdapter);

////////////////Handles getting all of the available terms and filing the term spinner.//////////////////////////////////////
        //final TermAdapter termAdapter = new TermAdapter();
        final ArrayAdapter<String> sp_CourseTermAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, termListTitle);
        sp_CourseTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_CourseTerm.setAdapter(sp_CourseTermAdapter);

        TermViewModel termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(@Nullable List<Term> termList) {
                if(termList!=null){
                    for(Term t : termList){
                        //termListTitle.add(t.getTerm_title());
                        termListTitle.add(t.getTerm_title());
                        termListTerm.add(t);
                    }
                }
                sp_CourseTermAdapter.notifyDataSetChanged();
            }
        });


///////////////////////////Gets all mentors and adds them to the mentor spinner list///////////////////////////////
        final ArrayAdapter<String> sp_CourseMentorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spCourseMentorList);
        sp_CourseMentorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_CourseMentor.setAdapter(sp_CourseMentorAdapter);


        MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mentorViewModel.getAllMentors().observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(@Nullable List<Mentor> mentorList) {
                if(mentorList!=null){
                    for(Mentor m : mentorList){
                        spCourseMentorList.add(m.getMentor_name());
                        mentorListMentor.add(m);
                        //termListTerm.add(m);
                    }
                }
                sp_CourseMentorAdapter.notifyDataSetChanged();
            }
        });


        setTitle("Add Course");

///////////////////////Start Date/////////////////////////////
        tv_StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_StartDate.getText().toString().equals("")){
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            CourseAddActivity.this, R.style.DatePickerDialogTheme,
                            startDateSetListener,
                            year, month, day);
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }else{
                    String startDateString = tv_StartDate.getText().toString();
                    Date startDate = DateUtil.stringToDateConverter(startDateString);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        int year = localStartDate.getYear();
                        int month = localStartDate.getMonthValue()-1;
                        int day = localStartDate.getDayOfMonth();
                        DatePickerDialog dialog = new DatePickerDialog(
                                CourseAddActivity.this, R.style.DatePickerDialogTheme,
                                startDateSetListener,
                                year, month, day);
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    } else {
                        int year = startDate.getYear();
                        int month = startDate.getMonth();
                        int day = startDate.getDay();
                        DatePickerDialog dialog = new DatePickerDialog(
                                CourseAddActivity.this, R.style.DatePickerDialogTheme,
                                startDateSetListener,
                                year, month, day);
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }
            }
        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateFormattedShort = DateUtil.dateConverter(year, month, dayOfMonth);
                tv_StartDate.setText(dateFormattedShort);
                Log.i(TAG, "onDateSet Start: short: " + dateFormattedShort);

            }
        };

///////////////////////End Date/////////////////////////////
        tv_EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_EndDate.getText().toString().equals("")){
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            CourseAddActivity.this, R.style.DatePickerDialogTheme,
                            endDateSetListener,
                            year, month, day);
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }else{
                    String endDateString = tv_EndDate.getText().toString();
                    Date startDate = DateUtil.stringToDateConverter(endDateString);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        int year = localStartDate.getYear();
                        int month = localStartDate.getMonthValue()-1;
                        int day = localStartDate.getDayOfMonth();
                        DatePickerDialog dialog = new DatePickerDialog(
                                CourseAddActivity.this, R.style.DatePickerDialogTheme,
                                endDateSetListener,
                                year, month, day);
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    } else {
                        int year = startDate.getYear();
                        int month = startDate.getMonth();
                        int day = startDate.getDay();
                        DatePickerDialog dialog = new DatePickerDialog(
                                CourseAddActivity.this, R.style.DatePickerDialogTheme,
                                endDateSetListener,
                                year, month, day);
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }

            }
        });

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateFormattedShort = DateUtil.dateConverter(year, month, dayOfMonth);
                tv_EndDate.setText(dateFormattedShort);
                Log.i(TAG, "onDateSet End: short: " + dateFormattedShort);

            }
        };

    }

    private void saveNewCourse(){
        String courseTitle = et_CourseTitle.getText().toString();
        String courseStartDate = tv_StartDate.getText().toString();
        String courseEndDate = tv_EndDate.getText().toString();
        String courseStatus = sp_CourseStatus.getSelectedItem().toString();

        int selectedTermPosition = sp_CourseTerm.getSelectedItemPosition();             //Get selected term position in list
        Term term = termListTerm.get(selectedTermPosition);                             //Use position to select that Term.
        int courseTermId = term.getTerm_id();                                           //Use that Term to select Term ID


        int selectedMentorPosition = sp_CourseMentor.getSelectedItemPosition();         //get selected mentors position in list
        Mentor mentor = mentorListMentor.get(selectedMentorPosition);                   //Use position to select that Mentor.
        int courseMentorId = mentor.getMentor_id();                                     //Use that mentor to select mentor ID as int.


        if(courseTitle.trim().isEmpty()||courseStartDate.trim().isEmpty()||courseEndDate.trim().isEmpty()||courseStatus.isEmpty()){
            Toast.makeText(this, "Please enter Title, Start Date, and End Date and Status", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_COURSE_TITLE, courseTitle);
        data.putExtra(EXTRA_COURSE_START_DATE, courseStartDate);
        data.putExtra(EXTRA_COURSE_END_DATE, courseEndDate);
        data.putExtra(EXTRA_COURSE_STATUS, courseStatus);
        data.putExtra(EXTRA_COURSE_TERM_ID, courseTermId);
        data.putExtra(EXTRA_COURSE_MENTOR_ID, courseMentorId);

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
            saveNewCourse();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
