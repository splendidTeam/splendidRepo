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
import java.util.ArrayList;
import java.util.List;

/**
 * 默认说明，可用于输出信息
 * @author Benjamin.Liu
 *
 */
public class DefaultResultMessage implements NebulaResultMessage, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4707572914064240028L;
	
	/**
	 * 输出的信息或输出的信息Key，如果用于输出给到普通用户看到，则应该使用Key模式
	 */
	private String message;
	
	/**
	 * 输出的信息中需要使用的参数值，参数是排序的
	 */
	private List<Object> params = new ArrayList<>();
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	
	
}
