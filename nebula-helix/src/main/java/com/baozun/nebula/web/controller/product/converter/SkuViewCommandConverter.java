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

import loxia.utils.PropListCopyable;
import loxia.utils.PropertyUtil;

import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.BreadcrumbsViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.SkuViewCommand;

/** 
 * 
 *   面包屑信息的视图模型转换器
* @Description 
* @author dongliang ma
* @date 2016年4月19日 下午4:45:34 
* @version   
*/
public class SkuViewCommandConverter extends
		BaseConverter<SkuViewCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2122116108014878879L;

	public SkuViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		if (data instanceof SkuCommand) {
			try {
				SkuCommand command = (SkuCommand) data;
				
				SkuViewCommand viewCommand =new SkuViewCommand();
				// 完成转换
				PropertyUtil.copyProperties(command, viewCommand,
						new PropListCopyable("extentionCode", "properties", "salePrice","listPrice"));
				viewCommand.setSkuId(command.getId());
				viewCommand.setLifecycle(Integer.valueOf(command.getState()));
				
				return viewCommand;
			} catch (Exception e) {
				// TODO should not occur
				e.printStackTrace();
			}
		} else {
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + BreadcrumbsViewCommand.class + "yet.");
		}

		return null;
	}

}
