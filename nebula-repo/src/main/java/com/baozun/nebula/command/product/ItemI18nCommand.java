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
package com.baozun.nebula.command.product;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemInfoLang;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.ItemPropertiesLang;

/**
 * 商品国际化信息，包含了商品、商品信息、商品信息国际化、商品属性、商品属性国际化
 * 
 * @author yimin.qiao
 */
public class ItemI18nCommand implements Command {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8418568219209462551L;
	
	/** 商品 */
	private Item item;
	
	/** 商品信息 */
	private ItemInfo itemInfo;
	
	/** 商品信息国际化 */
	private List<ItemInfoLang> itemInfoLangList = new ArrayList<ItemInfoLang>();
	
	/** 商品属性 */
	private List<ItemProperties> itemPropertiesList = new ArrayList<ItemProperties>();
	
	/** 商品属性国际化 */
	private List<ItemPropertiesLang> itemPropertiesLangList = new ArrayList<ItemPropertiesLang>();

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(ItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}

	public List<ItemInfoLang> getItemInfoLangList() {
		return itemInfoLangList;
	}

	public void setItemInfoLangList(List<ItemInfoLang> itemInfoLangList) {
		this.itemInfoLangList = itemInfoLangList;
	}

	public List<ItemProperties> getItemPropertiesList() {
		return itemPropertiesList;
	}

	public void setItemPropertiesList(List<ItemProperties> itemPropertiesList) {
		this.itemPropertiesList = itemPropertiesList;
	}

	public List<ItemPropertiesLang> getItemPropertiesLangList() {
		return itemPropertiesLangList;
	}

	public void setItemPropertiesLangList(
			List<ItemPropertiesLang> itemPropertiesLangList) {
		this.itemPropertiesLangList = itemPropertiesLangList;
	}
	
}
