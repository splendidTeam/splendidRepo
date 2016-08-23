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

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;

/**
 * 商品分类关系Dao
 * 
 * @author yi.huang
 * @date 2013-6-28 下午04:24:29
 */
public interface ItemCategoryDao extends GenericEntityDao<ItemCategory, Long> {

	/**
	 * 将一个商品关联到一个商品分类下
	 * 
	 * @param itemId
	 *            商品id
	 * @param categoryId
	 *            分类id
	 * @return
	 */
	@NativeUpdate
	Integer bindItemCategory(@QueryParam("itemId") Long itemId, @QueryParam("categoryId") Long categoryId, @QueryParam("isDefault") Boolean isDefault);

	/**
	 * 把一个或者多个商品从一个分类下解除关联
	 * 
	 * @param itemIds
	 *            商品id 数组
	 * @param categoryId
	 *            分类id
	 * @return
	 */
	@NativeUpdate
	Integer unBindItemCategory(@QueryParam("itemIds") Long[] itemIds, @QueryParam("categoryId") Long categoryId);

	/**
	 * 查询出所有 商品id 在itemIds和 分类id 在caztegoryIds中的ItemCategory
	 * 
	 * @param itemIds
	 *            商品id 数组
	 * @param categoryIds
	 *            分类id 数组
	 * @return
	 */
	@NativeQuery(model = ItemCategory.class)
	List<ItemCategory> findItemCategoryByItemIdAndCategoryId(@QueryParam("itemIds") Long[] itemIds, @QueryParam("categoryIds") Long[] categoryIds);

	/**
	 * 分页查找某个商品分类下的商品
	 * 
	 * @param categoryId
	 *            商品分类 id
	 * @param pager
	 *            分页数据
	 * @param sorts
	 *            排序数据
	 * @return
	 */
	@NativeQuery(model = Item.class)
	Pagination<Item> findItemListByCategoryId(@QueryParam("categoryId") Long categoryId, Page pager, Sort[] sorts);

	/**
	 * 查找某个商品分类下的商品
	 * 
	 * @param categoryId
	 *            商品分类 id
	 */
	@NativeQuery(model = Item.class)
	List<Item> findCategoryItemListByCategoryId(@QueryParam("categoryId") List<Long> categoryId);

	/**
	 * 删除当前itemId所属分类条目
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeUpdate
	Integer deleteItemCategoryByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 删除当前分类ids所属分类条目
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeUpdate
	Integer deleteItemCategoryByCategoryIds(@QueryParam("categoryIds") List<Long> categoryIds);

	/**
	 * 查找当前itemId所有分类条目
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = ItemCategory.class)
	List<ItemCategory> findItemCategoryListByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 查找itemIds所有分类条目
	 * 
	 * @param itemIds
	 * @return
	 */
	@NativeQuery(model = ItemCategory.class)
	List<ItemCategory> findItemCategoryListByItemIds(@QueryParam("itemIds") List<Long> itemIds);

	/**
	 * 分页获取有分类商品列表: (可按分类categoryId查询商品) 1.同个商品不同分类的为一条数据，分类名定为空
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */

	@NativeQuery(model = ItemCommand.class)
	Pagination<ItemCommand> findItemListCategoryByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap, @QueryParam("shopId") Long shopId);
	
	@NativeQuery(model = ItemCommand.class)
	Pagination<ItemCommand> findItemListEmptyCategoryByQueryMapWithPageNoShopid(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	@NativeQuery(model = ItemCommand.class)
	Pagination<ItemCommand> findItemListEmptyCategoryByQueryMapWithPageByCIDS(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap, @QueryParam("shopId") Long shopId);

	/**
	 * 根据itemId查询ItemCommand
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = ItemCommand.class)
	// ItemCommand findItemListEmptyCategoryByQueryItemId(@QueryParam("itemId")
	// Long itemId);
	ItemCommand findItemCommandByQueryItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 分页获取商品列表: 2.商品id和分类
	 * 
	 */
	@NativeQuery(model = ItemCategoryCommand.class)
	List<ItemCategoryCommand> findAllItemCategoryList();

	/**
	 * 
	 * 根据ItemId获取ItemCategoryCommand
	 * 
	 */
	@NativeQuery(model = ItemCategoryCommand.class)
	List<ItemCategoryCommand> findAllItemCategoryListByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 分页获取商品列表: 未分类商品
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @param ids
	 *            已分类商品ids
	 * @return
	 */

	@NativeQuery(model = ItemCommand.class)
	Pagination<ItemCommand> findItemNoctListByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap, @QueryParam("shopId") Long shopId);

	/**
	 * 根据商品id查询商品分类
	 * 
	 * @param itemId
	 * @return :ItemCategory
	 * @date 2014-2-19 下午01:03:37
	 */
	@NativeQuery(model = ItemCategoryCommand.class)
	ItemCategoryCommand findDefaultCategoryByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 查询默认分类Id为categoryId的商品
	 * 
	 * @param categoryId
	 * @return :List<ItemCategory>
	 * @date 2014-2-19 下午04:57:43
	 */
	@NativeQuery(model = ItemCategory.class)
	List<ItemCategory> findItemByDefaultCategoryId(@QueryParam("categoryId") Long categoryId);

	/**
	 * 查询商品的默认分类
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = ItemCategory.class)
	ItemCategory findDefaultCateoryByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 根据分类ID列表查询所包含的有效商品
	 * 
	 * @param cidList
	 * @param shopId
	 * @return
	 */
	@NativeQuery(alias = "id", clazzes = Long.class)
	List<Long> findEffectItemIdListByCategoryIdList(@QueryParam("cidList") List<Long> cidList, @QueryParam("shopId") Long shopId);

	/**
	 * 查询所有有效的商品ID列表
	 * 
	 * @param shopId
	 * @return
	 */
	@NativeQuery(alias = "id", clazzes = Long.class)
	List<Long> findAllEffectProductIdListByShopId(@QueryParam("shopId") Long shopId);

	/**
	 * 查询所有有效的商品ID列表
	 * 
	 * @param categoryIds
	 *            商品分类Id
	 * @return 去重后的商品id列表
	 */
	@NativeQuery(alias = "itemId", clazzes = Long.class)
	List<Long> findDistinctItemIdByCategory(@QueryParam("categoryIds") List<Long> categoryIds);
	
	/**
	 * 查询与该分类Id关联的商品Id
	 * @param categoryId
	 * @return
	 */
	@NativeQuery(model = ItemCategory.class)
	List<ItemCategory> findItemCategoryListByCategoryIds(@QueryParam("categoryIds") List<Long> categoryIds);
	
	/**
	 * 根据商品编码集合和行业ID, 店铺ID查询商品的分类行业信息
	 * @param itemCodeList
	 * @param queryMap
	 * @return
	 */
	@NativeQuery(model = ItemCategoryCommand.class)
	List<ItemCategoryCommand> findItemCategoryCommandByQueryMap(@QueryParam("itemCodeList") List<String> itemCodeList, @QueryParam() Map<String, Object> queryMap);
	
	
	/**
	 * 根据商品id查询商品分类
	 * 
	 * @param itemIds
	 * @return :ItemCategory
	 * @date 
	 */
	@NativeQuery(model = ItemCategoryCommand.class)
	List<ItemCategoryCommand> findDefaultCategoryByItemIds(@QueryParam("itemIds") List<Long> itemIds);
}
