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
package com.baozun.nebula.web.interceptor;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.baozun.nebula.manager.member.MemberStatusFlowProcessor;
import com.baozun.nebula.web.HelixConfig;
import com.baozun.nebula.web.HelixConstants;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.NeedLogin;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.feilong.core.Validator;

/**
 * 新版的用户信息拦截器，用于拦截需要登录的请求，并跳转
 * 对于NeedLogin(guest=true)的情况，当会话中 GUEST_ENTER_ONCE 被设置为true时允许透传过去，此时会话memberDetails为空
 * 对于NeedLogin的情况，即使会话中 GUEST_ENTER_ONCE 被设置为true，也不会透传过去
 * 游客标识 GUEST_ENTER_ONCE 在校验后会被删除
 * 
 * SecureSessionSignatureHandler 是用于混合模式下保证会员会话安全的处理器
 * LoginForwardHandler 是用于登录后地址跳转控制的控制器
 * 这两个都可以被重载后注入
 * 
 * @author Benjamin.Liu
 *
 */
public class MemberDetailsInterceptor extends HandlerInterceptorAdapter implements ServletContextAware{

	private static final Logger LOG = LoggerFactory.getLogger(MemberDetailsInterceptor.class);
	
	public static final String GUEST_ENTER_ONCE = "login.guestAllowed";
	
	@Autowired
	@Qualifier("secureSessionSignatureHandler")
	private SecureSessionSignatureHandler secureSessionSignatureHandler;
	
	@Autowired
	@Qualifier("loginForwardHandler")
	private LoginForwardHandler loginForwardHandler;
	
	@Autowired
	private MemberStatusFlowProcessor memberStatusFlowProcessor;
	
	private ServletContext servletContext;
	
	private int ajaxErrorCode = 900;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
       
        if (handler instanceof HandlerMethod) {

            HandlerMethod method = (HandlerMethod) handler;         
            NeedLogin needLogin = method.getMethodAnnotation(NeedLogin.class);
            if (needLogin == null) {
                needLogin = method.getMethod().getDeclaringClass().getAnnotation(NeedLogin.class);
            }
            if (needLogin != null){
            	LOG.debug("Request need Login: {}", request.getRequestURI());
                return doValidateNeedLogin(request, response, needLogin);
            }else {
            	LOG.debug("Request don't need Login: {}", request.getRequestURI());
            }
           
        }

        return true;
    }
	
	private String getReturnURL(HttpServletRequest request, NeedLogin needLogin){
		if(StringUtils.isEmpty(needLogin.returnUrl())){
			String url = new UrlPathHelper().getPathWithinApplication(request);
			String queryParam = request.getQueryString();
			if (StringUtils.isNotBlank(queryParam)) {
				url = url + "?" + queryParam;
			}
			return url;
		}else{
			LOG.debug("Return URL is found in NeedLogin");
			return needLogin.returnUrl();
		}
	}

	protected boolean doValidateNeedLogin(HttpServletRequest request, HttpServletResponse response, 
			NeedLogin needLogin) throws IOException {
		//获取Session中的MemberDetails，该信息是在登录成功后放入会话的，具体可以看
		//NebulaLoginController中的onAuthenticationSuccess方法
		MemberDetails memberDetails = (MemberDetails) request.getSession().getAttribute(SessionKeyConstants.MEMBER_CONTEXT);
    	
		Boolean guestAllowed = (Boolean) request.getSession().getAttribute(GUEST_ENTER_ONCE);
		if(memberDetails == null){
			if(Boolean.TRUE.equals(guestAllowed) && needLogin.guest()){
				LOG.debug("Guest action for next step: {}", request.getRequestURI());
				//删除游客标志
				
				//2016-6-2 18:26  由于订单确认页面以及创建订单ajax事件 都会标识 @NeedLogin(guest=true),会出现频繁的登陆以及异常,为了最小改动,和刘总讨论了, 这行去掉
				//request.getSession().removeAttribute(GUEST_ENTER_ONCE);
				return true;
			}else{
				LOG.info("No Login information found, need re-login");
				String returnURL = getReturnURL(request, needLogin);
				LOG.info("Current Request URL [{}] will be used for return URL after success login", returnURL);
				
				LOG.debug("Enter Login Page");
				interceptLogin(request, response, returnURL);
				return false;
			}			
		}else{
			//会员已经登录，需要检查当前会话是否安全
			if(LOG.isDebugEnabled()){
				LOG.debug("Active Member {} is found for next action.", memberDetails.getLoginName());
				LOG.debug("Session need be validated before next action");
			}
			if(secureSessionSignatureHandler.CheckSignature(request, response)){
				LOG.debug("Session is safe");
				
				//登录用户的状态流转
				return doMemberStatusFlowProcess(memberDetails, request, response);
			}else{
				LOG.info("Session Signature check is not passed.");
				secureSessionSignatureHandler.deleteSignature(response);
				onMemberCheckFailure(request, response);
				return false;
			}
		}
	}
	
	/**
	 * 跳转到登录的方法，可重载，默认是到达登录页面，并记录登录成功后的跳转地址
	 * @param request
	 * @param response
	 * @param returnURL
	 * @throws IOException
	 */
	protected void interceptLogin(HttpServletRequest request, HttpServletResponse response, String returnURL) throws IOException {
		if (request.getHeader("X-Requested-With") == null) {    
			//注意在LoginController中需要使用loginForwardHandler来重定向
			loginForwardHandler.setForwardURL(request, returnURL);
            response.sendRedirect(request.getContextPath() + HelixConfig.getInstance().get(HelixConstants.SITE_LOGIN_URL));
        } else {               	
            response.sendError(ajaxErrorCode);
        }
	}
	
	/**
	 * 用户会话检查失败后的方法，可重载，默认是去往会话检查失败的URL
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void onMemberCheckFailure(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if (request.getHeader("X-Requested-With") == null) {               	
            response.sendRedirect(request.getContextPath() + HelixConfig.getInstance().get(HelixConstants.SECURITY_SESSION_CHECK_FAILURE_URL));
        } else {               	
            response.sendError(ajaxErrorCode);
        }
	}
	
	/**
	 * 执行会员状态流转器
	 * @param memberDetails
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	protected boolean  doMemberStatusFlowProcess(MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response) throws IOException {
		LOG.debug("do member status flow processor");
		String action=memberStatusFlowProcessor.process(memberDetails,request);
		if(Validator.isNotNullOrEmpty(action)){
			LOG.info("[MEMBER_STATUS_FLOW_PROCESSOR]  memberId:{} ,status{} ,action{}",memberDetails.getMemberId(),memberDetails.getStatus().toArray(),action);
			if(request.getHeader("X-Requested-With") == null){
				response.sendRedirect(request.getContextPath()+action);
			}else{
				response.sendError(ajaxErrorCode);
			}
			return false;
		}
		return true;
	}

	public int getAjaxErrorCode() {
		return ajaxErrorCode;
	}

	public void setAjaxErrorCode(int ajaxErrorCode) {
		this.ajaxErrorCode = ajaxErrorCode;
	}
}
