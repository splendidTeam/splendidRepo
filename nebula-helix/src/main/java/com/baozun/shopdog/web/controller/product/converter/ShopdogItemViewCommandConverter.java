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

import org.springframework.beans.BeanUtils;

import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.shopdog.web.controller.ShopdogBaseConverter;
import com.baozun.shopdog.web.controller.product.viewcommand.ShopdogItemViewCommand;

/**
 * 驻店宝PDP-item视图模型转换
 * @author xingyu.liu
 *
 */
public class ShopdogItemViewCommandConverter extends ShopdogBaseConverter<ShopdogItemViewCommand> {

	private static final long serialVersionUID = -1961008406160845011L;

	public ShopdogItemViewCommand convert(Object data) {
		if(null == data){
			return null;
		}
		if(data instanceof ItemBaseInfoViewCommand){
			ShopdogItemViewCommand  shopdogItemViewCommand= new ShopdogItemViewCommand();
			try{
				ItemBaseInfoViewCommand itemBaseInfo = (ItemBaseInfoViewCommand) data;
				BeanUtils.copyProperties(itemBaseInfo, shopdogItemViewCommand);
				return shopdogItemViewCommand;
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + ShopdogItemViewCommandConverter.class + "yet.");
		}
		return null;
	}
}
