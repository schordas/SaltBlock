package io.mjolnir.saltblock.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.mjolnir.saltblock.data.Note;
import io.mjolnir.saltblock.R;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<Note> mNotes;

    private NoteClickListener mNoteClickListener;

    public NoteAdapter(NoteClickListener noteClickListener) {
        mNotes = new ArrayList<>();
        this.mNoteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(view, mNoteClickListener);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.bindTo(note);
    }

    public Note getNote(int position) {
        return mNotes.get(position);
    }

    public void updateList(List<Note> notes) {
        this.mNotes = notes;
        notifyDataSetChanged();
    }

}
