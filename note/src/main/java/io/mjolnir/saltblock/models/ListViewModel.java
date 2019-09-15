package io.mjolnir.saltblock.models;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.mjolnir.saltblock.SaltBlock;
import io.mjolnir.saltblock.data.Note;
import io.mjolnir.saltblock.data.NotesQueryLiveData;

public class ListViewModel extends ViewModel {
    private static final DatabaseReference USERS_REF = FirebaseDatabase.getInstance()
            .getReference("/users/");

    private String uId;

    private SaltBlock saltBlock = new SaltBlock();

    @NonNull
    public LiveData<DataSnapshot> getNotes(String uId) {
        this.uId = uId;
        return new NotesQueryLiveData(USERS_REF.child(uId));
    }

    public List<Note> parseSnapshot(DataSnapshot snapshot) {
        List<String> cipherTitles  = new ArrayList<>();
        List<String> cipherNotes = new ArrayList<>();
        for (DataSnapshot snap : snapshot.getChildren()) {
            if (!snap.getValue().toString().equals(uId)) {
                Note note = snap.getValue(Note.class);
                cipherTitles.add(note.title);
                cipherNotes.add(note.note);
            }
        }
        List<String> titles = saltBlock.decrypt("myNoteAlias", cipherTitles);
        List<String> notes = saltBlock.decrypt("myNoteAlias", cipherNotes);
        List<Note> noteObjs = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            Note note = new Note(titles.get(i), notes.get(i));
            noteObjs.add(note);
        }
        return noteObjs;
    }
}
