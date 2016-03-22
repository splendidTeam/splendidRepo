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
package com.baozun.nebula.web.controller.member.event;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.baozun.nebula.web.MemberDetails;

/**
 * 用户注册成功事件
 * 
 * @author D.C
 */
public class RegisterSuccessEvent extends ApplicationEvent{

	private static final long	serialVersionUID	= -3227325006512626684L;

	/* 上下文，ip agent等信息 */
	private Map<String, String>	clientContext;

	public RegisterSuccessEvent(MemberDetails source, Map<String, String> clientContext){
		super(source);
		this.setClientContext(clientContext);
	}

	public Map<String, String> getClientContext(){
		return clientContext;
	}

	public void setClientContext(Map<String, String> clientContext){
		this.clientContext = clientContext;
	}

}
