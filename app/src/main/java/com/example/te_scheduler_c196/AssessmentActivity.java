package com.example.te_scheduler_c196;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.te_scheduler_c196.Adapters.AssessmentAdapter;
import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.Utility.DateUtil;
import com.example.te_scheduler_c196.ViewModels.AssessmentViewModel;
import com.example.te_scheduler_c196.ViewModels.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class AssessmentActivity extends AppCompatActivity {
    public static final int ADD_ASS_REQUEST = 6;
    public static final int EDIT_ASS_REQUEST = 7;

    private AlertDialog alertDialog;

    private AssessmentViewModel assessmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        RecyclerView assessmentRecyclerView = findViewById(R.id.assessment_recycler_view);
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentRecyclerView.setHasFixedSize(true);

        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter();
        assessmentRecyclerView.setAdapter(assessmentAdapter);

        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAllAssessments().observe(this, new Observer<List<Assessment>>(){
            @Override
            public void onChanged(@Nullable List<Assessment> assessmentList) {
                assessmentAdapter.setAssessmentList(assessmentList);
            }
        });

        CourseViewModel courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                assessmentAdapter.setCourseList(courses);
            }
        });

        //This is to control the FAB to open the TermAddActivity
        FloatingActionButton btnAddTerm = findViewById(R.id.btn_add_assessment);
        btnAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            //New term onClick method
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentActivity.this, AssessmentAddEditActivity.class);
                startActivityForResult(intent, ADD_ASS_REQUEST);

            }
        });

        //handles swiping assessment to delete.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final Assessment swipedAssessment = assessmentAdapter.getAssessmentAt(viewHolder.getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage("Delete this assessment?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // Your action
                        assessmentViewModel.deleteAssessment(swipedAssessment);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        assessmentAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    }
                });
                alertDialog = builder.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        }).attachToRecyclerView(assessmentRecyclerView);

        assessmentAdapter.setOnAssClickListener(new AssessmentAdapter.onAssClickListener() {
            @Override
            public void onAssClick(Assessment assessment) {
                Intent intent = new Intent(AssessmentActivity.this, AssessmentAddEditActivity.class);
                intent.putExtra(AssessmentAddEditActivity.EXTRA_ASS_ID, assessment.getAss_id());
                intent.putExtra(AssessmentAddEditActivity.EXTRA_ASS_TITLE, assessment.getAss_title());

                //The date info received from the assessment is in the Date format.
                //Use my converter to convert to a readable string to pass to the EXTRA.
                String stringDueDate = DateUtil.dateToString(assessment.getAss_due_date());
                String stringGoalDate = DateUtil.dateToString(assessment.getAss_goal_date());

                intent.putExtra(AssessmentAddEditActivity.EXTRA_ASS_DUE_DATE, stringDueDate);
                intent.putExtra(AssessmentAddEditActivity.EXTRA_ASS_GOAL_DATE, stringGoalDate);
                intent.putExtra(AssessmentAddEditActivity.EXTRA_ASS_TYPE, assessment.getAss_type());
                intent.putExtra(AssessmentAddEditActivity.EXTRA_ASS_FK_COURSE_ID, assessment.getFk_course_id());

                startActivityForResult(intent, EDIT_ASS_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= RESULT_CANCELED){
            assert data != null;
            String assTitle = data.getStringExtra(AssessmentAddEditActivity.EXTRA_ASS_TITLE);
            String assDueDate = data.getStringExtra(AssessmentAddEditActivity.EXTRA_ASS_DUE_DATE);
            String goalDate = data.getStringExtra(AssessmentAddEditActivity.EXTRA_ASS_GOAL_DATE);
            int fk_courseId = data.getIntExtra(AssessmentAddEditActivity.EXTRA_ASS_FK_COURSE_ID,-1);
            String assType = data.getStringExtra(AssessmentAddEditActivity.EXTRA_ASS_TYPE);

            Date dateAssDueDate = DateUtil.stringToDateConverter(assDueDate);
            Date dateAssGoalDate = DateUtil.stringToDateConverter(goalDate);

            Assessment assessment;

            if(requestCode==ADD_ASS_REQUEST && resultCode==RESULT_OK){
                try{
                    //public Assessment(String ass_type, String ass_title, Date ass_due_date, Date ass_goal_date, int fk_course_id) {
                    assessment = new Assessment(assType, assTitle, dateAssDueDate, dateAssGoalDate, fk_courseId);
                    assessmentViewModel.insertAssessment(assessment);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if(requestCode==EDIT_ASS_REQUEST&&resultCode==RESULT_OK){
                int assId = data.getIntExtra(AssessmentAddEditActivity.EXTRA_ASS_ID, -1);
                try{
                    //public Assessment(String ass_type, String ass_title, Date ass_due_date, Date ass_goal_date, int fk_course_id) {
                    assessment = new Assessment(assType, assTitle, dateAssDueDate, dateAssGoalDate, fk_courseId);
                    assessment.setAss_id(assId);
                    assessmentViewModel.updateAssessment(assessment);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}
