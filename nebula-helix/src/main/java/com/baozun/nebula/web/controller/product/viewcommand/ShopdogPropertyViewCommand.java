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

public class ShopdogPropertyViewCommand extends BaseViewCommand {
	
	private static final long serialVersionUID = 698053626062198899L;
	
	 /** 属性名字. */
    private String name;
    
    /** 是否是颜色属性. */
    private Boolean isColorProp;
    
    /** 属性值列表 */
    private List<ShopdogPropertyValueViewCommand> propertyValues;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsColorProp() {
		return isColorProp;
	}

	public void setIsColorProp(Boolean isColorProp) {
		this.isColorProp = isColorProp;
	}

	public List<ShopdogPropertyValueViewCommand> getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(
			List<ShopdogPropertyValueViewCommand> propertyValues) {
		this.propertyValues = propertyValues;
	}
}
