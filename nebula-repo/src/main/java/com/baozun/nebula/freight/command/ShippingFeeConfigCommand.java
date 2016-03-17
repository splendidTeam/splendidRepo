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
package com.baozun.nebula.freight.command;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

/**
 * @author Tianlong.Zhang
 *
 */
public class ShippingFeeConfigCommand implements Command {

	private static final long serialVersionUID = 5667218373148424938L;

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
	 * 物流方式name
	 */
	private String distributionModeName;
	
	/**
	 *  目的地id:可能是省、市、区/县、镇的其中一个，可以随便进行维护，但是以最小单位优先
	 */
	private String destAreaId;
	
	/**
	 *  目的地name:可能是省、市、区/县、镇的其中一个，可以随便进行维护，但是以最小单位优先
	 */
	private String destAreaName;
	
	/**
	 * 计件，计重，基础(重量/重量单位*单价，低于基础价时使用基础价)
	 */
	private String calculationType;
	
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

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the shippingTemeplateId
	 */
	public Long getShippingTemeplateId() {
		return shippingTemeplateId;
	}

	/**
	 * @param shippingTemeplateId the shippingTemeplateId to set
	 */
	public void setShippingTemeplateId(Long shippingTemeplateId) {
		this.shippingTemeplateId = shippingTemeplateId;
	}

	/**
	 * @return the distributionModeId
	 */
	public Long getDistributionModeId() {
		return distributionModeId;
	}

	/**
	 * @param distributionModeId the distributionModeId to set
	 */
	public void setDistributionModeId(Long distributionModeId) {
		this.distributionModeId = distributionModeId;
	}

	/**
	 * @return the destAreaId
	 */
	public String getDestAreaId() {
		return destAreaId;
	}

	/**
	 * @param destAreaId the destAreaId to set
	 */
	public void setDestAreaId(String destAreaId) {
		this.destAreaId = destAreaId;
	}

	/**
	 * @return the firstPartUnit
	 */
	public Integer getFirstPartUnit() {
		return firstPartUnit;
	}

	/**
	 * @param firstPartUnit the firstPartUnit to set
	 */
	public void setFirstPartUnit(Integer firstPartUnit) {
		this.firstPartUnit = firstPartUnit;
	}

	/**
	 * @return the subsequentPartUnit
	 */
	public Integer getSubsequentPartUnit() {
		return subsequentPartUnit;
	}

	/**
	 * @param subsequentPartUnit the subsequentPartUnit to set
	 */
	public void setSubsequentPartUnit(Integer subsequentPartUnit) {
		this.subsequentPartUnit = subsequentPartUnit;
	}

	/**
	 * @return the firstPartPrice
	 */
	public BigDecimal getFirstPartPrice() {
		return firstPartPrice;
	}

	/**
	 * @param firstPartPrice the firstPartPrice to set
	 */
	public void setFirstPartPrice(BigDecimal firstPartPrice) {
		this.firstPartPrice = firstPartPrice;
	}

	/**
	 * @return the subsequentPartPrice
	 */
	public BigDecimal getSubsequentPartPrice() {
		return subsequentPartPrice;
	}

	/**
	 * @param subsequentPartPrice the subsequentPartPrice to set
	 */
	public void setSubsequentPartPrice(BigDecimal subsequentPartPrice) {
		this.subsequentPartPrice = subsequentPartPrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}


	public String getDistributionModeName() {
		return distributionModeName;
	}

	public void setDistributionModeName(String distributionModeName) {
		this.distributionModeName = distributionModeName;
	}

	public String getDestAreaName() {
		return destAreaName;
	}

	public void setDestAreaName(String destAreaName) {
		this.destAreaName = destAreaName;
	}

	public String getCalculationType() {
		return calculationType;
	}

	public void setCalculationType(String calculationType) {
		this.calculationType = calculationType;
	}
	
	
	
}
