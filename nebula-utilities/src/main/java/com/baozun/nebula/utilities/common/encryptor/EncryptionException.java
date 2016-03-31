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

public class EncryptionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3358193119637945864L;
	
	public static enum EncOperation {
		ENCRYPT,DECRYPT
	}
	
	/**
	 * 待加密/解密的文本
	 */
	private String originText;
	
	/**
	 * 加解密算法
	 */
	private String encType;
	
	private EncOperation encOperation;

	public EncryptionException() {
	}

	public EncryptionException(String arg0) {
		super(arg0);
	}

	public EncryptionException(Throwable arg0) {
		super(arg0);
	}

	public EncryptionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public EncryptionException(String originText, String encType, EncOperation encOperation, Throwable e){
		super(e);
		this.originText = originText;
		this.encType = encType;
		this.encOperation = encOperation;
	}

	public String getOriginText() {
		return originText;
	}

	public void setOriginText(String originText) {
		this.originText = originText;
	}

	public String getEncType() {
		return encType;
	}

	public void setEncType(String encType) {
		this.encType = encType;
	}

	public EncOperation getEncOperation() {
		return encOperation;
	}

	public void setEncOperation(EncOperation encOperation) {
		this.encOperation = encOperation;
	}
}
