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

import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogItemViewCommand;

/** 
 *ShopDog的属性转换器</br>
 *根据nebula的基础支持实现的属性view转换为ShopDog的属性view结构</br>
 * @Description 
 * @author dongliang ma
 * @date 2016年4月19日 下午4:45:34 
 * @version   
 */
public class ShopDogItemPropertyCommandConverter extends
		BaseConverter<ShopdogItemViewCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2122116108014878879L;

	public ShopdogItemViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		if (data instanceof ItemPropertyViewCommand) {
			try {
				
				return new ShopdogItemViewCommand();
			} catch (Exception e) {
				// TODO should not occur
				e.printStackTrace();
			}
		} else {
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + ItemPropertyViewCommand.class + "yet.");
		}

		return null;
	}

}
