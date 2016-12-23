package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.system.SchedulerTask;

public interface SdkSchedulerTaskManager extends BaseManager{

	/**
	 * 分页获取SchedulerTask列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */

	Pagination<SchedulerTask> findSchedulerTaskListByQueryMapWithPage(Page page,Sort[] sorts, Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用SchedulerTask
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	
	void enableOrDisableSchedulerTaskByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除SchedulerTask
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	
	void removeSchedulerTaskByIds(List<Long> ids);
	
	/**
	 * 获取有效的SchedulerTask列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	
	List<SchedulerTask> findAllEffectSchedulerTaskList();
	
	SchedulerTask findSchedulerTaskById(Long id);
	
	SchedulerTask saveSchedulerTask(SchedulerTask schedulerTask);
	
	SchedulerTask findSchedulerTaskByMethodName(String methodName);
}
