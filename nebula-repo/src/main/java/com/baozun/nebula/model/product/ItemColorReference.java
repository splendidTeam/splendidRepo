package com.baozun.nebula.model.product;

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
 * 用于将系统制定颜色与商品颜色对接
 * @author jumbo
 *
 */
@Entity
@Table(name = "t_pd_item_color_reference")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemColorReference extends BaseModel {

	/**
	 * 
	 */
	private Long id;
	
	/**
	 * 系统定义颜色Id
	 */
	private Long colorId;
	
	/**
	 * 商品定义颜色Id(propertyValueId)
	 */
	private Long itemColorId;

	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_COLOR_REF",sequenceName = "SEQ_T_PD_ITEM_COLOR_REF",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_ITEM_COLOR_REF")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SYS_COLOR_ID")
	public Long getColorId() {
		return colorId;
	}

	public void setColorId(Long colorId) {
		this.colorId = colorId;
	}

	@Column(name = "ITEM_COLOR_ID")
	public Long getItemColorId() {
		return itemColorId;
	}

	public void setItemColorId(Long itemColorId) {
		this.itemColorId = itemColorId;
	}
	
	
	
}
