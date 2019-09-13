package io.mjolnir.saltblock;

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
            default:
                //TODO: change this once other encryption methods exist.
                return null;
        }
    }

    public void encrypt(String keyAlias, String password, List<String> plainTexts) {

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
            default:
                //TODO: change this once other encryption methods exist.
                return null;
        }
    }

}
