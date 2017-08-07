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
package com.baozun.nebula.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Service;

import com.baozun.nebula.manager.auth.UserManager;
import com.baozun.nebula.web.UserDetails;

/**
 * @author songdianchao 监听事件
 */
@Service
@Scope("singleton")
public class HttpSessionEventListener implements ApplicationListener<HttpSessionDestroyedEvent>{

	@Autowired
	UserManager					userManager;

	private static final Logger	log	= LoggerFactory.getLogger(HttpSessionEventListener.class);

	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event){
		HttpSessionDestroyedEvent sessionEvent = (HttpSessionDestroyedEvent) event;
		try{
			Object o = sessionEvent.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
			if (o != null){
				SecurityContext context = (SecurityContext) o;
				UserDetails user = (UserDetails) context.getAuthentication().getPrincipal();
				userManager.logoutLog(user.getUserId(), sessionEvent.getSession().getId());
				log.debug(user.getRealName() + " logout");
			}
		}catch (IllegalStateException e){
			log.warn("HttpSessionEventListener error", e);
		}
	}

}
