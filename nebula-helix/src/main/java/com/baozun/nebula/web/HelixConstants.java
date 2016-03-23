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
package com.baozun.nebula.web;

/**
 * 框架支持的配置参数定义
 * 
 * @author Benjamin.Liu
 *
 */
public interface HelixConstants {
	
	/**
	 * 网站域名
	 */
	public static final String SITE_DOMAIN_NAME = "site.domain.name";
	
	/**
	 * 网站登录地址
	 */
	public static final String SITE_LOGIN_URL = "site.login.url";
	
	/**
	 * 网站请求协议头改写位置
	 */
	public static final String SITE_WEBHEADER_REQUEST_PROTOCOL = "site.webheader.request.protocol";

	/**
	 * Https混合模式下会话安全签名盐值
	 */
	public static final String SECURITY_HTTPS_SECURESESSION_SALT = "security.https.securesession.salt";
	
	/**
	 * 网站用户信息检查失败后的跳转地址
	 */
	public static final String SECURITY_SESSION_CHECK_FAILURE_URL = "security.session.check.failure.url";
	
	
}
