package io.mjolnir.saltblock.models;

import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.mjolnir.saltblock.EncryptionAlgorithm;
import io.mjolnir.saltblock.SaltBlockK;
import io.mjolnir.saltblock.data.Note;

public class AddNoteViewModel extends ViewModel {

    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private static String mId;

    private static boolean mIsAES = true;

    private static String mAlg = EncryptionAlgorithm.AES.name();

    public void editNote(String uId, String title, String text) {
        SaltBlockK saltBlockAES = new SaltBlockK();
        SaltBlockK saltBlockRSA = new SaltBlockK(EncryptionAlgorithm.RSA);

        List<String> list = new ArrayList<>();
        list.add(title);
        list.add(text);

        List<String> encrypted;

        if (mIsAES) {
            encrypted = saltBlockAES.encrypt("myAESAlias", list);
        } else {
            encrypted = saltBlockRSA.encrypt("myRSAAlias", list);
        }

        DatabaseReference noteRef = mDatabase.child("users/").child(uId);
        String id;
        DatabaseReference newNote;

        if (mId == null) {
            newNote = noteRef.push();
            id = newNote.getKey();
            setId(id);
        } else {
            newNote = noteRef.child(mId);
            id = mId;
        }

        newNote.setValue(new Note(id, encrypted.get(0), encrypted.get(1), mAlg));
    }

    public void setId(String id) {
        mId = id;
    }

    public void toggleAlg(boolean isAES) {
        mIsAES = isAES;
        if (mIsAES) {
            mAlg = EncryptionAlgorithm.AES.name();
        } else {
            mAlg = EncryptionAlgorithm.RSA.name();
        }
    }
}
