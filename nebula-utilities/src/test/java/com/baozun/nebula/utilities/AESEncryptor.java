package com.baozun.nebula.utilities;

import javax.crypto.SecretKey;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.baozun.nebula.utilities.common.ConfigurationUtil;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.convertor.Base64Convertor;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;

public class AESEncryptor {

    private static Base64Convertor base64 = new Base64Convertor();

    public static String encrypt(String plainText, String decryptKey) throws EncryptionException {
        try {
            SecretKey key = new SecretKeySpec(base64.parse(decryptKey), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] c = cipher.doFinal(plainText.getBytes(ConfigurationUtil.DEFAULT_ENCODING));
            return base64.format(c);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String cipherText, String decryptKey) throws EncryptionException {
        try {
            SecretKey key = new SecretKeySpec(base64.parse(decryptKey), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] b = cipher.doFinal(base64.parse(cipherText));
            return new String(b, ConfigurationUtil.DEFAULT_ENCODING);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
           
        	String key="xhf6Z6I3JePpm8pgYa5m6w==";
        	
        	//String pass=encrypt("test123",key);
        	//System.out.println(pass);
        	System.out.println(EncryptUtil.getInstance().decrypt("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",key));
        	
        } catch( Exception e) {
            e.printStackTrace();
        }
    }

}
