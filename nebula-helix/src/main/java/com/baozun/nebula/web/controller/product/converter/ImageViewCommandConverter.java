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

import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;

/**
 * 商品图片视图模型转换
 * @author xingyu.liu
 *
 */
public class ImageViewCommandConverter extends BaseConverter<ImageViewCommand> {

	private static final long serialVersionUID = -5780712115262994892L;

	public ImageViewCommand convert(Object data) {
		if(null == data){
			return null;
		}
		if(data instanceof ItemImage){
			ImageViewCommand  imageViewCommand= new ImageViewCommand();
			try{
				ItemImage itemImage = (ItemImage) data;
				imageViewCommand.setDescription(itemImage.getDescription());
				imageViewCommand.setUrl(itemImage.getPicUrl());
				return imageViewCommand;
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			throw new UnsupportDataTypeException(data.getClass()
					+ " cannot convert to " + ImageViewCommandConverter.class + "yet.");
		}
		return null;
	}
}
