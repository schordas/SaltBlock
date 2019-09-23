package io.mjolnir.saltblock;

import java.security.InvalidKeyException;
import java.security.Key;
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

    static List<String> encrypt(String keyAlias, List<String> plainTexts) throws
            NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PublicKey key = getPublicKey(keyAlias);

        Cipher cipher = Cipher.getInstance(Constants.RSA, Constants.KEY_STORE_WORK_AROUND);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        List<String> cipherTexts = new ArrayList<>();

        for (String plainText : plainTexts) {
            // Check that plainText is not too large
            ExtensionsKt.isOver(plainText.getBytes());
            String cipherStr = doEncrypt(cipher, plainText.getBytes());

            cipherTexts.add(cipherStr);
        }

        return cipherTexts;
    }

    static String doEncrypt(Cipher cipher, byte[] plainBytes) throws
            BadPaddingException, IllegalBlockSizeException {
        byte[] cipherBytes = cipher.doFinal(plainBytes);
        return Encoder.encodeToString(cipherBytes);
    }

    static List<String> decrypt(String keyAlias, List<String> cipherTexts) throws
            NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PrivateKey key = getPrivateKey(keyAlias);

        Cipher cipher = Cipher.getInstance(Constants.RSA, Constants.KEY_STORE_WORK_AROUND);
        cipher.init(Cipher.DECRYPT_MODE, key);

        List<String> plainTexts = new ArrayList<>();

        for (String cipherText : cipherTexts) {
            byte[] plainTextBytes = doDecrypt(cipher, cipherText);

            String plainText = Encoder.decodeToString(plainTextBytes);
            plainTexts.add(plainText);
        }

        return plainTexts;
    }

    static byte[] doDecrypt(Cipher cipher, String cipherText) throws BadPaddingException,
            IllegalBlockSizeException {
        byte[] cipherBytes = Encoder.decode(cipherText);
        return cipher.doFinal(cipherBytes);
    }

    static byte[] doFinal(int mode, String alias, byte[] bytes) throws NoSuchPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        Key key;
        if (mode == Cipher.ENCRYPT_MODE) {
           key = getPublicKey(alias);
        } else if(mode == Cipher.DECRYPT_MODE) {
           key = getPrivateKey(alias);
        } else {
            throw new IllegalArgumentException("Cipher mode must be specified to encrypt or decrypt");
        }

        Cipher cipher = Cipher.getInstance(Constants.RSA, Constants.KEY_STORE_WORK_AROUND);
        cipher.init(mode, key);

        return cipher.doFinal(bytes);
    }
}
