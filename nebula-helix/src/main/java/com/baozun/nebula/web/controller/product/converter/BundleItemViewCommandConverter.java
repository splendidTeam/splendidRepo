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
import com.baozun.nebula.command.product.BundleItemCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.BundleItemViewCommand;

/**
 * 捆绑类商品成员商品视图模型转换器
 * @author yue.ch
 * @time 2016年4月21日 下午2:20:40
 */
public class BundleItemViewCommandConverter extends BaseConverter<BundleItemViewCommand>{

	private static final long serialVersionUID = 2469968187118747476L;

	/* (non-Javadoc)
	 * @see com.baozun.nebula.web.controller.BaseConverter#convert(java.lang.Object)
	 */
	@Override
	public BundleItemViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		
		if (data instanceof BundleItemCommand) {
			BundleItemCommand bundleItemCommand = (BundleItemCommand) data;
			return (BundleItemViewCommand)ConvertUtils.convertTwoObject(new BundleItemViewCommand(), bundleItemCommand);
		} else {
			throw new UnsupportDataTypeException(
					data.getClass() + " cannot convert to " + BundleItemViewCommand.class + "yet.");
		}
	}

}
