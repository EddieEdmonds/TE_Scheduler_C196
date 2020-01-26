package com.example.te_scheduler_c196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.te_scheduler_c196.Adapters.TermAdapter;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.ViewModels.TermViewModel;

import java.util.List;

public class TermActivity extends AppCompatActivity {
    private TermViewModel termViewModel;
    private LiveData<List<Term>> allTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        RecyclerView termRecyclerView = findViewById(R.id.term_recycler_view);
        termRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        termRecyclerView.setHasFixedSize(true);

        final TermAdapter termAdapter = new TermAdapter();
        termRecyclerView.setAdapter(termAdapter);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> termList) {
                termAdapter.setTerms(termList);

            }
        });
    }




}
