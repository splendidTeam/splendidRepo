/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.command;

import java.util.List;

import com.baozun.nebula.model.auth.PrivilegeUrl;

import loxia.annotation.Column;

/**
 * 
* @Description: 提供菜单查询
* @author 何波
* @date 2014年11月3日 上午11:05:55 
*
 */
public class MenuQueryCommand implements Command {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8522138565300535988L;

	/** 菜单名称. */
	private String label;

	/** 路径. */
	private String url;

	/** 图标. */
	private String icon = "";

	/** The id. */
	private Long id;

	/** The parent id. */
	private Long parentId;

	/** 菜单序号. */
	private Integer sortNo;
	
	private String acl;
	
	private  String name;
	
	private String description;
	
	private String groupName;
	
	private Long orgType;
	
	private  Long priId;
	
	List<PrivilegeUrl>  privilegeUrls ;
	/**
	 * Gets the 菜单名称.
	 * 
	 * @return the 菜单名称
	 */
	@Column("LABEL")
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the 菜单名称.
	 * 
	 * @param label
	 *            the new 菜单名称
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the 路径.
	 * 
	 * @return the 路径
	 */
	@Column("URL")
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the 路径.
	 * 
	 * @param url
	 *            the new 路径
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the 图标.
	 * 
	 * @return the 图标
	 */
	@Column("ICON")
	public String getIcon() {
		return icon;
	}

	/**
	 * Sets the 图标.
	 * 
	 * @param icon
	 *            the new 图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Column("ID")
	public Long getId() {
		return id;
	}

	/**
	 * Sets the parent id.
	 * 
	 * @param parentId
	 *            the new parent id
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * Gets the parent id.
	 * 
	 * @return the parent id
	 */
	@Column("PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	@Column("SORT_NO")
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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

	@Column("ORG_TYPE_ID")
	public Long getOrgType() {
		return orgType;
	}

	public void setOrgType(Long orgType) {
		this.orgType = orgType;
	}
	public Long getPriId() {
		return priId;
	}

	public void setPriId(Long priId) {
		this.priId = priId;
	}

	public List<PrivilegeUrl> getPrivilegeUrls() {
		return privilegeUrls;
	}

	public void setPrivilegeUrls(List<PrivilegeUrl> privilegeUrls) {
		this.privilegeUrls = privilegeUrls;
	}

}
