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
package com.baozun.nebula.web.command;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.model.product.ItemImage;

/**
 * 用于封装 修改item图片的信息
 * 
 * @author chenguang.zhou
 * @date 2014-1-26 上午09:52:01
 */
public class ItemImageCommand{

	/**
	 * 属性值Id
	 */
	Long			propertyValueId;

	/**
	 * Item图片集合
	 */
	List<ItemImage>	itemImages	= new ArrayList<ItemImage>();

	public Long getPropertyValueId(){
		return propertyValueId;
	}

	public void setPropertyValueId(Long propertyValueId){
		this.propertyValueId = propertyValueId;
	}

	public List<ItemImage> getItemImages(){
		return itemImages;
	}

	public void setItemImages(List<ItemImage> itemImages){
		this.itemImages = itemImages;
	}

}
