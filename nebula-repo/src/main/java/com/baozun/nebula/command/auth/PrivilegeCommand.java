package com.baozun.nebula.command.auth;

import loxia.annotation.Column;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.auth.PrivilegeUrl;

public class PrivilegeCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876776932118793519L;

	private Long id;

	/**
	 * controller ID
	 */
	private String acl;

	/**
	 * 权限名称
	 */
	private String name;

	/**
	 * 对应的url地址
	 */
	private String url;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 权限分组
	 */
	private String groupName;

	/**
	 * 生命周期
	 */
	private Integer lifecycle;

	/**
	 * 机构类型(主要是有系统管理员，店铺管理员)
	 */
	private Long orgTypeId;
	
	private PrivilegeUrl[] urls;

	@Column("ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column("ACL")
	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	@Column("NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column("LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	@Column("ORG_TYPE_ID")
	public Long getOrgTypeId() {
		return orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId) {
		this.orgTypeId = orgTypeId;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column("DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column("GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column("URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PrivilegeUrl[] getUrls() {
		return urls;
	}

	public void setUrls(PrivilegeUrl[] urls) {
		this.urls = urls;
	}
	

}
