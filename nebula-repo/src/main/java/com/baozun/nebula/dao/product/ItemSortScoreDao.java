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
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;
import com.baozun.nebula.model.product.ItemSortScore;

/**
 * ItemSortScoreDao
 * @author  Justin
 *
 */
public interface ItemSortScoreDao extends GenericEntityDao<ItemSortScore,Long>{

	/**
	 * 获取所有ItemSortScore列表
	 * @return
	 */
	@NativeQuery(model = ItemSortScore.class)
	List<ItemSortScore> findAllItemSortScoreList();
	
	/**
	 * 通过ids获取ItemSortScore列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemSortScore.class)
	List<ItemSortScore> findItemSortScoreListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取ItemSortScore列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemSortScore.class)
	List<ItemSortScore> findItemSortScoreListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取ItemSortScore列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ItemSortScore.class)
	Pagination<ItemSortScore> findItemSortScoreListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用ItemSortScore
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableItemSortScoreByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除ItemSortScore
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeItemSortScoreByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的ItemSortScore列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemSortScore.class)
	List<ItemSortScore> findAllEffectItemSortScoreList();
	
	/**
	 * 通过参数map获取有效的ItemSortScore列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemSortScore.class)
	List<ItemSortScore> findEffectItemSortScoreListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ItemSortScore列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ItemSortScore.class)
	Pagination<ItemSortScore> findEffectItemSortScoreListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
}
