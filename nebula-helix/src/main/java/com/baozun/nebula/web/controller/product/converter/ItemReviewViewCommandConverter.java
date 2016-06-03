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

import java.util.List;

import loxia.utils.PropListCopyable;
import loxia.utils.PropertyUtil;

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.ItemReviewViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ReviewMemberViewCommand;

/**
 * 商品评论/评分 viewcommand转换器
 * @author chengchao
 *
 */
public class ItemReviewViewCommandConverter extends
	PdpExtendConverter<ItemReviewViewCommand> {

	private static final long serialVersionUID = 6312342339931052079L;

	@Override
	public ItemReviewViewCommand convert(Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemReviewViewCommand convertFromTwoObjects(Object obj1, Object obj2) {
		if(null == obj1||null == obj2){
			return null;
		}
		if(obj1 instanceof RateCommand && obj2 instanceof List){
			ItemReviewViewCommand itemReviewViewCommand = new ItemReviewViewCommand();
			try{
				RateCommand rateCommand = (RateCommand) obj1;
				PropertyUtil.copyProperties(rateCommand, itemReviewViewCommand,
						new PropListCopyable("score", "content", "reply",
								"replyTime"));
				itemReviewViewCommand.setReviewTime(rateCommand.getCreateTime());
				
				List members = (List)obj2;
				
				for(Object member : members){
					if(member instanceof ReviewMemberViewCommand){
						if(((ReviewMemberViewCommand)member).getId()==rateCommand.getMemberId()){
							itemReviewViewCommand.setMember((ReviewMemberViewCommand)member);
							break;
						}
					}else{
						throw new UnsupportDataTypeException(member.getClass()
								+ " cannot convert to " + ItemReviewViewCommand.class + "yet.");
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return itemReviewViewCommand;
		}else{
			if(obj1 instanceof RateCommand){
				throw new UnsupportDataTypeException(obj2.getClass()
						+ " cannot convert to " + List.class + "yet.");
			}else{
				throw new UnsupportDataTypeException(obj1.getClass()
						+ " cannot convert to " + ItemReviewViewCommand.class + "yet.");
			}
		}
	}


}
