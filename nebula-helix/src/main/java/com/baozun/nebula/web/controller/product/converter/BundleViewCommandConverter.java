/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.BundleViewCommand;

/**
 * 捆绑类商品视图模型转换器
 * @author yue.ch
 *
 */
public class BundleViewCommandConverter extends BaseConverter<BundleViewCommand> {

	private static final long serialVersionUID = -2308896727474580410L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baozun.nebula.web.controller.BaseConverter#convert(java.lang.Object)
	 */
	@Override
	public BundleViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		
		if (data instanceof BundleCommand) {
			BundleCommand bundleCommand = (BundleCommand) data;
			return (BundleViewCommand)ConvertUtils.convertTwoObject(new BundleViewCommand(), bundleCommand);
		} else {
			throw new UnsupportDataTypeException(
					data.getClass() + " cannot convert to " + BundleViewCommand.class + "yet.");
		}
	}

}
