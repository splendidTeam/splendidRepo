package com.baozun.nebula.sdk.manager.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.sdk.manager.SdkPriorityAdjustManager;

@Service("sdkPromotionRuleFilterManager")
@Transactional
public class SdkPromotionRuleFilterManagerImpl implements SdkPromotionRuleFilterManager{

    static final Logger              log = LoggerFactory.getLogger(SdkPromotionRuleFilterManagerImpl.class);

    @Autowired
    private SdkPriorityAdjustManager sdkPriorityAdjustManager;

    private List<PromotionCommand>   promos;

    public SdkPromotionRuleFilterManagerImpl(){
        super();
    }

    /**
     * 获取人群和商品的促销活动的交集
     * 
     * @param shopIds
     * @param crowdComIds
     * @param itemComboIds
     */
    @Override
    public List<PromotionCommand> getIntersectActivityRuleData(
                    List<Long> shopIds,
                    Set<String> crowdComboIds,
                    Set<String> itemComboIds,
                    Date currentTime){
        promos = EngineManager.getInstance().getPromotionCommandList();
        if (null == promos || promos.size() == 0){
            return null;
        }
        // 根据当前时间过滤促销规则
        promos = filterPromotionRule(promos, currentTime);
        // 调整促销优先级
        // promos = adjustPriority(promos, shopIds, currentTime);
        // 获取人群过滤后的促销规则
        List<PromotionCommand> memberProms = getActiveCrowdPromotionData(shopIds, crowdComboIds, currentTime);
        // 获取商品过滤后的促销规则
        List<PromotionCommand> productProms = getActiveItemScopePromotionData(shopIds, itemComboIds, currentTime);
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
     * 获取人群的促销活动
     * 
     * @param shopIds
     * @param crowdForCheckCommand
     * @param currentTime
     */
    @Override
    public List<PromotionCommand> getActiveCrowdPromotionData(List<Long> shopIds,Set<String> crowdComboIds,Date currentTime){
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
     * 获取商品的促销活动
     * 
     * @param shopIds
     * @param itemForCheckCommands
     * @param currentTime
     */
    @Override
    public List<PromotionCommand> getActiveItemScopePromotionData(List<Long> shopIds,Set<String> itemComboIds,Date currentTime){
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
     * 根据当前时间过滤促销规则
     * 
     * @param promotionList
     * @return
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

    /**
     * 
     * @param promotionId
     * @return
     */
    @Override
    public PromotionCommand getPromotionByPromotionId(Long promotionId){
        List<PromotionCommand> list = EngineManager.getInstance().getPromotionCommandList();
        if (list == null)
            return null;
        for (PromotionCommand one : list){
            if (one.getPromotionId().equals(promotionId)){
                return one;
            }
        }
        return null;
    }
}
