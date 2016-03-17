package com.baozun.nebula.collapsar.thread.config;

import java.util.Properties;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * 
* @Title: ThreadConstants.java 
* @Package com.jumbo.shop.thread.config 
* @Description: 线程池相关配置 
* @author zlh   
* @date 2016-01-16 下午12:50:12 
* @version V1.0
 */
public class ThreadConstants {

	/**
	 * 线程池相关配置
	 */
	 public static Properties     qsthread                = ProfileConfigUtil.findPro("config/qsthread.properties");
	 
	 /**线程池大小 */
	 public static final String  QS_POLL_SIZE             = qsthread.getProperty("thread.poolsize");
	 
	 /**队列消耗线程执行频率**/
	 public static final Integer  QS_THREAD_SCHEDULE_TIME  = Integer.parseInt(qsthread.getProperty("thread.scheduletime"));
	 
	 /**补偿线程执行频率**/
	 public static final Integer  QS_THREAD_TEMPDATA_SCHEDULE_TIME  = Integer.parseInt(qsthread.getProperty("thread.temp.scheduletime"));
	 
	 /**线程池创建后  第一次执行的延时时间 */
	 public static final Integer  QS_INIT_FIRSTDALY       = 1;
	 
	 /**下单队列名称  需要和frontend mobile一致  也可以自行定义多个队列**/
	 public static final String   QS_ORDER_QUEUE          = qsthread.getProperty("thread.queue.name");
}
