package com.baozun.nebula.manager;

import java.util.Date;

public interface SchedulerManager {

	/**
	 * 添加定时任务
	 * @param taskInstance  任务的实例
	 * @param taskMethod 调用任务的方法
	 * @param date 在什么时间执行
	 * @param jobName 任务名称(不能重复)，一般按照业务逻辑再加上一定的参数进行命名
	 */
	public void addTask(Object taskInstance,String taskMethod,Date date,String jobName)throws Exception;
	

	/**
	 * 添加定时任务
	 * @param taskInstance  任务的实例
	 * @param taskMethod 调用任务的方法
	 * @param timeExp 执行时间表达式
	 * @param jobName 任务名称(不能重复)，一般按照业务逻辑再加上一定的参数进行命名
	 */
	public void addTask(Object taskInstance,String taskMethod,String timeExp,String jobName)throws Exception;
	
	/**
	 * 删除定时任务
	 * @param jobName
	 */
	public void removeTask(String jobName)throws Exception;
	
	/**
	 * 定时清理任务
	 * @throws Exception
	 */
	public void timerClean()throws Exception;
}
