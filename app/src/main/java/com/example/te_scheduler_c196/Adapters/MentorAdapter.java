package com.example.te_scheduler_c196.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.Database.AppRepository;
import com.example.te_scheduler_c196.R;

import java.util.ArrayList;
import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorHolder> {
    private static final String TAG = AppRepository.class.getSimpleName();

    private OnMentorClickListener listener;


    private List<Mentor> mentorList = new ArrayList<>();


    class MentorHolder extends RecyclerView.ViewHolder{
        private TextView textViewMentorName;
        private TextView textViewMentorPhone;
        private TextView textViewMentorEmail;


        MentorHolder(@NonNull View itemView) {
            super(itemView);
            textViewMentorName = itemView.findViewById(R.id.textView_mentor_name);
            textViewMentorPhone = itemView.findViewById(R.id.textView_mentor_phone);
            textViewMentorEmail = itemView.findViewById(R.id.textView_mentor_email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null&&position!=RecyclerView.NO_POSITION){
                        listener.onMentorClick(mentorList.get(position));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MentorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentor_item, parent, false);
        return new MentorHolder(itemView);
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

    public void setMentorList(List<Mentor> mentors){
        this.mentorList = mentors;
        notifyDataSetChanged();
    }

    public Mentor getMentorAt(int position){return mentorList.get(position);}

    public interface OnMentorClickListener {
        void onMentorClick(Mentor mentor);
    }

    public void setOnMentorClickListener(OnMentorClickListener listener){
        this.listener = listener;
    }


}
