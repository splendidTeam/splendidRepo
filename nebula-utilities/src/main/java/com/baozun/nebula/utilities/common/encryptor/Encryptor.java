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

public interface Encryptor {
	
	/**
	 * 加密，使用系统默认的KeyPairs。加密返回值默认是BASE64转码
	 * @param plainText
	 * @return
	 * @throws EncryptionException
	 */
	String encrypt(String plainText) throws EncryptionException;
	
	/**
	 * 解密，使用系统默认的KeyPairs。加密值默认是BASE64转码
	 * @param cipherText
	 * @return
	 * @throws EncryptionException
	 */
	String decrypt(String cipherText) throws EncryptionException;
	
	/**
	 * 加密，使用系统默认的KeyPairs
	 * @param plainText
	 * @return
	 * @throws EncryptionException
	 */
	byte[] encryptToByteArray(String plainText) throws EncryptionException;
	
	/**
	 * 解密，使用系统默认的KeyPairs
	 * @param cipher
	 * @return
	 * @throws EncryptionException
	 */
	String decryptFromByteArray(byte[] cipher) throws EncryptionException;
	
	/**
	 * 使用入参Key加密。加密返回值默认是BASE64转码
	 * @param plainText
	 * @param masterkey
	 * @return
	 * @throws EncryptionException
	 */
	String encrypt(String plainText,String key) throws EncryptionException;
	
	/**
	 * 使用入参Key解密。加密值默认是BASE64转码
	 * @param cipherText
	 * @param masterkey
	 * @return
	 * @throws EncryptionException
	 */
	String decrypt(String cipherText,String key) throws EncryptionException;
	
	/**
	 * 加密，使用系统默认的KeyPairs
	 * @param plainText
	 * @param key
	 * @return
	 * @throws EncryptionException
	 */
	byte[] encryptToByteArray(String plainText, String key) throws EncryptionException;
	
	/**
	 * 解密，使用系统默认的KeyPairs
	 * @param cipher
	 * @param key
	 * @return
	 * @throws EncryptionException
	 */
	String decryptFromByteArray(byte[] cipher, String key) throws EncryptionException;
}
