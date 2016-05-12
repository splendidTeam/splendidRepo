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
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.product.transform.PropertyValueViewCommandTransformer;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyValueViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.comparator.PropertyComparator;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * 根据商品的基本信息、已获取的属性信息、色块信息构造属性元素集信息
 * @Description 
 * @author dongliang ma
 * @date 2016年4月21日 上午11:32:17 
 * @version   
 */
@Component
public class SortAndConstructElementViewResolverImpl implements SortAndConstructElementViewResolver{
	
	private static final Logger         LOGGER                        	= LoggerFactory.getLogger(SortAndConstructElementViewResolverImpl.class);

	/* 
	 * @see com.baozun.nebula.web.controller.product.resolver.SortAndConstructElementViewResolver#resolve(com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand, java.util.List, java.util.Map)
	 */
	@Override
	public List<PropertyElementViewCommand> resolve(
			ItemBaseInfoViewCommand baseInfoViewCommand,
			List<DynamicPropertyCommand> dynamicPropertyCommandList,
			Map<Long, ImageViewCommand> colorswatchMap) {
		Long itemId 	=baseInfoViewCommand.getId();
		String itemCode =baseInfoViewCommand.getCode();
		//按照property.sortNo排序 
        Collections.sort(dynamicPropertyCommandList, new PropertyComparator<DynamicPropertyCommand>("property.sortNo"));

        List<PropertyElementViewCommand> elementViewCommands =new ArrayList<PropertyElementViewCommand>();
        PropertyElementViewCommand elementViewCommand =null;
        for (DynamicPropertyCommand dynamicPropertyCommand : dynamicPropertyCommandList){
            //属性
        	PropertyViewCommand propertySubViewCommand = constructPropertyViewCommand(dynamicPropertyCommand);
            //属性值列表
            List<PropertyValueViewCommand> propertyValueViewSubCommandList = constructPropertyValueViewCommandList(
                            itemCode,
                            dynamicPropertyCommand,
                            colorswatchMap);
            if (null == propertyValueViewSubCommandList) {
                propertyValueViewSubCommandList = Collections.emptyList();
            }
            //添加商品属性元素
            elementViewCommand =new PropertyElementViewCommand();
        	elementViewCommand.setProperty(propertySubViewCommand);
        	elementViewCommand.setPropertyValues(propertyValueViewSubCommandList);
        	elementViewCommands.add(elementViewCommand);
        }
        if (Validator.isNullOrEmpty(elementViewCommands)) {
            LOGGER.warn(
                            "[RESOLVER_ITEMCOLORSWATCHVIEW] function resolve:itemId:[{}],itemCode:[{}],result isNullOrEmpty,but dynamicPropertyCommandList is:[{}]",
                            itemId,
                            itemCode,
                            JsonUtil.format(dynamicPropertyCommandList));
        }
        return elementViewCommands;
	}
	/**
	 * 属性(DB->view)
	 * @param dynamicPropertyCommand
	 * @return
	 */
	private PropertyViewCommand constructPropertyViewCommand(
			DynamicPropertyCommand dynamicPropertyCommand) {
		Property property = dynamicPropertyCommand.getProperty();
		PropertyViewCommand propertyViewCommand = new PropertyViewCommand();

        PropertyUtil.copyProperties(propertyViewCommand, property, 
        		"id",
        		"name",
        		"isColorProp",
        		"isSaleProp",
        		"groupName",
        		"valueType");
        return propertyViewCommand;
	}
	
	/**
	 * 属性值(DB->view)
	 * @param itemCode
	 * @param dynamicPropertyCommand
	 * @param colorswatchMap
	 * @return
	 */
	private List<PropertyValueViewCommand> constructPropertyValueViewCommandList(
			String itemCode, DynamicPropertyCommand dynamicPropertyCommand,
			Map<Long, ImageViewCommand> colorswatchMap) {
		List<ItemPropertiesCommand> itemPropertiesList = constructItemPropertiesCommandList(dynamicPropertyCommand);
        if (Validator.isNullOrEmpty(itemPropertiesList)) {
            return Collections.emptyList();
        }
        Property property = dynamicPropertyCommand.getProperty();
        
        PropertyValueViewCommandTransformer transformer = new PropertyValueViewCommandTransformer(
                        itemCode,
                        property,
                        colorswatchMap);
        //根据itemPropertiesList转换PropertyValueViewCommand
        List<PropertyValueViewCommand> propertyValueViewSubCommands = (List<PropertyValueViewCommand>) CollectionUtils
                        .collect(itemPropertiesList, transformer);

        propertyValueViewSubCommands = CollectionsUtil
                        .select(propertyValueViewSubCommands, PredicateUtils.<PropertyValueViewCommand> notNullPredicate());

        if (Validator.isNotNullOrEmpty(propertyValueViewSubCommands)) {
            //排序
            //这里不能按照 proSort 来排序,其实 proSort 是个理想的设想,但是属性是不断的新增的, 没有人时刻会来维护这里的排序
            Collections.sort(propertyValueViewSubCommands, new PropertyComparator<PropertyValueViewCommand>("propertyValue"));
        }
        return propertyValueViewSubCommands;
	}
	
	/**
	 * 获取该属性的itemPropertiesCommand集合
	 * @param dynamicPropertyCommand
	 * @return
	 */
	private List<ItemPropertiesCommand> constructItemPropertiesCommandList(
			DynamicPropertyCommand dynamicPropertyCommand) {
		List<ItemPropertiesCommand> resultItemPropertiesList = new ArrayList<ItemPropertiesCommand>();
		List<ItemPropertiesCommand> itemPropertiesList = dynamicPropertyCommand.getItemPropertiesList();
        if (Validator.isNotNullOrEmpty(itemPropertiesList)) {
        	resultItemPropertiesList.addAll(itemPropertiesList);
        }else{
            ItemPropertiesCommand itemPropertiesCommand = dynamicPropertyCommand.getItemProperties();
            if (Validator.isNotNullOrEmpty(itemPropertiesCommand)) {//如果 itemProperties不是空,表示是单值
            	resultItemPropertiesList.add(itemPropertiesCommand);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[RESOLVER_ITEMCOLORSWATCHVIEW] function constructItemPropertiesCommandList:{}", JsonUtil.format(resultItemPropertiesList));
        }
        return resultItemPropertiesList;
	}
	
	

}
