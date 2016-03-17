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
 * cms - 模块实例 通过一个模块模板可以实例化很多模块，每个模块针对模板有自己定制化的部分
 * 
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_CMS_MODULE_INSTANCE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CmsModuleInstance extends BaseModel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6505806169838367960L;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 模块名称
	 */
	private String				name;

	/**
	 * 模块编码(唯一)
	 */
	private String				code;

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
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * 是否已发布
	 */
	private Boolean				isPublished;

	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_CMS_MODULE_INSTANCE", sequenceName = "S_T_CMS_MODULE_INSTANCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CMS_MODULE_INSTANCE")
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

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

}
