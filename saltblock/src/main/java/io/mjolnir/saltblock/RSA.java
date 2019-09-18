package io.mjolnir.saltblock;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

class RSA extends RSAKeyProvider {

    static PublicKey getPublicKey(String keyAlias) {
        KeyPair keyPair = getRsaKey(keyAlias);
        return keyPair != null ? keyPair.getPublic() : null;
    }

    private static PrivateKey getPrivateKey(String keyAlias) {
        KeyPair keyPair = getRsaKey(keyAlias);
        return keyPair != null ? keyPair.getPrivate() : null;
    }

    static String encrypt(String keyAlias, String plainText) {
        List<String> list = new ArrayList<>();
        list.add(plainText);

        try {
            return encrypt(keyAlias, list).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<String> encrypt(String keyAlias, List<String> plainTexts) throws
            NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PublicKey key = getPublicKey(keyAlias);

        Cipher cipher = Cipher.getInstance(Constants.RSA, Constants.KEY_STORE_WORK_AROUND);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        List<String> cipherTexts = new ArrayList<>();

        for (String plainText : plainTexts) {
            byte[] cipherBytes = cipher.doFinal(plainText.getBytes());

            String cipherStr = Encoder.encodeToString(cipherBytes);

            cipherTexts.add(cipherStr);
        }

        return cipherTexts;
    }

    static List<String> decrypt(String keyAlias, List<String> cipherTexts) throws
            NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PrivateKey key = getPrivateKey(keyAlias);

        Cipher cipher = Cipher.getInstance(Constants.RSA, Constants.KEY_STORE_WORK_AROUND);
        cipher.init(Cipher.DECRYPT_MODE, key);

        List<String> plainTexts = new ArrayList<>();

        for (String cipherText : cipherTexts) {
            byte[] cipherBytes = Encoder.decode(cipherText);

            byte[] plainTextBytes = cipher.doFinal(cipherBytes);
            String plainText = Encoder.decodeToString(plainTextBytes);
            plainTexts.add(plainText);
        }

        return plainTexts;
    }

    static String decrypt(String keyAlias, String cipherText) {
        List<String> list = new ArrayList<>();
        list.add(cipherText);

        try {
            return decrypt(keyAlias, list).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
