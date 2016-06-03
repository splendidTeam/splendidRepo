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
package com.baozun.nebula.web.controller.breadcrumb.converter;

import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.breadcrumb.viewcommand.BreadcrumbsViewCommand;

/** 
 * 
 *   面包屑信息的视图模型转换器
 * @Description 
 * @author dongliang ma
 * @date 2016年5月18日 下午3:14:40 
 * @version   
 */
public class BreadcrumbsViewCommandConverter extends
		BaseConverter<BreadcrumbsViewCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2122116108014878879L;

	public BreadcrumbsViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		if (data instanceof CurmbCommand) {
			try {
				CurmbCommand curmbCommand = (CurmbCommand) data;
				// 完成转换
				BreadcrumbsViewCommand breadcrumbsViewCommand 
						= new BreadcrumbsViewCommand(curmbCommand.getName(),curmbCommand.getUrl());
				return breadcrumbsViewCommand;
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
