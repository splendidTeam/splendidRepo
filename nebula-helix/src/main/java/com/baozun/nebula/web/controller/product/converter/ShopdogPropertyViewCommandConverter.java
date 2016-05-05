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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogPropertyViewCommand;
import com.feilong.core.Validator;

/**  
 * shopdog的属性转换器</br>
 * 将nebula的基础支持实现的属性view转换为ShopDog的属性view结构 
 * @Description 
 * @author dongliang ma
 * @date 2016年5月5日 下午2:35:37 
 * @version   
 */
public class ShopdogPropertyViewCommandConverter extends
		BaseConverter<ShopdogPropertyViewCommand>{

	/** */
	private static final long serialVersionUID = -7566606647379421667L;
	
	@Autowired
	@Qualifier("shopdogPropertyValueViewCommandConverter")
	private ShopdogPropertyValueViewCommandConverter	shopdogPropertyValueViewCommandConverter;
	
	/* 
	 * @see com.baozun.nebula.web.controller.BaseConverter#convert(java.lang.Object)
	 */
	@Override
	public ShopdogPropertyViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		if (data instanceof PropertyElementViewCommand) {
			try {
				ShopdogPropertyViewCommand shopdogPropertyViewCommand =new ShopdogPropertyViewCommand();
				PropertyElementViewCommand propertyElementViewCommand =(PropertyElementViewCommand)data;
				if(Validator.isNotNullOrEmpty(propertyElementViewCommand.getProperty())){
					shopdogPropertyViewCommand.setName(propertyElementViewCommand.getProperty().getName());
					shopdogPropertyViewCommand.setIsColorProp(propertyElementViewCommand.getProperty().getIsColorProp());
					if(Validator.isNotNullOrEmpty(propertyElementViewCommand.getPropertyValues())){
						shopdogPropertyViewCommand.setPropertyValues(shopdogPropertyValueViewCommandConverter
								.convert(propertyElementViewCommand.getPropertyValues()));
					}
				}
				return shopdogPropertyViewCommand;
			} catch (Exception e) {
				// TODO should not occur
				e.printStackTrace();
			}
		} else {
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + ShopdogPropertyViewCommand.class + "yet.");
		}
		return null;
	}

}
