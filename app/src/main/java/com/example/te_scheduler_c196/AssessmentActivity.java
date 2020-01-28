package com.example.te_scheduler_c196;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.te_scheduler_c196.Adapters.AssessmentAdapter;
import com.example.te_scheduler_c196.Adapters.CourseAdapter;
import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.ViewModels.AssessmentViewModel;
import com.example.te_scheduler_c196.ViewModels.CourseViewModel;

import java.util.List;

public class AssessmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        RecyclerView assessmentRecyclerView = findViewById(R.id.assessment_recycler_view);
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentRecyclerView.setHasFixedSize(true);

        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter();
        assessmentRecyclerView.setAdapter(assessmentAdapter);

        AssessmentViewModel assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAllAssessments().observe(this, new Observer<List<Assessment>>(){
            @Override
            public void onChanged(@Nullable List<Assessment> assessmentList) {
                assessmentAdapter.setAssessmentList(assessmentList);
            }
        });
    }
}
