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

import java.util.List;
import java.util.Map;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 相关商品的颜色属性（或者同款商品）
 * <p>
 * 如果商品定义到款，推荐商品或者最近浏览商品在展示时可能会同时展示该商品的颜色属性；
 * 如果商品定义到色，推荐商品或者最近浏览商品在展示时可能会同时展示同款的其他商品。
 * 该command即定义商品展示块的调色板部分。
 * </p>
 *
 * @see com.baozun.nebula.web.controller.product.viewcommand.RelationItemViewCommand
 */
public class RelationItemColorSwatchViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = -1935368532585432697L;

	/** 商品Id */
	private Long itemId;
	
	/** 商品编码 */
	private String itemCode;
	
	/** 颜色属性Id */
	private Long itemPropertyId;
	
	/** 颜色属性名称 */
	private String propertyName;
	
	/** 颜色属性值 */
	private String propertyValue;
	
	/** 颜色属性显示值 */
	private String propertyDisplayValue;
	
	/** 颜色属性或商品的图片，[type，图片列表] */
	private Map<String, List<ImageViewCommand>> images;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Long getItemPropertyId() {
		return itemPropertyId;
	}

	public void setItemPropertyId(Long itemPropertyId) {
		this.itemPropertyId = itemPropertyId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getPropertyDisplayValue() {
		return propertyDisplayValue;
	}

	public void setPropertyDisplayValue(String propertyDisplayValue) {
		this.propertyDisplayValue = propertyDisplayValue;
	}

	public Map<String, List<ImageViewCommand>> getImages() {
		return images;
	}

	public void setImages(Map<String, List<ImageViewCommand>> images) {
		this.images = images;
	}
	
}
