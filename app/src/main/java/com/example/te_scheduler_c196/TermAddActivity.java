package com.example.te_scheduler_c196;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.te_scheduler_c196.Utility.DateUtil;

import java.util.Calendar;

public class TermAddActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_TERM_ID ="com.example.te_scheduler_c196.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE ="com.example.te_scheduler_c196.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START_DATE ="com.example.te_scheduler_c196.EXTRA_TERM_START_DATE";
    public static final String EXTRA_TERM_END_DATE ="com.example.te_scheduler_c196.EXTRA_TERM_END_DATE";


    private String dateFormattedShort = null;
    private TextView tv_StartDate, tv_EndDate;
    private EditText et_TermTitle;


    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        tv_StartDate = findViewById(R.id.tv_start_date);
        tv_EndDate = findViewById(R.id.tv_end_date);
        et_TermTitle = findViewById(R.id.et_term_title);

        setTitle("Add Term");

        tv_StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TermAddActivity.this,
                        android.R.style.Theme_Black,
                        startDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
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

        tv_EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TermAddActivity.this,
                        android.R.style.Theme_Black,
                        endDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
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
    }


    private void saveNewTerm(){
        String termTitle = et_TermTitle.getText().toString();
        String termStartDate = tv_StartDate.getText().toString();
        String termEndDate = tv_EndDate.getText().toString();

        if(termTitle.trim().isEmpty()||termStartDate.trim().isEmpty()||termEndDate.trim().isEmpty()){
            Toast.makeText(this, "Please enter Title, Start Date, and End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TERM_TITLE, termTitle);
        data.putExtra(EXTRA_TERM_START_DATE, termStartDate);
        data.putExtra(EXTRA_TERM_END_DATE, termEndDate);

        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_term_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //using IF statement here because we only have one button in our "menu". The save button.
        //IF we add more buttons in the future, below this is a version using a Switch statement.
        if (item.getItemId() == R.id.save_term) {
            saveNewTerm();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.save_term:
//                saveTerm();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
}