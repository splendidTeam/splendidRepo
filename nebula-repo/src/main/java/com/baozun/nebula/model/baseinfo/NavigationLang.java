package com.baozun.nebula.model.baseinfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

@Entity
@Table(name = "T_BASE_NAVIGATION_LANG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class NavigationLang extends BaseModel{

	private static final long	serialVersionUID	= 834740729850689203L;

	/** PK. */
	private Long				id;

	private Long				navigationId;

	/**
	 * 条件名称
	 */
	private String				name;

	/**
	 * 语言标识
	 */
	private String				lang;

	/**
	 * seoTitle
	 */
	private String				seoTitle;

	/**
	 * seo搜索关键字
	 */
	private String				seoKeywords;

	/**
	 * seo搜索描述
	 */
	private String				seoDescription;

	/**
	 * 扩展字段
	 */
	private String				extention;

	@Column(name = "LANG")
	public String getLang(){
		return lang;
	}

	public void setLang(String lang){
		this.lang = lang;
	}

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_BASE_NAVIGATION_LANG",sequenceName = "S_T_BASE_NAVIGATION_LANG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_BASE_NAVIGATION_LANG")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "NAVIGATION_ID")
	public Long getNavigationId(){
		return navigationId;
	}

	public void setNavigationId(Long navigationId){
		this.navigationId = navigationId;
	}

	@Column(name = "NAME")
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	@Column(name = "SEOTITLE")
	public String getSeoTitle(){
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle){
		this.seoTitle = seoTitle;
	}

	@Column(name = "SEOKEYWORDS")
	public String getSeoKeywords(){
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords){
		this.seoKeywords = seoKeywords;
	}

	@Column(name = "SEODESCRIPTION")
	public String getSeoDescription(){
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription){
		this.seoDescription = seoDescription;
	}

	@Column(name = "EXTENTION")
	public String getExtention(){
		return extention;
	}

	public void setExtention(String extention){
		this.extention = extention;
	}

}
