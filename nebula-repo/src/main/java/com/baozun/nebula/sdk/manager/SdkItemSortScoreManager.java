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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.ItemSortScoreCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.ItemSortScore;

/**
 * ItemSortScoreManager
 * @author  Justin
 *
 */
public interface SdkItemSortScoreManager extends BaseManager{

	/**
	 * 保存ItemSortScore
	 * 
	 */
	ItemSortScore saveItemSortScore(ItemSortScore model);
	
	/**
	 * 通过id获取ItemSortScore
	 * 
	 */
	ItemSortScore findItemSortScoreById(Long id);

	/**
	 * 获取所有ItemSortScore列表
	 * @return
	 */
	List<ItemSortScore> findAllItemSortScoreList();
	
	/**
	 * 通过ids获取ItemSortScore列表
	 * @param ids
	 * @return
	 */
	List<ItemSortScore> findItemSortScoreListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取ItemSortScore列表
	 * @param paraMap
	 * @return
	 */
	List<ItemSortScore> findItemSortScoreListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取ItemSortScore列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<ItemSortScore> findItemSortScoreListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用ItemSortScore
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableItemSortScoreByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除ItemSortScore
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removeItemSortScoreByIds(List<Long> ids);
	
	/**
	 * 获取有效的ItemSortScore列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<ItemSortScore> findAllEffectItemSortScoreList();
	
	/**
	 * 通过参数map获取有效的ItemSortScore列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<ItemSortScore> findEffectItemSortScoreListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ItemSortScore列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<ItemSortScore> findEffectItemSortScoreListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	Pagination<ItemSortScoreCommand> findEffectItemSortCommandScoreListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
}
