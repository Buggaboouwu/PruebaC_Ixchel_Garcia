package com.PruebaC.Security;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class AESUtil {

    private static final String CLAVE = "Nyunyaqwp123<3OMGPrincessOfPower"; 
    private static final int IV_LENGTH = 12; 
    private static final int TAG_LENGTH = 128; 
    
    private final SecureRandom secureRandom = new SecureRandom();

    public String encriptar(String password) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(), "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
            
            byte[] cipherText = cipher.doFinal(password.getBytes());
            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
            
            return Base64.getEncoder().encodeToString(combined);
            
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }

    public String desencriptar(String encryptedData) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] iv = new byte[IV_LENGTH];
            byte[] cipherText = new byte[decoded.length - IV_LENGTH];
            
            System.arraycopy(decoded, 0, iv, 0, iv.length);
            System.arraycopy(decoded, iv.length, cipherText, 0, cipherText.length);
      
            SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(), "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
            
            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText);
            
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }
    
    public boolean matches(String rawPassword, String encryptedPassword) {
        try {
            String decryptedPassword = desencriptar(encryptedPassword);
            return rawPassword.equals(decryptedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}