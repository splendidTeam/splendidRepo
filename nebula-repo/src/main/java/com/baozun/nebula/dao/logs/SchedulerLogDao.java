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
package com.baozun.nebula.dao.logs;

import java.util.Map;

import com.baozun.nebula.command.log.SchedulerLogCommand;
import com.baozun.nebula.model.logs.SchedulerLog;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 定时任务运行日志
 * @author jin.wang
 *
 */
public interface SchedulerLogDao extends GenericEntityDao<SchedulerLog, Long> {
	/**
	 * 分页查询定时任务日志
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model=SchedulerLogCommand.class)
	Pagination<SchedulerLogCommand> findSchedulerLogListByQueryBeanWithPage(Page page, Sort[] sorts,
			@QueryParam Map<String, Object> paraMap);

}
