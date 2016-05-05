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

public class ShopdogItemImageViewCommand extends BaseViewCommand {
	
	private static final long serialVersionUID = 7522687758516133640L;
	
	/** 商品颜色属性Id */
    private Long colorItemPropertyId;
	
	/** [type，图片列表] */
	private Map<String, List<ImageViewCommand>> images;

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
