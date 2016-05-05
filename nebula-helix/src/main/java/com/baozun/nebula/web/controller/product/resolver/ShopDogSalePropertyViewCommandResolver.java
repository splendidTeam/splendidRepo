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

import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogPropertyViewCommand;

/**
 * 构造有序的销售属性   
 * @Description 
 * @author dongliang ma
 * @date 2016年5月4日 下午3:17:03 
 * @version   
 */
public interface ShopDogSalePropertyViewCommandResolver {
	
	/**
	 * 构造有序的销售属性
	 * @param baseInfoViewCommand
	 * @param picUrls
	 * @return
	 */
	List<ShopdogPropertyViewCommand> resolve(ItemBaseInfoViewCommand baseInfoViewCommand,
			List<ShopdogItemImageViewCommand> picUrls);
	
}
