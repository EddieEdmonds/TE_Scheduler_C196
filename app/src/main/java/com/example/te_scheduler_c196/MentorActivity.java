package com.example.te_scheduler_c196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.te_scheduler_c196.Adapters.MentorAdapter;
import com.example.te_scheduler_c196.Adapters.TermAdapter;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.ViewModels.MentorViewModel;
import com.example.te_scheduler_c196.ViewModels.TermViewModel;

import java.util.List;

public class MentorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);

        RecyclerView mentorRecyclerView = findViewById(R.id.mentor_recycler_view);
        mentorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mentorRecyclerView.setHasFixedSize(true);

        final MentorAdapter mentorAdapter = new MentorAdapter();
        mentorRecyclerView.setAdapter(mentorAdapter);

        MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mentorViewModel.getAllMentors().observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(List<Mentor> mentorList) {
                mentorAdapter.setMentors(mentorList);
            }
        });
    }
}
