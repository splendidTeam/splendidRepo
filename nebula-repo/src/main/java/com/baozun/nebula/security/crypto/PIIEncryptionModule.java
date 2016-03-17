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

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.sdk.manager.SdkMataInfoManager;

/**
 * @author D.C
 * @date 2015年10月14日 上午10:28:56
 */
public class PIIEncryptionModule {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AESEncryptionModule.class);

	/** 是否启用数据库字段加解密功能 */
	private static final String PII_ENCRYPTION_ON_OFF = "PII_ENCRYPTION_ON_OFF";

	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;

	private AESEncryptionModule module;

	public PIIEncryptionModule() {
	}

	public PIIEncryptionModule(String masterKey, String ivParameter) {
		this.module = new AESEncryptionModule(masterKey, ivParameter);
	}

	public <T> T encryptModel(T model) {
		if(null == model) {
			return model;
		}
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field f : fields) {
			if (f.getAnnotation(PIIField.class) != null) {
				f.setAccessible(true);
				if (f.getType().toString().endsWith("String")) {
					try {
						if(f.get(model) != null) {
							f.set(model, encrypt((String) f.get(model)));
						}
					} catch (Exception e) {
						LOGGER.debug(String.format("加密实体%s的字段%s异常", model
								.getClass().getSimpleName(), f.getName()), e);
						e.printStackTrace();
					}
				}
			}
		}
		return model;
	}

	public <T> List<T> encryptModel(List<T> modelCollection) {
		if(null == modelCollection) {
			return modelCollection;
		}
		for (T t : modelCollection) {
			encryptModel(t);
		}
		return modelCollection;
	}

	public <T> T decryptModel(T model) {
		if(null == model) {
			return model;
		}
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field f : fields) {
			if (f.getAnnotation(PIIField.class) != null) {
				f.setAccessible(true);
				if (f.getType().toString().endsWith("String")) {
					try {
						if(f.get(model) != null) {
							f.set(model, decrypt((String) f.get(model)));
						}
					} catch (Exception e) {
						LOGGER.debug(String.format("加密实体%s的字段%s异常", model
								.getClass().getSimpleName(), f.getName()), e);
						e.printStackTrace();
					}
				}
			}
		}
		return model;
	}

	public <T> List<T> decryptModel(List<T> modelCollection) {
		if(null == modelCollection) {
			return modelCollection;
		}
		for (T t : modelCollection) {
			decryptModel(t);
		}
		return modelCollection;
	}

	public String encrypt(String plainText) {
		if (Boolean
				.valueOf(sdkMataInfoManager.findValue(PII_ENCRYPTION_ON_OFF))) {
			return module.encrypt(plainText);
		} else {
			return plainText;
		}
	}

	public String decrypt(String cipherText) {
		if (Boolean
				.valueOf(sdkMataInfoManager.findValue(PII_ENCRYPTION_ON_OFF))) {
			return module.decrypt(cipherText);
		} else {
			return cipherText;
		}
	}
}
