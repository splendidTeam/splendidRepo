package com.baozun.nebula.command.queue;

import java.io.Serializable;

/**
 * 
* @Title: QsMemberCacheCommand.java 
* @Package com.baozun.store.command.queue 
* @Description:  
 *     排队立即购买缓存对象
 *     对象会记录用户购买的qs商品以及尺码并记录当前状态，
 *     如果当前对象为新建状态则跳转则结算页面，如果当前为
 *     排队中状态则进入派对页面。当用户生成订单后清楚缓存对象。
 *     
 *     status 0 新建状态     status  1 排队中
* @author leihao.zhao  
* @date 2016-1-19 下午4:14:10 
* @version V1.0
* 
 */
public class QsMemberCacheCommand implements Serializable{

	private static final long serialVersionUID = -1992842747013401201L;
	
	/**记录用户购买状态  如:在购物车时**/
	public  transient static final String INIT_QUEUE_STATUS = "0";
	
	/**用户购买队列排队中**/
	public  transient static final String IN_QUEUE_STATUS = "1";

	private String rId;
	
	private String status;
	
	private String skuCode;
	
	private String upc;
	
	private String memberId;

	public String getrId() {
		return rId;
	}

	public void setrId(String rId) {
		this.rId = rId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}