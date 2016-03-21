/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller;

/**
 * 数据转换类型不支持异常，用于在ViewCommand的转换过程中出现类型不匹配时抛出。
 * 
 * @author Benjamin.Liu
 *
 */
public class UnsupportDataTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6032343993575469611L;

	public UnsupportDataTypeException() {
		super();
	}

	public UnsupportDataTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnsupportDataTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportDataTypeException(String message) {
		super(message);
	}

	public UnsupportDataTypeException(Throwable cause) {
		super(cause);
	}

	
}
