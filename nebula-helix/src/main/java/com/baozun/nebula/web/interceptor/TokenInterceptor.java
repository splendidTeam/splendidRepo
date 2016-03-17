package com.baozun.nebula.web.interceptor;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baozun.nebula.utils.TokenUtil;
import com.baozun.nebula.web.RequestToken;

public class TokenInterceptor extends HandlerInterceptorAdapter implements
        ServletContextAware {

    private static final Logger log = LoggerFactory
            .getLogger(TokenInterceptor.class);
    private ServletContext servletContext;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
       
        
        if (handler instanceof HandlerMethod) {

            //because all requests will go through here, so only dynamical request
            //should validate this.
            HandlerMethod method = (HandlerMethod) handler;

            RequestToken requestToken = method.getMethodAnnotation(RequestToken.class);
           
            if (requestToken != null){
        		
            	if(requestToken.value().equals(RequestToken.TYPE_MAKE)){
            		TokenUtil.addToken(request);
            	}
            	else{
            		if(!TokenUtil.checkToken(request)){
            			 response.sendError(604);
            		}
            	}
            }
                
        }

        return true;
    }


   

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
