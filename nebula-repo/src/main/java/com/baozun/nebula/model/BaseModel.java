package com.baozun.nebula.model;

import java.io.Serializable;

public abstract class BaseModel implements Serializable{

	private static final long	serialVersionUID	= 5052345993765158571L;

	/**
	 * 禁用/删除/不活动/无效 状态
	 */
	public static final Integer	STATUS_DISABLE		= 0;

	/**
	 * 活动/有效 状态
	 */
	public static final Integer	STATUS_ENABLE		= 1;

	/**
	 * 默认状态为有效状态
	 */
	public static final Integer	DEFAULT_STATUS		= STATUS_ENABLE;
	
	/**
	 * 生命周期：禁用/无效
	 */
	public static final Integer LIFECYCLE_DISABLE   = 0;
	
	
	/**
	 * 生命周期：可用/有效
	 */
	public static final Integer LIFECYCLE_ENABLE    = 1;
	
	/**
	 * 生命周期：逻辑删除
	 */
	public static final Integer LIFECYCLE_DELETED   = 2;
	
	/**
	 * 生命周期：未激活
	 */
	public static final Integer LIFECYCLE_UNACTIVE  = 3;
	/**
	 * 生命周期：初始化 (对于有些要求用户在添加后，再次进行设置的实体，可以有初始化状态)
	 */
	public static final Integer LIFECYCLE_INIT      = 5;
}