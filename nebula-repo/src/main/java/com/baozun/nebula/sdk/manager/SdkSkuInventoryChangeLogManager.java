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

import com.baozun.nebula.command.product.SkuInventoryChangeLogCommand;
import com.baozun.nebula.model.product.SkuInventoryChangeLog;

/**
 * SkuInventoryChangeLogManager
 * @author  dongliang.ma
 *
 */
public interface SdkSkuInventoryChangeLogManager {

	/**
	 * 保存SkuInventoryChangeLog
	 * 
	 */
	SkuInventoryChangeLog saveSkuInventoryChangeLog(SkuInventoryChangeLog model);
	
	/**
	 * 通过id获取SkuInventoryChangeLog
	 * 
	 */
	SkuInventoryChangeLog findSkuInventoryChangeLogById(Long id);

	/**
	 * 获取所有SkuInventoryChangeLog列表
	 * @return
	 */
	List<SkuInventoryChangeLog> findAllSkuInventoryChangeLogList();
	
	/**
	 * 通过ids获取SkuInventoryChangeLog列表
	 * @param ids
	 * @return
	 */
	List<SkuInventoryChangeLog> findSkuInventoryChangeLogListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取SkuInventoryChangeLog列表
	 * @param paraMap
	 * @return
	 */
	List<SkuInventoryChangeLog> findSkuInventoryChangeLogListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取SkuInventoryChangeLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<SkuInventoryChangeLogCommand> findSkuInventoryChangeLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	/**
	 * 删除一个月前数据
	 */
	void clearSkuInventoryChangeLogOneMonthAgo();
}
