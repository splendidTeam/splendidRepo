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
 * cms - 发布表
 * 编辑区域的数据发布到这张表中
 * 
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_CMS_PUBLISHED")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CmsPublished extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6505806169838367960L;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 页面code
	 */
	private String				pageCode;
	
	/**
	 * 模块code
	 * 与页面code二选一
	 */
	private String 				moduleCode;

	/**
	 * 区域code
	 */
	private String				areaCode;				

	/**
	 * 某区域的数据内容(text)
	 */
	private String				data;

	/**
	 * 发布时间
	 */
	private Date				publishTime;
	


	private Date				version;
	
	/**
	 *显示 1  隐藏 0
	 */
	private  Integer  hide; 


	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_CMS_PUBLISHED", sequenceName = "S_T_CMS_PUBLISHED", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CMS_PUBLISHED")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DATA")
	@Lob
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Column(name = "MODULE_CODE")
    @Index(name = "IDX_CMS_PUBLISHED_MODULE_CODE")
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

	@Column(name = "AREA_CODE")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "PUBLISH_TIME")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "HIDE")
	public Integer getHide() {
		return hide;
	}

	public void setHide(Integer hide) {
		this.hide = hide;
	}

}
