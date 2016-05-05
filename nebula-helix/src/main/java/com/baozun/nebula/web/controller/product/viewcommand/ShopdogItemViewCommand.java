/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product.viewcommand;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ShopdogItemViewCommand extends ShopdogBaseCommand {
	
	private static final long serialVersionUID = -3799906776769234650L;

	/** 商品id. */
    private Long id;
    
	/**
	 * 商品编码
	 */
	private String code;
	
	/** 
	 * 商品类型. 
	 */
	private Integer type;
	
	/** 
	 * 分组style 
	 */
	private String style;
	
	/**
	 * 商品名称
	 */
    private String title;

    /** 
     * 副标题. 
     */
    private String subTitle;
    
    /** 
     * 商品概述 . 
     */
	private String sketch;

    /** 
     * 商品详细描述. 
     */
    private String description;
    
    /**
	 * 吊牌价
	 */
	private BigDecimal listPrice;
	
	/**
	 * 销售价
	 */
	private BigDecimal salesPrice;
	
	/**
	 * 销售属性
	 */
	private List<ShopdogPropertyViewCommand> salesProperties;
	
	/**
	 * 非销售属性，[属性分组, 分组下的非销售属性集合]
	 */
	private Map<String, List<ShopdogPropertyViewCommand>> nonSalesProperties;
	
	/**
	 * sku
	 */
	private List<ShopdogSkuViewCommand> skus;
	
	/**
	 * 主图
	 */
	private List<String> mainPicture;
	
	/**
	 * 所有图片
	 */
	private List<ShopdogItemImageViewCommand> allPictures;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getSketch() {
		return sketch;
	}

	public void setSketch(String sketch) {
		this.sketch = sketch;
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

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public List<ShopdogPropertyViewCommand> getSalesProperties() {
		return salesProperties;
	}

	public void setSalesProperties(List<ShopdogPropertyViewCommand> salesProperties) {
		this.salesProperties = salesProperties;
	}

	public Map<String, List<ShopdogPropertyViewCommand>> getNonSalesProperties() {
		return nonSalesProperties;
	}

	public void setNonSalesProperties(
			Map<String, List<ShopdogPropertyViewCommand>> nonSalesProperties) {
		this.nonSalesProperties = nonSalesProperties;
	}

	public List<ShopdogSkuViewCommand> getSkus() {
		return skus;
	}

	public void setSkus(List<ShopdogSkuViewCommand> skus) {
		this.skus = skus;
	}

	public List<String> getMainPicture() {
		return mainPicture;
	}

	public void setMainPicture(List<String> mainPicture) {
		this.mainPicture = mainPicture;
	}

	public List<ShopdogItemImageViewCommand> getAllPictures() {
		return allPictures;
	}

	public void setAllPictures(List<ShopdogItemImageViewCommand> allPictures) {
		this.allPictures = allPictures;
	}

}
