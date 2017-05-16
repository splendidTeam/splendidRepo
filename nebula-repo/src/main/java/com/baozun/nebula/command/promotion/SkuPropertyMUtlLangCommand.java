/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.command.promotion;

import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.command.Command;

/**
 * @author Tianlong.Zhang
 *
 */
public class SkuPropertyMUtlLangCommand  implements Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -349066968131636960L;

	//skuId
	private Long id ;
	
	//upcCode
	private String code ;
	// upcCode
	private String groupCode;
	
    /*** 单个商品重量（如果是按照重量计费）*/
    private Double            weight;
    
    /*** 单个商品体积*/
    private Double volume;
    
	//表征 sku 的property 和 propertyValue
	private List<ItemPropertyMutlLangCommand> propertyList = null;
	
	private BigDecimal listPrice ;
	
	private BigDecimal salePrice;
	
	private int availableQty;

	/**
	 * @return the groupCode
	 */
	public String getGroupCode() {
		return groupCode;
	}
	/**
	 * @param groupCode the groupCode to set
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	/**
	 * @return the availableQty
	 */
	public int getAvailableQty() {
		return availableQty;
	}

	/**
	 * @param availableQty the availableQty to set
	 */
	public void setAvailableQty(int availableQty) {
		this.availableQty = availableQty;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the propertyList
	 */
	public List<ItemPropertyMutlLangCommand> getPropertyList() {
		return propertyList;
	}

	/**
	 * @param propertyList the propertyList to set
	 */
	public void setPropertyList(List<ItemPropertyMutlLangCommand> propertyList) {
		this.propertyList = propertyList;
	}

	/**
	 * @return the listPrice
	 */
	public BigDecimal getListPrice() {
		return listPrice;
	}

	/**
	 * @param listPrice the listPrice to set
	 */
	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	/**
	 * @return the salePrice
	 */
	public BigDecimal getSalePrice() {
		return salePrice;
	}

	/**
	 * @param salePrice the salePrice to set
	 */
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
    
    public Double getWeight(){
        return weight;
    }
    
    public void setWeight(Double weight){
        this.weight = weight;
    }
    
    public Double getVolume(){
        return volume;
    }
    
    public void setVolume(Double volume){
        this.volume = volume;
    }
}
