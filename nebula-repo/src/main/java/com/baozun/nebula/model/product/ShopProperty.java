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
package com.baozun.nebula.model.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

/**
 * 店铺、行业、属性的三叉关系表
 * 
 * @author Justin
 *
 */
@Entity
@Table(name = "T_PD_SHOPPROPERTY")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ShopProperty implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 5810799842523051552L;

	/**
	 * ID
	 */
	private Long id;
	
	/**
	 * 店铺id(shop)
	 */
	private Long shopId;
	
	/**
	 * 行业id(Industry )
	 */
	private Long industryId;
	
	/**
	 * 属性id(Property)
	 */
	private Long propertyId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_SHOPPROPERTY", sequenceName = "S_T_PD_SHOPPROPERTY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_SHOPPROPERTY")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	
	@Column(name = "SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "INDUSTRY_ID")
	public Long getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Long industryId) {
		this.industryId = industryId;
	}

	@Column(name = "PROPERTY_ID")
	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
	
	
	
}
