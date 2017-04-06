package com.baozun.nebula.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.baozun.nebula.model.BaseModel;

/**
 * 行业属性关系
 * @author johnny.xia
 */
@Entity
@Table(name = "T_PD_INDUSTRY_PROPERTY_RELATION")
public class IndustryPropertyRelation extends BaseModel{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7918575703095781475L;

	private Long 	id;
	
	/**行业Id*/
	private Long 	industryId;
	
	/**属性ID*/
	private Long 	propertyId;
	
	/**行业属性排序*/
	private Integer	sortNo;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_INDUSTRY_PROPERTY_RELATION",sequenceName = "S_T_PD_INDUSTRY_PROPERTY_RELATION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_INDUSTRY_PROPERTY_RELATION")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "INDUSTRY_ID")
    @Index(name = "IDX_INDUSTRY_PROPERTY_RELATION_INDUSTRY_ID")
	public Long getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Long industryId) {
		this.industryId = industryId;
	}

	@Column(name="PROPERTY_ID")
    @Index(name = "IDX_INDUSTRY_PROPERTY_RELATION_PROPERTY_ID")
	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
	
	@Column(name="SORT_NO")
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
	
}
