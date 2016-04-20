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

import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 分到款和到色两种情况，
 * 到款时：取某一个颜色属性下的图片
 * 到色时：取该商品下的图片
 * 
 */
public class ItemImageViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 6626730663899868812L;
	
	/** 商品Id */
	private Long itemId;
	
	/** 
	 * <pre>	 
	 * 1：到款商品颜色属性的Id{@link ItemProperties#getId()}；
	 * 2：为了到色商品切换颜色图片的逻辑一致，最后规定到色商品也会定义颜色属性（之前是定义图片类型为
	 * 颜色类型，现在是颜色属性对应的图片）。
	 * </pre>
	 */
	
	private Long colorItemPropertyId;
	
	/** [type，图片列表] */
	private Map<String, List<ImageViewCommand>> images;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getColorItemPropertyId() {
		return colorItemPropertyId;
	}

	public void setColorItemPropertyId(Long colorItemPropertyId) {
		this.colorItemPropertyId = colorItemPropertyId;
	}

	public Map<String, List<ImageViewCommand>> getImages() {
		return images;
	}

	public void setImages(Map<String, List<ImageViewCommand>> images) {
		this.images = images;
	}
}
