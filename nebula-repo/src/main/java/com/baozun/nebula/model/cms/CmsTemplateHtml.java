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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 最终发布模板信息html
 * @author 谢楠
 *
 */
@Entity
@Table(name = "T_CMS_TEMPLATE_HTML")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CmsTemplateHtml extends BaseModel{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1986945908177070477L;


	/**
	 * 最终发布模板信息id
	 */
	private Long				id;


	/**
	 * 发布模块模板对应的code
	 */
	private String				moduleCode;
	
	/**
	 * 发布页面模板对应的code
	 */
	private String				pageCode;
	
	/**
	 * 发布的版本id
	 */
	private Long              versionId;

	/**
	 * 模板数据文件(text)
	 */
	private String				data;

	/**
	 * 创建时间
	 */
	private Date				createTime;
	
	/**
	 * 发布开始时间
	 */
	private Date				startTime;
	
	/**
	 * 生命周期
	 */
	private Integer				lifecycle;
	
	/**
	 * 发布结束时间
	 */
	private Date				endTime;
	
	
	private Date				version;


	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_CMS_TEMPLATE_HTML", sequenceName = "S_T_CMS_TEMPLATE_HTML", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CMS_TEMPLATE_HTML")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "MODULE_CODE")
    @Index(name = "IDX_CMS_TEMPLATE_HTML_MODULE_CODE")
	public String getModuleCode() {
		return moduleCode;
	}


	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	@Column(name = "PAGE_CODE")
	public String getPageCode() {
		return pageCode;
	}


	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}


	@Column(name = "VERSION_ID")
	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
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


	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}


	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}


	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
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
		CmsTemplateHtml other = (CmsTemplateHtml) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
