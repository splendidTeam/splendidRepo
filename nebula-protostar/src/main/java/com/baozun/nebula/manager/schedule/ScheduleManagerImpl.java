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

import com.baozun.nebula.zk.ZooKeeperOperator;

/**
 * @author Tianlong.Zhang
 *
 */
@Service("scheduleManager")
public class ScheduleManagerImpl implements ScheduleManager{
	
	public static final String NOTICE_PATH=ZooKeeperOperator.LIFECYCLE_NODE+"/schedule";

	@Autowired
	protected ZooKeeperOperator zooKeeperOperator;
	
	@Override
	public boolean noticeTask(Long id) {
		String data = id.toString();
		boolean result = zooKeeperOperator.noticeZkServer(NOTICE_PATH, data);
		return result;
	}

}
