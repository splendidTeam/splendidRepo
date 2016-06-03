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
package com.baozun.nebula.curator.invoke;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.manager.SchedulerManager;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.system.SchedulerTask;
import com.baozun.nebula.sdk.manager.SdkSchedulerTaskManager;
import com.baozun.nebula.utils.spring.SpringUtil;
import com.feilong.core.Validator;

/**
 * 定时任务 执行者
 * @see com.baozun.nebula.zk.ScheduleTaskWatchInvoker
 * @author chengchao
 *
 */
public class ScheduleTaskWatchInvoker implements IWatcherInvoke {
	private Logger LOG = LoggerFactory.getLogger(ScheduleTaskWatchInvoker.class);
	
	public static final String PATH_KEY = "/scheduletaskwatch";
	
	@Autowired
	private SchedulerManager schedulerManager;
	
	@Autowired
	private SdkSchedulerTaskManager sdkSchedulerTaskManager;
	
	@Autowired
	private ZkOperator zkOperator;

	
	private void handleTask(SchedulerTask schedulerTask) throws Exception{
		Integer lifeCycle = schedulerTask.getLifecycle();
		String taskBeanName = schedulerTask.getBeanName();
		String methodName = schedulerTask.getMethodName();
		String jobName = schedulerTask.getCode();
		String timeExp = schedulerTask.getTimeExp();
		
		if(BaseModel.LIFECYCLE_ENABLE.equals(lifeCycle)){//如果是启用状态 1
			
			schedulerManager.removeTask(jobName);
			
			Object taskInstance = SpringUtil.getBean(taskBeanName);
			schedulerManager.addTask(taskInstance, methodName, timeExp, jobName);
			
		}else if(BaseModel.LIFECYCLE_DELETED.equals(lifeCycle)||BaseModel.LIFECYCLE_DISABLE.equals(lifeCycle)){//如果是禁用0或者逻辑删除2
			schedulerManager.removeTask(jobName);
		}
	}

	@Override
	public void invoke(String path, byte[] data) {
		try {
			byte[] datas = zkOperator.getZkData(path);
			Long id = Long.parseLong(new String(datas));
			
			// 根据Id 来做不同的事情
			if(id!=0){
				SchedulerTask schedulerTask = sdkSchedulerTaskManager.findSchedulerTaskById(id);
				if(null!=schedulerTask){
					LOG.info("handling task " + schedulerTask.getCode()+" begin");
					handleTask(schedulerTask);
					LOG.info("handling task " + schedulerTask.getCode()+" end");
				}else{
					LOG.warn("can't find taskinfo ,task id is "+id);
				}
			}else{//id=0 全部刷新
				List<SchedulerTask> taskList = sdkSchedulerTaskManager.findAllEffectSchedulerTaskList();
				
				//先清理掉所有的定时任务
				schedulerManager.timerClean();
				
				//遍历所有启用的任务，加载到定时计划中
				if(Validator.isNotNullOrEmpty(taskList)){
					for(SchedulerTask task:taskList){
						handleTask(task);
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

}
