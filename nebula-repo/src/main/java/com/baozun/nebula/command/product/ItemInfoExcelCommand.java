package com.baozun.nebula.command.product;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Lob;

import loxia.annotation.Column;

import com.baozun.nebula.command.Command;

public class ItemInfoExcelCommand implements Command {

	private static final long	serialVersionUID	= -3487341539950355671L;

	/** PK. */
	private Long				id;

	/**
	 * 商品详情id
	 */
	private Long				itemInfoId;

	private String				code;

	/**
	 * 商品名称
	 */
	private String				title;

	/**
	 * 副标题
	 */
	private String				subTitle;

	/** 商品概述 . */
	private String				sketch;

	/** 商品详细描述 */
	private String				description;

	/**
	 * seo搜索描述
	 */
	private String				seoDescription;

	/**
	 * seo搜索关键字
	 */
	private String				seoKeywords;

	/**
	 * seoTitle
	 */
	private String				seoTitle;

	/**
	 * 语言标识
	 */
	private String				lang;
	// 属性值
	private Map<String, String>	propValues			= new HashMap<String, String>();

	@Column("LANG")
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Column("ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@Column("ITEM_INFO_ID")
	public Long getItemInfoId() {
		return itemInfoId;
	}

	public void setItemInfoId(Long itemInfoId) {
		this.itemInfoId = itemInfoId;
	}

	@Column("TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column("SUB_TITLE")
	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	@Column("SKETCH")
	public String getSketch() {
		return sketch;
	}

	public void setSketch(String sketch) {
		this.sketch = sketch;
	}

	@Column("DESCRIPTION")
	@Lob
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column("SEODESCRIPTION")
	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	@Column("SEOKEYWORDS")
	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	@Column("SEOTITLE")
	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, String> getPropValues() {
		return propValues;
	}

	public void setPropValues(Map<String, String> propValues) {
		this.propValues = propValues;
	}

}
