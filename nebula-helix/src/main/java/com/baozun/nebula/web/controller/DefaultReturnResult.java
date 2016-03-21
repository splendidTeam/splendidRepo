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

import java.util.ArrayList;
import java.util.List;

/**
 * 默认返回数据结果类
 * 
 * @author Benjamin.Liu
 *
 */
public class DefaultReturnResult extends AbstractReturnResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5265655012973414164L;
	
	/**
	 * 默认返回成功类
	 */
	public static final DefaultReturnResult SUCCESS = new DefaultReturnResult();

	/**
	 * 额外的返回信息，当前端需要返回多个信息时使用
	 */
	private List<DefaultReturnResult> extraResultMessages = new ArrayList<DefaultReturnResult>();

	public List<DefaultReturnResult> getExtraResultMessages() {
		return extraResultMessages;
	}

	public void setExtraResultMessages(List<DefaultReturnResult> extraResultMessages) {
		this.extraResultMessages = extraResultMessages;
	}
}
