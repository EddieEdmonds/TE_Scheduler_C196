package com.example.te_scheduler_c196.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_scheduler_c196.DB_Entities.Term;
import com.example.te_scheduler_c196.R;

import java.util.ArrayList;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> {

    private List<Term> termList = new ArrayList<>();

    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_item, parent, false);
        return new TermHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {
        Term currentTerm = termList.get(position);
        holder.textViewTermTitle.setText(currentTerm.getTerm_title());

    }

    @Override
    public int getItemCount() {
        return termList.size();
    }

    public void setTerms(List<Term> terms){
        this.termList = terms;
        notifyDataSetChanged();
    }

    class TermHolder extends RecyclerView.ViewHolder{
        private TextView textViewTermTitle;


        public TermHolder(@NonNull View itemView) {
            super(itemView);
            textViewTermTitle = itemView.findViewById(R.id.textView_term_title);
        }
    }
}
