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
 * 
* @Description: 商品可见性 
* @author 何波
* @date 2014年9月24日 上午11:28:45 
*
 */
@Entity
@Table(name = "t_pd_item_visibility")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemVisibility extends BaseModel {

	/**
	 * 
	 */
	private Long id;

	/**
	 * 会员筛选器id
	 */
	private Long memFilterId;

	/**
	 * 商品筛选器id
	 */
	private Long itemFilterId;
	
	/** 
	 * version.
	 */
	private Date version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_VISIBILITY", sequenceName = "SEQ_T_PD_ITEM_VISIBILITY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_ITEM_VISIBILITY")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "MEM_FILTER_ID")
	public Long getMemFilterId() {
		return memFilterId;
	}

	public void setMemFilterId(Long memFilterId) {
		this.memFilterId = memFilterId;
	}

	@Column(name = "ITEM_FILTER_ID")
	public Long getItemFilterId() {
		return itemFilterId;
	}

	public void setItemFilterId(Long itemFilterId) {
		this.itemFilterId = itemFilterId;
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
