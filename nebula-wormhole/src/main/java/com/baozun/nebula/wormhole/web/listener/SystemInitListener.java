package com.baozun.nebula.wormhole.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * 系统初始化时的listener
 * @author Justin Hu
 *
 */
public class SystemInitListener implements ServletContextListener {
	
	private static final String PROFILE_KEY="spring.profiles.active";
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		initProfileUtil(sce);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	private void initProfileUtil(ServletContextEvent sce){
		String profileName = sce.getServletContext().getInitParameter(PROFILE_KEY);
		ProfileConfigUtil.setMode(profileName);
	}
	
}
