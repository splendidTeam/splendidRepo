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
package com.baozun.nebula.web.controller.product.viewcommand;

import java.io.Serializable;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * Shopdog command的基类
 * 
 */
public class ShopdogBaseCommand extends BaseViewCommand implements Serializable {
	
	private static final long serialVersionUID = 3702428534203898354L;
	
	//结果
	/** 结果, 成功：1 */
	public static final Integer RESULT_SUCCESS = 1;
	
	/** 结果, 失败：0 */
	public static final Integer RESULT_FAIL = 0;
	
	/**
	 * 结果（1：成功，0：失败）
	 */
	private Integer result;
	
	/**
	 * 错误编码
	 */
	private String errorCode;
	
	/**
	 * 错误信息
	 */
	private String message;


	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}