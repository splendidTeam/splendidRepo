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
package com.baozun.nebula.model.baseinfo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 菜单功能 与本身多对一关联(parent).
 * 
 * @author Justin
 */
@Entity
@Table(name = "T_AU_MENU")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Menu extends BaseModel{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 5059515852159799713L;

	/** ID. */
	private Long				id;

	/** 菜单名称. */
	private String				label;

	/** 菜单序号. */
	private Integer				sortNo;

	/** 路径. */
	private String				url;

	/** 自身关联. */
	private Menu				parent;

	/** 生命周期. */
	private Integer				lifecycle;

	/** 图标. */
	private String				icon;

	/** VERSION. */
	private Date				version;

	/**
	 * Gets the iD.
	 * 
	 * @return the iD
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_MENU",sequenceName = "S_T_AU_MENU",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_AU_MENU")
	public Long getId(){
		return id;
	}

	/**
	 * Sets the iD.
	 * 
	 * @param id
	 *            the new iD
	 */
	public void setId(Long id){
		this.id = id;
	}

	/**
	 * Gets the 菜单名称.
	 * 
	 * @return the 菜单名称
	 */
	@Column(name = "LABEL",length = 300)
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
	 * Gets the 菜单序号.
	 * 
	 * @return the 菜单序号
	 */
	@Column(name = "SORT_NO")
	public Integer getSortNo(){
		return sortNo;
	}

	/**
	 * Sets the 菜单序号.
	 * 
	 * @param sortNo
	 *            the new 菜单序号
	 */
	public void setSortNo(Integer sortNo){
		this.sortNo = sortNo;
	}

	/**
	 * Gets the 路径.
	 * 
	 * @return the 路径
	 */
	@Column(name = "URL",length = 300)
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
	 * Gets the 自身关联.
	 * 
	 * @return the 自身关联
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	@Index(name = "FIDX_AU_MENU_PARENT_ID")
	public Menu getParent(){
		return parent;
	}

	/**
	 * Sets the 自身关联.
	 * 
	 * @param parent
	 *            the new 自身关联
	 */
	public void setParent(Menu parent){
		this.parent = parent;
	}

	/**
	 * Gets the 生命周期.
	 * 
	 * @return the 生命周期
	 */
	@Column(name = "LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	/**
	 * Sets the 生命周期.
	 * 
	 * @param lifecycle
	 *            the new 生命周期
	 */
	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	/**
	 * Gets the vERSION.
	 * 
	 * @return the vERSION
	 */
	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	/**
	 * Sets the vERSION.
	 * 
	 * @param version
	 *            the new vERSION
	 */
	public void setVersion(Date version){
		this.version = version;
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
	 * Gets the 图标.
	 * 
	 * @return the 图标
	 */
	@Column(name = "ICON",length = 100)
	public String getIcon(){
		return icon;
	}
}
