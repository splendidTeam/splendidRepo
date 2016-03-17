package com.baozun.nebula.model.i18n;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.model.BaseModel;

@Entity
@Table(name = "T_MUTL_LANG_RESOURCE")
public class MutlLangResource extends BaseModel {
	private static final long serialVersionUID = 3931659473810028210L;
	public static final Logger log = LoggerFactory
			.getLogger(MutlLangResource.class);

	/**
	 * 数据id
	 */
	private long id;
	/**
	 * 全类名
	 */
	private String className;
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 属性名
	 */
	private String fieldName;
	/**
	 * 列名
	 */
	private String columnName;
	
	/**
	 * 关联表的主键属性
	 */
	private String idFieldName;
	
	/**
	 * 关联表的主键列名
	 */
	private String idColumnName;
	
	
	

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_MUTL_LANG_RESOURCE", sequenceName = "S_T_MUTL_LANG_RESOURCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_MUTL_LANG_RESOURCE")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "CLASS_NAME")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "TABLE_NAME")
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "FIELD_NAME")
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Column(name = "COLUMN_NAME")
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	@Column(name = "ID_FIELD_NAME")
	public String getIdFieldName() {
		return idFieldName;
	}

	public void setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
	}

	@Column(name = "ID_COLUMN_NAME")
	public String getIdColumnName() {
		return idColumnName;
	}

	public void setIdColumnName(String idColumnName) {
		this.idColumnName = idColumnName;
	}

}
