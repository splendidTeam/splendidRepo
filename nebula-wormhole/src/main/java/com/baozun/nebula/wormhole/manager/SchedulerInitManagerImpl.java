package com.baozun.nebula.wormhole.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.SchedulerManager;
import com.baozun.nebula.model.system.SchedulerTask;
import com.baozun.nebula.sdk.manager.SdkSchedulerTaskManager;

@Service("schedulerInitManager")
public class SchedulerInitManagerImpl implements SchedulerInitManager{

    private Logger                  logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SchedulerManager        schedulerManager;

    @Autowired
    private SdkSchedulerTaskManager sdkSchedulerTaskManager;

    @Autowired
    private ApplicationContext      applicationContext;

    @Override
    public void init(){
        List<SchedulerTask> taskList = sdkSchedulerTaskManager.findAllEffectSchedulerTaskList();

        for (SchedulerTask st : taskList){
            Object taskInstance = applicationContext.getBean(st.getBeanName());
            try{
                schedulerManager.addTask(taskInstance, st.getMethodName(), st.getTimeExp(), st.getCode());
            }catch (Exception e){
                logger.error("scheduler init task error", e);
                throw new IllegalStateException(e);
            }
        }
    }

}
