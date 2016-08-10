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
package com.baozun.nebula.sdk.manager.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.log.SchedulerLogCommand;
import com.baozun.nebula.dao.logs.SchedulerLogDao;
import com.baozun.nebula.model.logs.SchedulerLog;
import com.baozun.nebula.sdk.manager.SchedulerLogManager;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

@Transactional
@Service("schedulerLogManager")
public class SchedulerLogManagerImpl implements SchedulerLogManager {
	
	@Autowired
	private SchedulerLogDao schedulerLogDao;

	@Override
	public void saveSchedulerLog(SchedulerLog schedulerLog) {
		schedulerLogDao.save(schedulerLog);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<SchedulerLogCommand> findSchedulerLogListByQueryBeanWithPage(Page page, Sort[] sorts,
			Map<String, Object> paraMap) {
		Pagination<SchedulerLogCommand> result = schedulerLogDao.findSchedulerLogListByQueryBeanWithPage(page,sorts,paraMap);
		return result;
	}
	
}
