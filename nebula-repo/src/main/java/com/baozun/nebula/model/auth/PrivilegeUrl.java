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
package com.baozun.nebula.model.auth;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 权限和url的对照表
 * 通过acl进行关联
 * @author xianze.zhang
 *@creattime 2013-6-6
 */
@Entity
@Table(name = "T_AU_PRIVILEGE_URL")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PrivilegeUrl extends BaseModel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1750380638302077418L;
	
	/**
	 * ID
	 */
	private Long 				id;

	/**
	 * privilege
	 */
	private Privilege				privilege;

	/**
	 * 对应的url地址
	 */
	private String              url;
	
	/**
	 * 描述
	 */
	private String				description;
	
	private Long   parentId;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_PRIVILEGE_URL", sequenceName = "S_T_AU_PRIVILEGE_URL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_AU_PRIVILEGE_URL")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name = "URL",length = 300)
	public String getUrl() {
		return url;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRI_ID")
	@Index(name = "FIDX_RP_PRIVI")
	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege pri) {
		this.privilege = pri;
	}

	@Column(name = "DESCRIPTION",length = 512)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	
}
