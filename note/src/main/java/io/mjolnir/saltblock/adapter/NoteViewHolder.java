package io.mjolnir.saltblock.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.mjolnir.saltblock.data.Note;
import io.mjolnir.saltblock.R;

public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    NoteClickListener mNoteClickListener;

    public NoteViewHolder(@NonNull View itemView, NoteClickListener noteClickListener) {
        super(itemView);
        this.mNoteClickListener = noteClickListener;
        itemView.setOnClickListener(this);
    }

    public void bindTo(Note note) {
        TextView title = itemView.findViewById(R.id.title);
        title.setText(note.title);
    }

    @Override
    public void onClick(View view) {
        mNoteClickListener.onClick(view, getAdapterPosition());
    }
}
