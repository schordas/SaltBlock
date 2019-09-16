package io.mjolnir.saltblock.data;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Note {

    public String title;
    public String note;

    public Note() {
        // Default constructor for Firebase snapshot
    }

    public Note(String title, String note) {
        this.title = title;
        this.note = note;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
       if (this == obj){
           return true;
       }

       Note note = (Note) obj;
       return note.title.equals(this.title);
    }
}
