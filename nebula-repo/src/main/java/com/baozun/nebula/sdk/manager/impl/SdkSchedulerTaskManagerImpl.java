package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.system.SchedulerTaskDao;
import com.baozun.nebula.model.system.SchedulerTask;
import com.baozun.nebula.sdk.manager.SdkSchedulerTaskManager;

@Transactional
@Service("sdkSchedulerTaskManager")
public class SdkSchedulerTaskManagerImpl implements SdkSchedulerTaskManager {

	@Autowired
	private SchedulerTaskDao schedulerTaskDao;
	
	@Override
	@Transactional(readOnly=true)
	public Pagination<SchedulerTask> findSchedulerTaskListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {
		return schedulerTaskDao.findSchedulerTaskListByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public void enableOrDisableSchedulerTaskByIds(List<Long> ids, Integer state) {
		schedulerTaskDao.enableOrDisableSchedulerTaskByIds(ids, state);
	}

	@Override
	public void removeSchedulerTaskByIds(List<Long> ids) {
		schedulerTaskDao.removeSchedulerTaskByIds(ids);
	}

	@Override
	@Transactional(readOnly=true)
	public List<SchedulerTask> findAllEffectSchedulerTaskList() {
		return schedulerTaskDao.findAllEffectSchedulerTaskList();
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.wormhole.manager.SchedulerTaskManager#findEffectSchedulerTaskById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public SchedulerTask findSchedulerTaskById(Long id) {
		
		return schedulerTaskDao.getByPrimaryKey(id);
	}

    @Override
    public SchedulerTask saveSchedulerTask(SchedulerTask schedulerTask) {
        SchedulerTask result=null;
        if(schedulerTask.getId()!=null){
            List<Long> ids=new ArrayList<Long>();
            ids.add(schedulerTask.getId());
            schedulerTaskDao.updateSchedulerTaskById(schedulerTask,schedulerTask.getId());
            result=schedulerTask;
        }else{
            schedulerTask.setLifecycle(1);
            result=schedulerTaskDao.save(schedulerTask);
        }
        return result;
    }

}
