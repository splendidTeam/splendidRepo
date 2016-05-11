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
package com.baozun.shopdog.web.controller.product.converter;

import loxia.utils.PropListCopyable;
import loxia.utils.PropertyUtil;

import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.shopdog.web.controller.ShopdogBaseConverter;
import com.baozun.shopdog.web.controller.product.viewcommand.ShopdogSkuViewCommand;

/** 
 * shopdog  sku信息的视图模型转换器
 * @author xingyu.liu
 *
 */
public class ShopdogSkuViewCommandConverter extends
		ShopdogBaseConverter<ShopdogSkuViewCommand> {
	
	private static final long serialVersionUID = 2122116108014878879L;

	public ShopdogSkuViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		if (data instanceof SkuCommand) {
			try {
				SkuCommand command = (SkuCommand) data;
				
				ShopdogSkuViewCommand viewCommand =new ShopdogSkuViewCommand();
				// 完成转换
				PropertyUtil.copyProperties(command, viewCommand,
						new PropListCopyable("extentionCode", "properties", "salePrice","listPrice","availableQty"));
				viewCommand.setSkuId(command.getId());
				viewCommand.setLifecycle(Integer.valueOf(command.getState()));
				
				return viewCommand;
			} catch (Exception e) {
				// TODO should not occur
				e.printStackTrace();
			}
		} else {
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + ShopdogSkuViewCommand.class + "yet.");
		}

		return null;
	}

}
