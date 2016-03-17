package com.baozun.nebula.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("schedulerManager")
public class SchedulerManagerImpl implements SchedulerManager {

	@Autowired(required=false)
	private SchedulerFactoryBean scheduler;
	
	@Override
	public void addTask(Object taskInstance, String taskMethod, Date date, String jobName)throws Exception {
		// TODO Auto-generated method stub

		SimpleDateFormat sdf=new SimpleDateFormat("s m H d M ? yyyy");
		String strDate=sdf.format(date);
		addTask(taskInstance,taskMethod,strDate,jobName);
	}

	@Override
	public void removeTask(String jobName)throws Exception {
		// TODO Auto-generated method stub
		scheduler.getScheduler().deleteJob(jobName, Scheduler.DEFAULT_GROUP);
	}
	
	@Override
	public void timerClean() throws Exception {
		// TODO Auto-generated method stub
		String[] triggerNames=scheduler.getScheduler().getTriggerNames(Scheduler.DEFAULT_GROUP);
		
		for(String name:triggerNames){
			Trigger trigger=scheduler.getScheduler().getTrigger(name, Scheduler.DEFAULT_GROUP);
			//如果下次触发时间小于当前时间，代表此任务已经没有用处，清理掉(多加5秒安全期，预防并发问题)
			if(trigger.getNextFireTime()==null||trigger.getNextFireTime().getTime()<System.currentTimeMillis()-5000){
				removeTask(trigger.getJobName());
			}
		}
	}

	@Override
	public void addTask(Object taskInstance, String taskMethod, String timeExp, String jobName) throws Exception {
		// TODO Auto-generated method stub
		MethodInvokingJobDetailFactoryBean mifb=new MethodInvokingJobDetailFactoryBean();
		
		mifb.setTargetObject(taskInstance);
		mifb.setTargetMethod(taskMethod);
		mifb.setConcurrent(false);
		mifb.setName(jobName);
		mifb.afterPropertiesSet();
		
		CronTriggerBean ctb=new CronTriggerBean();
		ctb.setJobDetail(mifb.getObject());
		ctb.setCronExpression(timeExp);
//		ctb.setCronExpression("0 59 18 9 4 ?");
		
		ctb.setName(jobName+"trigger");
//		ctb.getNextFireTime();
		
		scheduler.getScheduler().scheduleJob(mifb.getObject(), ctb);
	}
	
	
	

}
