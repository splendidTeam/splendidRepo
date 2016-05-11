package com.baozun.nebula.model.cms;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * cms - 编辑区域
 * 针对模板定义一些编辑区域，保存到页面实例对应的地方
 * 
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_CMS_EDIT_VERSION_AREA")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CmsEditVersionArea extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6505806169838367960L;

	/** 隐藏 0*/ 
	public static final Integer CMS_EDIT_AREA_HIDE = 0;
	/** 显示 1*/
	public static final Integer CMS_EDIT_AREA_SHOW = 1;
	
	/**
	 * id
	 */
	private Long				id;

	/**
	 * 页面区域编码
	 */
	private String				code;
	
	/**
	 * 模块区域编码
	 */
	private String              moduleCode;

	/**
	 * 页面模板id
	 */
	private Long				templateId;
	

	/**
	 * 模块模板id
	 */
	private Long                moduleTemplateId;
	
	
	/**
	 * 页面实例id
	 */
	private Long				pageId;
	
	/**
	 * 模块实例id
	 */
	private Long                moduleId;

	/**
	 * 区域数据内容(text)
	 */
	private String				data;


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


	private Date				version;
	/**
	 *显示 1  隐藏 0
	 */
	private  Integer  hide; 
	
	/**
	 * versionId
	 */
	private  Long  versionId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_CMS_PAGE_TEMPLATE_VERSION", sequenceName = "S_T_CMS_PAGE_TEMPLATE_VERSION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CMS_PAGE_TEMPLATE_VERSION")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Column(name = "PAGE_ID")
	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	@Column(name = "DATA")
	@Lob
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

	@Column(name = "MODULE_TEMPLATE_ID")
	public Long getModuleTemplateId() {
		return moduleTemplateId;
	}

	public void setModuleTemplateId(Long moduleTemplateId) {
		this.moduleTemplateId = moduleTemplateId;
	}
	@Column(name = "MODULE_ID")
	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	@Column(name = "MODULE_CODE")
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	@Column(name = "HIDE")
	public Integer getHide() {
		return hide;
	}

	public void setHide(Integer hide) {
		this.hide = hide;
	}

	@Column(name = "VERSION_ID")
	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}
	
	
}
