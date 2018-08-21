package com.ppluobo.jtools.encrypt;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EncryptorUtils {

    /**
     * Generates a SecretKey.
     */
    public static SecretKey newSecretKey(String algorithm, String password) {
        return newSecretKey(algorithm, new PBEKeySpec(password.toCharArray()));
    }

    /**
     * Generates a SecretKey.
     */
    public static SecretKey newSecretKey(String algorithm, PBEKeySpec keySpec) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            return factory.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Not a valid encryption algorithm", e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Not a valid secret key", e);
        }
    }


    public static SecretKey newSecretKey(String algorithm) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            return kg.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Not a valid encryption algorithm", e);
        }

    }

    /**
     * Constructs a new Cipher.
     */
    public static Cipher newCipher(String algorithm) {
        try {
            return Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Not a valid encryption algorithm", e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalStateException("Should not happen", e);
        }
    }

    /**
     * Initializes the Cipher for use.
     */
    public static void initCipher(Cipher cipher, int mode, SecretKey secretKey) {
        try {
            cipher.init(mode, secretKey);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(
                    "Unable to initialize due to invalid secret key", e);
        }
    }

    /**
     * Invokes the Cipher to perform encryption or decryption (depending on the
     * initialized mode).
     */
    public static byte[] doFinal(Cipher cipher, byte[] input) {
        try {
            return cipher.doFinal(input);
        } catch (IllegalBlockSizeException e) {
            throw new IllegalStateException(
                    "Unable to invoke Cipher due to illegal block size", e);
        } catch (BadPaddingException e) {
            throw new IllegalStateException("Unable to invoke Cipher due to bad padding",
                    e);
        }
    }
}
