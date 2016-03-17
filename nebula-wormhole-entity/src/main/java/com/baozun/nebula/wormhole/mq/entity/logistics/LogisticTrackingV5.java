package com.baozun.nebula.wormhole.mq.entity.logistics;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物流跟踪信息
 * 定时接收并存储
 * 定时更改订单相关的物流跟踪信息
 * @author Justin Hu
 *
 */

public class LogisticTrackingV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7999175301463462407L;

	/**
	 * 商城订单号
	 */
	private String bsOrderCode;
	
	/**
	 * 同步时间
	 */
	private Date syncTime; 
	
	
	 /**
     * 物流单号
     * 快递单号：当出库时会提供
     */
    private String transCode;
    
    /**
     * 物流商编码
     */
    private String logisticsProviderCode;
    
    
    /**
     * 物流明细
     */
    private List<LogisticDetailV5> details;
    


	public String getBsOrderCode() {
		return bsOrderCode;
	}


	public void setBsOrderCode(String bsOrderCode) {
		this.bsOrderCode = bsOrderCode;
	}


	public Date getSyncTime() {
		return syncTime;
	}


	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}


	public String getTransCode() {
		return transCode;
	}


	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}


	public String getLogisticsProviderCode() {
		return logisticsProviderCode;
	}


	public void setLogisticsProviderCode(String logisticsProviderCode) {
		this.logisticsProviderCode = logisticsProviderCode;
	}


	public List<LogisticDetailV5> getDetails() {
		return details;
	}


	public void setDetails(List<LogisticDetailV5> details) {
		this.details = details;
	}
	
    
}
