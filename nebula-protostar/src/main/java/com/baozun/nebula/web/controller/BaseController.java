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

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.command.BackWarnEntity;

/**
 * BaseController
 * 
 * @author songdianchao
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 */
public class BaseController{

	@Resource
	protected ApplicationContext	context;

	/**
	 * ajax http header
	 */
	public static final String		HEADER_WITH_AJAX_SPRINGMVC	= "X-Requested-With=XMLHttpRequest";

	/**
	 * 默认的成功状态
	 * @return
	 */
	protected static final BackWarnEntity SUCCESS=new BackWarnEntity(true,"");
	
	
	protected static final Long MAX_EXCEL_SIZE=1024*1024*5L; 									//EXCEL MAX(MB)
	
	protected static final String[] EXCEL_TYPE=new String[]{"XLS"}; 							//EXCEL Type(ONLY)
	
	
	/**
	 * 默认的失败状态
	 * @return
	 */
	protected static final BackWarnEntity FAILTRUE=new BackWarnEntity(false,"");
	
	/**
	 * 获取当前登录用户信息
	 * 
	 * @return
	 */
	protected UserDetails getUserDetails(){
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * 生成spring 的跳转路径<br>
	 * e.g. getSpringRedirectPath("/shoppingcart") <br>
	 * 注:不需要你手工的 拼接request.getContextPath()
	 * 
	 * @param targetUrl
	 *            如果是相对根目录路径 只需要传递 如"/shoppingcart" spring会自动添加request.getContextPath() <br>
	 *            也可以传入绝对路径 如:http://www.baidu.com
	 * @return
	 */
	protected String getSpringRedirectPath(String targetUrl){
		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + targetUrl;
	}

	/**
	 * 生成 spring Forward 路径
	 * 
	 * @param forwardUrl
	 * @return
	 */
	protected String getSpringForwardPath(String forwardUrl){
		return UrlBasedViewResolver.FORWARD_URL_PREFIX + forwardUrl;
	}

	// **********************************************************************************************

	/**
	 * 获得消息信息
	 * 
	 * @param code
	 *            code
	 * @param args
	 *            args
	 * @return
	 */
	protected String getMessage(Integer errorCode){
		Object[] args = null;
		return getMessage(errorCode, args);
	}

	/**
	 * 获得消息信息
	 * 
	 * @param code
	 *            code
	 * @param args
	 *            args
	 * @return
	 */
	protected String getMessage(Integer errorCode,Object...args){
		if (Validator.isNotNullOrEmpty(errorCode)){
			return getMessage(ErrorCodes.BUSINESS_EXCEPTION_PREFIX + errorCode, args);
		}
		return null;
	}

	/**
	 * 获得消息信息
	 * 
	 * @param code
	 *            code
	 * @param args
	 *            args
	 * @return
	 */
	protected String getMessage(String code,Object...args){
		if (Validator.isNotNullOrEmpty(code)){
			return context.getMessage(code, args, LocaleContextHolder.getLocale());
		}
		return null;
	}

	/**
	 * 获得消息信息
	 * 
	 * @param messageSourceResolvable
	 *            适用于 ObjectError 以及 FieldError
	 * @return
	 */
	protected String getMessage(MessageSourceResolvable messageSourceResolvable){
		return context.getMessage(messageSourceResolvable, LocaleContextHolder.getLocale());
	}
}
