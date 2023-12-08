package com.example.ecabs.Utils;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class PasswordUtils {

    private static final String KEY_ALIAS = "MyKeyAlias";

    // Encrypt a password and return the encrypted base64 string
    public static String encryptPassword(Context context, String password) throws Exception {
        SecretKey secretKey = getOrCreateSecretKey(context);

        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_GCM + "/"
                + KeyProperties.ENCRYPTION_PADDING_NONE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    // Display an asterisk string to hide the password
    public static String getAsteriskString(int length) {
        StringBuilder asterisks = new StringBuilder();
        for (int i = 0; i < length; i++) {
            asterisks.append("*");
        }
        return asterisks.toString();
    }

    // Decrypt an encrypted password
    public static String decryptPassword(Context context, String encryptedPasswordBase64) throws Exception {
        SecretKey secretKey = getOrCreateSecretKey(context);

        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_GCM + "/"
                + KeyProperties.ENCRYPTION_PADDING_NONE);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encryptedBytes = Base64.decode(encryptedPasswordBase64, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private static SecretKey getOrCreateSecretKey(Context context) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());
            return keyGenerator.generateKey();
        }

        return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
    }
}
