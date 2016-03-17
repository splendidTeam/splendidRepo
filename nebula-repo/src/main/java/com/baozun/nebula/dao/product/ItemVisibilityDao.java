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

import com.baozun.nebula.command.product.ItemVisibilityCommand;
import com.baozun.nebula.model.product.ItemVisibility;

/**
 * ItemVisibilityDao
 * @author  何波
 *
 */
public interface ItemVisibilityDao extends GenericEntityDao<ItemVisibility,Long>{

	/**
	 * 获取所有ItemVisibility列表
	 * @return
	 */
	@NativeQuery(model = ItemVisibility.class)
	List<ItemVisibility> findAllItemVisibilityList();
	
	/**
	 * 通过ids获取ItemVisibility列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemVisibility.class)
	List<ItemVisibility> findItemVisibilityListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取ItemVisibility列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemVisibility.class)
	List<ItemVisibility> findItemVisibilityListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取ItemVisibility列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ItemVisibilityCommand.class)
	Pagination<ItemVisibilityCommand> findItemVisibilityListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用ItemVisibility
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableItemVisibilityByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除ItemVisibility
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeItemVisibilityByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的ItemVisibility列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemVisibility.class)
	List<ItemVisibility> findAllEffectItemVisibilityList();
	
	/**
	 * 通过参数map获取有效的ItemVisibility列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemVisibility.class)
	List<ItemVisibility> findEffectItemVisibilityListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ItemVisibility列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ItemVisibility.class)
	Pagination<ItemVisibility> findEffectItemVisibilityListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	@NativeQuery(model = ItemVisibilityCommand.class)
	List<ItemVisibilityCommand> findItemVisibilityCommand(@QueryParam Map<String, Object> params);
	
	
	/**
	 * 通过itemFilterIds获取ItemVisibility列表
	 * @param itemFilterIds
	 * @return
	 */
	@NativeQuery(model = ItemVisibility.class)
	List<ItemVisibility>  findItemVisibilityListByItemFilterIds(@QueryParam("itemFilterIds") List<String> itemFilterIds);
}
