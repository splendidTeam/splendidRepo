package com.baozun.nebula.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.command.product.FilterNavigationCommand;
import com.baozun.nebula.manager.navigation.NavigationHelperManager;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;
import com.feilong.core.Validator;

/**
 * 导航的filter，将商品集合的导航跳转到对应的collection中去
 * @author long.xia
 *
 */
public class NavigationFilter implements Filter{
	
	private WebApplicationContext webApplicationContext;
	
	private SdkNavigationManager	sdkNavigationManager;
	
	private NavigationHelperManager		navigationHelperManager;
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		ServletContext servletContext = filterConfig.getServletContext(); 
		 
		 webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext); 
		
		sdkNavigationManager = webApplicationContext.getBean(SdkNavigationManager.class);
		navigationHelperManager = webApplicationContext.getBean(NavigationHelperManager.class);
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String pathURI = request.getRequestURI();
		
		pathURI = pathURI.endsWith("/") ? pathURI.substring(0, pathURI.length()-1) : pathURI;
		
		String	queryStr = request.getQueryString();
		
		FilterNavigationCommand filterNavigation = navigationHelperManager.matchNavigationByUrl(pathURI, queryStr);
		
		//如果导航是实际的商品列表导航页面，那需要跳转到导航的列表页，
		if(Validator.isNotNullOrEmpty(filterNavigation)){
			StringBuffer _urlBuff = new StringBuffer("/sys/navigation?nid="+filterNavigation.getNavId());
			if(Validator.isNotNullOrEmpty(filterNavigation.getCollectionId())){
				_urlBuff.append("&cid=").append(filterNavigation.getCollectionId());
			}
			request.getRequestDispatcher(_urlBuff.toString()).forward(request, response);
		}else{
			chain.doFilter(request, response);
		}
		
		
		
		
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	

}
