/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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
package com.baozun.nebula.event;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import com.baozun.nebula.web.MemberDetails;

/**
 * 登录成功监听器
 * 
 * @author D.C
 */
public class LoginSuccessEventListener implements ApplicationListener<LoginSuccessEvent> {
	private static final Logger LOG = LoggerFactory.getLogger(LoginSuccessEventListener.class);

	@Override
	public void onApplicationEvent(LoginSuccessEvent event) {
		MemberDetails memberDetails = (MemberDetails)event.getSource();
		LOG.info("[MEM_LOGIN_SUCCESS] {} [{}] \"\"", memberDetails.getLoginName(), new Date());
		this.handler(memberDetails, event.getClientContext());
	}
	/**
	 * 可扩展的处理器
	 * @param source
	 * @param context
	 */
	protected void handler(MemberDetails source, Map<String, String> context) {
		//TODO 比如记录登录日志，处理购物车
	}
}
