package com.baozun.nebula.model.cms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * cms - 模块实例版本 通过一个模块模板可以实例化很多模块，每个模块针对模板有自己定制化的部分
 * 
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_CMS_MODULE_INSTANCE_VERSION")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CmsModuleInstanceVersion extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784330620726209287L;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 模块名称
	 */
	private String				name;


	/**
	 * 模板id
	 */
	private Long				templateId;


	/**
	 * 创建时间
	 */
	private Date				createTime;

	/**
	 * 修改时间
	 */
	private Date				modifyTime;
	
	/**
	 * 发布开始时间
	 */
	private Date                startTime;
	
	/**
	 * 发布结束时间
	 */
	private Date                endTime;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * 是否已发布
	 */
	private Boolean				isPublished;

	private Date				version;
	
	/**
	 * 模板实例id
	 */
	private Long                instanceId;
	
	/**
	 * 版本类型 (0:基础版本，1：发布版本)
	 */
	private Integer             type;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_CMS_MODULE_INSTANCE_VERSION", sequenceName = "S_T_CMS_MODULE_INSTANCE_VERSION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CMS_MODULE_INSTANCE_VERSION")
	public Long getId() {
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

	@Column(name = "TEMPLATE_ID")
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
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

	@Column(name = "ISPUBLISHED")
	public Boolean getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}
	
	@Column(name = "INSTANCE_ID")
	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
		CmsModuleInstanceVersion other = (CmsModuleInstanceVersion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}
