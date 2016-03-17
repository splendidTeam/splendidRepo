package com.baozun.nebula.collapsar.subHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPubSub;

/**
 * 
* @Title: QsRedisSubMessageHandler.java 
* @Package com.jumbo.shop.manager.subHandler 
* @Description: 订阅模式下单   暂时不采用该种模式
*                    订阅模式缺点，如果监听端不开启则数据将会丢失 
* @author zlh   
* @date 2015-4-20 下午6:23:49 
* @version V1.0
 */
@Deprecated
@Service("qsRedisSubMessageHandler")
public class QsRedisSubMessageHandler extends JedisPubSub{


	private static final Logger	log							= LoggerFactory.getLogger(QsRedisSubMessageHandler.class);
	
	/**
	 * 取得订阅的消息后的处理
	 */
	@Override
	public void onMessage(String channel, String message) {
		try{
			log.info("recive message: "+message);
		}catch(Exception e) {
			log.error("redis sub message error!",e);
		}
	}

	/**
	 * 初始化按表达式的方式订阅时候的处理  
	 */
	@Override
	public void onPMessage(String pattern, String channel, String message) {
		// TODO Auto-generated method stub
		
	}

	/**
	 *   初始化订阅时候的处理  
	 */
	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 取消订阅时候的处理  
	 */
	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 取消按表达式的方式订阅时候的处理 
	 */
	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 初始化按表达式的方式订阅时候的处理
	 */
	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}
}
