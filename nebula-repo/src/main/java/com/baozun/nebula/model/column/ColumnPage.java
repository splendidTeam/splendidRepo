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
 * 页面板块配置
 * 
 * @author Justin Hu
 * 
 */
@Entity
@Table(name = "T_COL_COLUMN_PAGE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ColumnPage extends BaseModel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6250980412796601492L;

	/**
	 * id
	 */
	private Long				id;

	private String				code;

	private String				name;

	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_COL_COLUMN_PAGE", sequenceName = "S_T_COL_COLUMN_PAGE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_COL_COLUMN_PAGE")
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

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

}
