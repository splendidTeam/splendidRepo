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
 */
package com.baozun.nebula.model.cms;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.springframework.format.annotation.DateTimeFormat;

import com.baozun.nebula.model.BaseModel;


/**
 * cms - 实例版本表
 * 每个页面实例的不同时段的不同内容
 * 
 * 
 * @author xienan
 * 
 */
@Entity
@Table(name = "T_CMS_PAGE_INSTANCE_VERSION")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CmsPageInstanceVersion extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6907906596432298508L;
	
	/**
	 * 基础实例版本
	 */
	public static final Integer	BASE_VERSION		= 0;
	
	/**
	 * 一般实例版本
	 */
	public static final Integer	NORMAL_VERSION		= 1;

	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 实例版本名称
	 */
	private String name;
	
	/**
	 * 实例id
	 */
	private Long instanceId;
	
	/**
	 * 模板id
	 */
	private Long templateId;
	
	/**
	 * 实例类型
	 */
	private Integer type;
	
	/**
	 * 是否有效
	 */
	private Integer lifecycle;
	
	/**
	 * 是否发布
	 */
	private boolean isPublished;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 版本
	 */
	private Date version;
	
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	
	/**
	 * 开始发布时间
	 */
	private Date startTime;
	
	/**
	 * 结束发布时间
	 */
	private Date endTime;

	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_INSTANCE_VERSION", sequenceName = "SEQ_T_INSTANCE_VERSION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_INSTANCE_VERSION")public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "INSTANCE_ID")
	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	@Column(name = "TEMPLATE_ID")
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "ISPUBLISHED")
	public boolean isPublished() {
		return isPublished;
	}

	public void setPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CmsPageInstanceVersion other = (CmsPageInstanceVersion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
