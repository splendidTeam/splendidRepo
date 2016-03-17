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
import java.util.Date;
import java.util.List;

import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;

/**
 * @author Tianlong.Zhang
 *
 */
public class ShippingTemeplateCommand extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7425749296608783142L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * 模板名称
	 */
	private String name;
	
	/**
	 * 计件，计重，基础(重量/重量单位*单价，低于基础价时使用基础价)
	 */
	private String calculationType;
	
	/**
	 * 是否默认
	 */
	private boolean isDefault;
	
	/**
	 * 模板的所属的店铺
	 */
	private Long shopId;
	
	/**
	 * 默认运费：如果通过支持的区域，找不到运费，则返回此默认运费
	 */
	private BigDecimal defaultFee;
	
	/**
	 * 具体运费配置的列表
	 */
	private List<ShippingFeeConfigCommand>  feeConfigs;
	
	/**
	 * 支持改模板的物流方式
	 */
	private List<DistributionModeCommand> distributionModes;
	
	private List<Long> distributionModeIds;
	
	private Date version;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the calculationType
	 */
	public String getCalculationType() {
		return calculationType;
	}

	/**
	 * @param calculationType the calculationType to set
	 */
	public void setCalculationType(String calculationType) {
		this.calculationType = calculationType;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the shopId
	 */
	public Long getShopId() {
		return shopId;
	}

	/**
	 * @param shopId the shopId to set
	 */
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	/**
	 * @return the defaultFee
	 */
	public BigDecimal getDefaultFee() {
		return defaultFee;
	}

	/**
	 * @param defaultFee the defaultFee to set
	 */
	public void setDefaultFee(BigDecimal defaultFee) {
		this.defaultFee = defaultFee;
	}

	/**
	 * @return the version
	 */
	public Date getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

	public void setFeeConfigs(List<ShippingFeeConfigCommand> feeConfigs) {
		this.feeConfigs = feeConfigs;
	}

	public List<ShippingFeeConfigCommand> getFeeConfigs() {
		return feeConfigs;
	}

	public void setDistributionModes(List<DistributionModeCommand> distributionModes) {
		this.distributionModes = distributionModes;
	}

	public List<DistributionModeCommand> getDistributionModes() {
		return distributionModes;
	}

	public List<Long> getDistributionModeIds() {
		return distributionModeIds;
	}

	public void setDistributionModeIds(List<Long> distributionModeIds) {
		this.distributionModeIds = distributionModeIds;
	}
	
	
}
