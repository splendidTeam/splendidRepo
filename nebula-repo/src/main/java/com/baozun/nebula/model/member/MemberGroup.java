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
package com.baozun.nebula.model.member;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 会员分组
 * 
 * @author Justin
 * 
 */
@Entity
@Table(name = "T_MEM_GROUP")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MemberGroup extends BaseModel {

	/**
	 */
	private static final long	serialVersionUID	= 4950067082662307004L;

	/**
	 * 常规分组类型
	 */
	public static final Integer	TYPE_NORMAL			= 1;

	/**
	 * 等级分组类型
	 */
	public static final Integer	TYPE_LEVEL			= 2;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 分组编码
	 */
	private String				code;

	/**
	 * 分组名称
	 */
	private String				name;

	/**
	 * 分组类型 1 常规分组 2 等级分组
	 * 
	 */
	private Integer				type;

	/**
	 * 分组描述
	 */
	private String				description;

	/**
	 * 创建时间
	 */
	private Date				createTime;

	/**
	 * 修改时间
	 */
	private Date				modifyTime;

	/**
	 * 创建者
	 */
	private String				creator;

	/**
	 * 修改者
	 */
	private String				modifier;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_MEM_IDENTIFY", sequenceName = "S_T_MEM_IDENTIFY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_MEM_IDENTIFY")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "CODE")
    @Index(name = "IDX_MEM_GROUP_CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME")
    @Index(name = "IDX_MEM_GROUP_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "LIFECYCLE")
    @Index(name = "IDX_MEM_GROUP_LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "CREATOR")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

    @Column(name = "MODIFIER")
	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

}
