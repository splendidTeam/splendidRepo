package com.baozun.nebula.model.product;

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
 * 搜索条件
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_PD_SEARCH_CON")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SearchCondition extends BaseModel {

	/**
	 * 价格类型
	 */
	public static final Integer SALE_PRICE_TYPE = 3;
	
	/**
	 * 常规区间类型
	 */
	public static final Integer NORMAL_AREA_TYPE = 2;
	
	/**
	 * 常规动态属性类型
	 */
	public static final Integer NORMAL_TYPE=1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8195576919245959945L;

	/** PK. */
	private Long				id;
	
	
	/**
	 * 条件名称
	 */
	private String				name;
	
	/**
	 * 类型
	 * 1.一般类型
	 * 2.区间类型
	 * 3.价格类型
	 */
	private Integer				type;
	
	/**
	 * 关联的商品属性id
	 */
	private Long 				propertyId;
	
	/**
	 * 搜索条件对应的分类Id
	 */
	private Long 				categoryId;
	
	/**
	 * 条件描述
	 */
	private String				description;
	
	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;
	
	/**
	 * 生命周期
	 */
	private Integer				lifecycle;
	
	/**
	 * 排序
	 */
	private Integer				sort;
	
	/** 最后操作者 */
	private Long				opeartorId;
	
	/** version. */
	private Date				version;
	
	
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_SEARCH_CON",sequenceName = "S_T_PD_SEARCH_CON",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_SEARCH_CON")
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

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "PROPERTY_ID")
	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
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

	@Column(name = "SORT")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Column(name = "OPEARTOR_ID")
	public Long getOpeartorId() {
		return opeartorId;
	}

	public void setOpeartorId(Long opeartorId) {
		this.opeartorId = opeartorId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "CATEGORY_ID")
	public Long getCategoryId() {
		return categoryId;
	}
}
