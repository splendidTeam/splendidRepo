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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.product.transform.ShopDogPropertyValueViewCommandTransformer;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogPropertyValueViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogPropertyViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.comparator.PropertyComparator;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * ShopDog定制专用Resolver  </br> 
 * 根据商品的基本信息、已获取的图片信息构造<span style="color:red">销售属性</span>信息
 * @Description 
 * @author dongliang ma
 * @date 2016年5月4日 下午3:10:13 
 * @version   
 */
@Component
public class ShopDogSalePropertyViewCommandResolverImpl implements ShopDogSalePropertyViewCommandResolver{
	
	private static final Logger         LOGGER                        	= LoggerFactory.getLogger(ShopDogSalePropertyViewCommandResolverImpl.class);
	
	@Autowired
	private ItemDetailManager										itemDetailManager;
	
	private static final String          KEY_PROPS_SALE		    	= "salePropCommandList";

	/* 
	 * @see com.baozun.nebula.web.controller.product.resolver.ShopDogSalePropertyViewCommandResolver#resolve(com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand, java.util.List)
	 */
	@Override
	public List<ShopdogPropertyViewCommand> resolve(ItemBaseInfoViewCommand baseInfoViewCommand,
			List<ShopdogItemImageViewCommand> picUrls) {
		//获取商品销售属性
		Map<String, Object> dynamicPropertyMap = itemDetailManager.gatherDynamicProperty(baseInfoViewCommand.getId());
		if(Validator.isNullOrEmpty(dynamicPropertyMap)||Validator.isNullOrEmpty(dynamicPropertyMap
                .get(KEY_PROPS_SALE))){
				LOGGER.debug("[RESOLVER_SHOPDOG_SALEPROP] function resolve:[{}],code:[{}],the item hasn't any saleproperties!",
						baseInfoViewCommand.getId(), baseInfoViewCommand.getCode());
				return new ArrayList<ShopdogPropertyViewCommand>();
		}
		Long itemId 	=baseInfoViewCommand.getId();
		String itemCode =baseInfoViewCommand.getCode();
		@SuppressWarnings("unchecked")
		List<DynamicPropertyCommand> saleDynamicPropertyCommandList = (List<DynamicPropertyCommand>) dynamicPropertyMap
                        .get(KEY_PROPS_SALE);

        if (Validator.isNullOrEmpty(saleDynamicPropertyCommandList)) {
            LOGGER.debug("[RESOLVER_SHOPDOG_SALEPROP] function resolve:[{}],code:[{}],saleDynamicPropertyCommandList is null!",
            		itemId, itemCode);
            return new ArrayList<ShopdogPropertyViewCommand>();
        }
        //获取颜色
        Map<Long, ShopdogItemImageViewCommand> colorswatchMap =constructColorSwatchMap(picUrls);
        
		//按照property.sortNo排序 
        Collections.sort(saleDynamicPropertyCommandList, new PropertyComparator<DynamicPropertyCommand>("property.sortNo"));

        List<ShopdogPropertyViewCommand> elementViewCommands =new ArrayList<ShopdogPropertyViewCommand>();
        ShopdogPropertyViewCommand elementViewCommand =null;
        for (DynamicPropertyCommand dynamicPropertyCommand : saleDynamicPropertyCommandList){
            
			//属性值列表
            List<ShopdogPropertyValueViewCommand> propertyValueViewSubCommandList = constructPropertyValueViewCommandList(
                            itemCode,
                            dynamicPropertyCommand,
                            colorswatchMap);
            if (null == propertyValueViewSubCommandList) {
                propertyValueViewSubCommandList = Collections.emptyList();
            }
            //添加商品属性元素
            elementViewCommand =new ShopdogPropertyViewCommand();
            //属性
            elementViewCommand.setName(dynamicPropertyCommand.getProperty().getName());
        	elementViewCommand.setIsColorProp(dynamicPropertyCommand.getProperty().getIsColorProp());;
        	elementViewCommand.setPropertyValues(propertyValueViewSubCommandList);
        	elementViewCommands.add(elementViewCommand);
        }
        if (Validator.isNullOrEmpty(elementViewCommands)) {
            LOGGER.warn(
                            "[RESOLVER_SHOPDOG_SALEPROP] function resolve:itemId:[{}],itemCode:[{}],result isNullOrEmpty,but dynamicPropertyCommandList is:[{}]",
                            itemId,
                            itemCode,
                            JsonUtil.format(saleDynamicPropertyCommandList));
        }
        return elementViewCommands;
	}
	
	private Map<Long, ShopdogItemImageViewCommand> constructColorSwatchMap(List<ShopdogItemImageViewCommand> picUrls){
		Map<Long, ShopdogItemImageViewCommand> colorswatchMap =new HashMap<Long, ShopdogItemImageViewCommand>();
		if(Validator.isNotNullOrEmpty(picUrls)){
			for (ShopdogItemImageViewCommand shopdogItemImageViewCommand : picUrls) {
				if(Validator.isNotNullOrEmpty(shopdogItemImageViewCommand.getColorItemPropertyId())){
					colorswatchMap.put(shopdogItemImageViewCommand.getColorItemPropertyId(), shopdogItemImageViewCommand);
				}
			}
		}
		return colorswatchMap;
	}
	
	/**
	 * 属性值(DB->view)
	 * @param itemCode
	 * @param dynamicPropertyCommand
	 * @param colorswatchMap
	 * @return
	 */
	private List<ShopdogPropertyValueViewCommand> constructPropertyValueViewCommandList(
			String itemCode, DynamicPropertyCommand dynamicPropertyCommand,
			Map<Long, ShopdogItemImageViewCommand> colorswatchMap) {
		List<ItemPropertiesCommand> itemPropertiesList = constructItemPropertiesCommandList(dynamicPropertyCommand);
        if (Validator.isNullOrEmpty(itemPropertiesList)) {
            return Collections.emptyList();
        }
        Property property = dynamicPropertyCommand.getProperty();
        
        ShopDogPropertyValueViewCommandTransformer transformer = new ShopDogPropertyValueViewCommandTransformer(
                        itemCode,
                        property,
                        colorswatchMap);
        //根据itemPropertiesList转换PropertyValueViewCommand
        List<ShopdogPropertyValueViewCommand> propertyValueViewSubCommands = (List<ShopdogPropertyValueViewCommand>) CollectionUtils
                        .collect(itemPropertiesList, transformer);

        propertyValueViewSubCommands = CollectionsUtil
                        .select(propertyValueViewSubCommands, PredicateUtils.<ShopdogPropertyValueViewCommand> notNullPredicate());

        if (Validator.isNotNullOrEmpty(propertyValueViewSubCommands)) {
            //排序
            //这里不能按照 proSort 来排序,其实 proSort 是个理想的设想,但是属性是不断的新增的, 没有人时刻会来维护这里的排序
            Collections.sort(propertyValueViewSubCommands, new PropertyComparator<ShopdogPropertyValueViewCommand>("propertyValue"));
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
            LOGGER.debug("[RESOLVER_SHOPDOG_SALEPROP] function constructItemPropertiesCommandList:{}", JsonUtil.format(resultItemPropertiesList));
        }
        return resultItemPropertiesList;
	}
	
	

}
