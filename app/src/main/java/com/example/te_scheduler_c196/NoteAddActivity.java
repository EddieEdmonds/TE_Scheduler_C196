package com.example.te_scheduler_c196;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import static com.example.te_scheduler_c196.CourseAddActivity.EXTRA_COURSE_ID;

public class NoteAddActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

//    public static final String EXTRA_NOTE_ID ="com.example.te_scheduler_c196.EXTRA_NOTE_ID";
//    public static final String EXTRA_FK_COURSE_ID ="com.example.te_scheduler_c196.EXTRA_FK_COURSE_ID";
    public static final String EXTRA_NOTE_TITLE ="com.example.te_scheduler_c196.EXTRA_NOTE_TITLE";
    public static final String EXTRA_NOTE_BODY ="com.example.te_scheduler_c196.EXTRA_NOTE_BODY";

    private EditText et_NoteTitle, et_NoteBody;
    private int fk_course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        final Intent courseData = getIntent();

        et_NoteTitle = findViewById(R.id.et_note_title);
        et_NoteBody = findViewById(R.id.et_note_body);
        fk_course_id = courseData.getIntExtra(EXTRA_COURSE_ID, -1);
        Log.i(TAG, "fk_course_id: " +fk_course_id);


        setTitle("Add Note");
    }

    private void saveNewNote(){
        String noteTitle = et_NoteTitle.getText().toString();
        String noteBody = et_NoteBody.getText().toString();

        if(noteTitle.trim().isEmpty()||noteBody.trim().isEmpty()){
            Toast.makeText(this, "Please enter a title and body.", Toast.LENGTH_SHORT).show();
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NOTE_TITLE, noteTitle);
        data.putExtra(EXTRA_NOTE_BODY, noteBody);
        data.putExtra(EXTRA_COURSE_ID, fk_course_id);

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
        //IF we add more buttons in the future, below this is a version using a Switch statement.
        if (item.getItemId() == R.id.save) {
            saveNewNote();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
