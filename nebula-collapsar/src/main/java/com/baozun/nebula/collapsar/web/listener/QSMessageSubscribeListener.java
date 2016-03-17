package com.baozun.nebula.collapsar.web.listener;

import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.collapsar.thread.config.ThreadConstants;
import com.baozun.nebula.manager.QsOrderCompensateThread;
import com.baozun.nebula.manager.QsOrderRedisThread;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/***
 * 
* @Title: QSMessageSubscribeListener.java 
* @Package com.baozun.nebula.collapsar.web.listener 
* @Description: 初始化配置 监听器
* @author zlh   
* @date 2016-1-16 下午3:01:02 
* @version V1.0
 */
public class QSMessageSubscribeListener implements ServletContextListener{

	/** The Constant log. */
	private static final Logger	log							= LoggerFactory.getLogger(QSMessageSubscribeListener.class);
	
	private static final String PROFILE_KEY = "spring.profiles.active";
	
	/** 初始线程池大小*/
	private static ScheduledExecutorService  service        =  null;

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent){
		final ServletContext servletContext = servletContextEvent.getServletContext();
		servletContext.log("Initialize InitConfigurationListener for application...");
		servletContext.log("Charset.defaultCharset().name():" + Charset.defaultCharset().name());
		initProfileUtil(servletContextEvent);

		log.debug("redis thread start!");
		/***
		 * 1)初始化线程池
		 */
		service = Executors.newScheduledThreadPool(Integer.parseInt(ThreadConstants.QS_POLL_SIZE));
		
		
		/***
		 * 2)开始启动监听队列消息
		 */
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		QsOrderRedisThread qsOrderThread = (QsOrderRedisThread) applicationContext.getBean("redisSubscribeThread");
		/**注:多线程下单  需要注意排队结果 redis排队结果覆盖问题(极小几率)  第一个版本暂时不考虑 */
		//线程池执行  下单操作
		service.scheduleWithFixedDelay(qsOrderThread, ThreadConstants.QS_INIT_FIRSTDALY, ThreadConstants.QS_THREAD_SCHEDULE_TIME, TimeUnit.SECONDS);
		service.scheduleWithFixedDelay(qsOrderThread, ThreadConstants.QS_INIT_FIRSTDALY, ThreadConstants.QS_THREAD_SCHEDULE_TIME, TimeUnit.SECONDS);
		service.scheduleWithFixedDelay(qsOrderThread, ThreadConstants.QS_INIT_FIRSTDALY, ThreadConstants.QS_THREAD_SCHEDULE_TIME, TimeUnit.SECONDS);
		service.scheduleWithFixedDelay(qsOrderThread, ThreadConstants.QS_INIT_FIRSTDALY, ThreadConstants.QS_THREAD_SCHEDULE_TIME, TimeUnit.SECONDS);
		service.scheduleWithFixedDelay(qsOrderThread, ThreadConstants.QS_INIT_FIRSTDALY, ThreadConstants.QS_THREAD_SCHEDULE_TIME, TimeUnit.SECONDS);
		service.scheduleWithFixedDelay(qsOrderThread, ThreadConstants.QS_INIT_FIRSTDALY, ThreadConstants.QS_THREAD_SCHEDULE_TIME, TimeUnit.SECONDS);

	    //补偿机制线程 不能彻底防止 不使用 目前采用排队状态 配合超时时间   由于系统杀掉tomcat进程  导致消息丢失  虽然出现几率很小
		QsOrderCompensateThread compensateThread = (QsOrderCompensateThread) applicationContext.getBean("qsOrderCompensateThread");
		service.scheduleWithFixedDelay(compensateThread, ThreadConstants.QS_INIT_FIRSTDALY, ThreadConstants.QS_THREAD_TEMPDATA_SCHEDULE_TIME, TimeUnit.SECONDS);
	}


	private void initProfileUtil(ServletContextEvent sce){
		String profileName = sce.getServletContext().getInitParameter(PROFILE_KEY);
		ProfileConfigUtil.setMode(profileName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce){
		// currently do nothing
	}
}
