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

import com.baozun.nebula.command.product.SkuInventoryChangeLogCommand;
import com.baozun.nebula.model.product.SkuInventoryChangeLog;

/**
 * SkuInventoryChangeLogDao
 * @author  dongliang.ma
 *
 */
public interface SkuInventoryChangeLogDao extends GenericEntityDao<SkuInventoryChangeLog,Long>{

	/**
	 * 获取所有SkuInventoryChangeLog列表
	 * @return
	 */
	@NativeQuery(model = SkuInventoryChangeLog.class)
	List<SkuInventoryChangeLog> findAllSkuInventoryChangeLogList();
	
	/**
	 * 通过ids获取SkuInventoryChangeLog列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = SkuInventoryChangeLog.class)
	List<SkuInventoryChangeLog> findSkuInventoryChangeLogListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取SkuInventoryChangeLog列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = SkuInventoryChangeLog.class)
	List<SkuInventoryChangeLog> findSkuInventoryChangeLogListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取SkuInventoryChangeLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = SkuInventoryChangeLogCommand.class)
	Pagination<SkuInventoryChangeLogCommand> findSkuInventoryChangeLogListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	@NativeUpdate
	void clearSkuInventoryChangeLogOneMonthAgo();
	
	
	
}
