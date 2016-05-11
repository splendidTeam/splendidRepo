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

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.RelationItemViewCommand;
import com.feilong.core.Validator;

/**
 * 商品图片视图模型转换
 * @author xingyu.liu
 *
 */
public class RelationItemViewCommandConverter extends BaseConverter<RelationItemViewCommand> {

	private static final long serialVersionUID = -852623715018569353L;

	public RelationItemViewCommand convert(Object data) {
		if(null == data){
			return null;
		}
		if(data instanceof ItemCommand){
			RelationItemViewCommand  relationItemViewCommand = new RelationItemViewCommand();
			try{
				ItemCommand itemCommand = (ItemCommand) data;
				relationItemViewCommand.setItemId(itemCommand.getId());
				relationItemViewCommand.setItemCode(itemCommand.getCode());
				relationItemViewCommand.setItemName(itemCommand.getTitle());
				relationItemViewCommand.setListPrice(itemCommand.getListPrice());
				relationItemViewCommand.setSalePrice(itemCommand.getSalePrice());
				//默认放一张图片
				if(Validator.isNotNullOrEmpty(itemCommand.getPicUrl())){
					List<String> imageUrls = new ArrayList<String>();
					imageUrls.add(itemCommand.getPicUrl());
					relationItemViewCommand.setImageUrl(imageUrls);
				}
				//暂无
				relationItemViewCommand.setColorSwatch(null);
				return relationItemViewCommand;
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + RelationItemViewCommandConverter.class + "yet.");
		}
		return null;
	}
}
