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
package com.baozun.nebula.model.salesorder;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 收货人信息
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "t_so_consignee")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Consignee extends BaseModel{


	private static final long serialVersionUID = -8782230075078229172L;


	/** PK. */
	private Long				id;

	/** 订单id */
	private Long				orderId;
	
	/** 姓名 */
	private String				name;
	
	/**
	 * 国家id 
	 */
	private Long 				countryId;
	
	/** 省 id*/
	private Long				provinceId;
	
	/** 市id */
	private Long			    cityId;
	
	/** 区 id*/
	private Long			    areaId;
	
	/** 镇id*/
	private Long				townId;
	
	/** 国 */
	private String				country;
	
	/** 省 */
	private String				province;
	
	/** 市 */
	private String			    city;
	
	/** 区 */
	private String			    area;
	
	/** 镇 */
	private String 				town;
	
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
	
	/** version*/
	private Date				version;
	
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_CONSIGNEE",sequenceName = "S_T_SAL_CONSIGNEE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_CONSIGNEE")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "ORDER_ID")
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "COUNTRY_ID")
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Column(name = "PROVINCE_ID")
	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	@Column(name = "CITY_ID")
	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	@Column(name = "AREA_ID")
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@Column(name = "TOWN_ID")
	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}


	@Column(name = "COUNTRY", length = 50)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Column(name = "PROVINCE", length = 50)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "CITY", length = 50)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "AREA", length = 50)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "TOWN", length = 500)
	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	@Column(name = "ADDRESS", length = 200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "MOBILE", length = 20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "TEL", length = 50)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "EMAIL", length = 200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "POSTCODE", length = 50)
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Column(name = "APPOINT_TIME_QUANTUM", length = 200)
	public String getAppointTimeQuantum() {
		return appointTimeQuantum;
	}

	public void setAppointTimeQuantum(String appointTimeQuantum) {
		this.appointTimeQuantum = appointTimeQuantum;
	}

	@Column(name = "APPOINT_TIME", length = 100)
	public String getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(String appointTime) {
		this.appointTime = appointTime;
	}
	

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "APPOINT_TYPE", length = 100)
	public String getAppointType() {
		return appointType;
	}

	public void setAppointType(String appointType) {
		this.appointType = appointType;
	}

	
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	
}
