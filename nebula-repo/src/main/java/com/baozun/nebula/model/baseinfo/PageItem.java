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
 * 页面元素
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_BASE_PAGE_ITEM")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PageItem extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8898369105819607788L;
	
	/** PK. */
	private Long				id;

	/**
	 * 页面元素编码
	 */
	private String				code;
	
	/**
	 * 页面元素名称
	 */
	private String				name;
	
	/**
	 * 页面模板id
	 */
	private Long				pageTemplateId;
	
	/**
	 * 排序
	 */
	private Integer				sort;
	
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
	@SequenceGenerator(name = "SEQ_T_BASE_PAGE_ITEM",sequenceName = "S_T_BASE_PAGE_ITEM",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_BASE_PAGE_ITEM")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PAGE_TEMPLATE_ID")
	public Long getPageTemplateId() {
		return pageTemplateId;
	}

	public void setPageTemplateId(Long pageTemplateId) {
		this.pageTemplateId = pageTemplateId;
	}

	@Column(name = "SORT")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
	
}
