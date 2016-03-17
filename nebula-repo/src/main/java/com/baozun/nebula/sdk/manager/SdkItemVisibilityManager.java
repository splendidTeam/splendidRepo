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
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.ItemVisibilityCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.ItemVisibility;

/**
 * ItemVisibilityManager
 * @author  何波
 *
 */
public interface SdkItemVisibilityManager extends BaseManager{

	/**
	 * 保存ItemVisibility
	 * 
	 */
	ItemVisibility saveItemVisibility(ItemVisibility model);
	
	/**
	 * 通过id获取ItemVisibility
	 * 
	 */
	ItemVisibility findItemVisibilityById(Long id);
	ItemVisibility findItemVisibilityByMemFilterId(Long memFilterId);
	ItemVisibility findItemVisibilityByItemFilterId(Long itemFilterId,Long memFilterId);
	
	/**
	 * 通过id获取ItemVisibility
	 * 
	 */
	ItemVisibilityCommand findItemVisibilityCommandbyId(Long id);

	/**
	 * 获取所有ItemVisibility列表
	 * @return
	 */
	List<ItemVisibility> findAllItemVisibilityList();
	
	/**
	 * 通过ids获取ItemVisibility列表
	 * @param ids
	 * @return
	 */
	List<ItemVisibility> findItemVisibilityListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取ItemVisibility列表
	 * @param paraMap
	 * @return
	 */
	List<ItemVisibility> findItemVisibilityListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取ItemVisibilityCommand列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<ItemVisibilityCommand> findItemVisibilityListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用ItemVisibility
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableItemVisibilityByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除ItemVisibility
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removeItemVisibilityByIds(List<Long> ids);
	
	/**
	 * 获取有效的ItemVisibility列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<ItemVisibility> findAllEffectItemVisibilityList();
	
	/**
	 * 通过参数map获取有效的ItemVisibility列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<ItemVisibility> findEffectItemVisibilityListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ItemVisibility列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<ItemVisibility> findEffectItemVisibilityListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	/**
	 * 通过itemFilterIds获取ItemVisibility列表
	 * @param itemFilterIds
	 * @return
	 */
	List<ItemVisibility>  findItemVisibilityListByItemFilterIds(List<String> itemFilterIds);
	
	
	/**
	* @author 何波
	* @Description:通过itemid获取人群筛选器ids 返回null则商品没有可见性关系,全场可见
	* @param itemId
	* @return   
	* List<Long>   
	* @throws
	 */
	Set<Long> getMembersByItemId(Long itemId);
	/**
	 * 
	* @author 何波
	* @Description: 验证用户是否有权限访问指定商品,游客时memId传入null
	* @param itemId 商品id
	* @param memId 会员id 
	* @return   
	* Boolean   
	* @throws
	 */
	Boolean checkItemIsVisibility(Long itemId,Long memId);
}
