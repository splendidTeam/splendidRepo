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
import com.baozun.nebula.model.column.ColumnPage;

/**
 * ColumnPageDao
 * @author  Justin
 *
 */
public interface ColumnPageDao extends GenericEntityDao<ColumnPage,Long>{

	/**
	 * 获取所有ColumnPage列表
	 * @return
	 */
	@NativeQuery(model = ColumnPage.class)
	List<ColumnPage> findAllColumnPageList();
	
	/**
	 * 通过ids获取ColumnPage列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnPage.class)
	List<ColumnPage> findColumnPageListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取ColumnPage列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ColumnPage.class)
	List<ColumnPage> findColumnPageListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取ColumnPage列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ColumnPage.class)
	Pagination<ColumnPage> findColumnPageListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用ColumnPage
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableColumnPageByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除ColumnPage
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeColumnPageByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的ColumnPage列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnPage.class)
	List<ColumnPage> findAllEffectColumnPageList();
	
	/**
	 * 通过参数map获取有效的ColumnPage列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ColumnPage.class)
	List<ColumnPage> findEffectColumnPageListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ColumnPage列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ColumnPage.class)
	Pagination<ColumnPage> findEffectColumnPageListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过页面code, 查询columnPage信息
	 * @return
	 */
	@NativeQuery(model = ColumnPage.class)
	ColumnPage findColumnPageByPageCode(@QueryParam("pageCode") String pageCode);
}
