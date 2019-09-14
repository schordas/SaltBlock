package io.mjolnir.saltblock;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NotesQueryLiveData extends LiveData<DataSnapshot> {

    private static final String LOG_TAG = NotesQueryLiveData.class.getSimpleName();

    private final Query mQuery;
    private final NoteValueEventListener mEventListener = new NoteValueEventListener();

    public NotesQueryLiveData(Query query) {
        this.mQuery = query;
    }

    public NotesQueryLiveData(DatabaseReference ref) {
        this.mQuery = ref;
    }

    @Override
    protected void onActive() {
       mQuery.addValueEventListener(mEventListener);
    }

    @Override
    protected void onInactive() {
       mQuery.removeEventListener(mEventListener);
    }

    private class NoteValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(LOG_TAG, "Error listening to query: " + mQuery, databaseError.toException());
        }
    }
}
