package com.example.te_scheduler_c196;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTermActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    //private TextView et_StartDate, et_EndDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        TextView tv_StartDate = findViewById(R.id.tv_start_date);
        TextView tv_EndDate = findViewById(R.id.tv_end_date);

        tv_StartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddTermActivity.this,
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
                month += 1;

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                String dateString = month +"/"+dayOfMonth+"/"+year;
                Date dateFormattedLong = null;
                String dateFormattedShort = null;
                try {
                    dateFormattedLong = sdf.parse(dateString);
                    assert dateFormattedLong != null;
                    dateFormattedShort = sdf.format(dateFormattedLong);
                } catch (ParseException e) {
                    Log.e(TAG, "ParseException: "+e);
                }
                Log.i(TAG, "onDateSet Start: Long: "+dateFormattedLong+" And short: "+dateFormattedShort);

            }
        };

        tv_EndDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddTermActivity.this,
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
                month += 1;

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                String dateString = month +"/"+dayOfMonth+"/"+year;
                Date dateFormattedLong = null;
                String dateFormattedShort = null;
                try {
                    dateFormattedLong = sdf.parse(dateString);
                    assert dateFormattedLong != null;
                    dateFormattedShort = sdf.format(dateFormattedLong);
                } catch (ParseException e) {
                    Log.e(TAG, "ParseException: "+e);
                }
                Log.i(TAG, "onDateSet End: Long: "+dateFormattedLong+" And short: "+dateFormattedShort);

            }
        };

    }
}
