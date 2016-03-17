package com.baozun.nebula.sdk.manager;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.baozun.nebula.command.product.CrowdForCheckCommand;
import com.baozun.nebula.command.product.ItemForCheckCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.manager.BaseManager;

public interface SdkPromotionRuleFilterManager extends BaseManager{
	/**
	 * 返回所有促销规则PromotionCommand
	 * @param shopIds
	 * @param memComboIds
	 * @param productComboIds
	 * @param currentTime
	 * @return
	 */
	public List<PromotionCommand> getIntersectActivityRuleData(
			List<Long> shopIds,Set<String> crowdComboIds,
			Set<String> itemComboIds, Date currentTime);

	/**
	 * 返回所有人群规则的Promotion
	 * @param shopIds
	 * @param memComboIds
	 * @param currentTime
	 * @return
	 */
	public List<PromotionCommand> getActiveCrowdPromotionData(
			List<Long> shopIds,Set<String> crowdComboIds, Date currentTime);

	/**
	 * 返回所有商品范围PromotionCommand
	 * @param shopIds
	 * @param productComboIds
	 * @param currentTime
	 * @return
	 */
	public List<PromotionCommand> getActiveItemScopePromotionData(
			List<Long> shopIds,Set<String> itemComboIds, Date currentTime);
	/**
	 * 获取促销活动
	 * @param promotionId
	 * @return
	 */
	public PromotionCommand getPromotionByPromotionId(Long promotionId);
	
}
