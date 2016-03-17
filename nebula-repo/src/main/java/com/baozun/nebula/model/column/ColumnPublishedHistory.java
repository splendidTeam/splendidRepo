package com.baozun.nebula.model.column;

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
 * 发布到线上的版块配置历史记录
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_COL_COLUMN_PUBLISHED_HISTORY")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ColumnPublishedHistory extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6250980412796601492L;
	
	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 页面code
	 */
	private String pageCode;
	
	/**
	 * 模板code
	 */
	private String moduleCode;
	
	/**
	 * json对象为value
	 * 
	 */
	private String value;

	/**
	 * 发布时间
	 */
	private Date publishedTime;
	
	private Date version;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_COL_COLUMN_PUBLISHED_HISTORY",sequenceName = "S_T_COL_COLUMN_PUBLISHED_HISTORY",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_COL_COLUMN_PUBLISHED_HISTORY")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "PAGE_CODE",length = 255)
	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	@Column(name = "MODULE_CODE",length = 255)
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	@Column(name = "VALUE")
	@Lob
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "PUBLISHED_TIME")
	public Date getPublishedTime() {
		return publishedTime;
	}

	public void setPublishedTime(Date publishedTime) {
		this.publishedTime = publishedTime;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	

}
