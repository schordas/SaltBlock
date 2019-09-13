package io.mjolnir.saltblock;

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
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

class KeyProvider {

    private static final String LOG_TAG = KeyProvider.class.getSimpleName();

    /** Generates a key that is then stored in the Android KeyStore
     * @param keyAlias will set the alias for this key for future reference
     **/
    private static void generateKey(String keyAlias) {

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

    static SecretKey getKey(String keyAlias) {
        try {
            return findOrGenerateKey(keyAlias);
        } catch (Exception e) {
            Log.d(LOG_TAG, "Failed to retrieve/generate a key: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a key from the keystore.
     * @param keyAlias
     * Should only be used if you used the {@link KeyProvider#generateKey(String)} method
     */
    private static SecretKey findOrGenerateKey(String keyAlias) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException,
            UnrecoverableEntryException {

        if (!hasKey(keyAlias)) {
            generateKey(keyAlias);
        }

        KeyStore keyStore = KeyStore.getInstance(Constants.KEY_STORE);
        keyStore.load(null);


        KeyStore.SecretKeyEntry secretKeyEntry =
                (KeyStore.SecretKeyEntry) keyStore.getEntry(keyAlias, null);

        return secretKeyEntry.getSecretKey();
    }

    private static boolean hasKey(String keyAlias) throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {

        KeyStore keyStore = KeyStore.getInstance(Constants.KEY_STORE);
        keyStore.load(null);
        Enumeration<String> keyStoreAliases = keyStore.aliases();

        while (keyStoreAliases.hasMoreElements()) {
            if (keyAlias.equals(keyStoreAliases.nextElement())) return true;
        }

        return false;
    }

    static void removeKey(String keyAlias) throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        if (hasKey(keyAlias)) {
            KeyStore keyStore = KeyStore.getInstance(Constants.KEY_STORE);
            keyStore.load(null);
            keyStore.deleteEntry(keyAlias);
        }
    }
}
