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
package com.baozun.nebula.web.controller.member.converter;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.member.viewcommand.MemberAddressViewCommand;

import loxia.dao.Page;
import loxia.dao.Sort;

public class MemberAddressViewCommandConverter extends BaseConverter<MemberAddressViewCommand> {

	/**
	 * 地址簿默认行数
	 */
	public static final int MEMBERADDRESSDEFAULTSIZE = 5;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7415881959809156733L;

	public MemberAddressViewCommand convert(Object data) {
		if (data == null)
			return null;
		ContactCommand contactCommand = (ContactCommand) data;
		// TODO
		// 完成转换
		return null;
	}

	public Page convertPage(Object data) {
		if (data == null)
			return null;
		Page page = (Page) data;
		// TODO
		// 完成转换
		return null;
	}

	public Sort[] convertSort(Object data) {
		if (data == null)
			return null;
		// TODO
		// 完成转换
		return null;
	}

}
