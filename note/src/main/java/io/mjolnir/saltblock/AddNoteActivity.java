package io.mjolnir.saltblock;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;

public class AddNoteActivity extends AppCompatActivity {

    private TextInputEditText note;
    private TextInputEditText title;
    private AddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.title_input);
        note = findViewById(R.id.note_input);

        viewModel = ViewModelProviders.of(this).get(AddNoteViewModel.class);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uId = auth.getCurrentUser().getUid();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleText = title.getText().toString();
                String noteText = note.getText().toString();
                viewModel.editNote(uId, titleText, noteText);
            }
        });
    }

}
