package com.baozun.nebula.command.product;

import java.util.List;

import com.baozun.nebula.command.Command;

public class CrowdForCheckCommand implements Command {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 377356486822653697L;

	private Long memId;
	
	private List<Long> groups;

	public Long getMemId() {
		return memId;
	}

	public void setMemId(Long memId) {
		this.memId = memId;
	}

	public List<Long> getGroups() {
		return groups;
	}

	public void setGroups(List<Long> groups) {
		this.groups = groups;
	}
	
	
}
