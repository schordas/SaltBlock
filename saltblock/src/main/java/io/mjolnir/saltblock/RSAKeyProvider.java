package io.mjolnir.saltblock;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

class RSAKeyProvider extends KeyProvider {

    private static final String LOG_TAG = RSAKeyProvider.class.getSimpleName();

    static KeyPair getRsaKey(String keyAlias) {

        try {
            return findOrGenerateRsaKey(keyAlias);
        } catch (Exception e) {
            Log.d(LOG_TAG, "Failed to retrieve/generate a key: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static KeyPair findOrGenerateRsaKey(String keyAlias) throws CertificateException,
            NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchProviderException,
            InvalidAlgorithmParameterException, UnrecoverableKeyException {
        if (!hasKey(keyAlias)) {
            generateRsaKey(keyAlias);
        }

        KeyStore keyStore = KeyStore.getInstance(Constants.KEY_STORE);
        keyStore.load(null);

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, null);
        PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();

        if (privateKey == null || publicKey == null) {
            throw new NullPointerException("Error retrieving public/private key pair");
        }

        return new KeyPair(publicKey, privateKey);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void generateRsaKey(String keyAlias) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA",
                Constants.KEY_STORE);
        KeyGenParameterSpec.Builder keyBuilder = new KeyGenParameterSpec.Builder(keyAlias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            keyBuilder.setUserAuthenticationValidWhileOnBody(true);
        }

        keyGenerator.initialize(keyBuilder.build());
        keyGenerator.generateKeyPair();
    }

    private static PublicKey publicKeyFromString(String key) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
       byte[] keyBytes = Encoder.decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }
}
