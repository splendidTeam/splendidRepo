/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.web.controller.product.resolver;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.baozun.nebula.model.product.ItemImage;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年4月21日 上午11:31:36 
 * @version   
 */
@Component
public class ColorSwatchResolverImpl implements ColorSwatchResolver {

	/* 
	 * @see com.baozun.nebula.web.controller.product.resolver.ColorSwatchResolver#resolve(java.util.List)
	 */
	@Override
	public Map<Long, String> resolve(List<ItemImage> itemImgList) {
		// TODO Auto-generated method stub
		return null;
	}

}
