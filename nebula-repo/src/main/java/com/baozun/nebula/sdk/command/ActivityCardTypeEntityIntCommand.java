package com.baozun.nebula.sdk.command;

import java.util.Date;

import com.baozun.nebula.command.Command;

public class ActivityCardTypeEntityIntCommand implements Command{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4910649503401610876L;

	private Long id;
	
	private String name;
	
	private String factorType;
	
	private String factor;
	
	private Date begineTime;
	
	private Date endTime;
	
	private Boolean useNum;
	
}
