package io.mjolnir.saltblock;

public enum KeyStorageMethod {

    /**
     * @see KeyProvider#generateKey(String)
     * KEY_STORE will leverage the Android Keystore
     * This suggests that the user does not need to implement a password specific to the app.
     * The Android KeyStore uses the device PIN/Password.
     * It also suggest that the KeyStore will generate the key.
     */
    KEY_STORE,


    /**
     * WARNING: Please read the documentation in the README regarding using this approach.
     *      * This option was created so as an excerice in learning password based encryption,
     *      * NOT to improve upon the security of the KeyStore.
     *
     * APP_CUSTOM_PASSWORD indicates that the app will require a password login.
     * This password will be used to generate a Hash with PBKDF2WithHmacSHA256 and AES 256.
     * SaltBlock will then generate a separate key using PBKDF2WithHmacSHA256 and AES 256.
     * The Salt and IV will be stored in order to recreate the key when the correct password has
     * been entered.
     *
     *
     * Salt block stores the above mentioned information in the file system. This is not
     * necessarily more secure than the KeyStore, although it does add another layer of protection
     * by using a custom password for the app itself. However, if the user clears the data on their
     * app, they will lose this information
     */
    APP_CUSTOM_PASSWORD
}
