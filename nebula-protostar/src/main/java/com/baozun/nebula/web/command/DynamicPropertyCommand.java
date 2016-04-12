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
package com.baozun.nebula.web.command;

import java.util.List;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueGroup;

/**
 * 获取动态属性集包含对应的属性及可选值
 * 
 * @author xingyu.liu
 */
public class DynamicPropertyCommand implements Command{

	private static final long serialVersionUID = -8683367771179653207L;

	/**
	 * 属性
	 */
	private Property property;
	
	/**
	 * 属性所选值
	 */
	private List<PropertyValue> propertyValueList;
	
	/**
	 * 属性值分组
	 */
	private List<PropertyValueGroup> propertyValueGroupList;
	
	
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}
	public List<PropertyValue> getPropertyValueList() {
		return propertyValueList;
	}
	public void setPropertyValueList(List<PropertyValue> propertyValueList) {
		this.propertyValueList = propertyValueList;
	}
	
	public List<PropertyValueGroup> getPropertyValueGroupList(){
		return propertyValueGroupList;
	}
	
	public void setPropertyValueGroupList(List<PropertyValueGroup> propertyValueGroupList){
		this.propertyValueGroupList = propertyValueGroupList;
	}
	
	
	
}
