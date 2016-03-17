package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitRuleFilterManager;

@Transactional
@Service("sdkPurchaseRuleFilterManager")
public class SdkPurchaseLimitRuleFilterManagerImpl implements SdkPurchaseLimitRuleFilterManager {

	private List<LimitCommand> limits;

	/**
	 * 返回所有限购人群规则的PurchaseLimitation
	 * 
	 * @param shopIds
	 * @param crowdComboIds
	 * @param currentTime
	 * @return
	 */
	@Override
	public List<LimitCommand> getPurchaseLimitCrowdPromotionData(List<Long> shopIds, Set<String> crowdComboIds) {
		List<LimitCommand> purchaseLimitations = limits;
		List<LimitCommand> purchaseLimitationsReturn = new ArrayList<LimitCommand>();
		if (null == purchaseLimitations || purchaseLimitations.size() == 0) {
			return null;
		}
		// 根据shopId、mem_combo_id筛选出不符合条件的
		List<LimitCommand> memberPurchaseLimitations = new ArrayList<LimitCommand>();
		if (null == crowdComboIds || crowdComboIds.size() == 0) {
			return null;
		}

		// 获取combo下的促销活动
		for (String comboId : crowdComboIds) {
			for (LimitCommand purchaseLimitation : purchaseLimitations) {
				if (String.valueOf(purchaseLimitation.getMemberComboId()).equals(comboId)) {
					memberPurchaseLimitations.add(purchaseLimitation);
				} else {
					continue;
				}
			}
		}
		// 根据店铺删选促销活动
		if (memberPurchaseLimitations != null && memberPurchaseLimitations.size() > 0) {
			for (LimitCommand promoLimit : memberPurchaseLimitations) {
				for (Long shopId : shopIds)
					if (String.valueOf(shopId).equals(String.valueOf(promoLimit.getShopId()))) {
						purchaseLimitationsReturn.add(promoLimit);
					}
			}
		}

		return purchaseLimitationsReturn;
	}

	/**
	 * 返回所有限购商品范围PurchaseLimitation
	 * 
	 * @param shopIds
	 * @param productComboIds
	 * @param currentTime
	 * @return
	 */
	@Override
	public List<LimitCommand> getPurchaseLimitItemScopePromotionData(List<Long> shopIds, Set<String> itemComboIds) {
		List<LimitCommand> purchaseLimitations = limits;
		List<LimitCommand> purchaseLimitationsReturn = new ArrayList<LimitCommand>();
		if (null == purchaseLimitations || purchaseLimitations.size() == 0) {
			return null;
		}

		if (null == itemComboIds || itemComboIds.size() == 0) {
			return null;
		}

		// 根据shopId、product_combo_id筛选出不符合条件的
		List<LimitCommand> productPurchaseLimitations = new ArrayList<LimitCommand>();
		// 获取combo下的促销活动
		for (LimitCommand promoLimit : purchaseLimitations) {
			for (String comboId : itemComboIds) {
				if (String.valueOf(promoLimit.getProductComboId()).equals(comboId)) {
					productPurchaseLimitations.add(promoLimit);
				} else {
					continue;
				}
			}
		}
		// 根据店铺筛选限购活动
		if (productPurchaseLimitations != null && productPurchaseLimitations.size() > 0) {
			for (LimitCommand promoLimit : productPurchaseLimitations) {
				for (Long shopId : shopIds)
					if (shopId.toString().equals(promoLimit.getShopId().toString())) {
						purchaseLimitationsReturn.add(promoLimit);
					}
			}
		}
		return purchaseLimitationsReturn;
	}

	/**
	 * 返回所有限购规则PurchaseLimitation
	 * 
	 * @param shopIds
	 * @param memComboIds
	 * @param productComboIds
	 * @param currentTime
	 * @return
	 */
	@Override
	public List<LimitCommand> getIntersectPurchaseLimitRuleData(List<Long> shopIds, Set<String> crowdComboIds, Set<String> itemComboIds, Date currentTime) {

		limits = EngineManager.getInstance().getLimitCommandList();

		if (null == limits || limits.size() == 0) {
			return null;
		}
		limits = filterLimitRule(limits, currentTime);

		// 获取人群过滤后的限购规则
		List<LimitCommand> memberPromLimits = getPurchaseLimitCrowdPromotionData(shopIds, crowdComboIds);
		// 获取商品过滤后的限购规则
		List<LimitCommand> productPromLimits = getPurchaseLimitItemScopePromotionData(shopIds, itemComboIds);
		if (null == memberPromLimits || memberPromLimits.size() == 0) {
			return null;
		}
		if (null == productPromLimits || productPromLimits.size() == 0) {
			return null;
		}
		// 取memberPromLimits与productPromLimits两个集合的交集
		memberPromLimits.retainAll(productPromLimits);
		return memberPromLimits;
	}

	/**
	 * 根据当前时间过滤促销规则
	 * 
	 * @param promotionList
	 * @return
	 */
	private List<LimitCommand> filterLimitRule(List<LimitCommand> limitList, Date currentTime) {
		long currentMilliseconds = currentTime.getTime();
		List<LimitCommand> newLimitList = new ArrayList<LimitCommand>();
		for (LimitCommand limit : limitList) {
			long startTimeMilliseconds = limit.getStartTime().getTime();
			long endTimeMilliseconds = limit.getEndTime().getTime();
			if (currentMilliseconds >= startTimeMilliseconds && currentMilliseconds <= endTimeMilliseconds) {
				newLimitList.add(limit);
			}
		}
		return newLimitList;
	}

	public List<LimitCommand> getLimits() {
		return limits;
	}

	public void setLimits(List<LimitCommand> limits) {
		this.limits = limits;
	}

}
