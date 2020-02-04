package com.example.te_scheduler_c196.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_scheduler_c196.DB_Entities.Course;
import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

    private List<Course> courseList = new ArrayList<>();
    private onCourseClickListener listener;

    class CourseHolder extends RecyclerView.ViewHolder{
        private TextView textViewCourseTitle;
        private TextView textViewCourseTermId;
        private TextView textViewCourseStartDate;
        private TextView textViewCourseEndDate;


        CourseHolder(@NonNull View itemView) {
            super(itemView);
            textViewCourseTitle = itemView.findViewById(R.id.textView_course_title);
            textViewCourseTermId = itemView.findViewById(R.id.textView_course_term_id);
            textViewCourseStartDate = itemView.findViewById(R.id.textView_course_start_date);
            textViewCourseEndDate = itemView.findViewById(R.id.textView_course_end_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null&&position!=RecyclerView.NO_POSITION){
                        listener.onCourseClick(courseList.get(position));
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, int position) {
        Course currentCourse = courseList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String courseStartDate = sdf.format(currentCourse.getCourse_start());
        String courseEndDate = sdf.format(currentCourse.getCourse_end());
        holder.textViewCourseTitle.setText(currentCourse.getCourse_title());
        holder.textViewCourseTermId.setText(String.valueOf(currentCourse.getFk_term_id()));
        holder.textViewCourseStartDate.setText(courseStartDate);
        holder.textViewCourseEndDate.setText(courseEndDate);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void setCourseList(List<Course> courses){
        this.courseList = courses;
        notifyDataSetChanged();
    }



    public Course getCourseAt(int position){
        return courseList.get(position);
    }

    public interface onCourseClickListener{
        void onCourseClick(Course course);
    }

    public void setOnCourseClickListener(onCourseClickListener listener){
        this.listener = listener;
    }


}
