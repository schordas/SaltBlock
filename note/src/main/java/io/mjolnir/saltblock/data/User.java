package io.mjolnir.saltblock.data;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String id;

    public User(String id) {
        this.id = id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uId", id);
        return map;
    }
}
