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
package com.baozun.nebula.sdk.manager.shoppingcart.handler;

import java.util.List;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public interface PromotionBriefBuilder{

    /**
     * 用于计算获取促销数据
     * 
     * @param shoppingCartCommand
     * @return
     */
    List<PromotionBrief> getPromotionBriefList(ShoppingCartCommand shoppingCartCommand);
    
    /**
     * 根据购物车获取粗过滤的活动
     * <br>采用人群和商品过滤
     *	@param shoppingCartCommand
     *	@return 活动id
     *	<p>binrui.dong
     *  <p>2017年4月19日 下午3:29:19
     */
    List<PromotionCommand> getCoarsePromotionBriefList(ShoppingCartCommand shoppingCartCommand);
    
}
