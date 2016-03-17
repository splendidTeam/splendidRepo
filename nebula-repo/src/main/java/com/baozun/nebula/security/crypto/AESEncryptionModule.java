/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.security.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AES算法加密/解密工具类。
 * 
 * @author D.C
 */
public class AESEncryptionModule implements EncryptionModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(AESEncryptionModule.class);

	/** 算法名称 */
	private static final String ALGORITHOM = "AES";

	/** 默认的安全服务提供者 */
	private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

	/** 缓存的密钥 */
	private SecretKey key;

	private static final String RANDOM_ALGORITHM = "SHA1PRNG";

	private static final String AES_ALGORITHM = "AES/CBC/PKCS7Padding";

	private static final String ENCODING = "UTF-8";

	private static final int ENCRYPTION_KEY_LENGTH = 256;
	/**
	 * 16 bit 
	 */
	private String ivParameter = "ryojvlzmdalyglrj";

	/**
	 * @return the ivParameter
	 */
	public String getIvParameter() {
		return ivParameter;
	}

	/**
	 * @param ivParameter
	 *            the ivParameter to set
	 */
	public void setIvParameter(String ivParameter) {
		this.ivParameter = ivParameter;
	}

	/**
	 * 使用默认的密钥加密给定的字符串。
	 */
	@Override
	public String encrypt(String plainText) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM, DEFAULT_PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivParameter.getBytes(ENCODING)));
			byte[] c = cipher.doFinal(plainText.getBytes(ENCODING));
			return new String(Hex.encodeHex(c));
		} catch (Exception e) {
			LOGGER.error("encrypt error", e);
		}
		return null;
	}

	/**
	 * 使用给定的密钥加密给定的字符串。
	 */
	public String encrypt(String plainText, String masterKey) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM, DEFAULT_PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(masterKey.getBytes(), ALGORITHOM),
					new IvParameterSpec(ivParameter.getBytes(ENCODING)));
			byte[] c = cipher.doFinal(plainText.getBytes(ENCODING));
			return new String(Hex.encodeHex(c));
		} catch (Exception e) {
			LOGGER.error("encrypt error", e);
		}
		return null;
	}

	/**
	 * 使用默认的密钥解密给定的字符串。
	 */
	@Override
	public String decrypt(String cipherText) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM, DEFAULT_PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivParameter.getBytes(ENCODING)));
			byte[] b = cipher.doFinal(Hex.decodeHex(cipherText.toCharArray()));
			return new String(b, ENCODING);
		} catch (Exception e) {
			LOGGER.error("decrypt error", e);
		}
		return null;
	}

	/**
	 * 使用给定的密钥解密给定的字符串。
	 */
	public String decrypt(String cipherText, String masterKey) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM, DEFAULT_PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(masterKey.getBytes(), ALGORITHOM),
					new IvParameterSpec(ivParameter.getBytes(ENCODING)));
			byte[] b = cipher.doFinal(Hex.decodeHex(cipherText.toCharArray()));
			return new String(b, ENCODING);
		} catch (Exception e) {
			LOGGER.error("decrypt error", e);
		}
		return null;
	}

	/**
	 * 生成随机密钥
	 * 
	 * @return
	 */
	public static String randomKey() {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
			SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
			generator.init(ENCRYPTION_KEY_LENGTH, random);
			SecretKey key = generator.generateKey();
			return new String(Hex.encodeHex(key.getEncoded()));
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("NoSuchAlgorithmException", e);
		}
		return null;
	}

	public AESEncryptionModule() {
	}

	/**
	 * 指定密钥
	 * 
	 * @param masterKey
	 */
	public AESEncryptionModule(String masterKey) {
		KeyGenerator generator;
		try {
			generator = KeyGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
			SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
			random.setSeed(masterKey.getBytes());
			generator.init(ENCRYPTION_KEY_LENGTH, random);
			this.key = generator.generateKey();
			LOGGER.debug("AES KEY:"
					+ new String(Hex.encodeHex(key.getEncoded())));
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("NoSuchAlgorithmException", e);
		}
	}

	/**
	 * @param ivParameter
	 */
	public AESEncryptionModule(String masterKey, String ivParameter) {
		this(masterKey);
		this.ivParameter = ivParameter;
	}
	
	
}
