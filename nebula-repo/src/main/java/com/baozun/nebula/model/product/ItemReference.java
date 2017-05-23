package com.baozun.nebula.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 用于将系统制定颜色与商品颜色对接
 * @author jumbo
 *
 */
@Entity
@Table(name = "T_PD_ITEM_REFERENCE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemReference extends BaseModel {

	/**
     * 
     */
    private static final long serialVersionUID = -4709102813646682984L;

    /**
	 * 
	 */
	private Long id;
	
	/**
	 * 系统定义Id
	 */
	private Long searchConditionItemId;
	
	/**
	 * 商品定义Id(itemPropertyId)
	 */
	private Long itemPropertyId;

	
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

	@Column(name = "SEARCH_CONDITION_ITEM_ID")
    @Index(name = "IDX_ITEM_REFERENCE_SEARCH_CONDITION_ITEM_ID")
	public Long getSearchConditionItemId() {
		return searchConditionItemId;
	}

	public void setSearchConditionItemId(Long searchConditionItemId) {
		this.searchConditionItemId = searchConditionItemId;
	}

	@Column(name = "ITEM_PROPERTY_ID")
    @Index(name = "IDX_ITEM_REFERENCE_ITEM_PROPERTY_ID")
	public Long getItemPropertyId() {
		return itemPropertyId;
	}

	public void setItemPropertyId(Long itemPropertyId) {
		this.itemPropertyId = itemPropertyId;
	}

	
}
