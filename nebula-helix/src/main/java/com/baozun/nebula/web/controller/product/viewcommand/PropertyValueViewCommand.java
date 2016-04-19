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

import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 属性值的Command
 */
public class PropertyValueViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 3207776073529744700L;
	
	private Long              itemPropertiesId;

    /** The property id. */
    private Long              propertyId;

    /** The property name(冗余,可能会用到). */
    private String            propertyName;

    /**
     * 属性值 .
     * <p>
     * 不完全等价于 {@link ItemPropertiesCommand#getPropertyValue()}
     * </p>
     * 
     * <p>
     * (for show,如果有颜色对照,尺码对照将会显示 显示的值,因此在某种程度上可以做到根据不同的逻辑设置不同的值)
     * </p>
     * 
     * <p style="color:green">
     * 一般来说,系统中的接口码对于前端页面没有什么作用,前端只关心我要显示什么值,和后端交互都是使用xxxId来交互的
     * </p>
     */
    private String            propertyValue;

    /**
     * 图片,一般是颜色的属性值有图片.
     * <p>
     * 目前逻辑是 取的 {@link com.baozun.nebula.model.product.ItemImage#picUrl}字段.
     * </p>
     * <p>
     * 是相对地址,需要在页面使用自定义标签渲染.
     * </p>
     */
    private String            imageUrl;

	public Long getItemPropertiesId() {
		return itemPropertiesId;
	}

	public void setItemPropertiesId(Long itemPropertiesId) {
		this.itemPropertiesId = itemPropertiesId;
	}

	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
