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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 属性值分组
 * @author johnny.xia
 *
 */
@Entity
@Table(name = "T_PD_PROPERTY_VALUE_GROUP")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PropertyValueGroup extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8540902982475059935L;

	private Long 	id;
	
	/**属性值组名称*/
	private String	name;
	
	/**属性id*/
	private Long	propertyId;
	
	/**创建时间*/
	private Date	createTime;
	
	/**version。每次修改都需要更新这个字段*/
	private Date	version;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_PROPERTY_VALUE_GROUP",sequenceName = "S_T_PD_PROPERTY_VALUE_GROUP",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_PROPERTY_VALUE_GROUP")
	public Long getId() {
		return id;
	}

	@Column(name="NAME")
	public String getName() {
		return name;
	}

	@Column(name="PROPERTY_ID")
	@Index(name = "IDX_PROPERTY_VALUE_GROUP_PROPERTY_ID")
	public Long getPropertyId() {
		return propertyId;
	}

	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	@Version
	@Column(name="VERSION")
	public Date getVersion() {
		return version;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	
}
