package com.baozun.nebula.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.manager.cms.SdkCmsInitManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.zk.WatchControl;

/**
 * 系统初始化时的servlet
 * @author Justin Hu
 *
 */
public class SystemInitServlet extends HttpServlet{
	
	private static final String PROFILE_KEY="spring.profiles.active";

	/**
	 * 
	 */
	private static final long serialVersionUID = 4724299124899039939L;

	@Override
	public void init(ServletConfig config) throws ServletException {
			
		initProfileUtil(config);
		initEngine();
		initZookeeper(config);
		initCms(config);
	}
	
	private void initProfileUtil(ServletConfig config){
		String profileName=config.getServletContext().getInitParameter(PROFILE_KEY);
		ProfileConfigUtil.setMode(profileName);
	}

	private void initEngine(){
		//EngineManager engineManager =EngineManager.getInstance();
		//engineManager.build();
	}
	
	private void initZookeeper(ServletConfig config){
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		
		WatchControl zo=(WatchControl)applicationContext.getBean("watchControl");
		zo.initWatch();
	}
	
	private void initCms(ServletConfig config){
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		
		SdkCmsInitManager sim=(SdkCmsInitManager)applicationContext.getBean("sdkCmsInitManager");
		sim.megerCacheUrl();
	}
	
}
