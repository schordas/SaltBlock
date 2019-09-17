package io.mjolnir.saltblock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import io.mjolnir.saltblock.R;
import io.mjolnir.saltblock.data.Note;
import io.mjolnir.saltblock.models.AddNoteViewModel;

public class EditNoteActivity extends AppCompatActivity {

    private TextInputEditText mNote;
    private TextInputEditText mTitle;
    private AddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = findViewById(R.id.title_input);
        mNote = findViewById(R.id.note_input);

        viewModel = ViewModelProviders.of(this).get(AddNoteViewModel.class);

        unBundle();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uId = auth.getCurrentUser().getUid();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleText = mTitle.getText().toString();
                String noteText = mNote.getText().toString();
                viewModel.editNote(uId, titleText, noteText);
            }
        });
    }

    private void unBundle() {
        Intent intent = getIntent();
        Note note = (Note) intent.getSerializableExtra("note");
        if (note != null) {
            viewModel.setId(note.id);
            mTitle.setText(note.title);
            mNote.setText(note.note);
        }
    }
}
