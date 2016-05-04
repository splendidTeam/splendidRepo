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
package com.baozun.nebula.web.controller.product.transform;

import java.util.Map;

import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogPropertyValueViewCommand;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * ShopDog定制专用transformer  </br> 
 * 用于将itemPropertiesCommand转换为ShopdogPropertyValueViewCommand
 * @Description 
 * @author dongliang ma
 * @date 2016年5月4日 下午3:09:45 
 * @version   
 */
public class ShopDogPropertyValueViewCommandTransformer implements Transformer<ItemPropertiesCommand, ShopdogPropertyValueViewCommand>{
	
	/** The Constant log. */
    private static final Logger                LOGGER = LoggerFactory.getLogger(ShopDogPropertyValueViewCommandTransformer.class);

    /** The item code. */
    private final String                       itemCode;
    
    /** The property. */
    private final Property                     property;
    
    /** The item properties id and colorswatch image map. */
    private final Map<Long, ShopdogItemImageViewCommand>           colorswatchMap;

	/**
	 * 
	 * @param itemCode
	 * @param property
	 * @param colorswatchMap
	 */
	public ShopDogPropertyValueViewCommandTransformer(String itemCode,
			Property property, Map<Long, ShopdogItemImageViewCommand> colorswatchMap) {
		super();
		this.itemCode = itemCode;
		this.property = property;
		this.colorswatchMap = colorswatchMap;
	}


	/* 
	 * @see org.apache.commons.collections4.Transformer#transform(java.lang.Object)
	 */
	@Override
	public ShopdogPropertyValueViewCommand transform(ItemPropertiesCommand itemPropertiesCommand) {
		String propertyName = property.getName();
        itemPropertiesCommand.setPropertyName(propertyName);
        itemPropertiesCommand.setItemCode(itemCode);

        Long itemPropertiesId = itemPropertiesCommand.getItem_properties_id();
        Boolean isColorProp = property.getIsColorProp();
        ShopdogItemImageViewCommand colorswatch = null == colorswatchMap ? null
                        : colorswatchMap.get(itemPropertiesId);

        //是颜色属性 但是没有色块 ,那么返回null
        if (isColorProp && Validator.isNullOrEmpty(colorswatch)) {
            LOGGER.warn(
                            "itemCode:[{}],itemPropertiesId:[{}] is isColorProp,propertyName:[{}],propertyValue:[{}],but has no mapping colorswatch,itemPropertiesIdAndColorswatchImageMap is:[{}]",
                            itemCode,
                            itemPropertiesId,
                            propertyName,
                            itemPropertiesCommand.getPropertyValue(),
                            JsonUtil.format(colorswatchMap));
            return null;
        }

        //****************************************************************************************************

        ShopdogPropertyValueViewCommand propertyValueViewCommand = new ShopdogPropertyValueViewCommand();

        propertyValueViewCommand.setItemPropertiesId(itemPropertiesId);
        propertyValueViewCommand.setPropertyValue(itemPropertiesCommand.getPropertyValue());
        //到色的话，可以有颜色属性，这个销售属性只有一个值，此时要存放图片的
        //ShopdogPropertyValueViewCommand将imgurl这个属性去掉了，是暂时用不到? 代码预留，后期处理时扩展
        //propertyValueViewCommand.setImageUrl(colorswatch == null ? "" : colorswatch.getUrl());
        //propertyValueViewCommand.setPropertyDisplayValue(colorswatch == null ? "" : colorswatch.getDescription());
        return propertyValueViewCommand;
	}

}
