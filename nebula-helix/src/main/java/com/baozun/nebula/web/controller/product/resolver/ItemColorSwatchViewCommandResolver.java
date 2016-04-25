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
import com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand;

/**
 * 颜色（或者其他属性，颜色是个统称）或商品切换部分
 * <p>
 * 		[条件]当pdp展示模式取:
 * </p>
 * <p>
 * 		商品到色，PDP根据款号聚合（到款显示）,即模式二@see (NebulaAbstractPdpController.PDP_MODE_COLOR_COMBINE)
 * </p>
 * <p>
 * 		设置pdp的view切换属性
 * </p>
 *    
 * @Description 
 * @author dongliang ma
 * @date 2016年4月22日 下午3:59:09 
 * @version   
 */
public interface ItemColorSwatchViewCommandResolver {
	/**
	 * 
	 * @param baseInfoViewCommand (应至少包含itemId、itemCode)
	 * @param itemPropertyViewCommand
	 * @return
	 */
	List<ItemColorSwatchViewCommand> resolve(ItemBaseInfoViewCommand baseInfoViewCommand
			);
}
