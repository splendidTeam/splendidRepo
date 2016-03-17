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
 */
package com.baozun.nebula.dao.product;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.ItemPropertiesLang;

/**
 * ItemProperties dao
 * 
 * @author xingyu.liu
 * 
 */
public interface ItemPropertiesDao extends GenericEntityDao<ItemProperties, Long>{

	/**
	 * 根据itemId查询ItemProperties
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = ItemProperties.class)
	List<ItemProperties> findItemPropertiesByItemId(@QueryParam("itemId") Long itemId);
	
	@NativeQuery(model = ItemProperties.class)
	List<ItemProperties> findItemPropertiesByItemIdI18n(@QueryParam("itemId") Long itemId,@QueryParam("lang") String lang);
	
	/**
	 * 根据itemId删除全部ItemProperties
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeUpdate
	Integer deleteItemPropertiesByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 根据商品id集合删除ItemProperties
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeUpdate
	Integer removeItemPropByIds(@QueryParam("ids") List<Long> ids);
	
	/**
	 * 通过商品ID集合查询商品属性
	 * @return
	 */
	@NativeQuery(model = ItemProperties.class)
	List<ItemProperties> findItemPropertieListByItemIds(@QueryParam("itemIds")List<Long> itemIds);

	/**
	 * 通过语言标识和商品ID集合查询商品属性(国际化方法)
	 * @return
	 */
	@NativeQuery(model = ItemProperties.class)
	List<ItemProperties> findItemPropertieListByItemIdsI18n(@QueryParam("itemIds")List<Long> itemIds, @QueryParam("langKey") String langKey);

	/**
	 * 根据itemPropertyIds查询ItemProperties
	 * 
	 * @param itemPropertyIds 商品属性Ids
	 * @return
	 */
	@NativeQuery(model = ItemProperties.class)
	List<ItemProperties> findItemPropertiesByIds(@QueryParam("ids") List<Long> ids);
	
	/**
	 * 根据itemPropertyIds和语言查询ItemProperties
	 * 
	 * @param itemPropertyIds 商品属性Ids
	 * @param lang 当前语言
	 * @return
	 */
	@NativeQuery(model = ItemProperties.class)
	List<ItemProperties> findItemPropertiesByIdsI18n(@QueryParam("ids") List<Long> ids, @QueryParam("lang") String lang);
	
	/**
	 * 通过商品Code查询商品的颜色属性
	 * @param itemCodes
	 * @return
	 */
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findItemPropertiesByItemCodes(@QueryParam("itemCodes") List<String> itemCodes);
	
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findPropertyValueByPropertyNameAndItemIds(@QueryParam("propertyName") String propertyName,@QueryParam("itemIds") List<Long> itemIds);
	
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findPropertyValueByPropertyNameAndItemIdsI18n(@QueryParam("propertyName") String propertyName,@QueryParam("itemIds") List<Long> itemIds,@QueryParam("lang") String lang);
	
	
	
	@NativeUpdate
	Integer deleteItemPropertiesByItemIdAndPropId(@QueryParam("itemId") Long itemId,@QueryParam("propId") Long propId);

	/**
	 * 根据商品ID和属性ID查询
	 * @param itemId
	 * @param propertyId
	 * @return
	 */
	@NativeQuery(model = ItemProperties.class)
	ItemProperties findItemPropertiesByItemIdAndPropertyId(@QueryParam("itemId") Long itemId, @QueryParam("propertyId") Long propertyId);
	
	/**
	 * 根据商品ID和属性值ID查询
	 * 
	 * @param itemId
	 * @param propertyValueId
	 * @return
	 */
	@NativeQuery(model = ItemProperties.class)
	ItemProperties findItemPropertiesByItemIdAndPropertyValueId(
			@QueryParam("itemId") Long itemId,
			@QueryParam("propertyValueId") Long propertyValueId);
	
	/**
	 * 根据商品ID和属性ID查询
	 * @param itemId 
	 * @author Arvin.Chang 2016年1月7日  fix 这个方法应该返回List propertyId 对应多条记录
	 * @param propertyId
	 * @return
	 */
	@NativeQuery(model = ItemProperties.class)
	List<ItemProperties> findItemPropertiesByItemIdAndPropertyIdForList(@QueryParam("itemId") Long itemId, @QueryParam("propertyId") Long propertyId);
	
	
	
	/**
	 * 根据ID更新属性值和属性值ID
	 * @param id
	 * @param valueId
	 * @param value
	 * @return
	 */
	@NativeUpdate
	Integer updatePropertyValueById(@QueryParam("id") Long id, @QueryParam("valueId") Long valueId, @QueryParam("value") String value);
	
	
	
	/**
	 * 根据ID更新属性值和属性值ID
	 * @param id
	 * @param valueId
	 * @param value
	 * @return
	 */
	@NativeUpdate
	int updatePropertiesLang(@QueryParam Map<String, Object> params);
	
	
	@NativeQuery(model = ItemPropertiesLang.class)
    List<ItemPropertiesLang> findItemPropertiesLangByIds(@QueryParam("ids") List<Long> ids,@QueryParam("langs") List<String> langs);
	
	@NativeUpdate
    int deleteItemPropertiesLangByIds(@QueryParam("ids") List<Long> ids);
	
	/**
	 * 根据商品编码集合,店铺ID和行业ID查询商品属性
	 * @param shopId
	 * @param industryId
	 * @param itemCodes
	 * @return
	 */
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findItemPropertiesByQueryMap(@QueryParam() Map<String, Object> paramMap, @QueryParam("propertyIdList") List<Long> propertyIdList, @QueryParam("itemCodeList") List<String> itemCodeList);

	/**
	 * 根据商品编码集合,店铺ID和行业ID查询商品属性(国际化)
	 * @param shopId
	 * @param industryId
	 * @param itemCodes
	 * @param langKey
	 * @return
	 */
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findItemPropertiesByQueryMapI18n(@QueryParam() Map<String, Object> paramMap, @QueryParam("propertyIdList") List<Long> propertyIdList, @QueryParam("itemCodeList") List<String> itemCodeList, @QueryParam("langKey") String langKey);
	
	/**
	 * 根据商品属性的ID集合,删除商品属性
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer deleteItemPropertiesByIds(@QueryParam("ids") List<Long> ids);
}
