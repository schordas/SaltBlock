package io.mjolnir.saltblock;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
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

    static SecretKey getKeyToWrap() {
        return getAesKeyToWrap();
    }

    static String encrypt(String alias, byte[] plainBytes) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        SecretKey key = getAesKey(alias);

        Cipher cipher = Cipher.getInstance(Constants.AES);

        return doEncrypt(cipher, key, plainBytes);
    }

    static byte[] encrypt(SecretKey key, byte[] plainBytes) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(Constants.AES);

        byte[] iv = getIV();
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        byte[] cipherBytes = cipher.doFinal(plainBytes);

        key = null;

        return processCipher(cipherBytes, iv);
    }

   private static byte[] processCipher(byte[] cipher, byte[] iv) {
       ByteBuffer buffer = ByteBuffer.allocate(4 + iv.length + cipher.length);
       buffer.putInt(iv.length);
       buffer.put(iv);

       Arrays.fill(iv, (byte) 0);

       buffer.put(cipher);
       cipher = buffer.array();
       cipher = Encoder.encode(cipher);

       return cipher;
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

    static byte[] decrypt(Key key, String cipherText) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException {
        byte[] cipher = Encoder.decode(cipherText);
        ByteBuffer buffer =ByteBuffer.wrap(cipher);
        int ivLength = buffer.getInt();
        if (ivLength < 12 || ivLength > 16) {
            throw new IllegalArgumentException("Invalid iv length");
        }
        byte[] iv = new byte[ivLength];
        buffer.get(iv);
        byte[] cipherBytes = new byte[buffer.remaining()];
        buffer.get(cipherBytes);

        Cipher decryptionCipher = Cipher.getInstance(Constants.AES);

        GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        Arrays.fill(iv, (byte) 0);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);

        key = null;

        return decryptionCipher.doFinal(cipherBytes);
    }

    static byte[] decrypt(String alias, String cipherText) throws NoSuchPaddingException,
            NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance(Constants.AES);

        SecretKey key = getAesKey(alias);

        try {
            return doDecrypt(cipher, key, cipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> decrypt(String keyAlias, List<String> cipherTexts) throws
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        SecretKey key = getAesKey(keyAlias);

        List<String> plainTexts = new ArrayList<>();

        Cipher cipher = Cipher.getInstance(Constants.AES);

        for (String cipherText : cipherTexts) {
            byte[] plainTextBytes = doDecrypt(cipher, key, cipherText);
            String plainText = Encoder.decodeToString(plainTextBytes);
            plainTexts.add(plainText);
        }

        return plainTexts;
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

        return cipher.doFinal(cipherBytes);
    }

    private static String[] parseCipherText(String cipherText) {

        String[] split = cipherText.split(Constants.IV_SEPARATOR, 2);
        if (split.length != 2) {
            throw new IllegalArgumentException(
                    "Cipher text is incorrect, could not parse cipher for decryption");
        }
        return split;
    }
}
