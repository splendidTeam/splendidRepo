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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogPropertyViewCommand;
import com.feilong.core.Validator;

/** 
 *ShopDog的属性转换器</br>
 *根据nebula的基础支持实现的属性view转换为ShopDog的属性view结构</br>
 *包括:</br>
 *销售属性和一般属性
 * @Description 
 * @author dongliang ma
 * @date 2016年4月19日 下午4:45:34 
 * @version   
 */
public class ShopdogItemPropertyCommandConverter extends
		BaseConverter<ShopdogItemPropertyViewCommand> {
	
	@Autowired
	@Qualifier("shopdogPropertyViewCommandConverter")
	private ShopdogPropertyViewCommandConverter shopdogPropertyViewCommandConverter;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2122116108014878879L;

	public ShopdogItemPropertyViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		if (data instanceof ItemPropertyViewCommand) {
			try {
				ShopdogItemPropertyViewCommand shopdogItemPropertyViewCommand =new ShopdogItemPropertyViewCommand();
				ItemPropertyViewCommand itemPropertyViewCommand =(ItemPropertyViewCommand)data;
				//销售属性
				if(Validator.isNotNullOrEmpty(itemPropertyViewCommand.getSalesProperties())){
					shopdogItemPropertyViewCommand.setSalesProperties(shopdogPropertyViewCommandConverter
							.convert(itemPropertyViewCommand.getSalesProperties()));
				}
				//一般属性
				if(Validator.isNotNullOrEmpty(itemPropertyViewCommand.getNonSalesProperties())){
					Map<String, List<ShopdogPropertyViewCommand>> nonSalesProperties 
											=new HashMap<String, List<ShopdogPropertyViewCommand>>();
					for (Map.Entry<String, List<PropertyElementViewCommand>> entry
							: itemPropertyViewCommand.getNonSalesProperties().entrySet()) {
						nonSalesProperties.put(entry.getKey(), shopdogPropertyViewCommandConverter
							.convert(entry.getValue()));
					}
					shopdogItemPropertyViewCommand.setNonSalesProperties(nonSalesProperties);
				}
				return shopdogItemPropertyViewCommand;
			} catch (Exception e) {
				// TODO should not occur
				e.printStackTrace();
			}
		} else {
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + ShopdogItemPropertyViewCommand.class + "yet.");
		}

		return null;
	}

}
