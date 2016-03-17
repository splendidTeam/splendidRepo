package com.baozun.nebula.model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 系统制定颜色表
 * @author jumbo
 *
 */
@Entity
@Table(name = "t_pd_sys_item_color")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SystemItemColor extends BaseModel {

	/**
	 * 颜色ID
	 */
	private Long id;
	
	/**
	 * 颜色名称
	 */
	private String name;
	
	/**
	 * 排序因子
	 */
	private Integer sortNo;
	
	/** 创建时间. */
	private Date createTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_SYS_ITEM",sequenceName = "SEQ_T_PD_SYS_ITEM",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_SYS_ITEM")
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
	
	
	@Column(name = "SORT_NO")
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
