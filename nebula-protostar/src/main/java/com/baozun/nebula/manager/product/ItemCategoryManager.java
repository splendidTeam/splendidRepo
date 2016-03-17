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
package com.baozun.nebula.manager.product;

import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.product.ItemInfoCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.web.command.ItemCategoryResultCommand;

/**
 *商品分类关系manager
 * 
 * @author yi.huang
 * @date 2013-6-28 下午04:04:46
 */
public interface ItemCategoryManager extends BaseManager{
	
	Long getDefaultItemCategoryId(Long[] categoryIds);

	/**
	 * 将一组商品关联到一个或者多个分类下
	 * 
	 * @param itemIds
	 *            商品id 数组
	 * @param categoryIds
	 *            分类id数组
	 * @return 详细记录 操作 失败 重复 成功情况,以供前台文案提示展示
	 */
	ItemCategoryResultCommand bindItemCategory(Long[] itemIds,Long[] categoryIds);

	/**
	 * 把一个或者多个商品从一个分类下解除关联(关系表物理删除)
	 * 
	 * @param itemIds
	 *            商品id 数组
	 * @param categoryId
	 *            分类id
	 * @return
	 */
	boolean unBindItemCategory(Long[] itemIds,Long categoryId);

	/**
	 * 根据分类id查询该分类关联的商品
	 * 
	 * @param categoryId
	 *            分类的id
	 * @param page
	 *            分页
	 * @param sorts
	 *            排序
	 * @return
	 */
	Pagination<Item> findItemListByCategoryId(Long categoryId,Page page,Sort[] sorts);
	
	/**
	 * 删除当前itemId所属分类条目
	 * @param itemId
	 * @return
	 */
	int deleteItemCategoryByItemId(Long itemId);
	
	
	/**
	 * 根据itemId查找CategoryList
	 * @param itemId
	 * @return
	 */
	List<ItemCategory> findItemCategoryListByItemId(@QueryParam("itemId") Long itemId);
	
	/**
	 * 获取已分类商品
	 * 		单个商品属于多个分类，分类按","号隔开
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */

	Pagination<ItemCommand> findItemCtListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId);
	/**
	 * 获取已分类商品*/
	Pagination<ItemCommand> findItemCtListByQueryMapWithPageByCIDS(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId);
	
	/**
	 * 获取未分类商品
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	Pagination<ItemCommand> findItemNoctListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId);
	/**
	 * 对一个商品关联分类
	 * @param itemId
	 * @param categoryIds
	 * @return
	 */
	int createOrUpdateItemCategory(ItemCommand itemCommand,Long itemId,Long[] categoryIds);
	
	int createOrUpdateItemCategory(ItemInfoCommand itemCommand,Long itemId,Long[] categoryIds);
	/**
	 * 校验脱离分类时选择的商品
	 * @param itemIds
	 * @param categoryId
	 * @return
	 */
	Boolean validateUnBindByItemIdsAndCategoryId(Long[] itemIds,Long categoryId);
	
	/**
	 * 根据商品id查询商品分类
	 * @param itemId
	 * @return	:ItemCategory
	 * @date 2014-2-19 下午12:57:40
	 */
	ItemCategoryCommand findDefaultCategoryByItemId(Long itemId);
	
	/**
	 * 对一个商品关联默认分类
	 * @param itemCommand
	 * @param itemId
	 * @param defaultCategoryId
	 * @return	:Integer
	 * @date 2014-2-19 下午03:38:47
	 */
	Integer createOrUpdateItemDefaultCategory(ItemCommand itemCommand,Long itemId,Long defaultCategoryId);
	
	/**
	 * 对一个商品关联默认分类
	 * @param itemCommand
	 * @param itemId
	 * @param defaultCategoryId
	 * @return	:Integer
	 * @date 2014-2-19 下午03:38:47
	 */
	Integer createOrUpdateItemDefaultCategory(ItemInfoCommand itemCommand,Long itemId,Long defaultCategoryId);
	/**
	 * 查询默认分类Id为categoryId的商品
	 * @param categoryId
	 * @return	:List<ItemCategory>
	 * @date 2014-2-19 下午04:59:42
	 */
	List<ItemCategory> findItemByDefaultCategoryId(Long categoryId);
	
	/**
	 * 查询与该分类Id关联的商品
	 * @param categoryId
	 * @return
	 */
	List<ItemCategory> findItemCategoryListByCategoryId(Long categoryId);
	
	Pagination<ItemCommand> findItemListEmptyCategoryByQueryMapWithPageNoShopid(Page page, Sort[] sorts,
			Map<String, Object> paraMap);
	
	
	/**
	 * 将多个商品设置一个默认分类,其实就是另一种特殊形式的绑定
	 * @param itemIds
	 * @param categoryId
	 * @return
	 */
	ItemCategoryResultCommand setDefaultCategory(Long[] itemIds,Long categoryId);
	
}
