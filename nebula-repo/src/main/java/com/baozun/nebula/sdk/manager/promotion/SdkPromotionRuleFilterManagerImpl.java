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
package com.baozun.nebula.sdk.manager.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.feilong.core.util.CollectionsUtil;

/**
 * The Class SdkPromotionRuleFilterManagerImpl.
 */
@Service("sdkPromotionRuleFilterManager")
@Transactional
public class SdkPromotionRuleFilterManagerImpl implements SdkPromotionRuleFilterManager{

    /**
     * 获得 promotion by promotion id.
     *
     * @param promotionId
     *            the promotion id
     * @return the promotion by promotion id
     */
    @Override
    public PromotionCommand getPromotionByPromotionId(Long promotionId){
        List<PromotionCommand> promotionCommandList = EngineManager.getInstance().getPromotionCommandList();
        //从promotionCommandList 查找 属性名字是promotionId, 属性值是 参数promotionId值的对象
        return CollectionsUtil.find(promotionCommandList, "promotionId", promotionId);
    }

    /**
     * 获取人群和商品的促销活动的交集.
     *
     * @param shopIds
     *            the shop ids
     * @param crowdComboIds
     *            the crowd combo ids
     * @param itemComboIds
     *            the item combo ids
     * @param currentTime
     *            the current time
     * @return the intersect activity rule data
     */
    @Override
    public List<PromotionCommand> getIntersectActivityRuleData(
                    List<Long> shopIds,
                    Set<String> crowdComboIds,
                    Set<String> itemComboIds,
                    Date currentTime){
        List<PromotionCommand> promos = EngineManager.getInstance().getPromotionCommandList();
        if (null == promos || promos.size() == 0){
            return null;
        }
        // 根据当前时间过滤促销规则
        promos = filterPromotionRule(promos, currentTime);
        // 调整促销优先级
        // promos = adjustPriority(promos, shopIds, currentTime);
        // 获取人群过滤后的促销规则
        List<PromotionCommand> memberProms = getActiveCrowdPromotionData(promos, shopIds, crowdComboIds);
        // 获取商品过滤后的促销规则
        List<PromotionCommand> productProms = getActiveItemScopePromotionData(promos, shopIds, itemComboIds);
        if (null == memberProms || memberProms.size() == 0 || null == productProms || productProms.size() == 0){
            return null;
        }
        // 取memberProms与productProms两个集合的交集
        memberProms.retainAll(productProms);
        if (null == memberProms || memberProms.size() == 0){
            return null;
        }
        Collections.sort(memberProms, new Comparator<PromotionCommand>(){

            public int compare(PromotionCommand arg0,PromotionCommand arg1){
                return arg0.getDefaultPriority().compareTo(arg1.getDefaultPriority());
            }
        });
        return memberProms;
    }

    /**
     * 获取人群的促销活动.
     *
     * @param shopIds
     *            the shop ids
     * @param crowdComboIds
     *            the crowd combo ids
     * @return the active crowd promotion data
     */
    private List<PromotionCommand> getActiveCrowdPromotionData(List<PromotionCommand> promos,List<Long> shopIds,Set<String> crowdComboIds){
        if (null == promos || promos.size() == 0 || null == crowdComboIds || crowdComboIds.size() == 0){
            return null;
        }
        List<PromotionCommand> promosReturn = new ArrayList<PromotionCommand>();
        // 根据shopId、mem_combo_id筛选出不符合条件的
        List<PromotionCommand> memberProms = new ArrayList<PromotionCommand>();
        // 获取combo下的促销活动
        for (String comboId : crowdComboIds){
            for (PromotionCommand promo : promos){
                if (String.valueOf(promo.getMemComboId()).equals(comboId)){
                    memberProms.add(promo);
                }else{
                    continue;
                }
            }
        }
        // 根据店铺删选促销活动
        if (memberProms.size() > 0){
            for (PromotionCommand promo : memberProms){
                for (Long shopId : shopIds)
                    if (shopId.toString().equals(promo.getShopId().toString())){
                        promosReturn.add(promo);
                    }
            }
        }
        return promosReturn;
    }

    /**
     * 获取商品的促销活动.
     *
     * @param shopIds
     *            the shop ids
     * @param itemComboIds
     *            the item combo ids
     * @return the active item scope promotion data
     */
    private List<PromotionCommand> getActiveItemScopePromotionData(
                    List<PromotionCommand> promos,
                    List<Long> shopIds,
                    Set<String> itemComboIds){
        if (null == promos || promos.size() == 0 || null == itemComboIds || itemComboIds.size() == 0){
            return null;
        }
        List<PromotionCommand> promosReturn = new ArrayList<PromotionCommand>();
        // 根据shopId、product_combo_id筛选出不符合条件的
        List<PromotionCommand> productProms = new ArrayList<PromotionCommand>();
        // 获取combo下的促销活动
        for (String comboId : itemComboIds){
            for (PromotionCommand promo : promos){
                if (String.valueOf(promo.getProductComboId()).equals(comboId)){
                    productProms.add(promo);
                }else{
                    continue;
                }
            }
        }
        // 根据店铺删选促销活动
        if (productProms.size() > 0){
            for (PromotionCommand promo : productProms){
                for (Long shopId : shopIds)
                    if (shopId.toString().equals(promo.getShopId().toString())){
                        promosReturn.add(promo);
                    }
            }
        }
        return promosReturn;
    }

    /**
     * 根据当前时间过滤促销规则.
     *
     * @param promotionList
     *            the promotion list
     * @param currentTime
     *            the current time
     * @return the list< promotion command>
     */
    private List<PromotionCommand> filterPromotionRule(List<PromotionCommand> promotionList,Date currentTime){
        long currentMilliseconds = currentTime.getTime();
        List<PromotionCommand> promList = new ArrayList<PromotionCommand>();
        for (PromotionCommand promo : promotionList){
            long startTimeMilliseconds = promo.getStartTime().getTime();
            long endTimeMilliseconds = promo.getEndTime().getTime();
            if (currentMilliseconds >= startTimeMilliseconds && currentMilliseconds <= endTimeMilliseconds){
                promList.add(promo);
            }
        }
        return promList;
    }

}
