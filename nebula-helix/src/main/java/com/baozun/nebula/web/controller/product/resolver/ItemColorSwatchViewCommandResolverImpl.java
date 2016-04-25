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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.product.converter.ItemImageViewCommandConverter;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年4月22日 下午4:00:31 
 * @version   
 */
public class ItemColorSwatchViewCommandResolverImpl implements ItemColorSwatchViewCommandResolver{
	
	private static final Logger         LOGGER                        	= LoggerFactory.getLogger(ItemColorSwatchViewCommandResolverImpl.class);
	
	@Autowired
	private ItemDetailManager											itemDetailManager;
	
	@Autowired
	private SdkItemManager 												sdkItemManager;
	
	@Autowired
	@Qualifier("itemImageViewCommandConverter")
	ItemImageViewCommandConverter                                   	itemImageViewCommandConverter;
	
	private static final String			IMG_TYPE_COLOR				   	="IMG_TYPE_COLOR";
	
	private static final String          KEY_PROPS_SALE		    		= "salePropCommandList";

	/* 
	 * @see com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolver#resolve(com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand, java.util.List, com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand)
	 */
	@Override
	public List<ItemColorSwatchViewCommand> resolve(
			ItemBaseInfoViewCommand baseInfoViewCommand) {
		Long itemId =baseInfoViewCommand.getId();
		String itemCode =baseInfoViewCommand.getCode();
		String style =baseInfoViewCommand.getStyle();
		if(Validator.isNullOrEmpty(style)){
			LOGGER.warn(
                    "resolve:{},can not find style=[{}] in baseInfoViewCommand, itemCode =[{}]",
                    new Date(),
                    JsonUtil.format(baseInfoViewCommand),
                    itemCode);
		}
		List<ItemColorSwatchViewCommand> colorSwatchViewCommands =new ArrayList<ItemColorSwatchViewCommand>();
		List<ItemCommand> itemCommands =itemDetailManager.findItemListByItemId(itemId, style);
		Map<Long, String> codeMap =new HashMap<Long, String>();
		//结果冗余的itemCode需要事先获取[itemId,itemCode]的对应关系
		List<Long> itemIds = new ArrayList<Long>();
		for (ItemCommand itemCommand : itemCommands) {
			itemIds.add(itemCommand.getId());
			codeMap.put(itemCommand.getId(), itemCommand.getCode());
		}
		Long colorItemPropertyId =null;
		List<ItemImage> itemImageList = sdkItemManager.findItemImageByItemIds(itemIds, null);
		List<ItemImageViewCommand> imageViewCommands =itemImageViewCommandConverter.convert(itemImageList);
		if(Validator.isNotNullOrEmpty(imageViewCommands)){
			for (ItemImageViewCommand itemImageViewCommand : imageViewCommands) {
				if(Validator.isNotNullOrEmpty(itemImageViewCommand.getColorItemPropertyId())){
					colorSwatchViewCommands.add(contructItemColorSwatchViewCommand(codeMap,
							itemImageViewCommand));
				}
				if(itemImageViewCommand.getItemId().equals(itemId)){
					//取一个colorItemPropertyId
					colorItemPropertyId =itemImageViewCommand.getColorItemPropertyId();
				}
			}
		}
		if(Validator.isNotNullOrEmpty(colorItemPropertyId)&&
				Validator.isNotNullOrEmpty(colorSwatchViewCommands)){
			//设置属性信息
			Map<String, Object> dynamicPropertyMap =itemDetailManager.findDynamicPropertyByItemIds(itemIds);
			@SuppressWarnings("unchecked")
			List<DynamicPropertyCommand> saleDynamicPropertyCommandList = (List<DynamicPropertyCommand>) dynamicPropertyMap
	                .get(KEY_PROPS_SALE);
			
			if(Validator.isNotNullOrEmpty(saleDynamicPropertyCommandList)){
				Property property =null;
				//[propertiesId,ItemPropertiesCommand]
				Map<Long, ItemPropertiesCommand> propertiesMap =new HashMap<Long, ItemPropertiesCommand>();
				for (DynamicPropertyCommand dynamicPropertyCommand : saleDynamicPropertyCommandList) {
					if(Validator.isNotNullOrEmpty(dynamicPropertyCommand.getItemProperties())){
						Long ipId =dynamicPropertyCommand.getItemProperties().getItem_properties_id();
						if(Validator.isNotNullOrEmpty(ipId)&&
								ipId.equals(colorItemPropertyId)){
							if(Validator.isNotNullOrEmpty(property)){
								property =dynamicPropertyCommand.getProperty();
							}
							if(Validator.isNotNullOrEmpty(propertiesMap)&&
									Validator.isNotNullOrEmpty(propertiesMap.get(ipId))){
								continue ;
							}else{
								propertiesMap.put(ipId, dynamicPropertyCommand.getItemProperties());
							}
						}
					}else if(Validator.isNotNullOrEmpty(dynamicPropertyCommand.getItemPropertiesList())){
						for (ItemPropertiesCommand itemPropertiesCommand : dynamicPropertyCommand.getItemPropertiesList()) {
							if(itemPropertiesCommand.getItem_properties_id().equals(colorItemPropertyId)){
								if(Validator.isNotNullOrEmpty(property)){
									property =dynamicPropertyCommand.getProperty();
								}
							}
							if(Validator.isNotNullOrEmpty(propertiesMap)&&
									Validator.isNotNullOrEmpty(propertiesMap.get(itemPropertiesCommand.getItem_properties_id()))){
								continue ;
							}else{
								propertiesMap.put(itemPropertiesCommand.getItem_properties_id(), itemPropertiesCommand);
							}
						}
					}
				}
				if(Validator.isNotNullOrEmpty(property)){
					for (ItemColorSwatchViewCommand colorSwatchViewCommand : colorSwatchViewCommands) {
						//属性名
						colorSwatchViewCommand.setPropertyName(property.getName());
						if(Validator.isNotNullOrEmpty(propertiesMap)&&
								Validator.isNotNullOrEmpty(propertiesMap.get(colorSwatchViewCommand.getItemPropertyId()))){
							ItemPropertiesCommand itemPropertiesCommand =propertiesMap.get(colorSwatchViewCommand.getItemPropertyId());
							//属性值
							colorSwatchViewCommand.setPropertyValue(itemPropertiesCommand.getPropertyValue());
						}
						
					}
				}
				
			}
		}
		
		return colorSwatchViewCommands;
	}
	
	private ItemColorSwatchViewCommand contructItemColorSwatchViewCommand(Map<Long, String> codeMap,
			ItemImageViewCommand itemImageViewCommand){
		ItemColorSwatchViewCommand colorSwatchViewCommand =new ItemColorSwatchViewCommand();
		colorSwatchViewCommand.setItemId(itemImageViewCommand.getItemId());
		colorSwatchViewCommand.setItemCode(codeMap.get(itemImageViewCommand.getItemId()));
		Map<String, List<ImageViewCommand>> images =itemImageViewCommand.getImages();
		if(Validator.isNotNullOrEmpty(images) && 
				Validator.isNotNullOrEmpty(images.get(IMG_TYPE_COLOR))){
			ImageViewCommand imageViewCommand=images.get(IMG_TYPE_COLOR).get(0);
			colorSwatchViewCommand.setItemPropertyId(itemImageViewCommand.getColorItemPropertyId());
			colorSwatchViewCommand.setPropertyDisplayValue(imageViewCommand.getDescription());
			colorSwatchViewCommand.setImage(imageViewCommand.getUrl());
		}
		
		return colorSwatchViewCommand;
	}
	

}
