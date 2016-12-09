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
package com.baozun.nebula.command;

import java.math.BigDecimal;
import java.util.Map;


/**
 * @author lin.liu
 */
public class SkuExportCommand implements Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 830907409163949885L;


	/**
	 * 商品ID
	 */
	private Long				id;
	/**
	 * 商品编码
	 */
	private String				code;

	/**
	 * 商品名称
	 */
	private String				upc;

	/**
	 * 销售价格
	 */
	private BigDecimal			salePrice;

	/**
	 * 吊牌价
	 */
	private BigDecimal			listPrice;
	
	private String            	properties;
	
	private Map<Long, String>   propertyVlaues;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public Map<Long, String> getPropertyVlaues() {
		return propertyVlaues;
	}

	public void setPropertyVlaues(Map<Long, String> propertyVlaues) {
		this.propertyVlaues = propertyVlaues;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
