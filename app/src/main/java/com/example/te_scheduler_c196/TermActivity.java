package com.example.te_scheduler_c196;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.te_scheduler_c196.Adapters.CourseAdapter;
import com.example.te_scheduler_c196.Adapters.TermAdapter;
import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Utility.DateUtil;
import com.example.te_scheduler_c196.ViewModels.CourseViewModel;
import com.example.te_scheduler_c196.ViewModels.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermActivity extends AppCompatActivity {
    private static final String TAG = TermActivity.class.getSimpleName();
    private LiveData<List<Term>> allTerms;
    public static final int ADD_TERM_REQUEST = 1;
    public static final int EDIT_TERM_REQUEST = 2;

    private TermViewModel termViewModel;

    private int courseCount;

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

        //This is to control the FAB to open the TermAddActivity
        FloatingActionButton btnAddTerm = findViewById(R.id.btn_add_term);
        btnAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            //New term onClick method
            public void onClick(View v) {
                Intent intent = new Intent(TermActivity.this, TermAddActivity.class);
                startActivityForResult(intent, ADD_TERM_REQUEST);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                Term checkTerm = termAdapter.getTermAt(viewHolder.getAdapterPosition());
                int termId = checkTerm.getTerm_id();

                Executor executor = Executors.newSingleThreadExecutor();
                CourseViewModel courseViewModel = ViewModelProviders.of(TermActivity.this).get(CourseViewModel.class);
                executor.execute(() -> {
                    Looper.prepare();
                    int test = courseViewModel.getCourseCountByTerm(termId);
                    if (test > 0) {
                        termAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        Toast.makeText(TermActivity.this, "There appear to be courses in this term.", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Course count: " + test);
                    }else{
                        termViewModel.deleteTerm(checkTerm);
                        Log.i(TAG, "Course count: " + courseCount);
                        Toast.makeText(TermActivity.this, "Term Deleted", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                });
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
        }).attachToRecyclerView(termRecyclerView);

        termAdapter.setOnTermClickListener(new TermAdapter.onTermClickListener() {
            @Override
            //Edit term onClick method. Passes these extras to Edit Term based on which one we click on.
            //EXTRA_TERM_ID should pass the term id to the new TermEditActivity that is opened.
            public void onTermClick(Term term) {
                Intent intent = new Intent(TermActivity.this, TermEditActivity.class);
                intent.putExtra(TermAddActivity.EXTRA_TERM_ID, term.getTerm_id());
                intent.putExtra(TermAddActivity.EXTRA_TERM_TITLE, term.getTerm_title());
                intent.putExtra(TermAddActivity.EXTRA_TERM_START_DATE, DateUtil.dateToString(term.getTerm_start_date()));
                intent.putExtra(TermAddActivity.EXTRA_TERM_END_DATE, DateUtil.dateToString(term.getTerm_end_date()));
                startActivityForResult(intent, EDIT_TERM_REQUEST);
            }
        });
    }

    //Handles results passed back from the TermAdd activity and the TermEdit activity
    //This if statement handles New and Edit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK) {
            //This handles new terms being added.
            assert data != null;
            String term_title = data.getStringExtra(TermAddActivity.EXTRA_TERM_TITLE);
            String term_start_date = data.getStringExtra(TermAddActivity.EXTRA_TERM_START_DATE);
            String term_end_date = data.getStringExtra(TermAddActivity.EXTRA_TERM_END_DATE);

            Log.i(TAG, "Extras: TERM_EXTRA: " + term_title + " START_EXTRA: " + term_start_date + " END_EXTRA: " + term_end_date);

            Date termStartDate = DateUtil.stringToDateConverter(term_start_date);
            Date termEndDate = DateUtil.stringToDateConverter(term_end_date);

            Term term;
            try {
                term = new Term(term_title, termStartDate, termEndDate);
                termViewModel.insertTerm(term);
                Log.i(TAG, "Created new term");
            } catch (Exception e) {
                Log.i(TAG, "Something went wrong.");
                e.printStackTrace();
            }
            Toast.makeText(this, "Term Saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_TERM_REQUEST && resultCode == RESULT_OK) {
            //This is the update portion.
            int id = data.getIntExtra(TermAddActivity.EXTRA_TERM_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Term can't be updated!", Toast.LENGTH_SHORT).show();
                return;
            }

            String term_title = data.getStringExtra(TermAddActivity.EXTRA_TERM_TITLE);
            String term_start_date = data.getStringExtra(TermAddActivity.EXTRA_TERM_START_DATE);
            String term_end_date = data.getStringExtra(TermAddActivity.EXTRA_TERM_END_DATE);
            Date termStartDate = DateUtil.stringToDateConverter(term_start_date);
            Date termEndDate = DateUtil.stringToDateConverter(term_end_date);

            Term term = new Term(term_title, termStartDate, termEndDate);
            term.setTerm_id(id);

            termViewModel.updateTerm(term);
            Toast.makeText(this, "Term updated!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Term NOT Saved.", Toast.LENGTH_SHORT).show();
        }
    }
}
