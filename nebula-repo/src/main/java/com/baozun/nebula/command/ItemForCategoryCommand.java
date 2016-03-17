package com.baozun.nebula.command;

import java.util.List;

import com.baozun.nebula.model.product.CategoryLang;

public class ItemForCategoryCommand extends ItemCategoryCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2563722343694128313L;


	/**
	 * 商品分类代码
	 */
	private Long categoryId;
	
	
	/**
	 * 商品分类排序因子
	 */
	private Integer sort_no;
	
	private Long parent_id;
	
	/**
	 * 
	 */
	private String code;

	private List<CategoryLang> categoryLangs;


	public Long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


	public Integer getSort_no() {
		return sort_no;
	}


	public void setSort_no(Integer sort_no) {
		this.sort_no = sort_no;
	}


	public Long getParent_id() {
		return parent_id;
	}


	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public List<CategoryLang> getCategoryLangs() {
		return categoryLangs;
	}


	public void setCategoryLangs(List<CategoryLang> categoryLangs) {
		this.categoryLangs = categoryLangs;
	}
	
}
