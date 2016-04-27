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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
 * 颜色（或者其他属性，颜色是个统称）或商品切换部分
 * <p>
 * 		[条件]当pdp展示模式取:
 * </p>
 * <p>
 * 		商品到色，PDP根据款号聚合（到款显示）,即模式二@see (NebulaAbstractPdpController.PDP_MODE_COLOR_COMBINE)
 * </p>
 * <p>
 * 		设置pdp的view切换属性
 * </p>  
 * @Description 
 * @author dongliang ma
 * @date 2016年4月22日 下午4:00:31 
 * @version   
 */
@Component
public class ItemColorSwatchViewCommandResolverImpl implements ItemColorSwatchViewCommandResolver{
	
	private static final Logger         LOGGER                        	= LoggerFactory.getLogger(ItemColorSwatchViewCommandResolverImpl.class);
	
	@Autowired
	private ItemDetailManager											itemDetailManager;
	
	@Autowired
	private SdkItemManager 												sdkItemManager;
	
	private static final String			IMG_TYPE_COLOR				   	="IMG_TYPE_COLOR";
	
	private static final String          KEY_PROPS_SALE		    		= "salePropCommandList";

	/* 
	 * ItemColorSwatchViewCommand中有三类东西:商品信息、图片信息、属性/值信息@see com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand
	 * @see com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolver#resolve(com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand, com.baozun.nebula.web.controller.product.converter.ItemImageViewCommandConverter)
	 */
	@Override
	public List<ItemColorSwatchViewCommand> resolve(
			ItemBaseInfoViewCommand baseInfoViewCommand,
			ItemImageViewCommandConverter itemImageViewCommandConverter) {
		Long itemId =baseInfoViewCommand.getId();
		String itemCode =baseInfoViewCommand.getCode();
		String style =baseInfoViewCommand.getStyle();
		if(Validator.isNullOrEmpty(style)){
			LOGGER.warn(
                    "[RESOLVER_ITEMCOLORSWATCHVIEW] function resolve:{},can not find style=[{}] in baseInfoViewCommand, itemCode =[{}]",
                    new Date(),
                    JsonUtil.format(baseInfoViewCommand),
                    itemCode);
			return Collections.emptyList();
		}
		//①.商品信息、图片信息
		List<ItemColorSwatchViewCommand> colorSwatchViewCommands =new ArrayList<ItemColorSwatchViewCommand>();
		//根据商品的style获取同款商品
		List<ItemCommand> itemCommands =itemDetailManager.findItemListByItemId(itemId, style);
		Map<Long, String> codeMap =new HashMap<Long, String>();
		//结果冗余的itemCode需要事先获取[itemId,itemCode]的对应关系
		List<Long> itemIds = new ArrayList<Long>();
		for (ItemCommand itemCommand : itemCommands) {
			itemIds.add(itemCommand.getId());
			codeMap.put(itemCommand.getId(), itemCommand.getCode());
		}
		//获取所有相关的图片
		List<ItemImage> itemImageList = sdkItemManager.findItemImageByItemIds(itemIds, null);
		//转换或者说是抽取我们所需要的颜色图片信息
		List<ItemImageViewCommand> imageViewCommands =itemImageViewCommandConverter.convert(itemImageList);
		if(Validator.isNotNullOrEmpty(imageViewCommands)){
			for (ItemImageViewCommand itemImageViewCommand : imageViewCommands) {
				//找到colorItemPropertyId，设值
				if(Validator.isNotNullOrEmpty(itemImageViewCommand.getColorItemPropertyId())){
					colorSwatchViewCommands.add(contructItemColorSwatchViewCommand(codeMap,
							itemImageViewCommand));
				}
			}
		}
		//②.属性/值信息
		if(Validator.isNotNullOrEmpty(colorSwatchViewCommands)){
			return filtrateColorSwatchViewinfos(colorSwatchViewCommands, itemIds);
		}
		return Collections.emptyList();
	}

	/**
	 * 筛选出最终颜色销售属性信息，并设值属性/值信息
	 * @param colorSwatchViewCommands
	 * @param itemIds
	 */
	private List<ItemColorSwatchViewCommand> filtrateColorSwatchViewinfos(
			List<ItemColorSwatchViewCommand> colorSwatchViewCommands,
			List<Long> itemIds) {
		List<ItemColorSwatchViewCommand> resultList =new ArrayList<ItemColorSwatchViewCommand>();
		Map<String, Object> dynamicPropertyMap =itemDetailManager.findDynamicPropertyByItemIds(itemIds);
		@SuppressWarnings("unchecked")
		List<DynamicPropertyCommand> saleDynamicPropertyCommandList = (List<DynamicPropertyCommand>) dynamicPropertyMap
		        .get(KEY_PROPS_SALE);
		
		if(Validator.isNotNullOrEmpty(saleDynamicPropertyCommandList)){
			Property property =null;
			//1.获取属性propery信息( 取颜色属性，目前处理只会有一个颜色属性的情况)
			//2.获取[propertiesId,ItemPropertiesCommand]，便于设置属性值
			Map<Long, ItemPropertiesCommand> propertiesMap =new HashMap<Long, ItemPropertiesCommand>();
			for (DynamicPropertyCommand dynamicPropertyCommand : saleDynamicPropertyCommandList) {
				if(Validator.isNullOrEmpty(property)){
					boolean isSetProperty =Validator.isNotNullOrEmpty(dynamicPropertyCommand.getProperty())&&
							dynamicPropertyCommand.getProperty().getIsSaleProp()&&
							dynamicPropertyCommand.getProperty().getIsColorProp();
					if(isSetProperty){
						property =dynamicPropertyCommand.getProperty();
					}
				}
				if(Validator.isNotNullOrEmpty(dynamicPropertyCommand.getItemProperties())){
					Long ipId =dynamicPropertyCommand.getItemProperties().getItem_properties_id();
					if(Validator.isNotNullOrEmpty(ipId)){
						if(Validator.isNotNullOrEmpty(propertiesMap)&&
								Validator.isNotNullOrEmpty(propertiesMap.get(ipId))){
							continue ;
						}else{
							propertiesMap.put(ipId, dynamicPropertyCommand.getItemProperties());
						}
					}
				}else if(Validator.isNotNullOrEmpty(dynamicPropertyCommand.getItemPropertiesList())){
					for (ItemPropertiesCommand itemPropertiesCommand : dynamicPropertyCommand.getItemPropertiesList()) {
						if(Validator.isNotNullOrEmpty(propertiesMap)&&
								Validator.isNotNullOrEmpty(propertiesMap.get(itemPropertiesCommand.getItem_properties_id()))){
							continue ;
						}else{
							propertiesMap.put(itemPropertiesCommand.getItem_properties_id(), itemPropertiesCommand);
						}
					}
				}
			}
			//是销售属性，且颜色属性
			if(Validator.isNotNullOrEmpty(property)){
				for (ItemColorSwatchViewCommand colorSwatchViewCommand : colorSwatchViewCommands) {
					//属性名
					colorSwatchViewCommand.setPropertyName(property.getName());
					//取属性匹配的颜色信息
					if(Validator.isNotNullOrEmpty(propertiesMap)&&
							Validator.isNotNullOrEmpty(propertiesMap.get(colorSwatchViewCommand.getItemPropertyId()))){
						ItemPropertiesCommand itemPropertiesCommand =propertiesMap.get(colorSwatchViewCommand.getItemPropertyId());
						if(Validator.isNotNullOrEmpty(itemPropertiesCommand.getPropertyId())&&
								property.getId().equals(itemPropertiesCommand.getPropertyId())){
							//属性值
							colorSwatchViewCommand.setPropertyValue(itemPropertiesCommand.getPropertyValue());
							resultList.add(colorSwatchViewCommand);
						}
					}
					
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 构造view对象，设置基本属性信息
	 * @param codeMap
	 * @param itemImageViewCommand
	 * @return
	 */
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
