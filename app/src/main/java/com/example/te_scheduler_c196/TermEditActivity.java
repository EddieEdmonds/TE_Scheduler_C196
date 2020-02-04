package com.example.te_scheduler_c196;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.te_scheduler_c196.Adapters.CourseAdapter;
import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Note;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Utility.DateUtil;
import com.example.te_scheduler_c196.ViewModels.AssessmentViewModel;
import com.example.te_scheduler_c196.ViewModels.CourseViewModel;
import com.example.te_scheduler_c196.ViewModels.NoteViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.te_scheduler_c196.TermAddActivity.EXTRA_TERM_END_DATE;
import static com.example.te_scheduler_c196.TermAddActivity.EXTRA_TERM_ID;
import static com.example.te_scheduler_c196.TermAddActivity.EXTRA_TERM_START_DATE;
import static com.example.te_scheduler_c196.TermAddActivity.EXTRA_TERM_TITLE;

public class TermEditActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int ADD_COURSE_REQUEST = 1;

    private TextView tv_StartDate, tv_EndDate;
    private EditText et_TermTitle;

    private String dateFormattedShort = null;

    private List<Note> notesAssocWithCourse = new ArrayList<>();
    private List<Assessment> assAssocWithCourse = new ArrayList<>();

    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        //Finding view items for editing the term.
        et_TermTitle = findViewById(R.id.et_term_title);
        tv_StartDate = findViewById(R.id.tv_start_date);
        tv_EndDate = findViewById(R.id.tv_end_date);


        final Intent termData = getIntent();
        et_TermTitle.setText(termData.getStringExtra(EXTRA_TERM_TITLE));
        tv_StartDate.setText(termData.getStringExtra(EXTRA_TERM_START_DATE));
        tv_EndDate.setText(termData.getStringExtra(EXTRA_TERM_END_DATE));

        int termId = termData.getIntExtra(EXTRA_TERM_ID, 0);


//////////////////Everything for handling the course display when inside a term.///////////////////////
        RecyclerView courseForTermRecyclerView = findViewById(R.id.courseForTerm_recycler_view);
        courseForTermRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseForTermRecyclerView.setHasFixedSize(true);

        final CourseAdapter courseAdapter = new CourseAdapter();
        courseForTermRecyclerView.setAdapter(courseAdapter);

        final CourseViewModel courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCoursesByTerm(termId).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courseList) {
                courseAdapter.setCourseList(courseList);
            }
        });

/////////////////////////Start Date//////////////////////////////////////////
        tv_StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDateString = tv_StartDate.getText().toString();
                //String endDateString = termData.getStringExtra(EXTRA_TERM_END_DATE);
                Date startDate = DateUtil.stringToDateConverter(startDateString);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int year = localStartDate.getYear();
                    int month = localStartDate.getMonthValue()-1;
                    int day = localStartDate.getDayOfMonth();
                    DatePickerDialog dialog = new DatePickerDialog(
                            TermEditActivity.this, R.style.DatePickerDialogTheme,
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
                            TermEditActivity.this, R.style.DatePickerDialogTheme,
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
                tv_StartDate.setText(dateFormattedShort);
                Log.i(TAG, "onDateSet Start: short: " + dateFormattedShort);

            }
        };

/////////////////////////End Date//////////////////////////////////////////
        tv_EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String startDateString = termData.getStringExtra(EXTRA_TERM_START_DATE);
                String endDateString = tv_EndDate.getText().toString();
                Date startDate = DateUtil.stringToDateConverter(endDateString);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int year = localStartDate.getYear();
                    int month = localStartDate.getMonthValue()-1;
                    int day = localStartDate.getDayOfMonth();
                    DatePickerDialog dialog = new DatePickerDialog(
                            TermEditActivity.this, R.style.DatePickerDialogTheme,
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
                            TermEditActivity.this, R.style.DatePickerDialogTheme,
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
                tv_EndDate.setText(dateFormattedShort);
                Log.i(TAG, "onDateSet End: short: " + dateFormattedShort);

            }
        };

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
                //onSwipe for delete of a Course in the TermEditActivity.
                //We grab the course that we swiped on.
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
                builder.show();
            }
        }).attachToRecyclerView(courseForTermRecyclerView);

        //This is to control the FAB to open the TermAddActivity
        ExtendedFloatingActionButton btnAddCourse = findViewById(R.id.fab_add_course);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            //New term onClick method
            public void onClick(View v) {
                Intent intent = new Intent(TermEditActivity.this, CourseAddActivity.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST);

            }
        });

        setTitle("Edit Term");
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
        //IF we add more buttons in the future, below this is a version using a Switch statement.
        if (item.getItemId() == R.id.save) {
            saveEditTerm();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void saveEditTerm() {
        String termTitle = et_TermTitle.getText().toString();
        String termStartDate = tv_StartDate.getText().toString();
        String termEndDate = tv_EndDate.getText().toString();

        if (termTitle.trim().isEmpty() || termStartDate.trim().isEmpty() || termEndDate.trim().isEmpty()) {
            Toast.makeText(this, "Please enter Title, Start Date, and End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent termData = new Intent();
        termData.putExtra(EXTRA_TERM_TITLE, termTitle);
        termData.putExtra(EXTRA_TERM_START_DATE, termStartDate);
        termData.putExtra(EXTRA_TERM_END_DATE, termEndDate);

        int id = getIntent().getIntExtra(EXTRA_TERM_ID, -1);
        if (id != -1) {
            termData.putExtra(EXTRA_TERM_ID, id);
        }

        setResult(RESULT_OK, termData);
        finish();

    }
}
