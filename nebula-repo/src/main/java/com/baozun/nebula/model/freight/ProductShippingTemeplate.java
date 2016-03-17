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
 * 商品运费表
 * @author Tianlong.Zhang
 *
 */
@Entity
@Table(name="T_SF_PRODUCT_SHIPPING_TEMEPLATE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
@Deprecated
public class ProductShippingTemeplate extends BaseModel{
	
	private static final long serialVersionUID = 8570105638328038632L;

	private Long id;
	
	/**
	 * 商品Id
	 */
	private Long itemId;
	
	/**
	 * 运费模板Id
	 */
	private Long shippingTemeplateId;
	
	private Date version;

	/**
	 * @return the id
	 */
	@Id
	@Column(name="ID")
	@SequenceGenerator(name = "SEQ_T_SF_PRODUCT_SHIPPING_TEMEPLATE",sequenceName = "SEQ_T_SF_PRODUCT_SHIPPING_TEMEPLATE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SF_PRODUCT_SHIPPING_TEMEPLATE")
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
	 * @return the shippingFeeTemeplateId
	 */
	@Column(name="SHIPPING_TMP_ID")
	public Long getShippingTemeplateId() {
		return shippingTemeplateId;
	}

	/**
	 * @param shippingFeeTemeplateId the shippingFeeTemeplateId to set
	 */
	public void setShippingTemeplateId(Long shippingTemeplateId) {
		this.shippingTemeplateId = shippingTemeplateId;
	}
	
	public void setVersion(Date version) {
		this.version = version;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name="ITEM_ID")
	public Long getItemId() {
		return itemId;
	}
}
