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
 * 商品的图片
 * <ul>
 *   <li>Nebula里商品如果有颜色属性，那么每一个颜色属性都会有一套图片。如果没有颜色属性，那么该商品只会有一套图片。</li>
 *   <li>在实施层面，一个商品一般会定义多个类型的图片用于不同的显示需求，比如，商品的多角度图、颜色图、尺码对照图、描述图等会分别定义不同的类型。</li>
 *   <li>无论商品是定义到款或是到色，在构造数据时会把该商品下的所有图片全部取出。不同的是，到色时，pdp切换了颜色意味着切换了商品，需要重新加载该商品的图片。</li>
 * </ul>
 */
public class ItemImageViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 6626730663899868812L;
	
	/** 商品Id */
	private Long itemId;
	
	/** 
	 * 商品颜色属性的Id{@link ItemProperties#getId()}。
	 * 如果商品没有颜色属性，则该属性为空。<strong>推荐的做法是，商品定义到色同时定义颜色属性。</strong>
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
