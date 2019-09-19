package io.mjolnir.saltblock;

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

class AES extends AESKeyProvider {

    static String encrypt(String alias, String plainText) {
        List<String> list = new ArrayList<>();
        list.add(plainText);
        try {
            return encrypt(alias, list).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> encrypt(String alias, List<String> plainTexts) throws
            NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException {

        SecretKey key = getAesKey(alias);
        List<String> cipherTexts = new ArrayList<>();

        Cipher cipher = Cipher.getInstance(Constants.AES);

        for (String plainText : plainTexts) {
            String encryptedString = doEncrypt(cipher, key, plainText.getBytes());
            cipherTexts.add(encryptedString);
        }

        return cipherTexts;
    }

    static String encrypt(String alias, byte[] plainBytes) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        SecretKey key = getAesKey(alias);

        Cipher cipher = Cipher.getInstance(Constants.AES);

        return doEncrypt(cipher, key, plainBytes);
    }

    static String doEncrypt(Cipher cipher, SecretKey key, byte[] plainBytes) throws
            BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        byte[] encryptedBytes = cipher.doFinal(plainBytes);
        String encryptedString = Encoder.encodeToString(iv) + Constants.IV_SEPARATOR;

        // fill iv for security
        Arrays.fill(iv, (byte) 0);

        encryptedString += Encoder.encodeToString(encryptedBytes);

        return encryptedString;
    }

    static String decrypt(String alias, String cipherText) {
        List<String> list = new ArrayList<>();
        list.add(cipherText);
        try {
            return decrypt(alias, list).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> decrypt(String keyAlias, List<String> cipherTexts) throws
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        SecretKey key = getAesKey(keyAlias);
        if (key == null) {
            throw new NullPointerException("Secret key is null");
        }

        List<String> plainTexts = new ArrayList<>();

        Cipher cipher = Cipher.getInstance(Constants.AES);

        for (String cipherText : cipherTexts) {
            byte[] plainTextBytes = doDecrypt(cipher, key, cipherText);
            String plainText = Encoder.decodeToString(plainTextBytes);
            plainTexts.add(plainText);
        }

        return plainTexts;
    }

    static byte[] decryptToObj(String alias, String cipherText) throws NoSuchPaddingException,
            NoSuchAlgorithmException {
        SecretKey key = getAesKey(alias);
        Cipher cipher = Cipher.getInstance(Constants.AES);
        try {
            return doDecrypt(cipher, key, cipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] doDecrypt(Cipher cipher, SecretKey key, String cipherText) throws
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {

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

        return decryptedBytes;
    }

    private static String[] parseCipherText(String cipherText) {

        String[] split = cipherText.split(Constants.IV_SEPARATOR, 2);
        if (split.length != 2) {
            throw new IllegalArgumentException("Cipher text is incorrect, could not parse cipher for decryption");
        }
        return split;
    }
}
