package com.baozun.nebula.command.queue;

import java.io.Serializable;
import java.util.Map;


/***
 * 
* @Title: QsOrderCacheCommand.java 
* @Package com.baozun.store.command.queue 
* @Description: 排队立即购买缓存对象  
*               对象会记录用户购买的qs商品以及尺码并记录当前状态，
*               如果当前对象为新建状态则跳转则结算页面，如果当前为
*               排队中状态则进入派对页面。当用户生成订单后清楚缓存对象
* @author leihao.zhao 
* @date 2016-1-19 下午4:15:21 
* @version V1.0
 */
public class QsOrderCacheCommand implements Serializable{

	private static final long serialVersionUID = -1992842747013401201L;

	private String queueId;
	
//    private String orderCode;
    
    private Boolean isSuccess;
    
    private String errorMessage;
    
    /**封装的支付参数等*/
    private Map<String, Object> data;
    
    private Boolean clearCache;
    
    /**有些业务异常可能有额外参数**/
    private Object args[];

	public String getQueueId() {
		return queueId;
	}

	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}

//	public String getOrderCode() {
//		return orderCode;
//	}
//
//	public void setOrderCode(String orderCode) {
//		this.orderCode = orderCode;
//	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public Boolean getClearCache(){
		return clearCache;
	}
	
	public void setClearCache(Boolean clearCache){
		this.clearCache = clearCache;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}
