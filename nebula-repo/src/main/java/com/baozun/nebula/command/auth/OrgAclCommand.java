package com.baozun.nebula.command.auth;

import com.baozun.nebula.command.Command;

public class OrgAclCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6097975892081894441L;

	
	private String acl;
	
	private Long orgId;

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	
}
