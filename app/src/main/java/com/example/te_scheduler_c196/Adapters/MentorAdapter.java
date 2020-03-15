package com.example.te_scheduler_c196.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_scheduler_c196.DB_Entities.Mentor;
import com.example.te_scheduler_c196.R;

import java.util.ArrayList;
import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorHolder> {
    private static final String TAG = MentorAdapter.class.getSimpleName();

    private OnMentorClickListener listener;

    private List<Mentor> mentorList = new ArrayList<>();


    class MentorHolder extends RecyclerView.ViewHolder{
        private TextView tvMentorName, tvMentorPhone, tvMentorEmail;

        MentorHolder(@NonNull View itemView) {
            super(itemView);
            tvMentorName = itemView.findViewById(R.id.tv_mentor_name);
            tvMentorPhone = itemView.findViewById(R.id.tv_mentor_phone);
            tvMentorEmail = itemView.findViewById(R.id.tv_mentor_email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null&&position!=RecyclerView.NO_POSITION){
                        listener.onMentorClick(mentorList.get(position));
                    }else{
                        Log.i(TAG, "hmmmmm");
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MentorAdapter.MentorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentor_item, parent, false);
        return new MentorAdapter.MentorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorAdapter.MentorHolder holder, int position) {
        Mentor currentMentor = mentorList.get(position);

        holder.tvMentorName.setText(currentMentor.getMentor_name());
        holder.tvMentorPhone.setText(currentMentor.getMentor_phone());
        holder.tvMentorEmail.setText(currentMentor.getMentor_email());
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

    public interface OnMentorClickListener{
        void onMentorClick (Mentor mentor);
    }


    public void setOnMentorClickListener(OnMentorClickListener listener){
        this.listener = listener;
    }


}
