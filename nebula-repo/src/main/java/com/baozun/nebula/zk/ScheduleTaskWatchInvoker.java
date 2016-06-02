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
package com.baozun.nebula.zk;

import java.util.List;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.manager.SchedulerManager;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.system.SchedulerTask;
import com.baozun.nebula.sdk.manager.SdkSchedulerTaskManager;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.spring.SpringUtil;

/**
 * 定时任务 执行者
 * @author Tianlong.Zhang
 *
 */
@Deprecated
public class ScheduleTaskWatchInvoker extends WatchInvoke {
	private static final Logger			log	= LoggerFactory.getLogger(ScheduleTaskWatchInvoker.class);
	
	public static final String LISTEN_PATH=ZooKeeperOperator.LIFECYCLE_NODE+"/schedule";
	
	@Autowired
	private SchedulerManager schedulerManager;
	
	@Autowired
	private SdkSchedulerTaskManager sdkSchedulerTaskManager;
	
	@Override
	public boolean isMatch(String path, EventType type) {
		if(path!=null&&path.startsWith(LISTEN_PATH)&&type==EventType.NodeDataChanged){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public boolean needContinueWatch(String path, EventType type,
			KeeperState state) {
		return isMatch(path,type);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.zk.WatchInvoke#getListenPath()
	 */
	@Override
	public String getListenPath() {
		return LISTEN_PATH;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.zk.WatchInvoke#invoke(java.lang.String)
	 */
	@Override
	public void invoke(String path) {
//		schedulerManager.
		String data=null;
		try {
			byte[] datas = zooKeeperOperator.getZk().getData(path, false, null);
			data = new String(datas);
			
			Long id = Long.parseLong(data);
			// 根据Id 来做不同的事情
			if(id!=0){
				SchedulerTask schedulerTask = sdkSchedulerTaskManager.findSchedulerTaskById(id);
				if(null!=schedulerTask){
					log.info("handling task " + schedulerTask.getCode()+" begin");
					handleTask(schedulerTask);
					log.info("handling task " + schedulerTask.getCode()+" end");
				}else{
					log.warn("can't find taskinfo ,task id is "+id);
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
			log.error(e.getMessage());
		}
	}
	
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

}
