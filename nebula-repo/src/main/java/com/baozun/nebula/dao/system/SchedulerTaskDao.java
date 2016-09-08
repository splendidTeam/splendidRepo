/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.dao.system;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;
import com.baozun.nebula.model.system.SchedulerTask;

/**
 * SchedulerTaskDao
 * @author  Justin
 *
 */
public interface SchedulerTaskDao extends GenericEntityDao<SchedulerTask,Long>{

	/**
	 * 获取所有SchedulerTask列表
	 * @return
	 */
	@NativeQuery(model = SchedulerTask.class)
	List<SchedulerTask> findAllSchedulerTaskList();
	
	/**
	 * 通过ids获取SchedulerTask列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = SchedulerTask.class)
	List<SchedulerTask> findSchedulerTaskListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取SchedulerTask列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = SchedulerTask.class)
	List<SchedulerTask> findSchedulerTaskListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取SchedulerTask列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = SchedulerTask.class)
	Pagination<SchedulerTask> findSchedulerTaskListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用SchedulerTask
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableSchedulerTaskByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除SchedulerTask
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeSchedulerTaskByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 修改
	 * @param schedulerTask
	 * @param id
	 */
	@NativeUpdate
	void updateSchedulerTaskById(@QueryParam("st")SchedulerTask schedulerTask,@QueryParam("id") Long id);
	
	/**
	 * 获取有效的SchedulerTask列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = SchedulerTask.class)
	List<SchedulerTask> findAllEffectSchedulerTaskList();
	
	/**
	 * 通过参数map获取有效的SchedulerTask列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = SchedulerTask.class)
	List<SchedulerTask> findEffectSchedulerTaskListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的SchedulerTask列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = SchedulerTask.class)
	Pagination<SchedulerTask> findEffectSchedulerTaskListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	@NativeQuery(model = SchedulerTask.class)
	SchedulerTask findSchedulerTaskByMethodName(@QueryParam("methodName")String methodName);
	
	
}
