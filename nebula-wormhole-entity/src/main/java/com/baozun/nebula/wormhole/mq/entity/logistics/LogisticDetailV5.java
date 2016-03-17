package com.baozun.nebula.wormhole.mq.entity.logistics;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 物流明细
 * @author Justin Hu
 *
 */

public class LogisticDetailV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9178440671880790248L;
	
	/**
	 * 处理时间
	 */
	private Date opTime;  
	
	/**
	 * 处理备注
	 */
	private String remark;  

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	

}
