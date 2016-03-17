package com.baozun.nebula.sdk.manager;

import java.util.Map;

import com.baozun.nebula.command.queue.QsSalesOrderCommand;
import com.baozun.nebula.manager.BaseManager;

/***
 * 
* @Title: QsSaleOrderManager.java 
* @Package com.baozun.store.manager.saleorder 
* @Description: 用来处理QS流程相关业务 
* @author leihao.zhao 
* @date 2016-1-18 下午7:38:07 
* @version V1.0
 */
public interface QsQueueSaleOrderManager extends BaseManager{

	 
	/***
	 * 处理QS排队
	 *     加入队列后续下单逻辑由nebula-collapsar处理
	 * @param shoppingCartCommand
	 * @param salesOrderCommand
	 * @param memCombo
	 */
	Map<String, Object> pushQsOrderToQueue(QsSalesOrderCommand salesOrderCommand) throws Exception;
	
	
	
	/***
	 * qs订单
	 * @param qsCommand
	 */
	public void saveQsOrder(QsSalesOrderCommand qsCommand,String[] tempData);
	
	
	
	/***
	 * 获取用户是否30分钟排队未响应
	 * @param qId
	 * @param memberId
	 * @return
	 */
	Boolean dealQueueIsTimeOut(String qId,Long memberId);
	
	
	
	
	/****
	 * 判断用户是否在排队状态 防止用户重复排队
	 * 
	 * @param memberId
	 * @param upc
	 * @return
	 */
	Boolean getCurrentQueueState(Long memberId, String upc);
	
	
	
	
	/**
	 * 获取qid
	 * 
	 * @param memberId
	 * @param upc
	 * @return
	 */
	String getUserQsQid(Long memberId, String upc);
	
	
	
	
	/****
	 * 反序列化订单对象
	 * @param message
	 * @return
	 */
	Map<String,Class<?>> analyisOrderCommand(String message);
	
	
	/***
	 * 排队状态对象
	 * @param memberId
	 * @param upc
	 * @return
	 */
	public String getUserQsRid(Long memberId, String upc);
	
	/***
	 * 反序列化对象
	 * @param classMap
	 * @param message
	 * @return
	 */
	QsSalesOrderCommand reversalMessageToOrderCommand(Map<String,Class<?>> classMap,String message);
	
	
	/***
	 * 队列中反序列化对象
	 * @param message
	 * @return
	 */
	QsSalesOrderCommand reSerializbleOrderCommand(String message);
	
	
	/***
	 * 补偿机制   尽量防止消息丢失
	 */
	String[] putOrderDataToTempPool(String message);
}
