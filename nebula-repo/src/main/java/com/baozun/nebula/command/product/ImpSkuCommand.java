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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.SearchConditionItem;


public class ImpSkuCommand implements Command {

	private static final long serialVersionUID = -6277596924176136831L;

	//商品code
	private String  code;
	//upc 
	private String upc;
	private BigDecimal listPrice;
	private BigDecimal salePrice;
	
	//动态销售属性
	private Map<String, String> props = new LinkedHashMap<String, String>();
	
	//国际化动态销售属性
	private Map<String, String> propsI18n = new LinkedHashMap<String, String>();
	//筛选条件
	private Map<String, String> scs = new LinkedHashMap<String, String>();
	//销售属性
  	private List<ItemProperties>  itemProps;
  	//筛选条件
  	private List<SearchConditionItem> searchConditionItems;
  
 	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Map<String, String> getProps() {
		return props;
	}
	public void setProps(Map<String, String> props) {
		this.props = props;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public Map<String, String> getScs() {
		return scs;
	}
	public void setScs(Map<String, String> scs) {
		this.scs = scs;
	}
	public List<ItemProperties> getItemProps() {
		return itemProps;
	}
	public void setItemProps(List<ItemProperties> itemProps) {
		this.itemProps = itemProps;
	}
	public List<SearchConditionItem> getSearchConditionItems() {
		return searchConditionItems;
	}
	public void setSearchConditionItems(
			List<SearchConditionItem> searchConditionItems) {
		this.searchConditionItems = searchConditionItems;
	}
	public Map<String, String> getPropsI18n() {
		return propsI18n;
	}
	public void setPropsI18n(Map<String, String> propsI18n) {
		this.propsI18n = propsI18n;
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
	
}
