package com.example.te_scheduler_c196;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CourseDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


    }
}
