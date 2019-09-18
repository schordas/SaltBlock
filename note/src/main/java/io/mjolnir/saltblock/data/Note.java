package io.mjolnir.saltblock.data;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Note implements Serializable  {

    public String id;
    public String title;
    public String note;
    public String alg;

    public Note() {
        // Default constructor for Firebase snapshot
    }

    public Note(String id, String title, String note, String alg) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.alg = alg;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
       if (this == obj){
           return true;
       }

       Note note = (Note) obj;
       return note.id.equals(this.id);
    }
}
