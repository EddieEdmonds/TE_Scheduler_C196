package com.example.te_scheduler_c196;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.te_scheduler_c196.Adapters.CourseAdapter;
import com.example.te_scheduler_c196.Adapters.MentorAdapter;
import com.example.te_scheduler_c196.Adapters.NoteAdapter;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Note;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Utility.DateUtil;
import com.example.te_scheduler_c196.ViewModels.CourseViewModel;
import com.example.te_scheduler_c196.ViewModels.MentorViewModel;
import com.example.te_scheduler_c196.ViewModels.NoteViewModel;
import com.example.te_scheduler_c196.ViewModels.TermViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_ID;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_TITLE;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_STATUS;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_END_DATE;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_START_DATE;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_MENTOR_ID;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_TERM_ID;

public class CourseEditActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int NOTE_ADD_REQUEST = 1;

    private EditText et_CourseEditTitle;
    private TextView tv_CourseStartDate, tv_CourseEndDate;
    private Spinner sp_CourseStatus, sp_CourseTerm, sp_CourseMentor;
    private CardView cv_MentorForCourse;

    private List<String> spCourseStatusList = new ArrayList<>();

    private List<String> mentorNameListString = new ArrayList<>();
    private List<Mentor> mentorListMentor = new ArrayList<>();

    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //Finding view items for editing the term.
        et_CourseEditTitle = findViewById(R.id.et_course_edit_title);
        tv_CourseStartDate = findViewById(R.id.tv_start_date);
        tv_CourseEndDate = findViewById(R.id.tv_end_date);
        sp_CourseStatus = findViewById(R.id.spinner_course_status);
        sp_CourseTerm = findViewById(R.id.spinner_course_term);
//        cv_MentorForCourse = findViewById(R.id.cv_mentor_for_course);

        final Intent courseData = getIntent();
        et_CourseEditTitle.setText(courseData.getStringExtra(EXTRA_COURSE_TITLE));
        tv_CourseStartDate.setText(courseData.getStringExtra(EXTRA_COURSE_START_DATE));
        tv_CourseEndDate.setText(courseData.getStringExtra(EXTRA_COURSE_END_DATE));

        spCourseStatusList.add("In Progress");
        spCourseStatusList.add("Completed");
        spCourseStatusList.add("Dropped");
        spCourseStatusList.add("Plan to Take");

        ArrayAdapter<String> sp_StatusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spCourseStatusList);
        sp_StatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_CourseStatus.setAdapter(sp_StatusAdapter);

////////////////////////////Displaying Notes for Course////////////////////////////////////////////////////////////////
        int courseId = courseData.getIntExtra(EXTRA_COURSE_ID, -1);
        RecyclerView rvNotesForCourse = findViewById(R.id.rv_note_for_course);
        rvNotesForCourse.setLayoutManager(new LinearLayoutManager(this));
        rvNotesForCourse.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        rvNotesForCourse.setAdapter(noteAdapter);

        final NoteViewModel noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getNotesByCourse(courseId).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> noteList) {
                noteAdapter.setNotes(noteList);
            }
        });


//////////////////////////////Getting mentor information for selected course///////////////////////////////////////////
        int mentorId = courseData.getIntExtra(EXTRA_COURSE_MENTOR_ID, -1);
        RecyclerView rvMentorForCourse = findViewById(R.id.rv_mentor_for_course);
        rvMentorForCourse.setLayoutManager(new LinearLayoutManager(this));
        rvMentorForCourse.setHasFixedSize(true);

        final MentorAdapter mentorAdapter = new MentorAdapter();
        rvMentorForCourse.setAdapter(mentorAdapter);

        final MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mentorViewModel.getMentorById(mentorId).observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(@Nullable List<Mentor> mentorList) {
                mentorAdapter.setMentors(mentorList);
            }
        });

//////////////////////////Clicking on Mentor/////////////////////////////////////////////////
//        final ArrayAdapter<String> sp_CourseMentorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mentorNameListString);
//        sp_CourseMentorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_CourseMentor.setAdapter(sp_CourseMentorAdapter);

        mentorAdapter.setOnMentorClickListener(new MentorAdapter.onMentorClickListener() {
            @Override
            public void onMentorClick(Mentor mentor) {
                final ArrayAdapter<String> sp_CourseMentorAdapter = new ArrayAdapter<String>(CourseEditActivity.this, android.R.layout.simple_spinner_item, mentorNameListString);
                sp_CourseMentorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_CourseMentor.setAdapter(sp_CourseMentorAdapter);


                mentorViewModel.getAllMentors().observe(CourseEditActivity.this, new Observer<List<Mentor>>() {
                    @Override
                    public void onChanged(@Nullable List<Mentor> mentorList) {
                        assert mentorList != null;
                        for(Mentor m:mentorList){
                            mentorNameListString.add(m.getMentor_name());
                            mentorListMentor.add(m);
                        }
                    }
                });




            }
            
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }
}
