/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product.viewcommand;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 商品属性
 * <p>
 * 包括销售属性和非销售属性
 * </p>
 *
 */
public class ItemPropertyViewCommand extends BaseViewCommand {
	
	private static final long serialVersionUID = 1077822621702435603L;
	
	/** 当属性分组为空时，非销售属性Map的Key */
	public static final String NULL_GROUP = "NullGroup";

	/**
	 * 销售属性
	 */
	private List<PropertyElementViewCommand> salesProperties;
	
	/**
	 * 非销售属性，[属性分组, 分组下的非销售属性集合]
	 */
	private Map<String, List<PropertyElementViewCommand>> nonSalesProperties;

	public List<PropertyElementViewCommand> getSalesProperties() {
		return salesProperties;
	}

	public void setSalesProperties(List<PropertyElementViewCommand> salesProperties) {
		this.salesProperties = salesProperties;
	}

	public Map<String, List<PropertyElementViewCommand>> getNonSalesProperties() {
		return nonSalesProperties;
	}

	public void setNonSalesProperties(
			Map<String, List<PropertyElementViewCommand>> nonSalesProperties) {
		this.nonSalesProperties = nonSalesProperties;
	}

}
