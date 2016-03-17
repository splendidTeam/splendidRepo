package com.baozun.nebula.web.interceptor;


import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baozun.nebula.manager.cms.CmsPageInstanceManager;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceManager;

public class CmsUrlInterceptor extends HandlerInterceptorAdapter implements
        ServletContextAware {

    private static final Logger log = LoggerFactory
            .getLogger(CmsUrlInterceptor.class);
    private ServletContext servletContext;
    
    @Autowired
	private SdkCmsPageInstanceManager sdkCmsPageInstanceManager;
    
    @Autowired
    private CmsPageInstanceManager cmsPageInstanceManager;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
       
        
        if (handler instanceof HandlerMethod) {

            //because all requests will go through here, so only dynamical request
            //should validate this.
         


            Map<String,CmsPageInstance> urlMap=sdkCmsPageInstanceManager.findUrlMap();
            //包含自定义的url
            if(urlMap.containsKey(request.getRequestURI())){
            	
            	CmsPageInstance pageInstance=urlMap.get(request.getRequestURI());
            	
            	Map<String,String> result=cmsPageInstanceManager.findPublishPage(pageInstance);
            	
            	request.setAttribute("page", pageInstance);
            	request.setAttribute("data", result.get("data"));
            	if(result.get("useCommonHeader").equals("true"))
            		request.getRequestDispatcher("/pages/commons/cms-page.jsp").forward(request, response);
            	else
            		request.getRequestDispatcher("/pages/commons/cms-page-nocommon.jsp").forward(request, response);
            	return false;
            	
            }
            
                
        }

        return true;
    }


   

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
