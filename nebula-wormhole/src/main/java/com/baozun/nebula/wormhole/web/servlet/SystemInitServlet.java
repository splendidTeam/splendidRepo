package com.baozun.nebula.wormhole.web.servlet;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.SchedulerManager;
import com.baozun.nebula.model.system.SchedulerTask;
import com.baozun.nebula.sdk.manager.SdkSchedulerTaskManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.wormhole.manager.SchedulerInitManager;
import com.baozun.nebula.zk.WatchControl;

/**
 * 系统初始化时的servlet
 * @author Justin Hu
 *
 */
public class SystemInitServlet extends HttpServlet{
	
	private static final String PROFILE_KEY="spring.profiles.active";
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 4724299124899039939L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		//FIXME
		//ProfileUtil放在servelt中初始化已经迟了，因为wormhole启动时消息监听先于servelt被初始化，此时如果有消息已经在队列中，由于在处理消息是会使用到该类
		//所以mode还未初始化，会造成处理消息出错。建议放在ContextLoaderListener之前初始化ProfileUtil。
		//此处先不删除该方法是因为有的商城还在使用该类
		initProfileUtil(config);
		
		initScheduler(config);
		
		initZookeeper(config);
	}
	
	private void initProfileUtil(ServletConfig config){
		String profileName=config.getServletContext().getInitParameter(PROFILE_KEY);
		ProfileConfigUtil.setMode(profileName);
	}
	
	
	/**
	 * 任务加进来
	 * @param config
	 */
	private void initScheduler(ServletConfig config){
		
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		
		SchedulerInitManager schedulerInitManager=(SchedulerInitManager)applicationContext.getBean("schedulerInitManager");
		
		schedulerInitManager.init();
		
	}
	
	private void initZookeeper(ServletConfig config){
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		
		WatchControl zo=(WatchControl)applicationContext.getBean("watchControl");
		zo.initWatch();
	}
	
}
