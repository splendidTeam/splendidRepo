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
package com.baozun.nebula.web.controller.product.converter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogItemImageViewCommand;
import com.feilong.core.Validator;

/**
 * shopdog商品图片视图模型转换
 * @author xingyu.liu
 *
 */
public class ShopdogItemImageViewCommandConverter extends BaseConverter<ShopdogItemImageViewCommand> {

	private static final long serialVersionUID = -127066731936568196L;

	public ShopdogItemImageViewCommand convert(Object data) {
		if(null == data){
			return null;
		}
		if(data instanceof ItemImage){
			ShopdogItemImageViewCommand  shopdogItemImageViewCommand= null;
			try{
				ItemImageViewCommand itemImageViewCommand = (ItemImageViewCommand) data;
				if(Validator.isNotNullOrEmpty(itemImageViewCommand.getImages())){
					shopdogItemImageViewCommand= new ShopdogItemImageViewCommand();
					shopdogItemImageViewCommand.setColorItemPropertyId(itemImageViewCommand.getColorItemPropertyId());
					Map<String, List<String>> images = new LinkedHashMap<String, List<String>>();
					
					Map<String, List<ImageViewCommand>> map = itemImageViewCommand.getImages();
					for(Entry<String, List<ImageViewCommand>> entry:map.entrySet()){
						List<ImageViewCommand> imageViewCommands = entry.getValue();
						List<String> urls = new ArrayList<String>();
						for(ImageViewCommand imageViewCommand:imageViewCommands){
							urls.add(imageViewCommand.getUrl());
						}
						
						images.put(entry.getKey(), urls);
					}
	
					shopdogItemImageViewCommand.setImages(images);
				}
				return shopdogItemImageViewCommand;
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + ShopdogItemImageViewCommandConverter.class + "yet.");
		}
		return null;
	}
}
