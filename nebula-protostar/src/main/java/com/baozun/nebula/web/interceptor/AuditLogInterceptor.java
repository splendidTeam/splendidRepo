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
package com.baozun.nebula.web.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baozun.nebula.model.system.SysAuditLog;
import com.baozun.nebula.sdk.manager.SdkSysAuditLogManager;
import com.baozun.nebula.web.UserDetails;
import com.feilong.core.CharsetType;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestAttributes;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.spring.web.method.HandlerMethodUtil;
import com.feilong.tools.jsonlib.JsonUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * <pre>
 *  系统审计日志：
 *  用于跟踪用户操作记录
 * </pre>
 * @author xingyu
 *
 */
public class AuditLogInterceptor extends HandlerInterceptorAdapter{

    /** The Constant LOGGER. */
    private static final Logger  LOGGER                = LoggerFactory.getLogger(AuditLogInterceptor.class);
    
    @Autowired
    private SdkSysAuditLogManager sysAuditLogManager;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{

         if (handler instanceof HandlerMethod){
        	
            HandlerMethod handlerMethod = (HandlerMethod) handler;
	        
	        try{
	        	createSysAuditLog(request,response);
	        	
	        	if (LOGGER.isDebugEnabled()){
		            LOGGER.debug(
		                            "[AUDIT_LOG_INFO] RequestInfoMapForLog:{}",
		                            JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));
		        }
		       
	        }catch (Exception e){//可能有异常,比如  往request/model里面设置了 不能被json处理的对象或者字段
	            LOGGER.error(Slf4jUtil.formatMessage(
	                            "[AUDIT_LOG_ERROR] preHandle [{}.{}()] occur exception,but we need goon!,just log it,request info:[{}]",
	                            HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
	                            HandlerMethodUtil.getHandlerMethodName(handlerMethod),
	                            JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request))), e);
	        }
        }
    	
        return true;
    }
  
    /**
     * 保存日志
     * @param request
     * @param response
     */
	private void createSysAuditLog(HttpServletRequest request ,HttpServletResponse response) {
	
    	UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String clientIp = RequestUtil.getClientIp(request);
    	 RequestUtil.getRequestFullURL(request, CharsetType.UTF8);
    	SysAuditLog sysAuditLog = new SysAuditLog();
    	
    	sysAuditLog.setIp(clientIp);
    	sysAuditLog.setException(null);
    	sysAuditLog.setMethod(request.getMethod());
    	sysAuditLog.setOperaterId(user.getUserId());
    	sysAuditLog.setParameters(JsonUtil.format(request.getParameterMap()));
    	sysAuditLog.setResponseCode(String.valueOf(response.getStatus()));
    	sysAuditLog.setUri(getRequestURI(request));
    	sysAuditLog.setCreateTime(new Date());
    	
    	sysAuditLogManager.saveSysAuditLog(sysAuditLog);
	}
	
	/**
	 * 获取URI（某些情况下一个forward()方法的目标servlet可能会需要知道真正原始的request URI）
	 * @param request
	 * @return
	 */
    public static String getRequestURI(HttpServletRequest request){
        String forwardRequestUri = RequestUtil.getAttribute(request, RequestAttributes.FORWARD_REQUEST_URI);
        return Validator.isNotNullOrEmpty(forwardRequestUri) ? forwardRequestUri : request.getRequestURI().toString();
    }

}