package io.mjolnir.saltblock;


import android.util.Base64;

import java.nio.charset.StandardCharsets;

class Encoder {

    static String encodeToString(byte[] toEncode) {
        return Base64.encodeToString(toEncode, Base64.DEFAULT);
    }

    static String decodeToString(byte[] toDecode) {
        return new String(toDecode, StandardCharsets.UTF_8);
    }
    static byte[] decode(String toDecode) {
        return Base64.decode(toDecode, Base64.DEFAULT);
    }
}
