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
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Utility.DateUtil;
import com.example.te_scheduler_c196.ViewModels.MentorViewModel;
import com.example.te_scheduler_c196.ViewModels.TermViewModel;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_END_DATE;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_ID;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_MENTOR_ID;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_START_DATE;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_STATUS;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_TERM_ID;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_TITLE;


public class CourseEditActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int NOTE_ADD_REQUEST = 1;

    private EditText et_CourseEditTitle;
    private TextView tv_CourseStartDate, tv_CourseEndDate;
    private Spinner sp_CourseStatus, sp_CourseTerm, sp_CourseMentor;
    private CardView cv_MentorForCourse;

    private String dateFormattedShort = null;

    private List<String> spCourseStatusList = new ArrayList<>();
    private List<String> mentorNameList = new ArrayList<>();
    private List<String> currentMentorList = new ArrayList<>();
    private List<String> termTitleList = new ArrayList<>();
    private List<String> currentTermList = new ArrayList<>();

    private List<Term> termListTerm = new ArrayList<>();
    private List<Mentor> mentorListMentor = new ArrayList<>();

    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        final Intent courseData = getIntent();

        //Finding view items for editing the term.
        et_CourseEditTitle = findViewById(R.id.et_course_edit_title);
        tv_CourseStartDate = findViewById(R.id.tv_start_date);
        tv_CourseEndDate = findViewById(R.id.tv_end_date);
        sp_CourseStatus = findViewById(R.id.spinner_course_status);
        sp_CourseTerm = findViewById(R.id.spinner_course_term);
        sp_CourseMentor = findViewById(R.id.spinner_course_mentor);

        //Setting Title, start, end dates if the Extras exist
        et_CourseEditTitle.setText(courseData.getStringExtra(EXTRA_COURSE_TITLE));
        tv_CourseStartDate.setText(courseData.getStringExtra(EXTRA_COURSE_START_DATE));
        tv_CourseEndDate.setText(courseData.getStringExtra(EXTRA_COURSE_END_DATE));

        //adding 4 options for course status.
        spCourseStatusList.add("In Progress");
        spCourseStatusList.add("Completed");
        spCourseStatusList.add("Dropped");
        spCourseStatusList.add("Plan to Take");


//////////////////////////Handles Course Status Spinner/////////////////////////////////////////////////
        ArrayAdapter<String> sp_StatusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spCourseStatusList);
        sp_StatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_CourseStatus.setAdapter(sp_StatusAdapter);

        //Checks which status is passed via EXTRA. Sets spinner to that status.
        String courseStatus = courseData.getStringExtra(EXTRA_COURSE_STATUS);
        for(String s:spCourseStatusList){
            assert courseStatus != null;
            if(courseStatus.equals(s)){
                int statusIndex = getIndexM(sp_CourseStatus, courseStatus);
                sp_CourseStatus.setSelection(statusIndex);
            }
        }

////////////////////////////Displaying Notes for Course////////////////////////////////////////////////////////////////
//        int courseId = courseData.getIntExtra(EXTRA_COURSE_ID, -1);
//        RecyclerView rvNotesForCourse = findViewById(R.id.rv_note_for_course);
//        rvNotesForCourse.setLayoutManager(new LinearLayoutManager(this));
//        rvNotesForCourse.setHasFixedSize(true);
//
//        final NoteAdapter noteAdapter = new NoteAdapter();
//        rvNotesForCourse.setAdapter(noteAdapter);
//
//        final NoteViewModel noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
//        noteViewModel.getNotesByCourse(courseId).observe(this, new Observer<List<Note>>() {
//            @Override
//            public void onChanged(@Nullable List<Note> noteList) {
//                noteAdapter.setNotes(noteList);
//            }
//        });


//////////////////////////////Getting mentor information for selected course to fill recycler view///////////////////////////////////////////
//        int mentorId = courseData.getIntExtra(EXTRA_COURSE_MENTOR_ID, -1);
//        RecyclerView rvMentorForCourse = findViewById(R.id.rv_mentor_for_course);
//        rvMentorForCourse.setLayoutManager(new LinearLayoutManager(this));
//        rvMentorForCourse.setHasFixedSize(true);
//
//        final MentorAdapter mentorAdapter = new MentorAdapter();
//        rvMentorForCourse.setAdapter(mentorAdapter);
//
//        final MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
//        mentorViewModel.getMentorById(mentorId).observe(this, new Observer<List<Mentor>>() {
//            @Override
//            public void onChanged(@Nullable List<Mentor> mentorList) {
//                mentorAdapter.setMentors(mentorList);
//                mentorAdapter.notifyDataSetChanged();
//            }
//        });

//////////////////////////Handling Mentor spinner menu/////////////////////////////////////////////////
        int mentorId = courseData.getIntExtra(EXTRA_COURSE_MENTOR_ID, -1);
        final ArrayAdapter<String> sp_CourseMentorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mentorNameList);
        sp_CourseMentorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_CourseMentor.setAdapter(sp_CourseMentorAdapter);

        final MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        mentorViewModel.getAllMentors().observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(@Nullable List<Mentor> mentorList) {
                if (mentorList != null) {
                    for (Mentor m : mentorList) {
                        mentorNameList.add(m.getMentor_name());
                        mentorListMentor.add(m);
                    }
                }
                sp_CourseMentorAdapter.notifyDataSetChanged();
                //This loop looks at the list of all mentors, checks mentorId for each one, if it matches the course we're editing,
                //finds the index in the spinner and sets spinner display proper mentor..
                for(Mentor m: mentorListMentor){
                    if (m.getMentor_id()==mentorId){
                        String mentorName = m.getMentor_name();
                        int spinnerSelection = getIndexM(sp_CourseMentor, mentorName);
                        sp_CourseMentor.setSelection(spinnerSelection);
                    }
                }
            }
        });

//////////////////////////Handling Term Spinner/////////////////////////////////////////////////
        int termId = courseData.getIntExtra(EXTRA_COURSE_TERM_ID, -1);
        final ArrayAdapter<String> sp_CourseTermAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, termTitleList);
        sp_CourseTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_CourseTerm.setAdapter(sp_CourseTermAdapter);

        final TermViewModel termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> termList) {
                if (termList != null) {
                    for (Term t : termList) {
                        termTitleList.add(t.getTerm_title());
                        termListTerm.add(t);
                    }
                }
                sp_CourseTermAdapter.notifyDataSetChanged();
                //This loop looks at the list of all terms, checks termId for each one, if it matches the course we're editing,
                //finds the index in the spinner and sets spinner display proper term.
                for(Term t: termListTerm){
                    if (t.getTerm_id()==termId){
                        String termTitle = t.getTerm_title();
                        int spinnerSelection = getIndexM(sp_CourseTerm, termTitle);
                        sp_CourseTerm.setSelection(spinnerSelection);
                    }
                }
            }
        });

/////////////////////////Start Date//////////////////////////////////////////
        tv_CourseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDateString = tv_CourseStartDate.getText().toString();
                //String endDateString = courseData.getStringExtra(EXTRA_TERM_END_DATE);
                Date startDate = DateUtil.stringToDateConverter(startDateString);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int year = localStartDate.getYear();
                    int month = localStartDate.getMonthValue() - 1;
                    int day = localStartDate.getDayOfMonth();
                    DatePickerDialog dialog = new DatePickerDialog(
                            CourseEditActivity.this, R.style.DatePickerDialogTheme,
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
                            CourseEditActivity.this, R.style.DatePickerDialogTheme,
                            startDateSetListener,
                            year, month, day);
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateFormattedShort = DateUtil.dateConverter(year, month, dayOfMonth);
                tv_CourseStartDate.setText(dateFormattedShort);
                Log.i(TAG, "onDateSet Start: short: " + dateFormattedShort);

            }
        };

/////////////////////////End Date//////////////////////////////////////////
        tv_CourseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String startDateString = courseData.getStringExtra(EXTRA_TERM_START_DATE);
                String endDateString = tv_CourseEndDate.getText().toString();
                Date startDate = DateUtil.stringToDateConverter(endDateString);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int year = localStartDate.getYear();
                    int month = localStartDate.getMonthValue() - 1;
                    int day = localStartDate.getDayOfMonth();
                    DatePickerDialog dialog = new DatePickerDialog(
                            CourseEditActivity.this, R.style.DatePickerDialogTheme,
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
                            CourseEditActivity.this, R.style.DatePickerDialogTheme,
                            endDateSetListener,
                            year, month, day);
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateFormattedShort = DateUtil.dateConverter(year, month, dayOfMonth);
                tv_CourseEndDate.setText(dateFormattedShort);
                Log.i(TAG, "onDateSet End: short: " + dateFormattedShort);

            }
        };



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveEditCourse();
            Log.i(TAG, "Save button clicked.");
        }
        return super.onOptionsItemSelected(item);
    }

    private int getIndexM(@NotNull Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexT(@NotNull Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                return i;
            }
        }
        return -1;
    }

    private void saveEditCourse() {
        String courseTitle = et_CourseEditTitle.getText().toString();
        String courseStartDate = tv_CourseStartDate.getText().toString();
        String courseEndDate = tv_CourseEndDate.getText().toString();
        String courseStatus = sp_CourseStatus.getSelectedItem().toString();

        int courseId = getIntent().getIntExtra(EXTRA_COURSE_ID, -1);

        int selectedTermPosition = sp_CourseTerm.getSelectedItemPosition();             //Get selected term position in list
        Term term = termListTerm.get(selectedTermPosition);                             //Use position to select that Term.
        int courseTermId = term.getTerm_id();                                           //Use that Term to select Term ID


        int selectedMentorPosition = sp_CourseMentor.getSelectedItemPosition();         //get selected mentors position in list
        Mentor mentor = mentorListMentor.get(selectedMentorPosition);                   //Use position to select that Mentor.
        int courseMentorId = mentor.getMentor_id();                                     //Use that mentor to select mentor ID as int.


        if (courseTitle.trim().isEmpty() || courseStartDate.trim().isEmpty() || courseEndDate.trim().isEmpty() || courseStatus.isEmpty()) {
            Toast.makeText(this, "Please enter Title, Start Date, and End Date and Status", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent courseData = new Intent();
        courseData.putExtra(EXTRA_COURSE_TITLE, courseTitle);
        courseData.putExtra(EXTRA_COURSE_START_DATE, courseStartDate);
        courseData.putExtra(EXTRA_COURSE_END_DATE, courseEndDate);
        courseData.putExtra(EXTRA_COURSE_STATUS, courseStatus);
        courseData.putExtra(EXTRA_COURSE_TERM_ID, courseTermId);
        courseData.putExtra(EXTRA_COURSE_MENTOR_ID, courseMentorId);
        if (courseId != -1) {
            courseData.putExtra(EXTRA_COURSE_ID, courseId);
        }

        setResult(RESULT_OK, courseData);
        finish();

    }


}

