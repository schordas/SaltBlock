package io.mjolnir.saltblock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import io.mjolnir.saltblock.R;
import io.mjolnir.saltblock.SaltBlock;
import io.mjolnir.saltblock.data.Note;
import io.mjolnir.saltblock.models.AddNoteViewModel;

public class EditNoteActivity extends AppCompatActivity {

    private TextInputEditText mNote;
    private TextInputEditText mTitle;
    private MaterialButton mAESBtn;
    private MaterialButton mRSABtn;

    private AddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = findViewById(R.id.title_input);
        mNote = findViewById(R.id.note_input);
        mAESBtn = findViewById(R.id.AES_button);
        mRSABtn = findViewById(R.id.RSA_button);

        viewModel = ViewModelProviders.of(this).get(AddNoteViewModel.class);

        initBtns();
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
                finish();
            }
        });
    }

    private void initBtns() {
       mAESBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               toggleAES();
               viewModel.toggleAlg(true);
           }
       });

       mRSABtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               toggleRSA();
               viewModel.toggleAlg(false);
           }
       });
    }

    private void unBundle() {
        Intent intent = getIntent();
        Note note = (Note) intent.getSerializableExtra("note");
        if (note != null) {
            viewModel.setId(note.id);
            boolean isAes = viewModel.setAlg(note.alg);
            if (isAes) {
                toggleAES();
            } else {
                toggleRSA();
            }
            mTitle.setText(note.title);
            mNote.setText(note.note);
        }
    }

    private void toggleAES() {
        mAESBtn.setEnabled(false);
        mAESBtn.setBackgroundColor(getColor(R.color.common_google_signin_btn_text_dark_disabled));

        mRSABtn.setEnabled(true);
        mRSABtn.setBackgroundColor(getColor(R.color.colorAccent));
    }

    private void toggleRSA() {
        mRSABtn.setEnabled(false);
        mRSABtn.setBackgroundColor(getColor(R.color.common_google_signin_btn_text_dark_disabled));

        mAESBtn.setEnabled(true);
        mAESBtn.setBackgroundColor(getColor(R.color.colorAccent));
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.setId(null);
    }
}
