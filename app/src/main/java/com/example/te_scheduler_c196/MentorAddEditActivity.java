package com.example.te_scheduler_c196;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class MentorAddEditActivity extends AppCompatActivity {
    private static final String TAG = MentorAddEditActivity.class.getSimpleName();

    public static final String EXTRA_MENTOR_ID = "com.example.te_scheduler_c196.EXTRA_MENTOR_ID";
    public static final String EXTRA_MENTOR_NAME = "com.example.te_scheduler_c196.EXTRA_MENTOR_NAME";
    public static final String EXTRA_MENTOR_PHONE = "com.example.te_scheduler_c196.EXTRA_MENTOR_PHONE";
    public static final String EXTRA_MENTOR_EMAIL = "com.example.te_scheduler_c196.EXTRA_MENTOR_EMAIL";

    private EditText et_MentorName, et_MentorPhone, et_MentorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_add);

        et_MentorName = findViewById(R.id.et_mentor_name);
        et_MentorPhone = findViewById(R.id.et_mentor_phone);
        et_MentorEmail = findViewById(R.id.et_mentor_email);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_MENTOR_ID)){
            setTitle("Edit Mentor");

            et_MentorName.setText(intent.getStringExtra(EXTRA_MENTOR_NAME));
            et_MentorPhone.setText(intent.getStringExtra(EXTRA_MENTOR_PHONE));
            et_MentorEmail.setText(intent.getStringExtra(EXTRA_MENTOR_EMAIL));

        }else{
            setTitle("New Mentor");
        }

    }

    public void SaveMentor(){
        String mentorName = et_MentorName.getText().toString();
        String mentorPhone = et_MentorPhone.getText().toString();
        String mentorEmail = et_MentorEmail.getText().toString();

        Intent data = new Intent();
        data.putExtra(EXTRA_MENTOR_NAME, mentorName);
        data.putExtra(EXTRA_MENTOR_PHONE, mentorPhone);
        data.putExtra(EXTRA_MENTOR_EMAIL, mentorEmail);

        int mentorId = getIntent().getIntExtra(EXTRA_MENTOR_ID, -1);
        if(mentorId!=-1){
            data.putExtra(EXTRA_MENTOR_ID, mentorId);
        }

        setResult(RESULT_OK, data);
        finish();
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
        //IF we add more buttons in the future, we can use a case statement.
        if (item.getItemId() == R.id.save) {
            SaveMentor();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
