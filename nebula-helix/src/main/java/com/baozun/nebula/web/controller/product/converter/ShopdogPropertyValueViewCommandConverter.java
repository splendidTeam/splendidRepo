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
package com.baozun.nebula.web.controller.product.converter;

import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyValueViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogPropertyValueViewCommand;

/**   
 * shopdog的属性值转换器</br>
 * 将nebula的基础支持实现的属性值view转换为ShopDog的属性值view结构 
 * @Description 
 * @author dongliang ma
 * @date 2016年5月5日 下午2:35:37 
 * @version   
 */
public class ShopdogPropertyValueViewCommandConverter extends
		BaseConverter<ShopdogPropertyValueViewCommand>{

	/** */
	private static final long serialVersionUID = -6440288042908793178L;

	/* 
	 * @see com.baozun.nebula.web.controller.BaseConverter#convert(java.lang.Object)
	 */
	@Override
	public ShopdogPropertyValueViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		if (data instanceof PropertyValueViewCommand) {
			try {
				ShopdogPropertyValueViewCommand shopdogPropertyValueViewCommand =new ShopdogPropertyValueViewCommand();
				PropertyValueViewCommand propertyValueViewCommand =(PropertyValueViewCommand)data;
				shopdogPropertyValueViewCommand.setItemPropertiesId(propertyValueViewCommand.getItemPropertiesId());
				shopdogPropertyValueViewCommand.setPropertyValue(propertyValueViewCommand.getPropertyValue());
				return shopdogPropertyValueViewCommand;
			} catch (Exception e) {
				// TODO should not occur
				e.printStackTrace();
			}
		} else {
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + ShopdogPropertyValueViewCommand.class + "yet.");
		}
		return null;
	}
	
}
