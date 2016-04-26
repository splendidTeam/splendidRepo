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

import loxia.dao.Sort;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 菜单导航
 * 供前台使用
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_BASE_NAVIGATION")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Navigation extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8458429224143520094L;
	
	/**
	 * 导航的通用排序：先父id，再排序号
	 */
	public static final Sort[] COMMOM_SORTS = Sort.parse("parent_id asc,sort asc");
	/**
	 * URL类型
	 */
	public static final Integer TYPE_URL = 1;
	/**
	 * 分类类型
	 */
	public static final Integer TYPE_CATEGORY = 2;

	/** PK. */
	private Long				id;
	
	/**
	 * 导航名称
	 */
	private String				name;
	
	/**
	 * 导航类型
	 * 1.url类型，设置一个url
	 * 2.分类类型，表示与分类相关联
	 */
	private Integer				type;
	
	/**
	 * 当type为分类类型时，这里填写分类id
	 */
	private Long 				param;
	
	/**
	 * 点击导航菜单前往的url地址
	 */
	private String				url;
	
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
	
	/**
	 * 是否为新窗口
	 */
	private Boolean				isNewWin;
	
	/** 最后操作者 */
	private Long				opeartorId;

	/** 父ID */
	private Long				parentId;
	
	/** version. */
	private Date				version;
	
	/**商品集合id*/
	private Long				collectionId;	

	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_BASE_NAVIGATION",sequenceName = "S_T_BASE_NAVIGATION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_BASE_NAVIGATION")
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

	@Column(name = "PARAM")
	public Long getParam() {
		return param;
	}

	public void setParam(Long param) {
		this.param = param;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	@Column(name = "IS_NEW_WIN")
	public Boolean getIsNewWin() {
		return isNewWin;
	}

	public void setIsNewWin(Boolean isNewWin) {
		this.isNewWin = isNewWin;
	}

	@Index(name = "IDX_NAVIGATION_PARENT_ID")
	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name="COLLECTION_ID")
	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	
	
	
}
