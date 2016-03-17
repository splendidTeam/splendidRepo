package com.baozun.nebula.command.queue;

import java.io.Serializable;
import java.util.Date;


/***
 * 
* @Title: QsOrderCacheCommand.java 
* @Package com.baozun.store.command.queue 
* @Description:  
*             由于采用redis 队列   没有ack机制   并不能保证消息成功消费
*             虽然消息丢失概率非常低  不过也进行一定的处理 
* @author leihao.zhao 
* @date 2016-1-19 下午4:15:21 
* @version V1.0
 */
public class QsOrderTempCommand implements Serializable{

	/**当前时间**/
	private Date   time;

	/**消息**/
	private String message;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
