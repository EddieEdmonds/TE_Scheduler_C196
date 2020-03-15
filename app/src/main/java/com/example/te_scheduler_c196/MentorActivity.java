package com.example.te_scheduler_c196;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_scheduler_c196.Adapters.MentorAdapter;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.ViewModels.MentorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MentorActivity extends AppCompatActivity{
    private static final String TAG = MentorActivity.class.getSimpleName();

    public static final int ADD_MENTOR_REQUEST = 8;
    public static final int EDIT_MENTOR_REQUEST = 9;

    private MentorViewModel mentorViewModel;

    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);

        RecyclerView mentorRecyclerView = findViewById(R.id.mentor_recycler_view);
        mentorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mentorRecyclerView.setHasFixedSize(true);

        final MentorAdapter mentorAdapter = new MentorAdapter();
        mentorRecyclerView.setAdapter(mentorAdapter);

        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mentorViewModel.getAllMentors().observe(this, mentorAdapter::setMentorList);

        FloatingActionButton btnAddTerm = findViewById(R.id.btn_add_mentor);
        btnAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            //New term onClick method
            public void onClick(View v) {
                Intent intent = new Intent(MentorActivity.this, MentorAddEditActivity.class);
                startActivityForResult(intent, ADD_MENTOR_REQUEST);

            }
        });

        //handles swiping mentor to delete.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final Mentor swipedMentor = mentorAdapter.getMentorAt(viewHolder.getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage("Delete this mentor?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // Your action
                        mentorViewModel.deleteMentor(swipedMentor);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        mentorAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    }
                });
                alertDialog = builder.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        }).attachToRecyclerView(mentorRecyclerView);

        mentorAdapter.setOnMentorClickListener(new MentorAdapter.OnMentorClickListener(){
            @Override
            public void onMentorClick(Mentor mentor) {
                Log.i(TAG, "do we get here?");
                Intent intent = new Intent(MentorActivity.this, MentorAddEditActivity.class);

                intent.putExtra(MentorAddEditActivity.EXTRA_MENTOR_ID, mentor.getMentor_id());
                intent.putExtra(MentorAddEditActivity.EXTRA_MENTOR_NAME, mentor.getMentor_name());
                intent.putExtra(MentorAddEditActivity.EXTRA_MENTOR_PHONE, mentor.getMentor_phone());
                intent.putExtra(MentorAddEditActivity.EXTRA_MENTOR_EMAIL, mentor.getMentor_email());

                startActivityForResult(intent, EDIT_MENTOR_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            assert data != null;
            String mentorName = data.getStringExtra(MentorAddEditActivity.EXTRA_MENTOR_NAME);
            String mentorPhone = data.getStringExtra(MentorAddEditActivity.EXTRA_MENTOR_PHONE);
            String mentorEmail = data.getStringExtra(MentorAddEditActivity.EXTRA_MENTOR_EMAIL);

            Mentor mentor;
            if (requestCode == ADD_MENTOR_REQUEST && resultCode == RESULT_OK) {
                mentor = new Mentor(mentorName, mentorPhone, mentorEmail);
                mentorViewModel.insertMentor(mentor);
            } else if (requestCode == EDIT_MENTOR_REQUEST && resultCode == RESULT_OK) {
                int mentorId = data.getIntExtra(MentorAddEditActivity.EXTRA_MENTOR_ID, -1);
                mentor = new Mentor(mentorName, mentorPhone, mentorEmail);
                mentor.setMentor_id(mentorId);
                mentorViewModel.updateMentor(mentor);

            }else {
                Toast.makeText(this, "Mentor not saved!", Toast.LENGTH_SHORT).show();
            }
        }
    }




}
