package com.baozun.nebula.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.collapsar.thread.config.ThreadConstants;
import com.baozun.nebula.command.queue.QsSalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.QsQueueSaleOrderManager;
import com.baozun.nebula.utilities.common.Validator;


/**
 * 
 * 排队  队列服务  暂时只采用单队列下单
 * 后续可采用多队列  采用多队列需要注意缓存的原子性操作
 * @author leihao.zhao
 *     只做为基础版本：
 *        实际使用过程中可以考虑增加
 *        ①队列监控
 *        ②排队开关(可以用来切换是否下单使用排队)
 *        ③防刷功能   对于一个用户频繁企图进入队列,在frontend点击排队处理时增加计数器记录
 *                  用户已经在排队中时仍然企图进入队列并给予比如10次机会,如果频繁进去队列
 *                  在处理下单时如果处理到该用户可将其队列数据放到队列尾部以减少其下单概率.同时如果超过XX次可放弃处理该用户并响应
 *                  给用户暂时无法下单等
 *     后续版本需要自己根据自身商城实际业务进行扩展
 *
 */
@Service("redisSubscribeThread")
public class QsOrderRedisThread extends Thread{
	
	private static final Logger	log							= LoggerFactory.getLogger(QsOrderRedisThread.class);
	
	
	@Autowired
	private CacheManager     redisCacheManager;

//	@Autowired
//	private QsSaleOrderManager  qsSalesOrderManager;
	
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
//			log.info("runing");
		  /**
		   * 
		   * 下单设置排队开关
		   *    方便测试
		   * 
		   * */
//		  QsOrderServiceSwitch qsOrderSwitch = qsSwitchManager.findOrderSwitchData();
//		  Boolean isRuning = this.qsSwitchManager.findOrderSwitchStatusByCache(qsOrderSwitch);
		  log.debug("Thread runing...: "+this.currentThread().getName());
//		  if(isRuning) {
				//队列数据
				while(redisCacheManager.listLen(ThreadConstants.QS_ORDER_QUEUE)>0) {
					//获取队列数据
					String message = redisCacheManager.popListHead(ThreadConstants.QS_ORDER_QUEUE);
					if(Validator.isNotNullOrEmpty(message)) {
						/**
						 * 1)由于redis 轻量级的队列 不具备ack 消费确认 因此自己做补偿机制减少由于  tomcat线程被杀导致的消息丢失
						 *   虽然该种场景非常少但也处理一下
						 *   
						 *   备注:不能彻底消除  因此采用 排队状态+超时时间控制
						 * ***/
						String tempData[] = null;
						try {
							tempData = this.orderManager.putOrderDataToTempPool(message);
						} catch (Exception e) {
							/**异常也继续执行**/ 
							log.error("putOrderDataToTempPool error!",e);
						}
//						
						/**2)调用qs下单流程**/
						QsSalesOrderCommand orderData = this.orderManager.reSerializbleOrderCommand(message);
						orderManager.saveQsOrder(orderData,tempData);
					}
				}
//		   }
		}catch(Exception e) {
			log.error("qs queue create order error!",e);
		}
	}
	
	/***
	 * 反序列化  下单对象
	 *         由于nebula中orderCommand复杂对象较多  并且包含复杂map因此要进行两次
	 *         转换
	 * @param message
	 * @return
	 */
	private QsSalesOrderCommand analyisOrderCommand(String message) {
		QsSalesOrderCommand orderData = null;
		Map<String,Class<?>> classMap = new HashMap<String,Class<?>>();
		classMap = orderManager.analyisOrderCommand(message);
		orderData = this.orderManager.reversalMessageToOrderCommand(classMap, message);
		
		
		
		/***
		 * 对map对象单独解析
		 */
		Map<Long, ShoppingCartCommand>  map = orderData.getShoppingCartCommand().getShoppingCartByShopIdMap();
		Map<String,Class<?>> classMap2 = new HashMap<String,Class<?>>();
		classMap.put(".*",ShoppingCartCommand.class);
		Map<Long, ShoppingCartCommand> map2 = new HashMap<Long, ShoppingCartCommand>();
		map = (Map<Long, ShoppingCartCommand>) JSONObject.toBean(JSONObject.fromObject(map), HashMap.class, classMap);
		orderData.getShoppingCartCommand().setShoppingCartByShopIdMap(map);
		if(Validator.isNotNullOrEmpty(orderData.getShoppingCartCommand())) {
			/**特殊情况由于shoppingCartCommand ShoppingCartLineCommand String 类型saleProperty 数据格式为  [223,444,2499]  影响反序列化  在不影响
			 * nebula 基础类情况下特殊处理
			 * **/
			List<ShoppingCartLineCommand> shoppingCartLineCommands = orderData.getShoppingCartCommand().getShoppingCartLineCommands();
			for(ShoppingCartLineCommand lineCommand : shoppingCartLineCommands) {
				lineCommand.setSaleProperty(Validator.isNotNullOrEmpty(lineCommand.getSaleProperty())?
						"["+lineCommand.getSaleProperty()+"]":"");
			}
		}
		Iterator it = map.keySet().iterator();
		map = orderData.getShoppingCartCommand().getShoppingCartByShopIdMap();
		while(it.hasNext()) {
			String next = (String) it.next();
			ShoppingCartCommand commond = map.get(next);
			/**特殊情况由于shoppingCartCommand ShoppingCartLineCommand String 类型saleProperty 数据格式为  [223,444,2499]  影响反序列化  在不影响
			 * nebula 基础类情况下特殊处理
			 * **/
			List<ShoppingCartLineCommand> shoppingCartLineCommands = commond.getShoppingCartLineCommands();
			for(ShoppingCartLineCommand lineCommand : shoppingCartLineCommands) {
				lineCommand.setSaleProperty(Validator.isNotNullOrEmpty(lineCommand.getSaleProperty())?
						"["+lineCommand.getSaleProperty()+"]":"");
			}
			map2.put(Long.parseLong(next),commond);
		}
		orderData.getShoppingCartCommand().setShoppingCartByShopIdMap(map2);
		return orderData;
	}
}
