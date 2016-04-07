package com.baozun.nebula.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.baozun.nebula.model.BaseModel;

/**
 * 商品属性 和属性值分组的关系表，记录商品对应属性选中的属性值分组
 * @author johnny.xia
 *
 */
@Entity
@Table(name = "t_pd_itempro_valgroup_reation")
public class ItemProValGroupRelation extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -70191225451251834L;

	private Long	id;
	
	/**商品属性id；itemProperty.id*/
	private Long	itemPropertyId;
	
	/**属性值分组id：PropertyValueGroup.id*/
	private Long	propertyValueGroupId;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEMPRO_VALGROUP_REATION",sequenceName = "S_T_PD_ITEMPRO_VALGROUP_REATION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_ITEMPRO_VALGROUP_REATION")
	public Long getId() {
		return id;
	}

	@Column(name="ITEM_PROPERTY_ID")
	public Long getItemPropertyId() {
		return itemPropertyId;
	}

	@Column(name="PROPERTY_VALUE_GROUP_ID")
	public Long getPropertyValueGroupId() {
		return propertyValueGroupId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setItemPropertyId(Long itemPropertyId) {
		this.itemPropertyId = itemPropertyId;
	}

	public void setPropertyValueGroupId(Long propertyValueGroupId) {
		this.propertyValueGroupId = propertyValueGroupId;
	}
	
	
	
}
