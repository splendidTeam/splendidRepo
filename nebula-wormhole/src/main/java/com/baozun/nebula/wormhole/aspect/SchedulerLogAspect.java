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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.model.logs.SchedulerLog;
import com.baozun.nebula.model.system.SchedulerTask;
import com.baozun.nebula.sdk.manager.SchedulerLogManager;
import com.baozun.nebula.sdk.manager.SdkSchedulerTaskManager;

@Aspect
public class SchedulerLogAspect {
	
	@Autowired
    private SdkSchedulerTaskManager sdkSchedulerTaskManager;
	
	@Autowired
	private SchedulerLogManager schedulerLogManager;
	
	@Around("execution(* com.baozun.nebula.wormhole.scm.timing..*.*(..))")
	public Object saveLog(ProceedingJoinPoint pjp) throws Throwable{
		String methodName = pjp.getSignature().getName();
		SchedulerTask task = sdkSchedulerTaskManager.findSchedulerTaskByMethodName(methodName);
		if(task==null)
			return pjp.proceed();
		SchedulerLog log = new SchedulerLog();
		log.setTaskId(task.getId());
		log.setBeginTime(new Date());
		Object result = pjp.proceed();
		log.setEndTime(new Date());
		log.setExecutionTime(log.getEndTime().getTime()-log.getBeginTime().getTime());
		schedulerLogManager.saveSchedulerLog(log);
		return result;
	}
}
