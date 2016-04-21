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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyValueViewCommand;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年4月21日 下午2:20:44 
 * @version   
 */
public class PropertyValueViewCommandTransformer implements Transformer<ItemPropertiesCommand, PropertyValueViewCommand>{
	
	/** The Constant log. */
    private static final Logger                LOGGER = LoggerFactory.getLogger(PropertyValueViewCommandTransformer.class);

    /** The item code. */
    private final String                       itemCode;
    
    /** The property. */
    private final Property                     property;
    
    /** The item properties id and colorswatch image map. */
    private final Map<Long, ImageViewCommand>           colorswatchMap;

	/**
	 * 
	 * @param itemCode
	 * @param property
	 * @param colorswatchMap
	 */
	public PropertyValueViewCommandTransformer(String itemCode,
			Property property, Map<Long, ImageViewCommand> colorswatchMap) {
		super();
		this.itemCode = itemCode;
		this.property = property;
		this.colorswatchMap = colorswatchMap;
	}


	/* 
	 * @see org.apache.commons.collections4.Transformer#transform(java.lang.Object)
	 */
	@Override
	public PropertyValueViewCommand transform(ItemPropertiesCommand itemPropertiesCommand) {
		String propertyName = property.getName();
        itemPropertiesCommand.setPropertyName(propertyName);
        itemPropertiesCommand.setItemCode(itemCode);

        Long itemPropertiesId = itemPropertiesCommand.getItem_properties_id();
        Boolean isColorProp = property.getIsColorProp();
        ImageViewCommand colorswatch = null == colorswatchMap ? null
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

        PropertyValueViewCommand propertyValueViewCommand = new PropertyValueViewCommand();

        propertyValueViewCommand.setItemPropertiesId(itemPropertiesId);
        propertyValueViewCommand.setPropertyId(itemPropertiesCommand.getPropertyId());
        propertyValueViewCommand.setPropertyName(propertyName);
        //TODO
        propertyValueViewCommand.setPropertyValue(itemPropertiesCommand.getPropertyValue());
        propertyValueViewCommand.setImageUrl(colorswatch == null ? "" : colorswatch.getUrl());
        //set displayValue
        //propertyValueViewCommand.setPropertyDisplayValue(colorswatch == null ? "" : colorswatch.getDescription());
        return propertyValueViewCommand;
	}

}
