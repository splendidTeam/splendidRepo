/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.utilities.common.encryptor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.baozun.nebula.utilities.common.ConfigurationUtil;
import com.baozun.nebula.utilities.common.ResourceUtil;
import com.baozun.nebula.utilities.common.convertor.Base64Convertor;

/**
 * RSA 加解密器
 * RSA加解密的公私秘钥对建议使用OpenSSL来生成，如：
 * openssl genrsa -out rsaprivatekey.pem 1024
 * 
 * 然后基于此私钥生成公钥
 * openssl rsa -in rsaprivatekey.pem -out rsapublickey.pem -pubout
 * 
 * 如果要生成PKCS8的私钥，还需要做一步
 * openssl pkcs8 -topk8 -in rsaprivatekey.pem -out pkcs8rsaprivatekey.pem -nocrypt
 * 然后确保私钥文件的名称和路径正确
 * 
 * @author Benjamin.Liu
 *
 */
public class RSAEncryptor implements Encryptor {
	
	private boolean privateKeyIsPKCS8 = false;
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public RSAEncryptor(){
		String privateKeyPlace = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("rsa.privateKey");	
		String publicKeyPlace = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("rsa.publicKey");	
		String pkcs8 = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("rsa.publicKey.pkcs8");	
		
		assert privateKeyPlace != null : "Cannot find Configuration for RSA Private Key";
		assert publicKeyPlace != null : "Cannot find Configuration for RSA Public Key";
		
		if(pkcs8 != null)
			privateKeyIsPKCS8 = Boolean.getBoolean(pkcs8);
		
		try {
			privateKey = privateKeyIsPKCS8 ? toRSAPrivateKeyPKCS8(getInputStream(privateKeyPlace)):
						toRSAPrivateKey(getInputStream(privateKeyPlace));
			publicKey = toRSAPublicKey(getInputStream(publicKeyPlace));
		} catch (Exception e) {
			
		}
		
		assert privateKey != null : "RSA Private Key do not setup properly";
		assert publicKey != null : "RSA Public Key do not setup properly";
		
		
	}
	
	private Base64Convertor base64 = new Base64Convertor();

	@Override
	public String encrypt(String plainText) throws EncryptionException {
		return base64.format(encryptToByteArray(plainText));
	}

	@Override
	public String decrypt(String cipherText) throws EncryptionException {
		return decryptFromByteArray(base64.parse(cipherText));
	}

	@Override
	public String encrypt(String plainText, String publicKey) throws EncryptionException {
		return base64.format(encryptToByteArray(plainText, publicKey));
	}

	@Override
	public String decrypt(String cipherText, String privateKey) throws EncryptionException {
		return decryptFromByteArray(base64.parse(cipherText), privateKey);
	}
	
	@Override
	public byte[] encryptToByteArray(String plainText) throws EncryptionException {
		return encrypt(plainText, publicKey);
	}

	@Override
	public String decryptFromByteArray(byte[] cipher) throws EncryptionException {
		return decrypt(cipher, privateKey);
	}
	
	@Override
	public byte[] encryptToByteArray(String plainText, String publicKey) throws EncryptionException {
		return encrypt(plainText, toRSAPublicKey(publicKey));
	}

	@Override
	public String decryptFromByteArray(byte[] cipher, String privateKey) throws EncryptionException {
		return decrypt(cipher, privateKeyIsPKCS8? toRSAPrivateKeyPKCS8(privateKey): toRSAPrivateKey(privateKey));
	}

	private byte[] encrypt(String plainText, PublicKey publicKey) throws EncryptionException{
        Cipher cipher= null;  
        try {  
            cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());  
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
            return cipher.doFinal(plainText.getBytes(ConfigurationUtil.DEFAULT_ENCODING)); 
        } catch (Exception e) {  
            throw new EncryptionException("RSA Encryption Error", e);  
        } 
	}
	
	private String decrypt(byte[] cipherBytes, PrivateKey privateKey) throws EncryptionException {
        Cipher cipher= null;  
        try {  
            cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());  
            cipher.init(Cipher.DECRYPT_MODE, privateKey);  
            byte[] output= cipher.doFinal(cipherBytes);  
            return new String(output, ConfigurationUtil.DEFAULT_ENCODING);
        } catch (Exception e) {  
            throw new EncryptionException("RSA Decryption Error", e);  
        }  
	}
	
	private InputStream getInputStream(String keyPath) throws EncryptionException, IOException{
		if(keyPath.startsWith("classpath:")){
			String path = keyPath.substring(10);
			return ResourceUtil.getResourceAsStream(path, RSAEncryptor.class);
		}else if(keyPath.startsWith("file:")){
			String path = keyPath.substring(5);
			return new FileInputStream(path);
		}else
			throw new EncryptionException("Unsupported path for rsa key:" + keyPath);
	}
	
	private String loadKeyFromInputStream(InputStream is) throws EncryptionException {
		if(is == null)
			throw new EncryptionException("No Key found, InputStream is null");
		try {  
            BufferedReader br= new BufferedReader(new InputStreamReader(is));  
            String readLine= null;  
            StringBuilder sb= new StringBuilder();  
            while((readLine= br.readLine())!=null){  
                if(readLine.charAt(0)=='-'){  
                    continue;  
                }else{  
                    sb.append(readLine);  
                    sb.append('\r');  
                }  
            }  
            return sb.toString();
        } catch (IOException e) {  
            throw new EncryptionException("Load key from InputStream Error", e);  
        } finally {
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
				}
		}
	}
	
	private RSAPublicKey toRSAPublicKey(InputStream is) throws EncryptionException {
		String strKey = loadKeyFromInputStream(is);
		return toRSAPublicKey(strKey);
	}
	
	private RSAPublicKey toRSAPublicKey(String publicKey) throws EncryptionException {
		if(publicKey == null)
			throw new EncryptionException("No Key found, PublicKey String is null");
		try {   
            byte[] bk = base64.parse(publicKey);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");  
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(bk);  
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (Exception e) {  
            throw new EncryptionException("Extract PublicKey Error",e);  
        }
	}
	
	private RSAPrivateKey toRSAPrivateKey(InputStream is) throws EncryptionException {
		String strKey = loadKeyFromInputStream(is);
		return toRSAPrivateKey(strKey);
	}

	private RSAPrivateKey toRSAPrivateKey(String privateKey) throws EncryptionException {
		if(privateKey == null)
			throw new EncryptionException("No Key found, PrivateKey String is null");
		try {  
			byte[] bk = base64.parse(privateKey);	
			org.bouncycastle.asn1.pkcs.RSAPrivateKey  pKey = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(bk); 
			RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(pKey.getModulus(), pKey.getPrivateExponent());  
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");  
			return (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivKeySpec);    
        } catch (Exception e) {  
            throw new EncryptionException("Extract PrivateKey Error",e);  
        }
	}
	
	private RSAPrivateKey toRSAPrivateKeyPKCS8(InputStream is) throws EncryptionException {
		String strKey = loadKeyFromInputStream(is);
		return toRSAPrivateKeyPKCS8(strKey);
	}
	
	private RSAPrivateKey toRSAPrivateKeyPKCS8(String privateKey) throws EncryptionException {
		if(privateKey == null)
			throw new EncryptionException("No Key found, PrivateKey String is null");
		try {  
			byte[] bk = base64.parse(privateKey);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(bk);  
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (Exception e) {  
            throw new EncryptionException("Extract PrivateKey Error",e);  
        }
	}
}
