package com.baozun.nebula.model.cms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * cms - 页面实例 通过一个页面模板可以实例化很多页面，每个页面针对模板有自己定制化的部分
 * 
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_CMS_PAGE_INSTANCE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CmsPageInstance extends BaseModel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6505806169838367960L;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 页面名称
	 */
	private String				name;

	/**
	 * 页面编码(唯一)
	 */
	private String				code;

	/**
	 * 模板id
	 */
	private Long				templateId;

	/**
	 * url地址
	 */
	private String				url;

	/**
	 * seoTitle
	 */
	private String				seoTitle;

	/**
	 * seoKeywords
	 */
	private String				seoKeywords;

	/**
	 * seoDescription
	 */
	private String				seoDescription;

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
	/**
	 * 页面生效时间
	 */
	private  Date   startTime;
	/**
	 * 页面失效时间
	 */
	private  Date   endTime;

	private  Integer supportType;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_CMS_PAGE_TEMPLATE", sequenceName = "S_T_CMS_PAGE_TEMPLATE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CMS_PAGE_TEMPLATE")
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

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "SEO_TITLE")
	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	@Column(name = "SEO_KEYWORDS")
	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	@Column(name = "SEO_DESCRIPTION", length = 500)
	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
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

	@Transient
	public Integer getSupportType() {
		return supportType;
	}

	public void setSupportType(Integer supportType) {
		this.supportType = supportType;
	}

}
