package com.example.te_scheduler_c196.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_scheduler_c196.DB_Entities.Assessment;
import com.example.te_scheduler_c196.Database.AppRepository;
import com.example.te_scheduler_c196.R;
import com.example.te_scheduler_c196.ViewModels.AssessmentViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentHolder>{
    private static final String TAG = AppRepository.class.getSimpleName();

    private List<Assessment> assessmentList = new ArrayList<>();

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentAdapter.AssessmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentHolder holder, int position) {
        Assessment currentAssessment = assessmentList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String assessmentDue = sdf.format(currentAssessment.getAss_due_date());

        holder.textViewAssessmentTitle.setText(currentAssessment.getAss_title());
        holder.textViewAssessmentDueDate.setText(assessmentDue);
        holder.textViewAssessmentType.setText(currentAssessment.getAss_type());
        //holder.textViewAssessmentCourseName.setText(currentCourseName);
    }

    @Override
    public int getItemCount() {
        return assessmentList.size();
    }

    public void setAssessmentList(List<Assessment> assessments){
        this.assessmentList = assessments;
        notifyDataSetChanged();
    }


    class AssessmentHolder extends RecyclerView.ViewHolder{
        private TextView textViewAssessmentTitle;
        private TextView textViewAssessmentCourseName;
        private TextView textViewAssessmentDueDate;
        private TextView textViewAssessmentType;


        public AssessmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewAssessmentTitle = itemView.findViewById(R.id.textView_ass_title);
            //textViewAssessmentCourseName = itemView.findViewById(R.id.textView_ass_course_title);
            textViewAssessmentDueDate = itemView.findViewById(R.id.textView_ass_due_date);
            textViewAssessmentType = itemView.findViewById(R.id.textView_ass_type);

        }
    }
}
