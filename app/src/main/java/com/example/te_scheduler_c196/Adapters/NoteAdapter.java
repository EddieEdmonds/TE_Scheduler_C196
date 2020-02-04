package com.example.te_scheduler_c196.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_scheduler_c196.DB_Entities.Note;
import com.example.te_scheduler_c196.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> noteList = new ArrayList<>();
    private onNoteClickListener listener;

    class NoteHolder extends RecyclerView.ViewHolder{
        private TextView tvNoteTitle, tvNoteDescription;

        NoteHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tv_note_title);
            tvNoteDescription = itemView.findViewById(R.id.tv_note_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null&&position!=RecyclerView.NO_POSITION){
                        listener.onNoteClick(noteList.get(position));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = noteList.get(position);
        holder.tvNoteTitle.setText(currentNote.getTitle());
        holder.tvNoteDescription.setText(currentNote.getTitle());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setNotes(List<Note> notes){
        this.noteList = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position){
        return noteList.get(position);
    }



    public interface onNoteClickListener{
        void onNoteClick(Note note);
    }

    public void setOnNoteClickListener(onNoteClickListener listener){
        this.listener = listener;
    }
}
