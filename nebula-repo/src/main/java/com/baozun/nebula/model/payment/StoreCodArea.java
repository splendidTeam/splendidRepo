/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.model.payment;

import java.util.Date;

import com.baozun.nebula.model.BaseModel;

/**
 * COD支持区域.
 * 
 * @author
 */
@Deprecated
public class StoreCodArea extends BaseModel {

	/** serialVersionUID. */
	private static final long serialVersionUID = -1921550087508623986L;

	// COD收款类型
	/** COD收款类型：现金 */
	public static final Integer COD_TYPE_CASH = 1;
	
	/** COD收款类型：刷卡 */
	public static final Integer COD_TYPE_CARD = 2;

	/** Id. */
	private Long id;

	/** 店铺Id. */
	private Long storeId;

	/** COD收款类型. */
	private Integer codType;

	/** 国家. */
	private String country;

	/** 国家Id. */
	private Long countryId;

	/** 省份. */
	private String province;

	/** 省份Id. */
	private Long provinceId;

	/** 城市. */
	private String city;

	/** 城市Id. */
	private Long cityId;

	/** 区. */
	private String district;

	/** 区Id. */
	private Long districtId;

	/** 创建时间. */
	private Date createTime;

	/** 更新时间. */
	private Date updateTime;


	/**
	 * Set the Id.
	 * 
	 * @param id
	 *            Id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the Id.
	 * 
	 * @return Id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the 店铺Id.
	 * 
	 * @param storeId
	 *            店铺Id
	 */
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	/**
	 * Get the 店铺Id.
	 * 
	 * @return 店铺Id
	 */
	public Long getStoreId() {
		return this.storeId;
	}

	/**
	 * Set the COD收款类型.
	 * 
	 * @param codType
	 *            COD收款类型
	 */
	public void setCodType(Integer codType) {
		this.codType = codType;
	}

	/**
	 * Get the COD收款类型.
	 * 
	 * @return COD收款类型
	 */
	public Integer getCodType() {
		return this.codType;
	}

	/**
	 * Set the 国家.
	 * 
	 * @param country
	 *            国家
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Get the 国家.
	 * 
	 * @return 国家
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * Set the 国家Id.
	 * 
	 * @param countryId
	 *            国家Id
	 */
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	/**
	 * Get the 国家Id.
	 * 
	 * @return 国家Id
	 */
	public Long getCountryId() {
		return this.countryId;
	}

	/**
	 * Set the 省份.
	 * 
	 * @param province
	 *            省份
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * Get the 省份.
	 * 
	 * @return 省份
	 */
	public String getProvince() {
		return this.province;
	}

	/**
	 * Set the 省份Id.
	 * 
	 * @param provinceId
	 *            省份Id
	 */
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	/**
	 * Get the 省份Id.
	 * 
	 * @return 省份Id
	 */
	public Long getProvinceId() {
		return this.provinceId;
	}

	/**
	 * Set the 城市.
	 * 
	 * @param city
	 *            城市
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Get the 城市.
	 * 
	 * @return 城市
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Set the 城市Id.
	 * 
	 * @param cityId
	 *            城市Id
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	/**
	 * Get the 城市Id.
	 * 
	 * @return 城市Id
	 */
	public Long getCityId() {
		return this.cityId;
	}

	/**
	 * Set the 区.
	 * 
	 * @param district
	 *            区
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * Get the 区.
	 * 
	 * @return 区
	 */
	public String getDistrict() {
		return this.district;
	}

	/**
	 * Set the 区Id.
	 * 
	 * @param districtId
	 *            区Id
	 */
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	/**
	 * Get the 区Id.
	 * 
	 * @return 区Id
	 */
	public Long getDistrictId() {
		return this.districtId;
	}

	/**
	 * Set the 创建时间.
	 * 
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * Get the 创建时间.
	 * 
	 * @return 创建时间
	 */
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * Set the 更新时间.
	 * 
	 * @param updateTime
	 *            更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * Get the 更新时间.
	 * 
	 * @return 更新时间
	 */
	public Date getUpdateTime() {
		return this.updateTime;
	}

}
