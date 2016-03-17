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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;


/**
 * <b>机构的类型</b><br/>
 * 作用:<br/>
 * <li>定义机构的类型</li>
 * <li>限定用户绑定的角色</li>
 * <li>限定用户绑定的组织机构</li>
 * 
 * @author Justin
 *
 */
@Entity
@Table(name = "T_AU_ORG_TYPE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class OrgType  extends BaseModel {


	private static final long serialVersionUID = -2127208528880809813L;

	public static final Long ID_SYS_TYPE=1l;
	
	public static final Long ID_SHOP_TYPE=2l;
	
	public static final String NAME_SYS_TYPE="系统";
	
	public static final String NAME_SHOP_TYPE="店铺";
	/**
	 * PK
	 */
	private Long 				id;
	
	/**
	 * 类型名称
	 */
	private String 				name;
	
	
	/**
	 * 描述
	 */
	private String 				desc;
	
	
	/**
	 * 生命周期
	 */
	private Integer 			lifecycle ;
	/**
	 * VERSION
	 */
	private Date 				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_ORG_TYPE", sequenceName = "S_T_AU_ORG_TYPE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_AU_ORG_TYPE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 300)
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
	

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
	}
	
	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}


	
}
