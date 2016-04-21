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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.feilong.core.Validator;

/**
 * 商品图片视图模型转换
 * @author xingyu.liu
 *
 */
public class ItemImageViewCommandConverter extends BaseConverter<ItemImageViewCommand> {

	private static final long serialVersionUID = -2286315924775522534L;
	
	@Autowired
	ImageViewCommandConverter                                       imageViewCommandConverter;

	public List<ItemImageViewCommand> convert(List<? extends Object> objectList) {
        if(objectList == null) return null;
        
		if(objectList instanceof List){
			List<ItemImage> itemImageList = null;
			try {
				itemImageList = (List<ItemImage>) objectList;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List<ItemImageViewCommand> itemImageViewCommands   = new ArrayList<ItemImageViewCommand>();
			Map<Long,List<ItemImage>> map = new HashMap<Long,List<ItemImage>>();
		    //根据商品颜色属性区分图片
			if(Validator.isNotNullOrEmpty(itemImageList)){
				for(ItemImage itemImage:itemImageList){
					Long itemProperties = itemImage.getItemProperties();
					List<ItemImage> res =  map.get(itemProperties);
					if(res!=null){
						res.add(itemImage);
					}else{
						res = new ArrayList<ItemImage>();
						res.add(itemImage);
						map.put(itemProperties, res);
					}
				}
				if(Validator.isNotNullOrEmpty(map)){
					// 有颜色属性
					for(Entry<Long, List<ItemImage>> entry:map.entrySet()){
						List<ItemImage> itemImages = entry.getValue();
						ItemImageViewCommand itemImageViewCommand = new ItemImageViewCommand();
						itemImageViewCommand.setColorItemPropertyId(entry.getKey());
						//每个颜色属性对应构造一个图片集
						itemImageViewCommand.setImages(constructImagesMap(itemImages));
						itemImageViewCommand.setItemId(itemImages.get(0).getItemId());
						
						itemImageViewCommands.add(itemImageViewCommand);
					}
				}else{
					// 无颜色属性
					ItemImageViewCommand itemImageViewCommand = new ItemImageViewCommand();
					itemImageViewCommand.setColorItemPropertyId(null);
					itemImageViewCommand.setImages(constructImagesMap(itemImageList));
					itemImageViewCommand.setItemId(itemImageList.get(0).getItemId());
					
					itemImageViewCommands.add(itemImageViewCommand);
				}
				
				
			}
			return itemImageViewCommands;
		}else{
			throw new UnsupportDataTypeException(objectList.getClass()
					+ " cannot convert to " + ItemImageViewCommandConverter.class + "yet.");
		}
	}
	
	private Map<String, List<ImageViewCommand>> constructImagesMap(List<ItemImage> itemImageList) {
		Map<String, List<ImageViewCommand>> images = new HashMap<String, List<ImageViewCommand>>();
        // 根据图片类型区分
		if(Validator.isNotNullOrEmpty(itemImageList)){
			for(ItemImage itemImage :itemImageList){
				String type = itemImage.getType();
				List<ImageViewCommand> imageViewCommands = images.get(type);
				ImageViewCommand  imageViewCommand= imageViewCommandConverter.convert(itemImage);
				if(imageViewCommands!=null){
					imageViewCommands.add(imageViewCommand);
				}else{
					imageViewCommands = new ArrayList<ImageViewCommand>();
					imageViewCommands.add(imageViewCommand);
					images.put(type, imageViewCommands);
				}
			}
		}
		
		return images;
	}

	@Override
	public ItemImageViewCommand convert(Object data) {
		// TODO Auto-generated method stub
		return null;
	}
}
