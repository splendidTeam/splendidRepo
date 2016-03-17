package com.baozun.nebula.model.baseinfo;

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
 * 页面模板
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_BASE_PAGE_TEMPLATE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PageTemplate extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8898369105819607788L;
	
	/** PK. */
	private Long				id;

	/**
	 * 页面编码
	 */
	private String				pageCode;
	
	/**
	 * 页面名称
	 */
	private String				pageName;
	
	/**
	 * 页面图片(预览图)
	 */
	private String				img;
	
	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;
	
	/**
	 * 生命周期
	 */
	private Integer				lifecycle;
	
	
	/** 最后操作者 */
	private Long				opeartorId;
	
	/** version. */
	private Date				version;


	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_BASE_PAGE_TEMPLATE",sequenceName = "S_T_BASE_PAGE_TEMPLATE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_BASE_PAGE_TEMPLATE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PAGE_CODE")
	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	@Column(name = "PAGE_NAME")
	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	@Column(name = "IMG")
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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

	
	@Column(name = "OPEARTOR_ID")
	public Long getOpeartorId() {
		return opeartorId;
	}

	public void setOpeartorId(Long opeartorId) {
		this.opeartorId = opeartorId;
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
