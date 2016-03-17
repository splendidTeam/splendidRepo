package com.baozun.nebula.model.column;

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
 * 版块-模块配置
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_COL_COLUMN_MODULE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ColumnModule extends BaseModel {

	/**
	 * 常规类型
	 */
	public static final Integer	TYPE_NORMAL			= 1;

	/**
	 * 商品类型
	 */
	public static final Integer	TYPE_ITEM			= 2;

	/**
	 * 分类类型
	 */
	public static final Integer	TYPE_CATEGORY		= 3;

	/**
	 * 数字类型
	 */
	public final static Integer	TYPE_NUMBER			= 4;

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6250980412796601492L;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 模块编码
	 */
	private String				code;

	/**
	 * 模块名称
	 */
	private String				name;

	/**
	 * 类型
	 */
	private Integer				type;

	/**
	 * 发布时间(定时发布)
	 */
	private Date				publishTime;

	/**
	 * 页面id
	 */
	private Long				pageId;

	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_COL_COLUMN_MODULE", sequenceName = "S_T_COL_COLUMN_MODULE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_COL_COLUMN_MODULE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CODE", length = 255)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", length = 255)
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

	@Column(name = "PUBLISH_TIME")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	@Column(name = "PAGE_ID")
	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
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
