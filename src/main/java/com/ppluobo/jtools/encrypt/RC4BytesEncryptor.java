package com.ppluobo.jtools.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Security;

import static com.ppluobo.jtools.encrypt.EncryptorUtils.*;


public final class RC4BytesEncryptor {

    private static final Charset utf8 = Charset.forName("UTF-8");

    private final SecretKey secretKey;
    private final Cipher encryptor;
    private final Cipher decryptor;

    private static final String RC4_ALGORITHM = "RC4";
    private static final String RC4_KEY_ALGORITHM = "PBEWithSHAAnd128BitRC4";


    public RC4BytesEncryptor(String password) {
        // add extra provider
        Security.addProvider(new BouncyCastleProvider());

        SecretKey secretKey = newSecretKey(RC4_KEY_ALGORITHM, password);
        this.secretKey = new SecretKeySpec(secretKey.getEncoded(), RC4_KEY_ALGORITHM);

        this.encryptor = newCipher(RC4_ALGORITHM);
        this.decryptor = newCipher(RC4_ALGORITHM);
    }

    public byte[] encrypt(byte[] bytes) {
        synchronized (this.encryptor) {
            initCipher(this.encryptor, Cipher.ENCRYPT_MODE, this.secretKey);
            return doFinal(this.encryptor, bytes);
        }
    }

    public byte[] decrypt(byte[] encryptedBytes) {
        synchronized (this.decryptor) {
            initCipher(this.decryptor, Cipher.DECRYPT_MODE, this.secretKey);
            return doFinal(this.decryptor, encryptedBytes);
        }
    }


    public String encryptHex(String data) {
        return Hex.toHexString(encrypt(data.getBytes(utf8)));
    }

    public String decryptHex(String encryptedHex) {
        return new String(decrypt(Hex.decode(encryptedHex)), utf8);
    }

    public String encryptBase64(String data) {
        return Base64.encodeBase64URLSafeString(encrypt(data.getBytes(utf8)));
    }

    public String decryptBase64(String encryptedBase64) {
        return new String(decrypt(Base64.decodeBase64(encryptedBase64)), utf8);
    }

}

