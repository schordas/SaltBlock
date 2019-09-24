package io.mjolnir.saltblock;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

class AESKeyProvider extends KeyStoreProvider {

    private static final int KEY_SIZE = 256;

    private static final String LOG_TAG = AESKeyProvider.class.getSimpleName();

    /**
     * Generates a key that is then stored in the Android KeyStore
     * @param keyAlias will set the alias for this key for future reference
     **/
    @TargetApi(Build.VERSION_CODES.M)
    private static void generateAesKey(String keyAlias) {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    Constants.KEY_STORE);
            KeyGenParameterSpec.Builder keyBuilder = new KeyGenParameterSpec.Builder(keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                keyBuilder.setUserAuthenticationValidWhileOnBody(true);
            }

            KeyGenParameterSpec keyParamSpec = keyBuilder.build();
            keyGen.init(keyParamSpec);
            keyGen.generateKey();

        } catch (NoSuchAlgorithmException | NoSuchProviderException |
                InvalidAlgorithmParameterException e) {
            Log.e(LOG_TAG, "Key gen error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static SecretKey getAesKey(String keyAlias) {
        try {
            return findOrGenerateAesKey(keyAlias);
        } catch (Exception e) {
            Log.d(LOG_TAG, "Failed to retrieve/generate a key: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    static SecretKey getAesKeyToWrap() {
        try {
            return generateAesKey();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SecretKey generateAesKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }


    static byte[] getIV() throws NoSuchAlgorithmException {
        SecureRandom secureRandom;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            secureRandom = SecureRandom.getInstanceStrong();
        } else {
            secureRandom = new SecureRandom();
        }

        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);

        return iv;

    }

    /**
     * Retrieves a secret key (AES) from the keystore.
     * @param keyAlias
     * Should only be used if you used the {@link AESKeyProvider#generateAesKey(String)} method
     */
    private static SecretKey findOrGenerateAesKey(String keyAlias) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException,
            UnrecoverableEntryException {

        if (!hasKey(keyAlias)) {
            generateAesKey(keyAlias);
        }

        KeyStore keyStore = KeyStore.getInstance(Constants.KEY_STORE);
        keyStore.load(null);


        KeyStore.SecretKeyEntry secretKeyEntry =
                (KeyStore.SecretKeyEntry) keyStore.getEntry(keyAlias, null);

        return secretKeyEntry.getSecretKey();
    }
}
