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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * 获取颜色小色块
 * <p>将pdp页面获取的图片信息封装成[itemPropertyId,imageCommand]的形式,便于补充属性信息的图片信息;</p>
 * <p>一般用于拥有颜色块属性的信息补充.</p>
 * @Description 
 * @author dongliang ma
 * @date 2016年4月21日 上午11:31:36 
 * @version   
 */
@Component
public class ColorSwatchResolverImpl implements ColorSwatchResolver {
	
	private static final Logger         LOGGER                        	= LoggerFactory.getLogger(ColorSwatchResolverImpl.class);
	
	/** 
	 * 颜色图片的type
	 * */
	private static final String			IMG_TYPE_COLOR				   	="IMG_TYPE_COLOR";

	/* 
	 * 
	 * 
	 * @see com.baozun.nebula.web.controller.product.resolver.ColorSwatchResolver#resolve(java.util.List)
	 */
	@Override
	public Map<Long, ImageViewCommand> resolve(List<ItemImageViewCommand> imageViewCommands) {
		if (Validator.isNullOrEmpty(imageViewCommands)) {
            return Collections.emptyMap();
        }
		Map<Long, ImageViewCommand> resultMap =new HashMap<Long, ImageViewCommand>();
		List<ImageViewCommand> imageList =null;
		for (ItemImageViewCommand itemImageViewCommand : imageViewCommands) {
			//ItemImageViewCommand中的colorItemPropertyId如果有值，一般就说明这是个颜色属性，
			//这个值就是itemPropertyId，可根据这个值获取图片信息
			if(Validator.isNotNullOrEmpty(itemImageViewCommand.getColorItemPropertyId())&&
					Validator.isNotNullOrEmpty(itemImageViewCommand.getImages())){
				imageList =itemImageViewCommand.getImages().get(IMG_TYPE_COLOR);
				if(Validator.isNotNullOrEmpty(imageList)){
					//取第一个
					resultMap.put(itemImageViewCommand.getColorItemPropertyId(), imageList.get(0));
				}
			}
		}
		
        if (Validator.isNullOrEmpty(resultMap)) {
            LOGGER.warn("	[RESOLVER_COLORSWATCH] function resolve:{},can not find type=[{}] imageViewCommandList",
                            JsonUtil.format(imageViewCommands),
                            IMG_TYPE_COLOR);
            return Collections.emptyMap();
        }
        return resultMap;
	}

}
