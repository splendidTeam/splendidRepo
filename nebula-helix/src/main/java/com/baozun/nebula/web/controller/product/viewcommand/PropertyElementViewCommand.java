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

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 商品的一个属性和值的Command
 */
public class PropertyElementViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 3337324347141885581L;

	private PropertyViewCommand property;
	
	private List<PropertyValueViewCommand> propertyValues;

	public PropertyViewCommand getProperty() {
		return property;
	}

	public void setProperty(PropertyViewCommand property) {
		this.property = property;
	}

	public List<PropertyValueViewCommand> getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(List<PropertyValueViewCommand> propertyValues) {
		this.propertyValues = propertyValues;
	}
	
	
}
