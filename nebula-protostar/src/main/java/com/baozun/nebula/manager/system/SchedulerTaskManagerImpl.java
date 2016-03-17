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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.manager.schedule.ScheduleManager;
import com.baozun.nebula.model.system.SchedulerTask;
import com.baozun.nebula.sdk.manager.SdkSchedulerTaskManager;

/**
 * SchedulerTaskManager
 *
 * @author: shiyang.lv
 * @date: 2014年5月29日
 **/
@Transactional
@Service("schedulerTaskManager")
public class SchedulerTaskManagerImpl implements SchedulerTaskManager{

    @Autowired
    private SdkSchedulerTaskManager sdkSchedulerTaskManager;
    
    @Autowired
    private ScheduleManager scheduleManager;
    
    @Override
    public void unEnableSchedulerTaskByIds(List<Long> ids) {
        sdkSchedulerTaskManager.enableOrDisableSchedulerTaskByIds(ids,0);
        for(Long id:ids){
            scheduleManager.noticeTask(id);
        }
    }

    @Override
    public void enableSchedulerTaskByIds(List<Long> ids) {
        sdkSchedulerTaskManager.enableOrDisableSchedulerTaskByIds(ids,1);
        for(Long id:ids){
            scheduleManager.noticeTask(id);
        }
    }

    @Override
    public void deleteSchedulerTaskByIds(List<Long> ids) {
        sdkSchedulerTaskManager.removeSchedulerTaskByIds(ids);
        for(Long id:ids){
            scheduleManager.noticeTask(id);
        }
    }

    @Override
    public SchedulerTask saveSchedulerTask(SchedulerTask schedulerTask) {
    	String beanName =schedulerTask.getBeanName();
    	String beanMethod =schedulerTask.getMethodName();
    	schedulerTask.setBeanName(beanName.trim());
    	schedulerTask.setMethodName(beanMethod.trim());
        schedulerTask=sdkSchedulerTaskManager.saveSchedulerTask(schedulerTask);
        scheduleManager.noticeTask(schedulerTask.getId());
        return schedulerTask;
    }

    @Override
    public Pagination<SchedulerTask> findSchedulerTaskListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {
        return sdkSchedulerTaskManager.findSchedulerTaskListByQueryMapWithPage(page, sorts, paraMap);
    }

    @Override
    public SchedulerTask findSchedulerTaskById(Long id) {
        if(id!=null){
            SchedulerTask result=sdkSchedulerTaskManager.findSchedulerTaskById(id);
            return result;
        }
        
        return null;
    }
    
}

