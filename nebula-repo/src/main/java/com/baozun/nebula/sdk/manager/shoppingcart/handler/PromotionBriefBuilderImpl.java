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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionRuleFilterManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("promotionBriefBuilder")
public class PromotionBriefBuilderImpl implements PromotionBriefBuilder{

    /** The sdk promotion calculation manager. */
    @Autowired
    private SdkPromotionCalculationManager sdkPromotionCalculationManager;

    /** The sdk promotion rule filter manager. */
    @Autowired
    private SdkPromotionRuleFilterManager  sdkPromotionRuleFilterManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.handler.PromotionBriefBuilder#getPromotionBriefList(com.baozun.nebula.sdk.command.
     * shoppingcart.ShoppingCartCommand)
     */
    @Override
    public List<PromotionBrief> getPromotionBriefList(ShoppingCartCommand shoppingCartCommand){
        List<PromotionCommand> promotionCommandList = getPromotionCommandList(shoppingCartCommand);

        if (Validator.isNotNullOrEmpty(promotionCommandList)){
            // 通过购物车和促销集合计算商品促销
            return sdkPromotionCalculationManager.calculationPromotion(shoppingCartCommand, promotionCommandList);
        }
        return Collections.emptyList();
    }

    /**
     * @param shoppingCartCommand
     * @return
     */
    private List<PromotionCommand> getPromotionCommandList(ShoppingCartCommand shoppingCartCommand){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingCartCommand.getShoppingCartLineCommands();
        Set<String> memboSet = shoppingCartCommand.getUserDetails().getMemComboList();

        // 获取人群和商品促销的交集
        Set<Long> shopIdSet = CollectionsUtil.getPropertyValueSet(shoppingCartLineCommandList, "shopId");
        Set<String> itemComboIdsSet = ShoppingCartUtil.getItemComboIds(shoppingCartLineCommandList);
        return sdkPromotionRuleFilterManager.getIntersectActivityRuleData(
                        new ArrayList<Long>(shopIdSet),
                        memboSet,
                        itemComboIdsSet,
                        shoppingCartCommand.getCurrentTime());
    }

    @Override
    public List<PromotionCommand> getCoarsePromotionBriefList(ShoppingCartCommand shoppingCartCommand){       
        return getPromotionCommandList(shoppingCartCommand);
    }

}
