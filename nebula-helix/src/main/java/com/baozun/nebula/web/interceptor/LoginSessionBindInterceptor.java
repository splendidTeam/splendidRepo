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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baozun.nebula.web.HelixConfig;
import com.baozun.nebula.web.HelixConstants;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.accessor.session.SessionKeyAccessor;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * 
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
public class LoginSessionBindInterceptor extends HandlerInterceptorAdapter{

    /**  */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginSessionBindInterceptor.class);

    public static final String BIND_CLIENT_INFO = LoginSessionBindInterceptor.class.getName() + "_BIND_CLIENT_INFO";

    /**  */
    private int ajaxErrorCode = 901;

    //---------------------------------------------------------------------

    /**  */
    @Autowired
    @Qualifier("loginBindSessionKeyAccessor")
    private SessionKeyAccessor sessionKeyAccessor;

    /**  */
    @Autowired
    @Qualifier("loginBindCookieAccessor")
    private CookieAccessor cookieAccessor;

    //---------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        if (LOGGER.isDebugEnabled()){
            Map<String, Object> requestInfoMapForLog = RequestUtil.getRequestInfoMapForLog(request);
            LOGGER.debug(JsonUtil.format(requestInfoMapForLog));
        }
        //---------------------------------------------------------------------
        //获取Session中的MemberDetails，该信息是在登录成功后放入会话的，具体可以看 NebulaLoginController中的onAuthenticationSuccess方法
        HttpSession session = request.getSession(false);
        // If there is no session (session == null) then there isn't anything to worry about
        if (null == session){
            LOGGER.debug("session is null,return true");
            return true;
        }

        //---------------------------------------------------------------------
        MemberDetails memberDetails = (MemberDetails) session.getAttribute(SessionKeyConstants.MEMBER_CONTEXT);
        if (null == memberDetails){ //没有会员信息 通过
            LOGGER.debug(" no find memberDetails in session,return true");
            return true;
        }

        //---------------------------------------------------------------------

        ClientInfo clientInfo = sessionKeyAccessor.get(BIND_CLIENT_INFO, request);
        if (null == clientInfo){
            LOGGER.error("clientInfo is null,abortUser && onMemberCheckFailure, return false");
            return goToError(request, response);
        }

        //---------------------------------------------------------------------
        String headerUserAgent = RequestUtil.getHeaderUserAgent(request);
        if (isNullOrEmpty(headerUserAgent)){
            LOGGER.error("headerUserAgent is null or empty,will abortUser && onMemberCheckFailure, return false");
            return goToError(request, response);
        }

        //---------------------------------------------------------------------
        String sessionUserAgent = clientInfo.getUserAgent();

        if (!headerUserAgent.equals(sessionUserAgent)){//没值 或者被删除了   
            LOGGER.error("sessionUserAgent:[{}] not eqauls headerUserAgent:[{}],will abortUser && onMemberCheckFailure, return false", sessionUserAgent, headerUserAgent);
            return goToError(request, response);
        }

        //---------------------------------------------------------------------

        //session中绑定的值
        String sessionBindValue = clientInfo.getFingerPrint();
        if (isNullOrEmpty(sessionBindValue)){//没值 或者被删除了   
            LOGGER.error("sessionBindValue is null or empty,will abortUser && onMemberCheckFailure, return false");
            return goToError(request, response);
        }

        //---------------------------------------------------------------------
        String cookieBindValue = cookieAccessor.get(request);
        if (isNullOrEmpty(cookieBindValue)){//没值 或者被删除了
            LOGGER.error("cookieBindValue is null or empty,will abortUser && onMemberCheckFailure, return false");
            return goToError(request, response);
        }

        //---------------------------------------------------------------------
        if (!sessionBindValue.equals(cookieBindValue)){
            LOGGER.error("sessionBindValue :[{}],not equals cookieBindValue:[{}],will abortUser && onMemberCheckFailure,return false", sessionBindValue, cookieBindValue);
            return goToError(request, response);
        }

        LOGGER.debug("sessionBindValue :[{}],equals cookieBindValue,return [true]", sessionBindValue);
        return true;
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @since 5.3.2.18
     */
    private boolean goToError(HttpServletRequest request,HttpServletResponse response) throws IOException{
        abortUser(request, response);
        onMemberCheckFailure(request, response);
        return false;
    }

    /**
     * 用户会话检查失败后的方法，可重载，默认是去往会话检查失败的URL.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void onMemberCheckFailure(HttpServletRequest request,HttpServletResponse response) throws IOException{
        if (RequestUtil.isNotAjaxRequest(request)){
            response.sendRedirect(request.getContextPath() + HelixConfig.getInstance().get(HelixConstants.SECURITY_SESSION_CHECK_FAILURE_URL));
        }else{
            response.sendError(ajaxErrorCode);
        }
    }

    /**
     * 
     *
     * @param request
     * @param response
     */
    private void abortUser(HttpServletRequest request,HttpServletResponse response){
        cookieAccessor.remove(response);
        request.getSession().invalidate();
    }

}
