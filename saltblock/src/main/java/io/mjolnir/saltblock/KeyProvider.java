package io.mjolnir.saltblock;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

class KeyProvider {

    static boolean hasKey(String keyAlias) throws KeyStoreException, CertificateException,
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
