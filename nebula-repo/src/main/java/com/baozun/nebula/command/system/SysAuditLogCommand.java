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
package com.baozun.nebula.command.system;

import java.util.Date;

import com.baozun.nebula.command.Command;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年6月17日 下午3:07:06 
 * @version   
 */
public class SysAuditLogCommand implements Command{

	/** */
	private static final long serialVersionUID = 2349578507009529267L;

	private Long		id;
	
	/**
	 * 请求的uri
	 */
	private String		uri;
	
	/**
	 * 请求的参数
	 */
	private String		parameters;

	/**
	 * 请求的类型（post，get）
	 */
	private String		method;

	/**
	 * 操作人IP
	 */
	private String		ip;
	
	/**
	 * 返回的结果码
	 */
	private String		responseCode;
	
	/**
	 * 返回的异常
	 */
	private String      exception;
	
	/**
	 * 操作人ID
	 */
	private Long        operaterId;
	
	/**
	 * 操作时间
	 */
	private Date		createTime;
	
	/** 
	 * 以下为冗余字段
	 * */
	
	/** 操作人显示名*/
	private String		operatorLabel;
	
	/** 请求参数显示*/
	private String		parametersLabel;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

	/**
	 * @return the operaterId
	 */
	public Long getOperaterId() {
		return operaterId;
	}

	/**
	 * @param operaterId the operaterId to set
	 */
	public void setOperaterId(Long operaterId) {
		this.operaterId = operaterId;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the operatorLabel
	 */
	public String getOperatorLabel() {
		return operatorLabel;
	}

	/**
	 * @param operatorLabel the operatorLabel to set
	 */
	public void setOperatorLabel(String operatorLabel) {
		this.operatorLabel = operatorLabel;
	}

	/**
	 * @return the parametersLabel
	 */
	public String getParametersLabel() {
		return parametersLabel;
	}

	/**
	 * @param parametersLabel the parametersLabel to set
	 */
	public void setParametersLabel(String parametersLabel) {
		this.parametersLabel = parametersLabel;
	}

}
