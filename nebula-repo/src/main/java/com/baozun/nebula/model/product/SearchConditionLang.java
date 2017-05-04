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
 * 搜索条件
 * @author 何波
 */
@Entity
@Table(name = "T_PD_SEARCH_CON_LANG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SearchConditionLang extends BaseModel {

	private static final long serialVersionUID = 834740729850689203L;

	/** PK. */
	private Long id;

	private Long searchConditionId;

	/**
	 * 条件名称
	 */
	private String name;
	
	/**
	 * 语言标识
	 */
	private String lang;
	
	
	@Column(name = "LANG")
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_SEARCH_CON_LANG", sequenceName = "S_T_PD_SEARCH_CON_LANG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_SEARCH_CON_LANG")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "SEARCH_CONDITION_ID")
    @Index(name = "IDX_SEARCH_CON_LANG_SEARCH_CONDITION_ID")
	public Long getSearchConditionId() {
		return searchConditionId;
	}

	public void setSearchConditionId(Long searchConditionId) {
		this.searchConditionId = searchConditionId;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
