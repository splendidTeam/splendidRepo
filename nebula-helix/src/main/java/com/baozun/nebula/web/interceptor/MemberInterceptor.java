package com.baozun.nebula.web.interceptor;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utils.http.HttpsLoginUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.NeedLogin;
import com.baozun.nebula.web.constants.CommonUrlConstants;
import com.baozun.nebula.web.constants.CookieKeyConstants;
import com.baozun.nebula.web.constants.ImgConstants;
import com.baozun.nebula.web.constants.MemberCookieLoginTypeConstatns;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.feilong.servlet.http.CookieUtil;

public class MemberInterceptor extends HandlerInterceptorAdapter implements
        ServletContextAware {

    private static final Logger log = LoggerFactory
            .getLogger(MemberInterceptor.class);
    private ServletContext servletContext;

    @Value("#{meta['upload.img.domain.base']}")
    private String imgBase;
    
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
       
        //ip取国家信息弹出访问提示
        //countryLookUp(request,response);
        if (handler instanceof HandlerMethod) {

            //because all requests will go through here, so only dynamical request
            //should validate this.
            HandlerMethod method = (HandlerMethod) handler;

            NeedLogin needLogin = method.getMethodAnnotation(NeedLogin.class);
            if (needLogin == null) {
                needLogin = method.getMethod().getDeclaringClass().getAnnotation(NeedLogin.class);
            }
            if (needLogin != null)
                return doValidateNeedLogin(request, response, needLogin);
            
            request.setAttribute("imgBaseReq", ImgConstants.IMG_BASE);
            request.setAttribute("defaultImgReq", ImgConstants.DEFAULT_IMG_URL);
            
        }

        return true;
    }


    /**
     * 处理 needLogin
     *
     * @return
     * @throws java.io.IOException
     */
    private boolean doValidateNeedLogin(HttpServletRequest request, HttpServletResponse response, NeedLogin needLogin) throws IOException {
    	
    	
    	MemberDetails userDetails = (MemberDetails) request.getSession().getAttribute(SessionKeyConstants.MEMBER_CONTEXT);
    	
    	if(userDetails==null){ //会员未登录
    		
    		
    		if(needLogin.guest()){//如方法中表示支持游客登录
    			
	    		Cookie mt=CookieUtil.getCookie(request, CookieKeyConstants.MEMBER_COOKIE_LOGIN_STATUS);
	    		//游客已登录
	    		if(mt!=null&&MemberCookieLoginTypeConstatns.TYPE_GUEST.equals(mt.getValue())){
	    			
	    			return true;
	    			
	    		}
	    			    		
    		}
    			    		
	        //游客也没有登录
	        //会员没有登录
        	 String uri = null;
        	 String contextPath = request.getContextPath();
        	 
        	 //如果returnUrl为null,则表示使用当前url做为跳回地址
        	 if(StringUtils.isBlank(needLogin.returnUrl())){
	        	 uri=request.getRequestURI();
	             String queryParam = request.getQueryString();
	             if (StringUtils.isNotBlank(queryParam)) {
	                    uri = uri + "?" + queryParam;
	             }
	    
	             if (StringUtils.isNotBlank(contextPath) && !"/".equals(contextPath)) {
	                    uri = uri.replaceFirst(contextPath, "");
	             }
        	 }
        	 else{
        		 uri=needLogin.returnUrl();
        	 }

             log.info("Cached request uri: [" + uri + "].");

             //将对当前uri地址加密后做为来路url传递到登录页面
             String loginUrl=contextPath + CommonUrlConstants.LOGIN_URL;
             
             Cookie memberLoginCk=CookieUtil.getCookie(request, CookieKeyConstants.MEMBER_COOKIE_LOGIN_STATUS);
             
            //登录状态不会为，并且是会员登录状态，则删除cookie
            if(memberLoginCk!=null&&memberLoginCk.getValue().equals(CookieKeyConstants.MEMBER_LOGIN_STATUS)){
            	CookieUtil.deleteCookie(CookieKeyConstants.MEMBER_COOKIE_LOGIN_STATUS, response);
            }		 
            		 
             //非ajax的请求
             //才需要保存iback_url做为登录后的跳转地址
             if (request.getHeader("X-Requested-With") == null) {
                	request.getSession().setAttribute(SessionKeyConstants.MEMBER_IBACK_URL, URLEncoder.encode(uri, "UTF-8"));
                    response.sendRedirect(contextPath + loginUrl);
             } else {
                	
                    response.sendError(900);
             }

             return false;
    		
    		
    	}else{					//会员已登录需要判数https的签名是否正确
    		
    		String scheme=request.getHeader("scheme");
    		
    		if(scheme!=null&&scheme.endsWith("https")){  //当前为https访问模式
    			
    			String sign=CookieUtil.getCookieValue(request, CookieKeyConstants.HTTPS_SIGN);
    			
    			Properties properties=ProfileConfigUtil.findPro("config/metainfo.properties");
    			
    			String secret=properties.getProperty("https.secret", "defaultsecret");
    			
    			String s=HttpsLoginUtil.makeSign(request.getSession().getId(), String.valueOf(userDetails.getMemberId()), secret);
    			
    			if(s.equals(sign)){	//签名是正确的，则通过验证
    				return true;
    			}
    			else{					//签名是错误的，则删除签名，并返回未登录状态
    				CookieUtil.deleteCookie(CookieKeyConstants.HTTPS_SIGN, response);
    				
    				 if (request.getHeader("X-Requested-With") == null) {
    	                	
    	                    response.sendRedirect(request.getContextPath() + CommonUrlConstants.LOGIN_URL);
    	             } else {
    	                	
    	                    response.sendError(900);
    	             }
    				
    				return false;
    			}
    		}
    		
    		return true;
    		
    	}
    	
   
        
    }



    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
