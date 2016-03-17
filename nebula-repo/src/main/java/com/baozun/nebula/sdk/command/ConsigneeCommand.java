/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.command;

import java.util.Date;

import com.baozun.nebula.api.RiskControl;
import com.baozun.nebula.api.RiskLevel;
import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;


public class ConsigneeCommand extends BaseModel implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7177968149000387037L;

	private Long 				id;
	
	/** 订单id */
	private Long				orderId;
	
	/** 姓名 */
	private String				name;
	
	/** 国 */
	private String				country;
	
	/** 省 */
	private String				province;
	
	/** 市 */
	private String			    city;
	
	/** 区 */
	private String			    area;
	
	/** 地址 */
	private String				address;
	
	/** 手机 */
	private String				mobile;
	
	/** 固话 */
	private String				tel;
	
	/** email */
	private String				email;
	
	/** 邮编 */
	private String				postcode;
	
	/** 指定时间段 */
	private String				appointTimeQuantum;
	
	/** 指定日期 */
	private String				appointTime;
	
	/** 指定类型 */
	private String				appointType;
	
	/** 修改时间 */
	private Date				modifyTime;


	@RiskControl(RiskLevel.MEDIUM)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	@RiskControl(RiskLevel.MEDIUM)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@RiskControl(RiskLevel.MEDIUM)
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	@RiskControl(RiskLevel.MEDIUM)
	public String getAppointTimeQuantum() {
		return appointTimeQuantum;
	}

	public void setAppointTimeQuantum(String appointTimeQuantum) {
		this.appointTimeQuantum = appointTimeQuantum;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(String appointTime) {
		this.appointTime = appointTime;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getAppointType() {
		return appointType;
	}

	public void setAppointType(String appointType) {
		this.appointType = appointType;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	
}
