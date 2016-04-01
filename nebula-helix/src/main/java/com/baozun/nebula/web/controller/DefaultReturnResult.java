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
	public static final DefaultReturnResult SUCCESS = new ImmutableReturnResult(true);
	
	/**
	 * 默认返回失败类
	 */
	public static final DefaultReturnResult FAILURE = new ImmutableReturnResult(false);

	/**
	 * 额外的返回信息，当前端需要返回多个信息时使用
	 */
	private List<DefaultResultMessage> extraResultMessages = new ArrayList<DefaultResultMessage>();

	public List<DefaultResultMessage> getExtraResultMessages() {
		return extraResultMessages;
	}

	public void setExtraResultMessages(List<DefaultResultMessage> extraResultMessages) {
		this.extraResultMessages = extraResultMessages;
	}
	
	private static class ImmutableReturnResult extends DefaultReturnResult {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3687715075882594379L;

		public ImmutableReturnResult(boolean result){
			super.setResult(result);
		}

		@Override
		public void setExtraResultMessages(List<DefaultResultMessage> extraResultMessages) {
			throw new RuntimeException("Cannot change Immutable Object property.");
		}

		@Override
		public void setResult(boolean result) {
			throw new RuntimeException("Cannot change Immutable Object property.");
		}

		@Override
		public void setStatusCode(String statusCode) {
			throw new RuntimeException("Cannot change Immutable Object property.");
		}

		@Override
		public void setResultMessage(DefaultResultMessage resultMessage) {
			throw new RuntimeException("Cannot change Immutable Object property.");
		}

		@Override
		public void setReturnObject(Object returnObject) {
			throw new RuntimeException("Cannot change Immutable Object property.");
		}
	}
}
