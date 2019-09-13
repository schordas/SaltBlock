package io.mjolnir.saltblock;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class AES extends KeyProvider {

    private static final String LOG_TAG = AES.class.getSimpleName();

    static List<String> encrypt(String alias, List<String> plainTexts) throws
            NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException {

        SecretKey key = getKey(alias);
        List<String> cipherTexts = new ArrayList<>();

        Cipher cipher = Cipher.getInstance(Constants.AES);

        for (String plainText : plainTexts) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] iv = cipher.getIV();
            byte [] encryptedBytes = cipher.doFinal(plainText.getBytes());
            String encryptedString = Encoder.encodeToString(iv) + Constants.IV_SEPARATOR;

            // clean array for security
            Arrays.fill(iv, (byte) 0);

            encryptedString += Encoder.encodeToString(encryptedBytes);
            cipherTexts.add(encryptedString);
        }

        return cipherTexts;
    }

    static List<String> decrypt(String keyAlias, List<String> cipherTexts) throws
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        SecretKey key = getKey(keyAlias);
        if (key == null) {
            throw new NullPointerException("Secret key is null");
        }

        List<String> plainTexts = new ArrayList<>();

        Cipher cipher = Cipher.getInstance(Constants.AES);

        for (String cipherText : cipherTexts) {
            String[] split = parseCipherText(cipherText);
            String iv = split[0];
            byte[] ivBytes = Encoder.decode(iv);

            // clean iv string
            iv = null;
            GCMParameterSpec spec = new GCMParameterSpec(128, ivBytes);

            // clean ivByteArray
            Arrays.fill(ivBytes, (byte) 0);

            cipherText = split[1];

            byte[] cipherBytes = Encoder.decode(cipherText);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] decryptedBytes = cipher.doFinal(cipherBytes);

            plainTexts.add(new String(decryptedBytes, StandardCharsets.UTF_8));
        }

        return plainTexts;

    }

    private static String[] parseCipherText(String cipherText) {

        String[] split = cipherText.split(Constants.IV_SEPARATOR, 2);
        if (split.length != 2) {
            throw new IllegalArgumentException("Cipher text is incorrect, could not parse cipher for decryption");
        }
        return split;
    }
}
