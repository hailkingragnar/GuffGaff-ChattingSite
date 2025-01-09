package org.web.guffgaff.guffgaff.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.util.Base64;

public class EncryptionUtil {
    private static final String SECRET_KEY = "PrakritiBabe0000";

    public static String encrypt(String plainText) {
        try{
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));

        }catch(Exception e) {
            throw new RuntimeException("Error encrypting message");
        }
    }
    public static String decrypt(String cipherText) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        }catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

}
