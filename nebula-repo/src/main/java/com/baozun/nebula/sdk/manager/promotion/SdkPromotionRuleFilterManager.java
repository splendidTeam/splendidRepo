package com.baozun.nebula.sdk.manager.promotion;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.manager.BaseManager;

public interface SdkPromotionRuleFilterManager extends BaseManager{

    /**
     * 返回所有促销规则PromotionCommand
     * 
     * @param shopIds
     * @param memComboIds
     * @param productComboIds
     * @param currentTime
     * @return
     */
    public List<PromotionCommand> getIntersectActivityRuleData(
                    List<Long> shopIds,
                    Set<String> crowdComboIds,
                    Set<String> itemComboIds,
                    Date currentTime);

    /**
     * 获取促销活动
     * 
     * @param promotionId
     * @return
     */
    public PromotionCommand getPromotionByPromotionId(Long promotionId);

}
