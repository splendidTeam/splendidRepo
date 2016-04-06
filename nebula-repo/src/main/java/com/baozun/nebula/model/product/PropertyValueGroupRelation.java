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
 * 属性值分组 和属性值关系表
 * @author johnny.xia
 *
 */
@Entity
@Table(name = "t_pd_property_value_group_relation")
public class PropertyValueGroupRelation extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3427447651933029192L;

	private Long	id;
	
	/**属性值分组Id：PropertyValueGroup.id*/
	private Long	proValGroupId;
	
	/**属性id ： PropertyValue.id*/
	private Long	proValueId;
	

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_PROPERTY_VALUE_GROUP_RELATION",sequenceName = "S_T_PD_PROPERTY_VALUE_GROUP_RELATION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_PROPERTY_VALUE_GROUP_RELATION")
	public Long getId() {
		return id;
	}

	@Column(name="PRO_VAL_GROUP_ID")
	public Long getProValGroupId() {
		return proValGroupId;
	}

	@Column(name="PRO_VALUE_ID")
	public Long getProValueId() {
		return proValueId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProValGroupId(Long proValGroupId) {
		this.proValGroupId = proValGroupId;
	}

	public void setProValueId(Long proValueId) {
		this.proValueId = proValueId;
	}

	

}
