/**
 * 
 */
package com.baozun.nebula.web.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.manager.cms.PublishCmsPageInstanceManager;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPageInstanceManagerImpl;



/**
 * @author xianze.zhang
 *@creattime 2013-7-22
 */
public class UrlDispatcherFilter implements Filter{
	private WebApplicationContext webApplicationContext;

	    
	private PublishCmsPageInstanceManager cmsPageInstanceManager;

	private  Log  log = LogFactory.getLog(getClass());
	/**
	 *  支持的类型 0:综合 ,1:pc,2:mobile
	 */
	private  static String  supportType ;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException{
		 ServletContext servletContext = filterConfig.getServletContext(); 
		 webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext); 
		 cmsPageInstanceManager = (PublishCmsPageInstanceManager) webApplicationContext.getBean(PublishCmsPageInstanceManager.class);
		 supportType = filterConfig.getInitParameter("supportType");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException,ServletException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		String path = httpServletRequest.getRequestURI();
		

		Map<String,CmsPageInstance> urlMap= SdkCmsPageInstanceManagerImpl.urlMap;
		String key = path;
		if(supportType != null){
			key = path+"-"+supportType;
		}
		CmsPageInstance pageInstance = urlMap.get(key);
		if(pageInstance == null){
			 pageInstance = urlMap.get(path+"-"+0);
			 log.debug("cms信息未找到:"+"["+key+"]"+"["+path+"-"+0+"]");
		}
        //包含自定义的url
        if(pageInstance != null){
        	
        	if(dealPublish(pageInstance) == false){
        		chain.doFilter(httpServletRequest, httpServletResponse);
        	}else{
        		Map<String,String> result=cmsPageInstanceManager.findPublishPage(pageInstance);
        		
            	request.setAttribute("page", pageInstance);
            	request.setAttribute("data", result.get("data"));
            	request.setAttribute("resource", result.get("resource"));
            	if(result.get("useCommonHeader").equals("true")){
            		log.debug("useCommonHeader:"+path);
            		request.getRequestDispatcher("/pages/commons/cms-page.jsp").forward(request, response);
//            		System.out.println(result.get("data"));
//            		System.out.println("==============================================");
            	}else{
            		log.debug("no-useCommonHeader:"+path);
            		request.getRequestDispatcher("/pages/commons/cms-page-nocommon.jsp").forward(request, response);
//            		System.out.println(result.get("data"));
//            		System.out.println("==============================================");
            	}
            		
        	}
        }
        else{
        	chain.doFilter(httpServletRequest, httpServletResponse);
        }
	}
	
	/**
	* @author 何波
	* @Description: 处理是否到正确的发布时间    
	* void   
	* @throws
	 */
	private boolean dealPublish(CmsPageInstance pageInstance){
		Date stime = pageInstance.getStartTime();
		Date etime = pageInstance.getEndTime();
		
		if(stime !=null && stime.compareTo(new Date()) > 0){
			return false;
		}
		if(etime !=null && etime.compareTo(new Date()) < 0){
			return false;
		}
		
		return true;
		
	}
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy(){
		// TODO Auto-generated method stub
		
	}
	
}
