package com.baozun.nebula.utilities.common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

/**
 * 此方法用于与js端的加密进行联动操作，如果只使用服务端的
 * aes加密,请使用AESEncryptor
 * @author Justin Hu
 *
 */
public class AesUtil {
	 	private static  int keySize;
	    private static  int iterationCount;
	    private static	 String iv;
	    private static	 String salt;
	    private static	 String passphrase;
	    private static  Cipher cipher;
	    
	    private static AesUtil aesUtil=new AesUtil();
	    
	    public AesUtil() {
	        
	    	String strKeySize=ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("partnership.aes.key.size");
	    	String strIterationCount=ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("partnership.aes.iteration.count");
	    	keySize=Integer.parseInt(strKeySize);
	    	iterationCount=Integer.parseInt(strIterationCount);
	    	salt=ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("partnership.aes.salt");
	    	iv=ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("partnership.aes.iv");
	    	passphrase=ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("partnership.aes.passphrase");
	    	
	        try {
	            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        }
	        catch (Exception e) {
	            throw fail(e);
	        }
	    }
	    
	    public static String encrypt( String plaintext){
	    	return aesUtil.encrypt(salt,iv,passphrase,plaintext);
	    }
	    
	    private String encrypt(String salt, String iv, String passphrase, String plaintext) {
	        try {
	            SecretKey key = generateKey(salt, passphrase);
	            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
	            return base64(encrypted);
	        }
	        catch (UnsupportedEncodingException e) {
	            throw fail(e);
	        }
	    }
	    
	    public static String decrypt( String ciphertext){
	    	if(StringUtils.isBlank(ciphertext))
	    		return null;
	    	
	    	return aesUtil.decrypt(salt,iv,passphrase,ciphertext);
	    }
	    
	    private String decrypt(String salt, String iv, String passphrase, String ciphertext) {
	        try {
	            SecretKey key = generateKey(salt, passphrase);
	            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
	            return new String(decrypted, "UTF-8");
	        }
	        catch (UnsupportedEncodingException e) {
	            throw fail(e);
	        }
	    }
	    
	    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
	        try {
	            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
	            return cipher.doFinal(bytes);
	        }
	        catch (Exception e) {
	            throw fail(e);
	        }
	    }
	    
	    private SecretKey generateKey(String salt, String passphrase) {
	        try {
	            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
	            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	            return key;
	        }
	        catch (Exception e) {
	            throw fail(e);
	        }
	    }
	    
	    public static String random(int length) {
	        byte[] salt = new byte[length];
	        new SecureRandom().nextBytes(salt);
	        return hex(salt);
	    }
	    
	    public static String base64(byte[] bytes) {
	        try {
				return new String(Base64.encodeBase64(bytes),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	    }
	    
	    public static byte[] base64(String str) {
	        try {
				return Base64.decodeBase64(str.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	    }
	    
	    public static String hex(byte[] bytes) {
	        return new String(Hex.encodeHex(bytes)) ;
	    }
	    
	    public static byte[] hex(String str) {
	        try {
	            return Hex.decodeHex(str.toCharArray());
	        }
	        catch (DecoderException e) {
	            throw new IllegalStateException(e);
	        }
	    }
	    
	    private IllegalStateException fail(Exception e) {
	        return new IllegalStateException(e);
	    }
	    
	    public static void main(String[] args){
	    	AesUtil au=new AesUtil();
	    	
	    	String ct1=au.encrypt("test");
	    	System.out.println(ct1);
	    	String ct="w8Dlpnrw8jiNAnnCk8Xfcw==";
	    	
	    	System.out.println(au.decrypt(ct));
	    	
	    }
	    
}

