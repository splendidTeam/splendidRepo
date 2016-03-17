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
 */
package com.baozun.nebula.sdk.command;

import java.util.List;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Property;

/**
 * 动态属性
 * 
 * @author chenguang.zhou
 * @date 2014-3-10 上午10:00:05
 **/
public class DynamicPropertyCommand implements Command {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 3158831281078082690L;

	/**
	 * 属性
	 */
	private Property					property;

	/**
	 * 商品对应的属性
	 */
	private List<ItemPropertiesCommand>	itemPropertiesList;

	/**
	 * 商品属性
	 */
	private ItemPropertiesCommand		itemProperties;

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public List<ItemPropertiesCommand> getItemPropertiesList() {
		return itemPropertiesList;
	}

	public void setItemPropertiesList(
			List<ItemPropertiesCommand> itemPropertiesList) {
		this.itemPropertiesList = itemPropertiesList;
	}

	public ItemPropertiesCommand getItemProperties() {
		return itemProperties;
	}

	public void setItemProperties(ItemPropertiesCommand itemProperties) {
		this.itemProperties = itemProperties;
	}
}
