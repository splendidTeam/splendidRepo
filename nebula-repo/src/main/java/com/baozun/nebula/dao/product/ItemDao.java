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
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.product.ItemInfoLangCommand;
import com.baozun.nebula.command.product.ProductInventoryCommand;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.sdk.command.ItemBaseCommand;

/**
 * Item dao
 * 
 * @author yi.huang
 * @date 2013-7-1 上午09:38:56
 */

public interface ItemDao extends GenericEntityDao<Item, Long> {

	/**
	 * 根据id查询Item
	 * 
	 * @param itemId
	 * @return
	 */
	// TODO 联合其他表的其他属性
	@NativeQuery(model = Item.class)
	Item findItemById(@QueryParam("itemId") Long itemId);

	/**
	 * 根据code查询ItemCommand
	 * 
	 * @param itemId
	 * @return
	 */
	// TODO 联合其他表的其他属性
	@NativeQuery(model = ItemCommand.class)
	ItemCommand findItemCommandByCode(@QueryParam("itemCode") String itemCode);

	/**
	 * 根据itemId集合查询Item
	 * 
	 * @param itemId
	 *            id集合
	 * @return
	 */
	@NativeQuery(model = Item.class)
	List<Item> findItemListByIds(@QueryParam("itemIds") List<Long> itemIds);

	/**
	 * 根据shopId和industryId查询商品
	 * 
	 * @param shopId
	 * @param industryId
	 * @return
	 */
	@NativeQuery(model = Item.class)
	List<Item> findItemListByShopIdAndIndustryId(@QueryParam("shopId") Long shopId, @QueryParam("industryId") Long industryId);

	/**
	 * 根据shopId查询商品
	 * 
	 * @param shopId
	 * @param industryId
	 * @return
	 */
	@NativeQuery(model = Item.class)
	List<Item> findItemListByShopId(@QueryParam("shopId") Long shopId);
	
	@NativeQuery(model = Item.class)
	List<Long> findItemIdsByShopId(@QueryParam("shopId") Long shopId);

	/**
	 * 根据itemIds,更新是否加入分类状态为state
	 * 
	 * @param ids
	 */
	@NativeUpdate
	Integer updateItemIsAddCategory(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("state") Integer state);

	/**
	 * 根据itemIds,更新是否加入标签状态为state
	 * 
	 * @param ids
	 */
	@NativeUpdate
	Integer updateItemIsAddTag(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("state") Integer state);

	/**
	 * 查询是否有相同的code
	 * 
	 * @param code
	 * @param code
	 * @return
	 */
	@NativeQuery(alias = "CNT", clazzes = Integer.class)
	Integer validateItemCode(@QueryParam("code") String code, @QueryParam("shopId") Long shopId);

	/**
	 * 分页获取店铺所有商品列表
	 * 
	 * @param page
	 * @param sort
	 * @param Map
	 * @param shopId
	 */
	@NativeQuery(model = ItemCommand.class)
	Pagination<ItemCommand> findItemListByQueryMap(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap, @QueryParam("shopId") Long shopId);

	
	
	/**
	 * 分页获取所有有效商品列表
	 * 
	 * @param page
	 * @param sort
	 * @param Map
	 * @param shopId
	 */
	@NativeQuery(model = ItemCommand.class)
	Pagination<ItemCommand> findEffectItemInfoListByQueryMap(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 获取商品信息
	 * 
	 * @param shopId
	 * @param itemId
	 */
	@NativeQuery(model = ItemCommand.class)
	ItemCommand findItemByShopIDItemID(@QueryParam("shopId") Long shopId, @QueryParam("itemId") Long itemId);

	/**
	 * 启用禁用商品
	 * 
	 * @param state
	 * @param ids
	 */
	@Deprecated
	@NativeUpdate
	Integer enableOrDisableItemByIds(@QueryParam("ids") List<Long> ids, @QueryParam("state") Integer state);

	/**
	 * 启用禁用商品
	 * 
	 * @param state
	 * @param ids
	 * @param updateListTimeFlag 是否更新list_time字段的标记（通过获取数据库中的配置信息而来的）
	 */
	@NativeUpdate
	Integer enableOrDisableItemByIds(@QueryParam("ids") List<Long> ids, @QueryParam("state") Integer state, @QueryParam("updateListTimeFlag") String updateListTimeFlag);

	
	/**
	 * (逻辑删除)
	 * 
	 * @param ids
	 */
	@NativeUpdate
	void removeItemByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过商品Id查询商品基本信息
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = ItemBaseCommand.class)
	ItemBaseCommand findItemBaseInfo(@QueryParam("itemId") Long itemId);
	
	@NativeQuery(model = ItemBaseCommand.class)
	ItemBaseCommand findItemBaseInfoLang(@QueryParam("itemId") Long itemId,@QueryParam("lang")String lang);

	/**
	 * 通过商品code查询商品基本信息
	 * 
	 * @param code
	 * @return
	 */
	@NativeQuery(model = ItemBaseCommand.class)
	ItemBaseCommand findItemBaseInfoByCode(@QueryParam("code") String code);
	
	@NativeQuery(model = ItemBaseCommand.class)
	ItemBaseCommand findItemBaseInfoByCodeLang(@QueryParam("code") String code, @QueryParam("lang")String lang);

	/**
	 * 通过商品Code集合查询商品集合
	 * 
	 * @param itemCodes
	 * @return
	 */
	@NativeQuery(model = Item.class)
	List<Item> findItemListByCodes(@QueryParam("itemCodes") List<String> itemCodes, @QueryParam("shopId") Long shopId);

	/**
	 * 根据itemId集合查询ItemCommand集合
	 * 
	 * @param itemId
	 *            id集合
	 * @return
	 */
	@NativeQuery(model = ItemCommand.class)
	List<ItemCommand> findItemCommandListByIds(@QueryParam("itemIds") List<Long> itemIds);

	/**
	 * 通过style查询同款商品
	 * 
	 * @param style
	 * @return
	 */
	@NativeQuery(model = ItemCommand.class)
	List<ItemCommand> findItemCommandByStyle(@QueryParam("style") String style);
	
	@NativeQuery(model = ItemCommand.class)
	List<ItemCommand> findItemCommandByStyleI18n(@QueryParam("style") String style,@QueryParam("lang")String lang);

	/**
	 * 通过Id查询商品信息
	 * 
	 * @param style
	 * @return
	 */
	@NativeQuery(model = ItemCommand.class)
	ItemCommand findItemCommandById(@QueryParam("itemId") Long itemId);

	/**
	 * 获取有效的Item列表 lifecycle != 2
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = Item.class)
	List<Item> findAllOnSalesItemList();

	/**
	 * 通过itemCodes查询itemCommand
	 * 
	 * @param itemCodeList
	 * @return
	 */
	@NativeQuery(model = ItemCommand.class)
	List<ItemCommand> findItemCommandByCodes(@QueryParam("itemCodes") List<String> itemCodes);

	@NativeQuery(model = Item.class)
	List<Item> findAllDeletedItemList();
	
	@NativeQuery(model = Item.class)
	List<Item> findEffectiveItemList();

	@NativeQuery(model = ProductInventoryCommand.class)
	List<ProductInventoryCommand> findInventoryByItemList(@QueryParam("itemIds") List<Long> itemIds,@QueryParam("shopId") Long shopId);
	
	
	/**
	 * 查询商品信息
	 * 
	 * @param page
	 * @param sort
	 * @param Map
	 * @param shopId
	 */
	@NativeQuery(model = ItemCommand.class)
	List<ItemCommand> findItemCommandByQueryMapAndItemCodes(@QueryParam Map<String, Object> paraMap, @QueryParam("itemCodeList") List<String> itemCodeList);

	/**
	 *  查询商品信息(可国际化的字段数据)
	 * @param paraMap
	 * @param itemCodeList
	 * @param langList
	 * @return
	 */
	@NativeQuery(model = ItemCommand.class)
	List<ItemCommand> findItemCommandByQueryMapAndItemCodesI18n(@QueryParam Map<String, Object> paraMap, 
			@QueryParam("itemCodeList") List<String> itemCodeList,
			@QueryParam("langKey") String langKey);
	
	@NativeQuery(model = Item.class)
	Item findItemByExtentionCode(@QueryParam("extentionCode") String extentionCode);

	
}
