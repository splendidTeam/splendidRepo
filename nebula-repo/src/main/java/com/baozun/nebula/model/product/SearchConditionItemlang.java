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
 * 搜索条件选项
 * @author 何波
 */
@Entity
@Table(name = "T_PD_SEARCH_CON_ITEM_LANG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SearchConditionItemlang extends BaseModel {

	private static final long serialVersionUID = -1572528242313833299L;

	/** PK. */
	private Long id;
	
	private Long searchConditionItemId;

	/**
	 * 选项名称(在页面上显示的搜索选项的具体值的名称)
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
	@SequenceGenerator(name = "SEQ_T_PD_SEARCH_CON_ITEM_LANG", sequenceName = "S_T_PD_SEARCH_CON_ITEM_LANG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_SEARCH_CON_ITEM_LANG")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "SEARCH_CONDITION_ITEM_ID")
	public Long getSearchConditionItemId() {
		return searchConditionItemId;
	}

	public void setSearchConditionItemId(Long searchConditionItemId) {
		this.searchConditionItemId = searchConditionItemId;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
