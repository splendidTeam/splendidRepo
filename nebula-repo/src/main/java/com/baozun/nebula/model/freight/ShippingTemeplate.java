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

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 运费模板
 * @author Tianlong.Zhang
 *
 */
@Entity
@Table(name = "T_SF_SHIPPING_TEMEPALTE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ShippingTemeplate extends BaseModel {

	private static final long serialVersionUID = 8152289113747735531L;
	
	/** 默认模板*/
	public static final boolean ISDEFAULT = true;
	/** 普通模板*/
	public static final boolean NOTDEFAULT = false;
	
	public static final String CAL_TYPE_BY_UNIT = "unit";
	
	public static final String CAL_TYPE_BY_WEIGHT = "weight";
	
	public static final String CAL_TYPE_BY_BASE = "base";
	
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
	
	private Date version;
	
	public ShippingTemeplate(){
		
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@Column(name="ID")
	@SequenceGenerator(name = "SEQ_T_SF_SHIPPING_TEMEPALTE",sequenceName = "SEQ_T_SF_SHIPPING_TEMEPALTE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SF_SHIPPING_TEMEPALTE")
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME",length=255)
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
	 * @return the type
	 */
	@Column(name = "CALCULATION_TYPE",length=20)
	public String getCalculationType() {
		return calculationType;
	}

	/**
	 * @param type the type to set
	 */
	public void setCalculationType(String calculationType) {
		this.calculationType = calculationType;
	}

	/**
	 * @return the isDefault
	 */
	@Column(name = "ISDEFAULT")
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setDefaultFee(BigDecimal defaultFee) {
		this.defaultFee = defaultFee;
	}

	@Column(name = "DEFAULT_FEE")
	public BigDecimal getDefaultFee() {
		return defaultFee;
	}
	
}
