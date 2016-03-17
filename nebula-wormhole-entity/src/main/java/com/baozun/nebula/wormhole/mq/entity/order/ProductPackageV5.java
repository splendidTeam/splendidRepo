package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 商品包装
 * @author Justin Hu
 *
 */

public class ProductPackageV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6773606758819841723L;

	
	/**
	 * 包装类型：SCM定义的列表中选取
	 */
	private Integer type;
	
	/**
	 * 备注：其他信息
	 */
	private String remark;
	

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
