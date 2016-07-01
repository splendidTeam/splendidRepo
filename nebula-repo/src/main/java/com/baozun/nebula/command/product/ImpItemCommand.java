package com.baozun.nebula.command.product;
/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemProperties;


public class ImpItemCommand implements Command {

	private static final long serialVersionUID = -6277596924176136831L;

    private String code;
    private String title;
    private String description;
    private String sketch;
    private BigDecimal listPrice;
    private BigDecimal salePrice;
    private String seoDesc;
    private String seoTitle;
    private String seoKeyWords;
    private String categoryCodes;
    private String itemStyle;
    //动态非销售属性
  	private Map<String, String> props = new LinkedHashMap<String, String>();
  	//非销售属性
  	private List<ItemProperties>  itemProps;
	//分类
  	private List<ItemCategory> itemCategoryList;
  	private String subTitle;
  	//商品分类
  	private String itemType;
  	/**
  	 * 商品类型
  	 * @see com.baozun.nebula.model.product.Item#type
  	 */
  	private Integer type;
  	
  	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getItemStyle() {
		return itemStyle;
	}
	public void setItemStyle(String itemStyle) {
		this.itemStyle = itemStyle;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getListPrice() {
		return listPrice;
	}
	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	public String getSeoDesc() {
		return seoDesc;
	}
	public void setSeoDesc(String seoDesc) {
		this.seoDesc = seoDesc;
	}
	public String getSeoTitle() {
		return seoTitle;
	}
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}
	public String getSeoKeyWords() {
		return seoKeyWords;
	}
	public void setSeoKeyWords(String seoKeyWords) {
		this.seoKeyWords = seoKeyWords;
	}
	public Map<String, String> getProps() {
		return props;
	}
	public void setProps(Map<String, String> props) {
		this.props = props;
	}
	public List<ItemProperties> getItemProps() {
		return itemProps;
	}
	public void setItemProps(List<ItemProperties> itemProps) {
		this.itemProps = itemProps;
	}
	public String getSketch() {
		return sketch;
	}
	public void setSketch(String sketch) {
		this.sketch = sketch;
	}
	public String getCategoryCodes() {
		return categoryCodes;
	}
	public void setCategoryCodes(String categoryCodes) {
		this.categoryCodes = categoryCodes;
	}
	public List<ItemCategory> getItemCategoryList() {
		return itemCategoryList;
	}
	public void setItemCategoryList(List<ItemCategory> itemCategoryList) {
		this.itemCategoryList = itemCategoryList;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	

}
