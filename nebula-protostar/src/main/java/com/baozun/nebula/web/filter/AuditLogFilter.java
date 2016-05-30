/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.web.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.baozun.nebula.model.system.SysAuditLog;
import com.baozun.nebula.sdk.manager.SdkSysAuditLogManager;
import com.baozun.nebula.web.UserDetails;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestAttributes;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 系统审计日志，用于追踪用户操作记录
 * 
 * @author xingyu
 * 
 */
public class AuditLogFilter extends OncePerRequestFilter {

	   /** The Constant LOGGER. */
    private static final Logger  LOGGER                = LoggerFactory.getLogger(OncePerRequestFilter.class);
    
    private SdkSysAuditLogManager sysAuditLogManager;

	/**
	 * Check for a multipart request via this filter's MultipartResolver, and
	 * wrap the original request with a MultipartHttpServletRequest if
	 * appropriate.
	 * <p>
	 * All later elements in the filter chain, most importantly servlets,
	 * benefit from proper parameter extraction in the multipart case, and are
	 * able to cast to MultipartHttpServletRequest if they need to.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		 try{
			    filterChain.doFilter(request, response);
			    
				if(validateSuffix(getRequestURI(request))){
					createSysAuditLog(request,response);
		        	if (LOGGER.isDebugEnabled()){
			            LOGGER.debug(
			                            "[AUDIT_LOG_INFO] RequestInfoMapForLog:{}",
			                            JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));
			        }
		        	
				}
		       
	        }catch (Exception e){//可能有异常,比如  往request/model里面设置了 不能被json处理的对象或者字段
	            LOGGER.error(Slf4jUtil.formatMessage(
	                            "[AUDIT_LOG_ERROR] AuditLogFilter occur exception,but we need goon!,just log it,request info:[{}]",
	                            JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request))), e);
	        }
		
	}
	
	/**
     * 保存系统日志
     * @param request
     * @param response
     */
	private void createSysAuditLog(HttpServletRequest request ,HttpServletResponse response ) {
		
		SecurityContext securityContext = (SecurityContext)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
    	
    	SysAuditLog sysAuditLog = new SysAuditLog();
    	
    	sysAuditLog.setIp(RequestUtil.getClientIp(request));
    	
    	String exception = "";
    	Object ex = request.getAttribute("exception");
    	if(ex!=null){
    		if(ex.toString().length()>1000){
    		    exception = ex.toString().substring(0, 1000);
    		}else{
    			exception = ex.toString();
    		}
    	}
    	
    	if(securityContext!=null){
    		UserDetails user = (UserDetails)securityContext.getAuthentication().getPrincipal();
    		sysAuditLog.setOperaterId(user!=null?user.getUserId():null);
    	}
    	
    	sysAuditLog.setException(exception);
    	sysAuditLog.setMethod(request.getMethod());
    	sysAuditLog.setParameters(JsonUtil.format(request.getParameterMap()));
    	sysAuditLog.setResponseCode(String.valueOf(response.getStatus()));
    	sysAuditLog.setUri(getRequestURI(request));
    	sysAuditLog.setCreateTime(new Date());
    	
    	ServletContext servletContext = request.getSession().getServletContext(); 
    	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    	sysAuditLogManager = (SdkSysAuditLogManager)ctx.getBean("sdkSysAuditLogManager");
    	
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
    
	/**
	 *  过滤css js ，
	 *  过滤图片 jpg、png、ico、bmp、gif、tif、pcx、tga 
	 * @return
	 */
	private boolean validateSuffix(String uri){
		
		if(StringUtils.isBlank(uri))
				return false;
		
		if(uri.contains(".css") || uri.contains(".js")
				        || uri.contains(".jpg") || uri.contains(".png")
						|| uri.contains(".ico") || uri.contains(".bmp")
						|| uri.contains(".gif") || uri.contains(".tif")
						|| uri.contains(".pcx") || uri.contains(".tga")
						)
			return false;
		return true;
		
	}
}
