package com.baozun.nebula.web.listener;

import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.curator.watcher.DefaultNodeDataChangedWatcher;
import com.baozun.nebula.manager.cms.SdkCmsInitManager;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.utilities.library.address.AddressUtil;

/**
 * 系统初始化时的listener
 * 
 * @author D.C
 *
 */
public class NebulaSystemInitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		initZookeeper(sce);
		initCms(sce);
		initUrl(sce);
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

	private void initCms(ServletContextEvent sce) {
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext());

		SdkCmsInitManager sim = (SdkCmsInitManager) applicationContext.getBean("sdkCmsInitManager");
		sim.megerCacheUrl();
	}
	
	private void initUrl(ServletContextEvent sce){
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext());

		SdkI18nLangManager sdkI18nLangManager = (SdkI18nLangManager) applicationContext.getBean("sdkI18nLangManager");
		// 设置要加载的地址信息
		AddressUtil.setI18nOffOn(LangProperty.getI18nOnOff());
		AddressUtil.setLanguageList(sdkI18nLangManager.getAllEnabledI18nKeyList());
		AddressUtil.init();
	}
	
	
	

}
