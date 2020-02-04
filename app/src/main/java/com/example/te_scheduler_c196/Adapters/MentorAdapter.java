package com.example.te_scheduler_c196.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.Database.AppRepository;
import com.example.te_scheduler_c196.R;
import com.example.te_scheduler_c196.ViewModels.MentorViewModel;

import java.util.ArrayList;
import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorHolder> {
    private static final String TAG = AppRepository.class.getSimpleName();
    private MentorAdapter.onMentorClickListener listener;


    private List<Mentor> mentorList = new ArrayList<>();
    private List<Course> courseList = new ArrayList<>();

    class MentorHolder extends RecyclerView.ViewHolder{
        private TextView textViewMentorName;
        private TextView textViewMentorPhone;
        private TextView textViewMentorEmail;


        public MentorHolder(@NonNull View itemView) {
            super(itemView);
            textViewMentorName = itemView.findViewById(R.id.textView_mentor_name);
            textViewMentorPhone = itemView.findViewById(R.id.textView_mentor_phone);
            textViewMentorEmail = itemView.findViewById(R.id.textView_mentor_email);
        }
    }

    @NonNull
    @Override
    public MentorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentor_item, parent, false);
        return new MentorAdapter.MentorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorHolder holder, int position) {
        Mentor currentMentor = mentorList.get(position);
        holder.textViewMentorName.setText(currentMentor.getMentor_name());
        holder.textViewMentorEmail.setText(currentMentor.getMentor_email());
        holder.textViewMentorPhone.setText(currentMentor.getMentor_phone());

    }

    @Override
    public int getItemCount() {
        return mentorList.size();
    }

    public void setMentors(List<Mentor> mentors){
        this.mentorList = mentors;
        notifyDataSetChanged();
    }

    public interface onMentorClickListener{
        void onMentorClick(Mentor mentor);
    }

    public void setOnMentorClickListener(onMentorClickListener listener){
        this.listener = listener;
    }


}
