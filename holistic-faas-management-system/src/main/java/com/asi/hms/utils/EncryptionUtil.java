package com.asi.hms.utils;

import com.asi.hms.exceptions.HolisticFaaSException;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtil {

    private EncryptionUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ALGORITHM = "AES";

    // Generate a new AES key for each user
    public static String generateKey() {

        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new HolisticFaaSException("Error generating encryption key", e);
        }
        keyGen.init(256); // for example, 256 bits AES

        SecretKey secretKey = keyGen.generateKey();

        return Base64.getEncoder().encodeToString(secretKey.getEncoded());

    }


    public static String encrypt(String strToEncrypt, String secret) {

        try {

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, new byte[12]);

            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(secret), ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {

            throw new HolisticFaaSException("Error encrypting string", e);

        }

    }


    public static String decrypt(String strToDecrypt, String secret) {

        try {

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, new byte[12]);

            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(secret), ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {

            throw new HolisticFaaSException("Error decrypting string", e);

        }

    }

}

