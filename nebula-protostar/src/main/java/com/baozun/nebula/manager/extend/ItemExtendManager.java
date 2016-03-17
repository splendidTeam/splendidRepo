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
package com.baozun.nebula.manager.extend;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.SkuPropertyCommand;
import com.baozun.nebula.command.product.ImpItemCommand;
import com.baozun.nebula.command.product.ItemI18nCommand;
import com.baozun.nebula.command.product.ItemInfoCommand;
import com.baozun.nebula.command.product.ItemInfoExcelCommand;
import com.baozun.nebula.command.product.ItemPropertiesCommand;
import com.baozun.nebula.command.promotion.SkuPropertyMUtlLangCommand;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Property;

/**
 * 用于商品相关的扩展点
 * @author yimin.qiao
 *
 */
public interface ItemExtendManager extends BaseExtendManager {

	/**
	 * PTS中使用修改或新增商品页面对单个商品创建或修改时的扩展点
	 * @param item 新增或修改后的Item, 含有ID信息
	 * @param itemCommand 从新增或修改商品页面各输入项构造的command
	 * @param categoriesIds 商品分类IDs
	 * @param iProperties 经过Nebula处理后的商品属性
	 * @param skuPropertyCommand 商品的sku command
	 */
	void extendAfterCreateOrUpdateItem(Item item, ItemCommand itemCommand, Long[] categoriesIds, List<ItemProperties> itemProperties, SkuPropertyCommand[] skuPropertyCommand);
	
	/**
	 * 国际化版本中，PTS中使用修改或新增商品页面对单个商品创建或修改时的扩展点
	 * @param item 新增或修改后的Item, 含有ID信息
	 * @param itemInfoCommand 从新增或修改商品页面各输入项构造的command
	 * @param categoriesIds 商品分类IDs
	 * @param iProperties 经过Nebula处理后的商品属性
	 * @param skuPropertyCommand 商品的sku command
	 * @param itemI18nCommand 商品的国际化信息，目前包含了保存后的item、itemProperties、itemPropertiesLang
	 */
	void extendAfterCreateOrUpdateItemI18n(Item item, ItemInfoCommand itemInfoCommand, Long[] categoriesIds, List<ItemPropertiesCommand> itemProperties, 
			SkuPropertyMUtlLangCommand[] skuPropertyMUtlLangCommand, ItemI18nCommand itemI18nCommand);
	
	/**
	 * PTS中使用导入Excel的方式新增或修改商品时的扩展点，该扩展点不包含sku的信息
	 * @param impItemCommandList 商品基本信息的列表
	 * @param itemStatusMap 商品是否在系统中已存在，true:已存在；false:不存在
	 * @param itemMap 商品编码-ID的map，<itemCode, itemID>
	 * @param propMap 该行业和店铺所有属性的map，<属性ID, 属性>
	 * @param itemPropIdMap 商品属性ID的map，<item.id_itemProperty.propertyId_itemProperty.propertyValueId, itemProperty.id>
	 * @param itemPropVMap 商品属性值ID的map，<item.id_itemProperty.propertyId_itemProperty.propertyValue, itemProperty.id>
	 * @param itemPropMap 商品属性ID的map，<item.id_itemProperty.propertyId, itemProperty.id>
	 * @param shopId 店铺ID
	 * @param industryId 行业ID
	 * @param notSalePropIdList 非销售属性的ID列表
	 */
	void extendAfterSaveImpItemCommand(List<ImpItemCommand> impItemCommandList, Map<String, Boolean> itemStatusMap, Map<String, Long> itemMap, Map<Long, Property> propMap, 
			Map<String, Long> itemPropIdMap, Map<String, Long> itemPropVMap,Map<String, Long> itemPropMap, Long shopId, Long industryId, List<Long> notSalePropIdList);
	
	/**
	 * PTS中使用导入Excel的方式新增或修改商品时的扩展点，该扩展点不包含sku的信息
	 * @param impItemCommandList 商品基本信息的列表
	 * @param itemStatusMap 商品是否在系统中已存在，true:已存在；false:不存在
	 * @param itemMap 商品编码-ID的map，<itemCode, itemID>
	 * @param propMap 该行业和店铺所有属性的map，<属性ID, 属性>
	 * @param itemPropIdMap 商品属性ID的map，<item.id_itemProperty.propertyId_itemProperty.propertyValueId, itemProperty.id>
	 * @param itemPropVMap 商品属性值ID的map，<item.id_itemProperty.propertyId_itemProperty.propertyValue, itemProperty.id>
	 * @param itemPropMap 商品属性ID的map，<item.id_itemProperty.propertyId, itemProperty.id>
	 * @param shopId 店铺ID
	 * @param industryId 行业ID
	 * @param notSalePropIdList 非销售属性的ID列表
	 * @param allI18nItemInfos 语言和商品信息的map
	 */
	void extendAfterSaveImpItemCommandI18n(List<ImpItemCommand> impItemCommandList, Map<String, Boolean> itemStatusMap, Map<String, Long> itemMap, Map<Long, Property> propMap, 
			Map<String, Long> itemPropIdMap, Map<String, Long> itemPropVMap,Map<String, Long> itemPropMap, Long shopId, Long industryId, List<Long> notSalePropIdList, Map<String, 
			List<ItemInfoExcelCommand>> allI18nItemInfos);
	
	/**
	 * PTS中使用编辑导入的方式修改商品是的扩展点，该扩展点不包含sku的信息
	 * @param itemBeans 读取的excel的数据，可以通过如下代码取得不同语言导入的商品数据:
	 * <pre>
	 *     Map<String, Object> itemBeans = itemBeansMap.get(langKey);
	 *     List<ImpItemCommand> impItemCommandList = (List<ImpItemCommand>) itemBeans.get("impItemCommand");
	 * </pre>
	 * @param propertyCodeMap excel文件中存在的列 (key[common or base]:value[key[列编号]:value[列号]])，base指商品基本属性，common指行业中定义的属性
	 * @param propertyMap 属性的Map，<propertyID, property>
	 */
	void extendAfterItemImport(Map<String, Map<String, Object>> itemBeans, Map<String, Map<String, Integer>> propertyCodeMap, Map<Long, Property> propertyMap);
}
