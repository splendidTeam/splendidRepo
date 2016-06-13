package com.baozun.nebula.web.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.curator.watcher.IWatcher;
import com.baozun.nebula.curator.watcher.ZkWatcherControl;

/**
 * 系统初始化时的listener
 * 
 * @author D.C
 *
 */
public class NebulaSystemInitListener implements ServletContextListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(NebulaSystemInitListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		initZkWatcher(sce);
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

	/**
	 * 初使化zookeeper watcher
	 * @param sce
	 */
	private void initZkWatcher(ServletContextEvent sce) {
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext());
		
		ZkWatcherControl zkWatcherControl = applicationContext.getBean(ZkWatcherControl.class);
		
		List<IWatcher> watchers = zkWatcherControl.getWatchers();
		if(watchers != null) {
			for(IWatcher watcher : watchers) {
				try {
					watcher.initListen();
				} catch (Exception e) {
					LOG.error("[zookeeper] Zookeeper watcher init error!", e);
				}
			}
		}
		
	}
}
