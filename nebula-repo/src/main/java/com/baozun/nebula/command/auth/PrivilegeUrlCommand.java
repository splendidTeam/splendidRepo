package com.baozun.nebula.command.auth;

import com.baozun.nebula.command.Command;

public class PrivilegeUrlCommand implements Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876776932118793519L;

	/**
	 * 对应的url地址
	 */
	private String              url;
	
	private String				acl;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	
	
	
}
