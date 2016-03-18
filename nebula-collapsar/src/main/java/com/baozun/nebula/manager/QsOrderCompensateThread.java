package com.baozun.nebula.manager;

import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.queue.QsOrderTempCommand;
import com.baozun.nebula.command.queue.QsSalesOrderCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.QsQueueSaleOrderManager;
import com.feilong.core.date.DateUtil;
import com.feilong.core.tools.jsonlib.JsonUtil;
import com.feilong.core.util.Validator;


/***
 * 
 * 补偿机制线程  一定程度防止由于系统杀掉tomcat进程  导致消息丢失  虽然出现几率很小
 * @author zlh
 *
 */
@Service("qsOrderCompensateThread")
public class QsOrderCompensateThread extends Thread{
	
	private static final Logger	log							= LoggerFactory.getLogger(QsOrderCompensateThread.class);
	
	
	@Autowired
	private CacheManager     redisCacheManager;

	@Autowired
	private QsQueueSaleOrderManager orderManager;
	
	/**
	 * 队列任务
	 */
	public void run() {
		/**注意异常捕获
		 * JDK desription: If any execution of the task encounters an exception, subsequent executions are suppressed.Otherwise, 
		 * the task will only terminate via cancellation or termination of the executor.
		 * ***/
		try{
			Set<String> temps = this.redisCacheManager.findSetAll(Constants.QS_ORDER_TEMP_POOL);
			if(Validator.isNotNullOrEmpty(temps)) {
				/**判断是否需要重新进入队列**/
				for(String temp : temps) {
					QsOrderTempCommand command = JsonUtil.toBean(temp, QsOrderTempCommand.class);
					if(Validator.isNotNullOrEmpty(command)) {
						Date createTime = command.getTime();
						Date expireTime = DateUtil.addMinute(createTime, 5);
						String message = command.getMessage();
						/**消息进入5分钟  还未消费完成**/
						if(DateUtil.isBefore(expireTime,new Date() ) && Validator.isNotNullOrEmpty(message)) {
							QsSalesOrderCommand orderData = this.orderManager.reSerializbleOrderCommand(message);
							Long memberId = orderData.getMemberId();
							String extentionCode = orderData.getShoppingCartCommand().getShoppingCartLineCommands().get(0).getExtentionCode();
							if(Validator.isNotNullOrEmpty(memberId) && Validator.isNotNullOrEmpty(extentionCode)) {
								String key = this.orderManager.getUserQsRid(memberId, extentionCode);
								if(Validator.isNotNullOrEmpty(key)) {
									//清除 rid 缓存
									this.redisCacheManager.remove(key);
								}
							}
							/**移除当前message**/
							String array[] = {temp};
							this.redisCacheManager.removeFromSet(Constants.QS_ORDER_TEMP_POOL, array);
//							this.redisCacheManager.pushToListFooter(ThreadConstants.QS_ORDER_QUEUE, message);	
						}
					}
				}
			}
		}catch(Exception e) {
			log.error("QsOrderCompensateThread error!",e);
		}
	}
}
