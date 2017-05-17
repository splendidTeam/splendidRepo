/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.model.freight;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 运费配置
 * @author Tianlong.Zhang
 * 
 */
@Entity
@Table(name="T_SF_SHIPPING_CONFIG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ShippingFeeConfig extends BaseModel {

	private static final long serialVersionUID = 4970426409409963994L;

	/**
	 * id
	 */
	private Long id;
	
	/**
	 *  运费模板Id
	 */
	private Long shippingTemeplateId;
	
	/**
	 * 物流方式Id
	 */
	private Long distributionModeId;
	
	/**
	 *  目的地id:可能是省、市、区/县、镇的其中一个，可以随便进行维护，但是以最小单位优先
	 */
	private String destAreaId;
	
	/**
	 * 首件运费的单位
	 */
	private Integer firstPartUnit;
	
	/**
	 * 续件运费的单位
	 */
	private Integer subsequentPartUnit;
	
	/**
	 * 首件运费价格
	 */
	private BigDecimal firstPartPrice;
	
	/**
	 * 续件运费价格
	 */
	private BigDecimal subsequentPartPrice;
	
	/**
	 * 对应了 基础 类型的 基础价格
	 */
	private BigDecimal basePrice;
	
	private Date version;

	/**
	 * @return the id
	 */
	@Id
	@Column(name="ID")
	@SequenceGenerator(name = "SEQ_T_SF_SHIPPING_CONFIG",sequenceName = "SEQ_T_SF_SHIPPING_CONFIG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SF_SHIPPING_CONFIG")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the shippingTemeplateId
	 */
	@Column(name="SHIPPING_TEMEPLATE_ID")
    @Index(name = "IDX_SHIPPING_CONFIG_SHIPPING_TEMEPLATE_ID")
	public Long getShippingTemeplateId() {
		return shippingTemeplateId;
	}

	/**
	 * @param shippingTemeplateId
	 *            the shippingTemeplateId to set
	 */
	public void setShippingTemeplateId(Long shippingTemeplateId) {
		this.shippingTemeplateId = shippingTemeplateId;
	}


//	/**
//	 * @return the destProvince
//	 */
//	@Column(name="DEST_PROVINCE")
//	public String getDestProvince() {
//		return destProvince;
//	}
//
//	/**
//	 * @param destProvince
//	 *            the destProvince to set
//	 */
//	public void setDestProvince(String destProvince) {
//		this.destProvince = destProvince;
//	}
//
//	/**
//	 * @return the destCity
//	 */
//	@Column(name="DEST_CITY")
//	public String getDestCity() {
//		return destCity;
//	}
//
//	/**
//	 * @param destCity
//	 *            the destCity to set
//	 */
//	public void setDestCity(String destCity) {
//		this.destCity = destCity;
//	}
//
//	/**
//	 * @return the destCounty
//	 */
//	@Column(name="DEST_COUNTY")
//	public String getDestCounty() {
//		return destCounty;
//	}
//
//	/**
//	 * @param destCounty
//	 *            the destCounty to set
//	 */
//	public void setDestCounty(String destCounty) {
//		this.destCounty = destCounty;
//	}

	/**
	 * @return the firstPartUnit
	 */
	@Column(name="FIRST_PART_UNIT")
	public Integer getFirstPartUnit() {
		return firstPartUnit;
	}

	/**
	 * @param firstPartUnit
	 *            the firstPartUnit to set
	 */
	public void setFirstPartUnit(Integer firstPartUnit) {
		this.firstPartUnit = firstPartUnit;
	}

	/**
	 * @return the subsequentPartUnit
	 */
	@Column(name="SUBSEQUENT_PART_UNIT")
	public Integer getSubsequentPartUnit() {
		return subsequentPartUnit;
	}

	/**
	 * @param subsequentPartUnit
	 *            the subsequentPartUnit to set
	 */
	public void setSubsequentPartUnit(Integer subsequentPartUnit) {
		this.subsequentPartUnit = subsequentPartUnit;
	}

	/**
	 * @return the firstPartPrice
	 */
	@Column(name="FIRST_PART_PRICE")
	public BigDecimal getFirstPartPrice() {
		return firstPartPrice;
	}

	/**
	 * @param firstPartPrice
	 *            the firstPartPrice to set
	 */
	public void setFirstPartPrice(BigDecimal firstPartPrice) {
		this.firstPartPrice = firstPartPrice;
	}

	/**
	 * @return the subsequentPartPrice
	 */
	@Column(name="SUBSEQUENT_PART_PRICE")
	public BigDecimal getSubsequentPartPrice() {
		return subsequentPartPrice;
	}

	/**
	 * @param subsequentPartPrice
	 *            the subsequentPartPrice to set
	 */
	public void setSubsequentPartPrice(BigDecimal subsequentPartPrice) {
		this.subsequentPartPrice = subsequentPartPrice;
	}

	/**
	 * @return the version
	 */
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

	public void setDistributionModeId(Long distributionModeId) {
		this.distributionModeId = distributionModeId;
	}

	@Column(name="DISTRIBUTION_MODE_ID")
    @Index(name = "IDX_SHIPPING_CONFIG_DISTRIBUTION_MODE_ID")
	public Long getDistributionModeId() {
		return distributionModeId;
	}

	public void setDestAreaId(String destAreaId) {
		this.destAreaId = destAreaId;
	}

	@Column(name = "DEST_AREA_ID")
    @Index(name = "IDX_SHIPPING_CONFIG_DEST_AREA_ID")
	public String getDestAreaId() {
		return destAreaId;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	@Column(name = "BASE_PRICE")
	public BigDecimal getBasePrice() {
		return basePrice;
	}

//	public void setShippingProviderId(Long shippingProviderId) {
//		this.shippingProviderId = shippingProviderId;
//	}
//
//	@Column(name="SHIPPING_Provider_Id")
//	public Long getShippingProviderId() {
//		return shippingProviderId;
//	}

}
