/**
 * Copyright (c) 2013 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.manager.system;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.system.SchedulerTask;

/**
 * SchedulerTaskManager
 *
 * @author: shiyang.lv
 * @date: 2014年5月29日
 **/
public interface SchedulerTaskManager {
    /**
     * 批量禁用
     * @param ids
     */
    public void unEnableSchedulerTaskByIds(List<Long> ids);
    
    /**
     * 批量启用
     * @param ids
     */
    public void enableSchedulerTaskByIds(List<Long> ids);
    
    /**
     * 批量删除
     * @param ids
     */
    public void deleteSchedulerTaskByIds(List<Long> ids);
    
    /**
     * 创建或者修改schedulerTask
     * @param schedulerTask
     */
    public SchedulerTask saveSchedulerTask(SchedulerTask schedulerTask);
    
    /**
     * 分页获取SchedulerTask列表
     * @param start
     * @param size
     * @param paraMap
     * @param sorts 
     * @return
     */
    public Pagination<SchedulerTask> findSchedulerTaskListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
    
    /**
     * 根据ID查询单个
     * @param id
     * @return
     */
    public SchedulerTask findSchedulerTaskById(Long id);
}

