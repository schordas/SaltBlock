package io.mjolnir.saltblock.models;

import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.mjolnir.saltblock.data.Note;
import io.mjolnir.saltblock.SaltBlock;

public class AddNoteViewModel extends ViewModel {

    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void editNote(String uId, String title, String text) {
        SaltBlock saltBlock = new SaltBlock();

        List<String> list = new ArrayList<>();
        list.add(title);
        list.add(text);

        List<String> encrypted = saltBlock.encrypt("myNoteAlias", list);
        DatabaseReference noteRef = mDatabase.child("users/").child(uId).push();
        noteRef.setValue(new Note(encrypted.get(0), encrypted.get(1)));
    }
}
