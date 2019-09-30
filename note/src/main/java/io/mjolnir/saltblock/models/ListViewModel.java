package io.mjolnir.saltblock.models;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.mjolnir.saltblock.EncryptionAlgorithm;
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
        List<String> ids = new ArrayList<>();
        List<String> cipherTitles = new ArrayList<>();
        List<String> cipherNotes = new ArrayList<>();
        List<String> algs = new ArrayList<>();
        for (DataSnapshot snap : snapshot.getChildren()) {
            if (!snap.getValue().toString().equals(uId)) {
                Note note = snap.getValue(Note.class);
                ids.add(note.id);
                cipherTitles.add(note.title);
                cipherNotes.add(note.note);
                algs.add(note.alg);
            }
        }

       List<Note> notes = new ArrayList<>();

        for (int i = 0; i < algs.size(); i++) {
            String plainTitle;
            String plainNote;
            if (algs.get(i).equals(EncryptionAlgorithm.AES.name())) {
                plainTitle = saltBlock.decryptAES("myAESAlias", cipherTitles.get(i));
                plainNote = saltBlock.decryptAES("myAESAlias", cipherNotes.get(i));
            } else {
                plainTitle = saltBlock.decryptRSA("myRSAAlias", cipherTitles.get(i));
                plainNote = saltBlock.decryptRSA("myRSAAlias", cipherNotes.get(i));
            }

            notes.add(new Note(ids.get(i), plainTitle, plainNote, algs.get(i)));
        }

        return notes;
    }
}
