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

import java.io.Serializable;

public abstract class AbstractReturnResult implements NebulaReturnResult, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -874986142275290821L;

	/**
	 * 返回结果
	 */
	private boolean result = true;	
	
	/**
	 * 返回状态码
	 */
	private String statusCode;
	
	/**
	 * 对应返回信息
	 */
	private DefaultResultMessage resultMessage;
	
	/**
	 * 对应返回的数据对象
	 */
	private Object returnObject;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public DefaultResultMessage getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(DefaultResultMessage resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}
}
