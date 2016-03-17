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
import com.baozun.nebula.command.ItemTagRelationCommand;
import com.baozun.nebula.model.product.ItemTagRelation;

/**
 * ItemTagRelationDao
 * 
 * @author Justin
 * 
 */
public interface ItemTagRelationDao extends GenericEntityDao<ItemTagRelation, Long> {

	/**
	 * 获取所有ItemTagRelation列表
	 * 
	 * @return
	 */
	@NativeQuery(model = ItemTagRelationCommand.class)
	List<ItemTagRelationCommand> findAllItemTagRelationList();

	/**
	 * 
	 * 根据ItemId查询ItemTagRelationCommand
	 */
	@NativeQuery(model = ItemTagRelationCommand.class)
	List<ItemTagRelationCommand> findAllItemTagRelationListByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 通过ids获取ItemTagRelation列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemTagRelation.class)
	List<ItemTagRelation> findItemTagRelationListByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过参数map获取ItemTagRelation列表
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemTagRelation.class)
	List<ItemTagRelation> findItemTagRelationListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取ItemTagRelation列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = ItemTagRelation.class)
	Pagination<ItemTagRelation> findItemTagRelationListByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 通过ids批量启用或禁用ItemTagRelation 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableItemTagRelationByIds(@QueryParam("ids") List<Long> ids, @QueryParam("state") Integer state);

	/**
	 * 通过ids批量删除ItemTagRelation 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeItemTagRelationByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 获取有效的ItemTagRelation列表 lifecycle =1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemTagRelation.class)
	List<ItemTagRelation> findAllEffectItemTagRelationList();

	/**
	 * 通过参数map获取有效的ItemTagRelation列表 强制加上lifecycle =1
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemTagRelation.class)
	List<ItemTagRelation> findEffectItemTagRelationListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取有效的ItemTagRelation列表 强制加上lifecycle =1
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = ItemTagRelation.class)
	Pagination<ItemTagRelation> findEffectItemTagRelationListByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取有标签商品列表: (可按分类tagId查询商品) 1.同个商品不同标签的为一条数据，标签名定为空
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */

	@NativeQuery(model = ItemCommand.class)
	Pagination<ItemCommand> findItemListEmptyTagByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap, @QueryParam("shopId") Long shopId);

	/**
	 * 分页获取商品列表: 未标签商品
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @param shopId
	 *            店铺id
	 * @return
	 */

	@NativeQuery(model = ItemCommand.class)
	Pagination<ItemCommand> findItemListNoTagByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap, @QueryParam("shopId") Long shopId);

	/**
	 * 查询出所有 商品id 在itemIds和 标签id 在tagIds中的ItemTagRelation
	 * 
	 * @param itemIds
	 *            商品id 数组
	 * @param tagIds
	 *            标签id 数组
	 * @return
	 */
	@NativeQuery(model = ItemTagRelation.class)
	List<ItemTagRelation> findItemTagRelationByItemIdAndCategoryId(@QueryParam("itemIds") Long[] itemIds, @QueryParam("tagIds") Long[] tagIds);

	/**
	 * 将一个商品关联到一个商品标签下
	 * 
	 * @param itemId
	 *            商品id
	 * @param tagId
	 *            标签id
	 * @return
	 */
	@NativeUpdate
	Integer bindItemTag(@QueryParam("itemId") Long itemId, @QueryParam("tagId") Long tagId);

	/**
	 * 把一个或者多个商品从一个标签下解除关联
	 * 
	 * @param itemIds
	 *            商品id 数组
	 * @param tagId
	 *            标签id
	 * @return
	 */
	@NativeUpdate
	Integer unBindItemTag(@QueryParam("itemIds") Long[] itemIds, @QueryParam("tagId") Long tagId);

	/**
	 * 查找当前itemId所有标签条目
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = ItemTagRelation.class)
	List<ItemTagRelation> findItemTagRelationListByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 查找itemIds所有标签条目
	 * 
	 * @param itemIds
	 * @return
	 */
	@NativeQuery(model = ItemTagRelation.class)
	List<ItemTagRelation> findItemTagRelationListByItemIds(@QueryParam("itemIds") List<Long> itemIds);
}
