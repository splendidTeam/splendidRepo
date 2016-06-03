/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.ScheduleTaskWatchInvoker;


/**
 * @author Tianlong.Zhang
 *
 */
@Service("scheduleManager")
public class ScheduleManagerImpl implements ScheduleManager{
	
	@Autowired
	protected ZkOperator zkOperator;
	
	@Override
	public boolean noticeTask(Long id) {
		String data = id.toString();
		boolean result = zkOperator.noticeZkServer(zkOperator.getPath(ScheduleTaskWatchInvoker.PATH_KEY), data);
		return result;
	}

}
