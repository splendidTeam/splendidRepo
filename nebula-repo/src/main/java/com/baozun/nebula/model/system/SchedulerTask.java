package com.baozun.nebula.model.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 定时任务配置表
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_SYS_SCHEDULER_TASK")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SchedulerTask extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3625548963265860860L;

	
	/**
	 * PK
	 */
	private Long				id;
	
	/**
	 * 任务编码,如：
	 * SYS_SCHEDULER_ORDER_SEND
	 */
	private String				code;
	
	/**
	 * quartz时间表达式
	 */
	private String				timeExp;
	
	/**
	 * 任务描述
	 */
	private String				description;
	
	/**
	 * spring容器中通过beanName来找到实例
	 */
	private String				beanName;
	
	
	/**
	 * 定时调用的方法
	 */
	private String				methodName;
	
	
	/**
	 * 生命周期
	 */
	private Integer 			lifecycle;
	
	/**
	 * VERSION
	 */
	private Date 				version;



	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_SCHEDULER_TASK",sequenceName = "S_T_SYS_SCHEDULER_TASK",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_SCHEDULER_TASK")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CODE",length = 100)
    @Index(name = "IDX_SCHEDULER_TASK_CODE")
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	@Column(name = "TIME_EXP",length = 50)
	public String getTimeExp() {
		return timeExp;
	}


	public void setTimeExp(String timeExp) {
		this.timeExp = timeExp;
	}


	@Column(name = "DESCRIPTION",length = 100)
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name = "BEAN_NAME",length = 100)
	public String getBeanName() {
		return beanName;
	}


	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}


	@Column(name = "METHOD_NAME",length = 100)
	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Column(name = "LIFECYCLE")
    @Index(name = "IDX_SCHEDULER_TASK_LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}


	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}
	
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	
}
