package com.baozun.nebula.sdk.manager;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.manager.BaseManager;

public interface SdkPurchaseLimitRuleFilterManager extends BaseManager{

    /**
     * 返回所有限购人群规则的PurchaseLimitation
     * 
     * @param shopIds
     * @param crowdComboIds
     * @param currentTime
     * @return
     */
    List<LimitCommand> getPurchaseLimitCrowdPromotionData(List<Long> shopIds,Set<String> crowdComboIds);

    /**
     * 返回所有限购商品范围PurchaseLimitation
     * 
     * @param shopIds
     * @param productComboIds
     * @param currentTime
     * @return
     */
    List<LimitCommand> getPurchaseLimitItemScopePromotionData(List<Long> shopIds,Set<String> itemComboIds);

    /**
     * 返回所有限购规则PurchaseLimitation
     * 
     * @param shopIds
     * @param memComboIds
     * @param productComboIds
     * @param currentTime
     * @return
     */
    List<LimitCommand> getIntersectPurchaseLimitRuleData(
                    List<Long> shopIds,
                    Set<String> crowdComboIds,
                    Set<String> itemComboIds,
                    Date currentTime);
}
