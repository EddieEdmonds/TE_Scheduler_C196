package com.example.te_scheduler_c196;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.ViewModels.TermViewModel;

import java.util.List;

public class TermActivity extends AppCompatActivity {
    private TermViewModel termViewModel;
    private LiveData<List<Term>> allTerms;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

    }
}
