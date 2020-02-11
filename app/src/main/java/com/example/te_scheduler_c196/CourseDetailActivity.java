package com.example.te_scheduler_c196;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.te_scheduler_c196.CourseActivity.EDIT_COURSE_REQUEST;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_END_DATE;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_ID;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_MENTOR_ID;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_START_DATE;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_STATUS;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_TERM_ID;
import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_TITLE;
import static com.example.te_scheduler_c196.NoteAddActivity.EXTRA_NOTE_BODY;
import static com.example.te_scheduler_c196.NoteAddActivity.EXTRA_NOTE_TITLE;

public class CourseDetailActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int ADD_NOTE_REQUEST = 5;

    private TextView tv_CourseStartDate, tv_CourseEndDate, tv_CourseStatus, tv_courseTerm, tv_CourseEditTitle;
    private int courseId, termId, mentorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        //Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        final Intent courseData = getIntent();

        //Finding view items for editing the term.
        tv_CourseEditTitle = findViewById(R.id.tv_course_edit_title);
        tv_CourseStartDate = findViewById(R.id.tv_start_date);
        tv_CourseEndDate = findViewById(R.id.tv_end_date);
        tv_CourseStatus = findViewById(R.id.tv_course_status_display);
        tv_courseTerm = findViewById(R.id.tv_course_term_display);

        //Setting Title, start, end dates if the Extras exist
        tv_CourseEditTitle.setText(courseData.getStringExtra(EXTRA_COURSE_TITLE));
        tv_CourseStartDate.setText(courseData.getStringExtra(EXTRA_COURSE_START_DATE));
        tv_CourseEndDate.setText(courseData.getStringExtra(EXTRA_COURSE_END_DATE));
        tv_CourseStatus.setText(courseData.getStringExtra(EXTRA_COURSE_STATUS));

        courseId = courseData.getIntExtra(EXTRA_COURSE_ID, -1);
        termId = courseData.getIntExtra(EXTRA_COURSE_TERM_ID, -1);
        mentorId = courseData.getIntExtra(EXTRA_COURSE_MENTOR_ID, -1);

        //Mentor is passed in via an ID so I need to get that ID.
        //Check it against all mentors and get the name of the match. Display that name.
        int termId = courseData.getIntExtra(EXTRA_COURSE_TERM_ID, -1);
        final TermViewModel termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getTermByid(termId).observe(this, mentorList -> {
            if (mentorList != null) {
                for (Term t : mentorList) {
                    String termTitle = t.getTerm_title();
                    tv_courseTerm.setText(termTitle);
                }
            }
        });


//////////////////////////////Getting mentor information for selected course to fill recycler view///////////////////////////////////////////
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
                mentorAdapter.setMentorList(mentorList);
//                mentorAdapter.notifyDataSetChanged();
            }
        });

////////////////////////////Displaying Notes for Course////////////////////////////////////////////////////////////////
        int courseId = courseData.getIntExtra(EXTRA_COURSE_ID, -1);
        RecyclerView rvNotesForCourse = findViewById(R.id.rv_note_for_course);
        rvNotesForCourse.setLayoutManager(new LinearLayoutManager(this));
        rvNotesForCourse.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        rvNotesForCourse.setAdapter(noteAdapter);

        final NoteViewModel noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getNotesByCourse(courseId).observe(this, noteAdapter::setNotes);

        //This is to control the EFAB to open the NoteAddActivity
        ExtendedFloatingActionButton btnAddNote = findViewById(R.id.efab_add_note);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailActivity.this, NoteAddActivity.class);
                intent.putExtra(EXTRA_COURSE_ID, courseId);
                startActivityForResult(intent, ADD_NOTE_REQUEST);

            }
        });

        ///////////////////////////On swipe event for deleting a course in the TermEditActivity.//////////////////////////////
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                //onSwipe for delete of a Note in the TermEditActivity.
                //We grab the note that we swiped on.
                final Note swipedNote = noteAdapter.getNoteAt(viewHolder.getAdapterPosition());

                //We pop a dialog letting users know that Notes and Assessments will be deleted as well.
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage("Delete this note?");

                //If they click positive button: note, note, and assessments deleted
                // Note deleted via the below deleteNote method.
                // Notes and Assessments deleted through "on delete CASCADE" in the respective DAOs.
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // Your action
                        noteViewModel.deleteNote(swipedNote);
                        dialog.cancel();
                    }
                });
                //If you click cancel, view is refreshed and note is returned.
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        noteAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        }).attachToRecyclerView(rvNotesForCourse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Intent intent = new Intent(CourseDetailActivity.this, CourseEditActivity.class);
            intent.putExtra(CourseAddActivity.EXTRA_COURSE_TITLE, tv_CourseEditTitle.getText().toString());
            intent.putExtra(CourseAddActivity.EXTRA_COURSE_START_DATE, tv_CourseStartDate.getText());
            intent.putExtra(CourseAddActivity.EXTRA_COURSE_END_DATE, tv_CourseEndDate.getText());
            intent.putExtra(CourseAddActivity.EXTRA_COURSE_STATUS, tv_CourseStatus.getText());

            intent.putExtra(CourseAddActivity.EXTRA_COURSE_ID, courseId);
            intent.putExtra(CourseAddActivity.EXTRA_COURSE_TERM_ID, termId);
            intent.putExtra(CourseAddActivity.EXTRA_COURSE_MENTOR_ID, mentorId);

            startActivityForResult(intent, EDIT_COURSE_REQUEST);
            Log.i(TAG, "Edit button clicked.");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            assert data != null;
            CourseViewModel courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
            NoteViewModel noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

            Course course;
            Note note;

            int courseId = data.getIntExtra(EXTRA_COURSE_ID, -1);

            //Handles course edits and notes added to the course.
            //The below code updates the db with the new info.
            if (requestCode == EDIT_COURSE_REQUEST && resultCode == RESULT_OK) {                                        //This portion handles Edit Course from the CourseDetailActivity
                String courseTitle = data.getStringExtra(CourseAddActivity.EXTRA_COURSE_TITLE);
                String courseStartDate = data.getStringExtra(CourseAddActivity.EXTRA_COURSE_START_DATE);
                String courseEndDate = data.getStringExtra(CourseAddActivity.EXTRA_COURSE_END_DATE);
                String courseStatus = data.getStringExtra(CourseAddActivity.EXTRA_COURSE_STATUS);
                int courseTermId = data.getIntExtra(CourseAddActivity.EXTRA_COURSE_TERM_ID, -1);
                int fk_courseMentorId = data.getIntExtra(CourseAddActivity.EXTRA_COURSE_MENTOR_ID, -1);

                Date courseStartDateConverted = DateUtil.stringToDateConverter(courseStartDate);                        //Dates received from CourseAddActivity are in String format.
                Date courseEndDateConverted = DateUtil.stringToDateConverter(courseEndDate);                            //These two lines convert to Date format.

                tv_CourseEditTitle.setText(courseTitle);
                tv_CourseStartDate.setText(courseStartDate);
                tv_CourseEndDate.setText(courseEndDate);
                tv_CourseStatus.setText(courseStatus);

                //if editing course from detail page, this handles updating mentor information on detail page.
                RecyclerView rvMentorForCourse = findViewById(R.id.rv_mentor_for_course);
                rvMentorForCourse.setLayoutManager(new LinearLayoutManager(this));
                rvMentorForCourse.setHasFixedSize(true);
                final MentorAdapter mentorAdapter = new MentorAdapter();
                rvMentorForCourse.setAdapter(mentorAdapter);
                final MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
                mentorViewModel.getMentorById(fk_courseMentorId).observe(this, mentorAdapter::setMentorList);


                try {
                    if (courseId != -1) {
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
            }else if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){                                       //This one handles results if new note added from CourseDetailActivity.
                String noteTitle = data.getStringExtra(EXTRA_NOTE_TITLE);
                String noteBody = data.getStringExtra(EXTRA_NOTE_BODY);
                note = new Note(noteTitle, noteBody, courseId);
                noteViewModel.insertNote(note);
            }
            else {
                Toast.makeText(this, "Course not saved!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
