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

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.ItemReviewViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ReviewMemberViewCommand;

/**
 * 商品评论-评论用户viewcommand
 * @author chengchao
 *
 */
public class ReviewMemberViewCommandConverter extends
	PdpExtendConverter<ReviewMemberViewCommand> {

	private static final long serialVersionUID = 6312342339931052079L;

	@Override
	public ReviewMemberViewCommand convert(Object data) {
		if(null == data){
			return null;
		}
		if(data instanceof MemberCommand){
			ReviewMemberViewCommand reviewMemberViewCommand = new ReviewMemberViewCommand();
			try{
				MemberCommand memberCommand = (MemberCommand) data;
				PropertyUtil.copyProperties(memberCommand, reviewMemberViewCommand,
						new PropListCopyable("id","realName"));
				
				reviewMemberViewCommand.setEmail(memberCommand.getLoginEmail());
				reviewMemberViewCommand.setMobile(memberCommand.getLoginMobile());
				reviewMemberViewCommand.setName(memberCommand.getLoginName());
				
				return reviewMemberViewCommand;
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + ReviewMemberViewCommandConverter.class + "yet.");
		}
		return null;
	}

	@Override
	public ReviewMemberViewCommand convertFromTwoObjects(Object obj1,
			Object obj2) {
		// TODO Auto-generated method stub
		return null;
	}


}
