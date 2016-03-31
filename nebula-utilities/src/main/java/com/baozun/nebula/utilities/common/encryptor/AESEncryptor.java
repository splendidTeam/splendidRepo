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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.utilities.common.ConfigurationUtil;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.convertor.Base64Convertor;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException.EncOperation;

public class AESEncryptor implements Encryptor {

	private Base64Convertor base64 = new Base64Convertor();
	private SecretKey key;

	public AESEncryptor() {
		String masterkey = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("aes.masterkey");
		key = new SecretKeySpec(base64.parse(masterkey), "AES");
		assert key != null : "AES Secret Key initiate failed";
	}

	@Override
	public String encrypt(String plainText) throws EncryptionException {
		return encrypt(plainText, null);
	}

	public String encrypt(String plainText, String masterkey) throws EncryptionException {
		return base64.format(encryptToByteArray(plainText, masterkey));		
	}

	@Override
	public String decrypt(String cipherText) throws EncryptionException {
		return decrypt(cipherText, null);
	}

	public String decrypt(String cipherText, String masterkey) throws EncryptionException {
		return decryptFromByteArray(base64.parse(cipherText), masterkey);
	}

	@Override
	public byte[] encryptToByteArray(String plainText) throws EncryptionException {
		return encryptToByteArray(plainText, null);
	}

	@Override
	public String decryptFromByteArray(byte[] cipher) throws EncryptionException {
		return decryptFromByteArray(cipher, null);
	}

	@Override
	public byte[] encryptToByteArray(String plainText, String masterkey) throws EncryptionException {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			if (StringUtils.isBlank(masterkey)) {
				cipher.init(Cipher.ENCRYPT_MODE, key);
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(base64.parse(masterkey), "AES"));
			}

			return cipher.doFinal(plainText.getBytes(ConfigurationUtil.DEFAULT_ENCODING));
		} catch (Exception e) {
			throw new EncryptionException(plainText, "AES", EncOperation.ENCRYPT ,e); 
		} 
	}

	@Override
	public String decryptFromByteArray(byte[] cipher, String masterkey) throws EncryptionException {
		try {
			Cipher c = Cipher.getInstance("AES");
			if (StringUtils.isBlank(masterkey)) {
				c.init(Cipher.DECRYPT_MODE, key);
			} else {
				c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(base64.parse(masterkey), "AES"));
			}
			byte[] b = c.doFinal(cipher);
			return new String(b, ConfigurationUtil.DEFAULT_ENCODING);
		} catch (Exception e) {
			throw new EncryptionException(base64.format(cipher), "AES", EncOperation.DECRYPT ,e); 
		}
	}

	public static void main(String[] args) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			SecureRandom random = EncryptUtil.getSecureRandom();
			String skeylen = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("aes.key.length");
			generator.init(Integer.parseInt(skeylen), random);
			SecretKey key = generator.generateKey();
			System.out.println("========== AES Key ==========");
			Base64Convertor base64 = new Base64Convertor();
			System.out.println("aes.masterkey=" + base64.format(key.getEncoded()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
