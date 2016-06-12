package com.baozun.nebula.web.listener;

import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.curator.watcher.DefaultNodeDataChangedWatcher;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * 系统初始化时的listener
 * 
 * @author D.C
 *
 */
public class NebulaSystemInitListener implements ServletContextListener {

	private static final String PROFILE_KEY = "spring.profiles.active";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		initProfileUtil(sce);
		initZookeeper(sce);
		extendProcess(sce);
	}

	/**
	 * 扩展点
	 * 
	 * @param sce
	 */
	protected void extendProcess(ServletContextEvent sce) {

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	private void initProfileUtil(ServletContextEvent sce) {
		String profileName = sce.getServletContext().getInitParameter(PROFILE_KEY);
		ProfileConfigUtil.setMode(profileName);
	}

	private void initZookeeper(ServletContextEvent sce) {
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext());
		Map<String, DefaultNodeDataChangedWatcher> watchers = applicationContext
				.getBeansOfType(DefaultNodeDataChangedWatcher.class);
		for (DefaultNodeDataChangedWatcher watcher : watchers.values()) {
			try {
				watcher.initListen();
			} catch (Exception e) {
			}
		}
	}
}
