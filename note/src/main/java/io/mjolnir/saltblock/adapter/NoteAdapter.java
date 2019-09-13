package io.mjolnir.saltblock.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.mjolnir.saltblock.Note;
import io.mjolnir.saltblock.R;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<Note> mNotes;

    public NoteAdapter(List<Note> notes) {
        this.mNotes = notes;
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(view);
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

}
