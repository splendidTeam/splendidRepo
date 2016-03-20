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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;

import com.baozun.nebula.event.EventPublisher;

/**
 * BaseController
 * 
 * @author D.C
 */
public abstract class BaseController {

	@Resource
	protected ApplicationContext context;
	@Resource
	protected EventPublisher eventPublisher;

	// TODO未实现
	/**
	 * 使用RSA非对称加解密 默认使用全局的public key，使用servlet初始化，此处保持空实现 如果安全上要求每个用户使用不同public
	 * key时需要商城重写
	 * 
	 * @param request
	 * @param model
	 */
	protected void prepare4SensitiveDataEncryptedByJs(HttpServletRequest request, Model model) {
	}

	/**
	 * 敏感数据解密过程
	 * 
	 * @param sensitiveData
	 * @return
	 */
	protected String decryptSensitiveDataEncryptedByJs(String sensitiveData, HttpServletRequest request) {
		String result = sensitiveData;
		// TODO 解密动作
		try {

		} catch (Exception e) {
		} // 解密出错原样使用
		return result;
	}
}
