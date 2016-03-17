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

import com.baozun.nebula.command.TagCommand;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.product.ItemTag;

/**
 * ItemTagDao
 * @author  Justin
 *
 */
public interface ItemTagDao extends GenericEntityDao<ItemTag,Long>{

	/**
	 * 获取所有ItemTag列表
	 * @return
	 */
	@NativeQuery(model = ItemTag.class)
	List<ItemTag> findAllItemTagList();
	
	/**
	 * 通过ids获取ItemTag列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemTag.class)
	List<ItemTag> findItemTagListByIds(@QueryParam("ids")Long[] ids);
	
	/**
	 * 通过参数map获取ItemTag列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemTag.class)
	List<ItemTag> findItemTagListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取ItemTag列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ItemTag.class)
	Pagination<ItemTag> findItemTagListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用ItemTag
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableItemTagByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除ItemTag
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeItemTagByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的ItemTag列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemTag.class)
	List<ItemTag> findAllEffectItemTagList();
	
	/**
	 * 通过参数map获取有效的ItemTag列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = TagCommand.class)
	List<TagCommand> findEffectItemTagListByQueryMap(@QueryParam Map<String, Object> paraMap,Sort[] sorts);
	
	/**
	 * 分页获取有效的ItemTag列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ItemTag.class)
	Pagination<ItemTag> findEffectItemTagListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 验证标签名称
	 * @param groupName
	 * @return
	 */
	@NativeQuery(model = ItemTag.class)
	ItemTag validateTagName(@QueryParam("tagName")String tagName);
	
	
	/**
	 * 通过ids批量删除ItemTag
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer removeTagByIds(@QueryParam("ids")List<Long> ids);
	
	
}
