package com.baozun.nebula.web.interceptor;


import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baozun.nebula.utils.CookieUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.NeedLogin;
import com.baozun.nebula.web.NoClientCache;
import com.baozun.nebula.web.constants.CommonUrlConstants;
import com.baozun.nebula.web.constants.CookieKeyConstants;
import com.baozun.nebula.web.constants.MemberCookieLoginTypeConstatns;
import com.baozun.nebula.web.constants.SessionKeyConstants;

public class NoClientCacheInterceptor extends HandlerInterceptorAdapter implements
        ServletContextAware {

    private static final Logger log = LoggerFactory
            .getLogger(NoClientCacheInterceptor.class);
    private ServletContext servletContext;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
       
        
        if (handler instanceof HandlerMethod) {

            //because all requests will go through here, so only dynamical request
            //should validate this.
            HandlerMethod method = (HandlerMethod) handler;

            NoClientCache noClientCache = method.getMethodAnnotation(NoClientCache.class);
            if (noClientCache == null) {
            	noClientCache = method.getMethod().getDeclaringClass().getAnnotation(NoClientCache.class);
            }
            if (noClientCache != null){
        		//Http 1.0 header
        		response.setDateHeader("Expires", 0);
        		response.addHeader("Pragma", "no-cache");
        		//Http 1.1 header
        		response.setHeader("Cache-Control", "no-cache");
            }
                
        }

        return true;
    }


   

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
