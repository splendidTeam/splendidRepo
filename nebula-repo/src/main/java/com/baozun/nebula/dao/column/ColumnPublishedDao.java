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
package com.baozun.nebula.dao.column;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;
import com.baozun.nebula.model.column.ColumnPublished;

/**
 * ColumnPublishedDao
 * @author  Justin
 *
 */
public interface ColumnPublishedDao extends GenericEntityDao<ColumnPublished,Long>{

	/**
	 * 获取所有ColumnPublished列表
	 * @return
	 */
	@NativeQuery(model = ColumnPublished.class)
	List<ColumnPublished> findAllColumnPublishedList();
	
	/**
	 * 通过ids获取ColumnPublished列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnPublished.class)
	List<ColumnPublished> findColumnPublishedListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取ColumnPublished列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ColumnPublished.class)
	List<ColumnPublished> findColumnPublishedListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取ColumnPublished列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ColumnPublished.class)
	Pagination<ColumnPublished> findColumnPublishedListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用ColumnPublished
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableColumnPublishedByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除ColumnPublished
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeColumnPublishedByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的ColumnPublished列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnPublished.class)
	List<ColumnPublished> findAllEffectColumnPublishedList();
	
	/**
	 * 通过参数map获取有效的ColumnPublished列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ColumnPublished.class)
	List<ColumnPublished> findEffectColumnPublishedListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ColumnPublished列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ColumnPublished.class)
	Pagination<ColumnPublished> findEffectColumnPublishedListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
}
