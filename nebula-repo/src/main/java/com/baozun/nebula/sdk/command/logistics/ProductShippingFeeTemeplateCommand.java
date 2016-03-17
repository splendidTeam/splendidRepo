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
package com.baozun.nebula.sdk.command.logistics;

import com.baozun.nebula.model.BaseModel;


/**
 * @author Tianlong.Zhang
 * 
 */
public class ProductShippingFeeTemeplateCommand extends BaseModel {
	
	private static final long serialVersionUID = 1762813596352862736L;

	private Long id;

	/**
	 * 商品Id
	 */
	private Long itemId;

	/**
	 * 运费模板Id
	 */
	private Long shippingFeeTemeplateId;

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
	 * @return the shippingFeeTemeplateId
	 */
	public Long getShippingFeeTemeplateId() {
		return shippingFeeTemeplateId;
	}

	/**
	 * @param shippingFeeTemeplateId the shippingFeeTemeplateId to set
	 */
	public void setShippingFeeTemeplateId(Long shippingFeeTemeplateId) {
		this.shippingFeeTemeplateId = shippingFeeTemeplateId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getItemId() {
		return itemId;
	}
	
	
}
