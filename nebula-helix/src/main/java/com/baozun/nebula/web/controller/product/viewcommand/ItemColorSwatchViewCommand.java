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

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * Pdp同款商品的Command
 * <p>
 * 如果商品定义到款，Pdp展示时只是不同销售属性之间的切换。但如果商品定义到色（这个色是个笼统的概念，比如，衣服的颜色、眼镜的度数都可以叫色），Pdp展示时就会涉及到商品的切换。</br>
 * 该Command就是为了表示同款下的不同商品，一般在商品定义到色时使用。为了和到款商品使用上的统一化，一般要求到色商品也同时定义一个颜色属性。
 * </p>
 *
 */
public class ItemColorSwatchViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 3016160425868695170L;
    
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
	
	/** 颜色属性值的图片 */
	private String propertyValueThumb;
	
	/** 颜色属性显示值 */
	private String propertyDisplayValue;
	
	/** 颜色属性图片 */
	private String image;

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

	public String getPropertyValueThumb() {
		return propertyValueThumb;
	}

	public void setPropertyValueThumb(String propertyValueThumb) {
		this.propertyValueThumb = propertyValueThumb;
	}

	public String getPropertyDisplayValue() {
		return propertyDisplayValue;
	}

	public void setPropertyDisplayValue(String propertyDisplayValue) {
		this.propertyDisplayValue = propertyDisplayValue;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
