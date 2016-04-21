/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product.resolver;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;

/**   
 * 
 * 构造销售属性和一般属性
 * <p>
 * 		1.到款：颜色作为一个多选的属性，从数据库里取，图片从图片管理里取
 * </p>
 * <p>
 * 		2.到色：颜色作为一个单选的属性，图片也从图片管理里取，但额外补充同款其他商品(itemColorSwatches)
 * </p>
 * 
 * @Description 
 * @author dongliang ma
 * @date 2016年4月20日 上午11:37:08 
 * @version   
 */
public interface ItemPropertyViewCommandResolver {
	/**
	 * 根据baseInfoViewCommand(至少包含itemId)、images(图片信息)构造ViewCommand
	 * @param baseInfoViewCommand
	 * @param images
	 * @return
	 */
	ItemPropertyViewCommand resolve(ItemBaseInfoViewCommand baseInfoViewCommand, Map<String, List<ImageViewCommand>> images);
}
