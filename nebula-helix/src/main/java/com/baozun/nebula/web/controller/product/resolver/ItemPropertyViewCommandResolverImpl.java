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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年4月20日 下午5:15:53 
 * @version   
 */
@Component
public class ItemPropertyViewCommandResolverImpl implements
		ItemPropertyViewCommandResolver {
	
	private static final Logger          LOGGER                     = LoggerFactory.getLogger(ItemPropertyViewCommandResolverImpl.class);
	
	@Autowired
	private ItemDetailManager										itemDetailManager;
	
	@Autowired
	private ColorSwatchResolver										colorSwatchResolver;
	
	@Autowired
	private SortAndConstructElementViewResolver						sortAndConstructElementViewResolver;
	
	private static final String          KEY_PROPS_SALE		    	= "salePropCommandList";
    
    private static final String          KEY_PROPS_GENERAL 			= "generalGroupPropMap";

    

	/* 
	 * @see com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolver#resolve(java.lang.Long, java.util.Map)
	 */
	@Override
	public ItemPropertyViewCommand resolve(ItemBaseInfoViewCommand baseInfoViewCommand,
			List<ItemImageViewCommand> imageViewCommands) {
		
		Map<String, Object> dynamicPropertyMap = itemDetailManager.gatherDynamicProperty(baseInfoViewCommand.getId());
		if(Validator.isNullOrEmpty(dynamicPropertyMap)){
			return new ItemPropertyViewCommand();
		}
		ItemPropertyViewCommand viewCommand =new ItemPropertyViewCommand();
		viewCommand.setSalesProperties(constructSalesPropertiesMap(baseInfoViewCommand, dynamicPropertyMap, imageViewCommands));
		viewCommand.setNonSalesProperties(constructNonSalesProperties(baseInfoViewCommand, dynamicPropertyMap));
		return viewCommand;
	}

	/**
	 * 构造销售属性
	 * @param baseInfoViewCommand
	 * @param dynamicPropertyMap
	 * @param imageViewCommands
	 * @return
	 */
	private List<PropertyElementViewCommand> constructSalesPropertiesMap(
			ItemBaseInfoViewCommand baseInfoViewCommand,
			Map<String, Object> dynamicPropertyMap,
			List<ItemImageViewCommand> imageViewCommands) {
		Long itemId = baseInfoViewCommand.getId();
        String itemCode = baseInfoViewCommand.getCode();
        @SuppressWarnings("unchecked")
		List<DynamicPropertyCommand> saleDynamicPropertyCommandList = (List<DynamicPropertyCommand>) dynamicPropertyMap
                        .get(KEY_PROPS_SALE);

        if (Validator.isNullOrEmpty(saleDynamicPropertyCommandList)) {
            LOGGER.error("item id:{},code:[{}],saleDynamicPropertyCommandList is null!", itemId, itemCode);
            return Collections.emptyList();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("saleDynamicPropertyCommandList:{}", JsonUtil.format(saleDynamicPropertyCommandList));
        }
        Map<Long, ImageViewCommand> itemPropertiesIdAndColorswatchMap = colorSwatchResolver.resolve(imageViewCommands);
        return sortAndConstructElementViewResolver.resolve(baseInfoViewCommand, saleDynamicPropertyCommandList, itemPropertiesIdAndColorswatchMap);
	}
	
	/**
	 * 构造一般属性
	 * @param baseInfoViewCommand
	 * @param dynamicPropertyMap
	 * @return
	 */
	private Map<String, List<PropertyElementViewCommand>> constructNonSalesProperties(
			ItemBaseInfoViewCommand baseInfoViewCommand,
			Map<String, Object> dynamicPropertyMap) {
		Long itemId = baseInfoViewCommand.getId();
        String itemCode = baseInfoViewCommand.getCode();
		@SuppressWarnings("unchecked")
		Map<String, List<DynamicPropertyCommand>> generalGroupPropMap =(Map<String, List<DynamicPropertyCommand>>)dynamicPropertyMap
						.get(KEY_PROPS_GENERAL);
		if (Validator.isNullOrEmpty(generalGroupPropMap)) {
            LOGGER.error("item id:{},code:[{}],nonSalesPropertiesMap is null!", itemId, itemCode);
            return Collections.emptyMap();
        }
		Map<String, List<PropertyElementViewCommand>> resultMap =new HashMap<String, List<PropertyElementViewCommand>>();
		for (Map.Entry<String, List<DynamicPropertyCommand>> entry :generalGroupPropMap.entrySet()) {
			if(Validator.isNotNullOrEmpty(entry.getValue())){
				List<DynamicPropertyCommand> generalPropCommandList = entry.getValue();
				List<PropertyElementViewCommand> elementViewCommands = sortAndConstructElementViewResolver.resolve(baseInfoViewCommand,
						generalPropCommandList, null);
				if(Validator.isNotNullOrEmpty(elementViewCommands)){
					resultMap.put(entry.getKey(), elementViewCommands);
				}
			}
		}
		if (LOGGER.isDebugEnabled()) {
		    LOGGER.debug("nonSalesPropertiesMap:{}", JsonUtil.format(resultMap));
		}
		return resultMap;
	}

}
