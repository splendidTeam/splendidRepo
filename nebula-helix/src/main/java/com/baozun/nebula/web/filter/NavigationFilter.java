package com.baozun.nebula.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;

import com.baozun.nebula.model.baseinfo.Navigation;
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
	
	

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
		sdkNavigationManager = webApplicationContext.getBean(SdkNavigationManager.class);
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String pathURI = request.getRequestURI();
		Navigation navigation = sdkNavigationManager.findEffectNavigationByUrl(pathURI);
		//如果导航是实际的商品列表导航页面，那需要跳转到导航的列表页，
		if(Validator.isNotNullOrEmpty(navigation) && Navigation.TYPE_ITEM_LIST.equals(navigation.getType())){
			request.getRequestDispatcher("/sys/navigation?navId="+navigation.getId()).forward(request, response);
		}else{
			chain.doFilter(request, response);
		}
		
		
		
		
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	

}
