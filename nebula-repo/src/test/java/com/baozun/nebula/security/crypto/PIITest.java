package com.baozun.nebula.security.crypto;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


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

/**
 * @author D.C
 * @date 2015年10月14日 上午10:36:32
 */
public class PIITest {
	//pii.key=33348e22d832205ce001930dd7f7559e7fd4755c725e75cba5c9c38537e9de5e
	//pii.ivParameter=ryo!vlzmda$yg*rj
	@Test
	public void testPII() {
		/*String key = AESEncryptionModule.randomKey();
		System.out.println(String.format("key = %s", key));*/

		PIIEncryptionModule module = new PIIEncryptionModule("33348e22d832205ce001930dd7f7559e7fd4755c725e75cba5c9c38537e9de5e", "ryo!vlzmda$yg*rj");

		User user = new User("阿斯蒂芬里撒娇阿萨德放假阿桑德拉科罚金阿斯利康$#$#@535as");
		module.encryptModel(user);

		System.out.println(user.getName());

		module.decryptModel(user);

		System.out.println(user.getName());

		List<User> list = new ArrayList<User>();
		list.add(new User("test1"));
		list.add(new User("test2"));

		for (User u : list) {
			System.out.println(u.getName());
		}

		list = module.encryptModel(list);

		for (User u : list) {
			System.out.println(u.getName());
		}

		list = module.decryptModel(list);

		for (User u : list) {
			System.out.println(u.getName());
		}
	}

	public class User {
		@PIIField
		private String name;

		public User(String name) {
			this.name = name;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
	}

	@Test
	public void testRSA() {
		// RSAEncryptionModule rsa = new RSAEncryptionModule();
		// RSAPublicKey publicKey = rsa.getDefaultPublicKey();
		// System.out.println(new
		// String(Hex.encodeHex(publicKey.getModulus().toByteArray())));
		// System.out.println(new
		// String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray())));
		// rsa.encrypt("wwe");
	}
	
	@Test
	public void testAES() {
		String key = AESEncryptionModule.randomKey();
		System.out.println("密钥：" + key);
		System.out.println("密钥位数：" + (key.length() * 4));
		AESEncryptionModule aes = new AESEncryptionModule(key);
		aes.setIvParameter("1234567890123456");

		String source = "我是一段很长很长很长很长很长的很长很长很长很长很长的文字，把我加密一下。。。";
		String encrypted = aes.encrypt(source);
		System.out.println("密文：" + encrypted);

		System.out.println("明文：" + aes.decrypt(encrypted));

		System.out.println("==我是分割线===================================");

		key = AESEncryptionModule.randomKey();
		aes = new AESEncryptionModule(key);
		aes.setIvParameter("1234567890123456");

		System.out.println("再次加密：" + aes.encrypt(source));
		System.out.println("再次解密：" + aes.decrypt(encrypted));
	}
}
