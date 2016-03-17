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

import loxia.annotation.Column;

/**
 * @author dianchao.song
 */
public class MenuCommand implements Command{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -8522138565300535988L;

	/** 菜单名称. */
	private String				label;

	/** 路径. */
	private String				url;

	/** 图标. */
	private String				icon				= "";

	/** The id. */
	private Long				id;

	/** The parent id. */
	private Long				parentId;

	/** The children. */
	private List<MenuCommand>	children;

	/**
	 * Gets the 菜单名称.
	 * 
	 * @return the 菜单名称
	 */
	@Column("LABEL")
	public String getLabel(){
		return label;
	}

	/**
	 * Sets the 菜单名称.
	 * 
	 * @param label
	 *            the new 菜单名称
	 */
	public void setLabel(String label){
		this.label = label;
	}

	/**
	 * Gets the 路径.
	 * 
	 * @return the 路径
	 */
	@Column("URL")
	public String getUrl(){
		return url;
	}

	/**
	 * Sets the 路径.
	 * 
	 * @param url
	 *            the new 路径
	 */
	public void setUrl(String url){
		this.url = url;
	}

	/**
	 * Gets the 图标.
	 * 
	 * @return the 图标
	 */
	@Column("ICON")
	public String getIcon(){
		return icon;
	}

	/**
	 * Sets the 图标.
	 * 
	 * @param icon
	 *            the new 图标
	 */
	public void setIcon(String icon){
		this.icon = icon;
	}

	/**
	 * Gets the children.
	 * 
	 * @return the children
	 */
	public List<MenuCommand> getChildren(){
		return children;
	}

	/**
	 * Sets the children.
	 * 
	 * @param children
	 *            the new children
	 */
	public void setChildren(List<MenuCommand> children){
		this.children = children;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id){
		this.id = id;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Column("ID")
	public Long getId(){
		return id;
	}

	/**
	 * Sets the parent id.
	 * 
	 * @param parentId
	 *            the new parent id
	 */
	public void setParentId(Long parentId){
		this.parentId = parentId;
	}

	/**
	 * Gets the parent id.
	 * 
	 * @return the parent id
	 */
	@Column("PARENT_ID")
	public Long getParentId(){
		return parentId;
	}
}
