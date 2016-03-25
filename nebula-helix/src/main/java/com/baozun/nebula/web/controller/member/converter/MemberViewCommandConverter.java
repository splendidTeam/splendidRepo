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

import loxia.utils.PropListCopyable;
import loxia.utils.PropertyUtil;

import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.member.viewcommand.MemberViewCommand;

/**
 * 会员Profile信息的视图模型转换器
 * 
 * @author Benjamin.Liu
 * 
 */
public class MemberViewCommandConverter extends
		BaseConverter<MemberViewCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2122116108014878879L;

	public MemberViewCommand convert(Object data) {
		if (data == null) {
			return null;
		}
		if (data instanceof MemberCommand) {
			MemberCommand memberCommand = (MemberCommand) data;
			// TODO
			// 完成转换
			MemberViewCommand memberViewCommand = new MemberViewCommand();
			try {
				// 实现将MemberCommand对象数据全部转入MemberViewCommand中
				PropertyUtil.copyProperties(memberCommand, memberViewCommand,
						new PropListCopyable("id", "loginName", "loginEmail",
								"loginMobile", "password", "oldPassword",
								"thirdPartyIdentify", "source", "type",
								"isaddgroup", "sex", "birthday", "repassword",
								"lifecycle", "realName", "receiveMail",
								"newPassword"));
				return memberViewCommand;
			} catch (Exception e) {
				// TODO should not occur
				e.printStackTrace();
			}
		} else {
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + MemberViewCommand.class + "yet.");
		}

		return null;
	}

}
