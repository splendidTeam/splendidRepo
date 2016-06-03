package com.baozun.nebula.web.controller.order.resolver;

import java.io.Serializable;

/**
 * 订单生成成功后返回给前端的信息
 * 
 * @author weihui.tang
 */
public class SalesOrderReturnObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -752189810870844194L;

	/** 交易流水号 */
	private String subOrdinate;

	/** 订单Id */
	private Long id;

	/** 订单编号 */
	private String code;
	
	private Integer payType;

	public String getSubOrdinate() {
		return subOrdinate;
	}

	public void setSubOrdinate(String subOrdinate) {
		this.subOrdinate = subOrdinate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

}
