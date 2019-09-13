package io.mjolnir.saltblock;


import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class Encoder {

    static String encodeToString(byte[] toEncode) {
        return Base64.encodeToString(toEncode, Base64.DEFAULT);
    }

    static String decode(byte[] toDecode) {
        byte[] bytes = Base64.decode(toDecode, Base64.DEFAULT);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    static byte[] decode(String toDecode) {
        return Base64.decode(toDecode, Base64.DEFAULT);
    }
}
