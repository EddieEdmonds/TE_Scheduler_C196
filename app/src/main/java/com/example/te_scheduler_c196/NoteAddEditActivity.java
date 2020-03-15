package com.example.te_scheduler_c196;

import android.app.Notification;
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

public class NoteAddEditActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    //    public static final String EXTRA_NOTE_ID ="com.example.te_scheduler_c196.EXTRA_NOTE_ID";
//    public static final String EXTRA_FK_COURSE_ID ="com.example.te_scheduler_c196.EXTRA_FK_COURSE_ID";
    public static final String EXTRA_NOTE_TITLE = "com.example.te_scheduler_c196.EXTRA_NOTE_TITLE";
    public static final String EXTRA_NOTE_BODY = "com.example.te_scheduler_c196.EXTRA_NOTE_BODY";
    public static final String EXTRA_NOTE_ID = "com.example.te_scheduler_c196.EXTRA_NOTE_ID";

    private EditText et_NoteTitle, et_NoteBody;
    private int fk_course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        final Intent noteData = getIntent();

        et_NoteTitle = findViewById(R.id.et_note_title);
        et_NoteBody = findViewById(R.id.et_note_body);
        fk_course_id = noteData.getIntExtra(EXTRA_COURSE_ID, -1);
        Log.i(TAG, "fk_course_id: " + fk_course_id);


        if (noteData.hasExtra(EXTRA_NOTE_ID)) {
            setTitle("Edit Note");
            et_NoteBody.setText(noteData.getStringExtra(EXTRA_NOTE_BODY));
            et_NoteTitle.setText(noteData.getStringExtra(EXTRA_NOTE_TITLE));
        } else {
            setTitle("Add Note");
        }


    }

    private void saveNewNote() {
        String noteTitle = et_NoteTitle.getText().toString();
        String noteBody = et_NoteBody.getText().toString();

        if (noteTitle.trim().isEmpty() || noteBody.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a title and body.", Toast.LENGTH_SHORT).show();
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NOTE_TITLE, noteTitle);
        data.putExtra(EXTRA_NOTE_BODY, noteBody);
        data.putExtra(EXTRA_COURSE_ID, fk_course_id);

        int noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);
        if (noteId != -1) {
            data.putExtra(EXTRA_NOTE_ID, noteId);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private void shareNote(){
        Intent intent = new Intent(Intent.ACTION_SEND);

        String body = et_NoteBody.getText().toString();
        String subject = et_NoteTitle.getText().toString();

        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.setType("text/plain");

        String shareNote = "Share Note";

        Intent chooser = Intent.createChooser(intent, shareNote);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(chooser);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //using IF statement here because we only have one button in our "menu". The save button.
        //IF we add more buttons in the future, below this is a version using a Switch statement.
        switch (item.getItemId()) {
            case R.id.save:
                saveNewNote();
                return true;
            case R.id.share:
                shareNote();
            default:
                return super.onOptionsItemSelected(item);

        }
//        if (item.getItemId() == R.id.save) {
//            saveNewNote();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);

        }
    }
