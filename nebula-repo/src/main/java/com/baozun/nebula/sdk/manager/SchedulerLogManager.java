/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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

import java.util.Map;

import com.baozun.nebula.command.log.SchedulerLogCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.logs.SchedulerLog;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

public interface SchedulerLogManager  extends BaseManager	{
	/**
	 * 保存定时任务运行日志
	 * @param schedulerLog
	 */
	void saveSchedulerLog(SchedulerLog schedulerLog);
	
	/**
	 * 分页查询日志
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	Pagination<SchedulerLogCommand> findSchedulerLogListByQueryBeanWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
}
