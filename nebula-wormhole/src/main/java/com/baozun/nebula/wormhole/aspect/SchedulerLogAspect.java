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
package com.baozun.nebula.wormhole.aspect;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.constant.SchedulerConstants;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.logs.SchedulerLog;
import com.baozun.nebula.model.system.SchedulerTask;
import com.baozun.nebula.sdk.manager.SchedulerLogManager;
import com.baozun.nebula.sdk.manager.SdkSchedulerTaskManager;
import com.feilong.core.Validator;

@SuppressWarnings("all")
@Aspect
public class SchedulerLogAspect {

	@Autowired
	private SdkSchedulerTaskManager sdkSchedulerTaskManager;

	@Autowired
	private SchedulerLogManager schedulerLogManager;

	@Autowired
	private CacheManager cacheManager;

	@Around("execution(* com.baozun.nebula.wormhole.scm.timing..*.*(..))")
	public Object saveLog(ProceedingJoinPoint pjp) throws Throwable {
		String methodName = pjp.getSignature().getName();
		SchedulerTask task = getTasks().get(methodName);
		if (task == null)
			return pjp.proceed();
		SchedulerLog log = new SchedulerLog();
		log.setTaskId(task.getId());
		log.setBeginTime(new Date());
		Object result = pjp.proceed();
		log.setEndTime(new Date());
		log.setExecutionTime(log.getEndTime().getTime() - log.getBeginTime().getTime());
		schedulerLogManager.saveSchedulerLog(log);
		return result;
	}

	private Map<String, SchedulerTask> getTasks() {
		Map<String, SchedulerTask> tasks = (HashMap<String, SchedulerTask>) cacheManager.getObject(SchedulerConstants.SCHEDULERTASKS);
		if (Validator.isNullOrEmpty(tasks)) {
			List<SchedulerTask> taskList = sdkSchedulerTaskManager.findAllEffectSchedulerTaskList();
			tasks = new HashMap<String, SchedulerTask>();
			if (Validator.isNotNullOrEmpty(taskList)) {
				for (SchedulerTask task : taskList) {
					if (tasks != null && !tasks.containsKey(task.getMethodName())) {
						tasks.put(task.getMethodName(), task);
					} else
						continue;
				}
				cacheManager.setObject(SchedulerConstants.SCHEDULERTASKS, tasks, SchedulerConstants.TASKS_CACHE_EXPIRESECONDS);
			}
		}
		return tasks;
	}
}
