package io.mjolnir.saltblock;

import java.security.PublicKey;
import java.util.List;

public class SaltBlock {

    private final EncryptionAlgorithm mAlg;

    public SaltBlock() {
        this(EncryptionAlgorithm.AES);
    }

    /**
     * @param alg specifies which encryption algorithm you want to use. AES is default
     */
    public SaltBlock(EncryptionAlgorithm alg) {
        this.mAlg = alg;
    }

    /**
     * @param plainTexts to encrypt
     * @return return list of cipher texts
     * Note that this uses the default encryption method AES/GCM/NoPadding
     */
    public List<String> encrypt(String keyAlias, List<String> plainTexts) {

        switch (mAlg) {
            case AES:
                try {
                    return AES.encrypt(keyAlias, plainTexts);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            case RSA:
                try {
                    return RSA.encrypt(keyAlias, plainTexts);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            default:
               return  null;
        }
    }

    public List<String> decrypt(String keyAlias, List<String> cipherTexts) {
        switch (mAlg) {
            case AES:
                try {
                    return AES.decrypt(keyAlias, cipherTexts);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            case RSA:
                try {

                    return RSA.decrypt(keyAlias, cipherTexts);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            default:
                //TODO: change this once other encryption methods exist.
                return null;
        }
    }

    /**
     * @return RSA public key. Returns null if RSA is not implemented
     */
    public PublicKey getPublicKey(String keyAlias) {
       if (mAlg == EncryptionAlgorithm.AES) return null;

       return RSA.getPublicKey(keyAlias);
    }

}
