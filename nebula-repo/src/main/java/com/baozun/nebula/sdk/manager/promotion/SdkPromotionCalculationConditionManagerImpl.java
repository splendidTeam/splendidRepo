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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.calculateEngine.condition.ItemFactor;
import com.baozun.nebula.calculateEngine.param.ConditionType;
import com.baozun.nebula.calculateEngine.param.ScopeType;
import com.baozun.nebula.command.promotion.ConditionComplexCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.model.promotion.PromotionConditionStepComparator;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.model.rule.MemberTagRule;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeConditionLoader;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeFilterLoader;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;

@Transactional
@Service("sdkPromotionCalculationConditionManager")
public class SdkPromotionCalculationConditionManagerImpl implements SdkPromotionCalculationConditionManager {

	private static final Logger log = LoggerFactory.getLogger(SdkPromotionCalculationConditionManager.class);

	private static final Integer CHECKFAILURE = 0;
	private static final String CONDITIONREGX = "[\\(\\,\\:\\)]";

	// nolmt
	// ordamt(200)整单金额大于等于200
	// ordpcs(5)整单件数大于等于5件
	// scpordamt(500,cid:188)男鞋整单金额大于等于500
	// scpordpcs(3,cid:188)男鞋整单件数大于等于3
	// scpprdamt(300,cid:188)男鞋单品金额大于等于300
	// scpprdpcs(2,cid:188)男鞋单品件数大于等于2
	// ordcoupon(2)整单5元券
	// scpcoupon(1,cid:188)男鞋类10元券
	@Autowired
	private SdkShoppingCartManager shoppingCartmanager;

	@Autowired
	private SdkPromotionCalculationSettingManager sdkPromotionSettingManager;

	@Override
	public List<AtomicSetting> getStepAtomicComplexSetting(List<AtomicSetting> settingList, long complexConditionId) {
		List<AtomicSetting> returnListOneElementIn = new ArrayList<AtomicSetting>();

		for (AtomicSetting one : settingList) {
			if (one.getComplexConditionId() == complexConditionId) {
				returnListOneElementIn.add(one);
				break;
			}
		}

		return returnListOneElementIn;
	}

	/*
	 * 检查阶梯设置，并返回阶梯档次，设置的时候取该档次的设置即可。0不符合条件。
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkPromotionConditionCheckingManager#
	 * getStepByAtomicComplexConditionList
	 * (com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
	 * java.util.List)
	 */
	@Override
	public long getStepByAtomicComplexConditionList(ShoppingCartCommand shopCart, List<AtomicCondition> complexConditionList, long shopId, List<PromotionBrief> briefListPrevious) {
		AtomicCondition matchedPrevious = null;
		PromotionConditionStepComparator priorityComparator = new PromotionConditionStepComparator();

		Collections.sort(complexConditionList, priorityComparator);

		BigDecimal needToPay = BigDecimal.ZERO;

		BigDecimal prevoiusDiscAMTByAll = BigDecimal.ZERO;
		BigDecimal prevoiusDiscAMTByItem = BigDecimal.ZERO;
		BigDecimal prevoiusDiscAMTByCategory = BigDecimal.ZERO;
		BigDecimal prevoiusDiscAMTByCombo = BigDecimal.ZERO;

		for (AtomicCondition condition : complexConditionList) {
			// 整单金额
			if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDAMT)) {
				needToPay = shoppingCartmanager.getAllAmount(shopCart.getShoppingCartLineCommands());
				prevoiusDiscAMTByAll = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
				needToPay = needToPay.subtract(prevoiusDiscAMTByAll);
				if (needToPay.compareTo(condition.getConditionValue()) >= 0) {
					condition.setConditionResult(true);
					matchedPrevious = condition;
					continue;
				} else {
					break;
				}
			}
			// 整单件数
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDPCS)) {
				if (shopCart.getOrderQuantity() >= condition.getConditionValue().intValue()) {
					condition.setConditionResult(true);
					matchedPrevious = condition;
					continue;
				} else {
					break;
				}
			}
			// 整单Coupon,范围Coupon
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDCOUPON) || condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPCOUPON)) {
				List<PromotionCouponCodeCommand> coupons = shopCart.getCouponCodeCommands();
				if (checkCoupon(shopCart.getShoppingCartLineCommands(), condition, coupons, shopId) == true) {
					condition.setConditionResult(true);
					matchedPrevious = condition;
					continue;
				} else {
					break;
				}
			}
			// 范围内整单金额
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPORDAMT)) {
				// 根据范围ID，计算每个Line SKU下Item的金额
				BigDecimal ordAMT = BigDecimal.ZERO;
				if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
					ordAMT = shoppingCartmanager.getAllAmount(shopCart.getShoppingCartLineCommands());
					prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
					needToPay = ordAMT.subtract(prevoiusDiscAMTByItem);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
					ordAMT = shoppingCartmanager.getProductAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
					prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(shopCart,briefListPrevious, condition.getScopeValue());
					needToPay = ordAMT.subtract(prevoiusDiscAMTByItem);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
					ordAMT = shoppingCartmanager.getCategoryAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
					prevoiusDiscAMTByCategory = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(shopCart,briefListPrevious, condition.getScopeValue());
					needToPay = ordAMT.subtract(prevoiusDiscAMTByCategory);
				}else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
					List<Long> itemIdList = new ArrayList<Long>();
					BigDecimal prevoiusDiscAMTByCustom = BigDecimal.ZERO;
					itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
					ordAMT = shoppingCartmanager.getCustomAmount(itemIdList, shopCart.getShoppingCartLineCommands());
					prevoiusDiscAMTByCategory = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(shopCart,briefListPrevious, condition.getScopeValue());
					needToPay = ordAMT.subtract(prevoiusDiscAMTByCategory);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
					ordAMT = shoppingCartmanager.getComboAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
					//prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByComboId(briefListPrevious, condition.getScopeValue());
					prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(shopCart,briefListPrevious, condition.getScopeValue());
					
					needToPay = ordAMT.subtract(prevoiusDiscAMTByCombo);
				}
				if (needToPay.compareTo(condition.getConditionValue()) >= 0) {
					condition.setConditionResult(true);
					matchedPrevious = condition;
					continue;
				} else {
					break;
				}
			}
			// 范围内整单件数
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPORDPCS)) {
				// 根据范围ID，计算每个Line SKU下Item的金额
				int ordQTY = 0;
				if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
					ordQTY = shoppingCartmanager.getQuantityInShoppingCartByAll(shopCart);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
					ordQTY = shoppingCartmanager.getProductQuantity(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
					ordQTY = shoppingCartmanager.getCategoryQuantity(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
				}else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
					List<Long> itemIdList = new ArrayList<Long>();
					itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
					ordQTY = shoppingCartmanager.getCustomQuantity(itemIdList, shopCart.getShoppingCartLineCommands());
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
					ordQTY = shoppingCartmanager.getComboQuantity(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
				}
				if (ordQTY >= condition.getConditionValue().intValue()) {
					condition.setConditionResult(true);
					matchedPrevious = condition;
					continue;
				} else {
					break;
				}
			}
			// 范围内单品金额
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDAMT)) {
				// 根据范围ID，计算每个Line SKU下Item的金额
				BigDecimal toCompareAMT = BigDecimal.ZERO;
				if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
					toCompareAMT = shoppingCartmanager.getAllAmount(shopCart.getShoppingCartLineCommands());
					prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByItem);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
					toCompareAMT = shoppingCartmanager.getProductAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
					prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(shopCart,briefListPrevious, condition.getScopeValue());
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByItem);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
					toCompareAMT = shoppingCartmanager.getCategoryAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
					prevoiusDiscAMTByCategory = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(shopCart,briefListPrevious, condition.getScopeValue());
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByCategory);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
					List<Long> itemIdList = new ArrayList<Long>();
					BigDecimal prevoiusDiscAMTByCustom = BigDecimal.ZERO;
					itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
					toCompareAMT = shoppingCartmanager.getCustomAmount(itemIdList, shopCart.getShoppingCartLineCommands());					
					prevoiusDiscAMTByCustom = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(shopCart.getShoppingCartLineCommands(),briefListPrevious, itemIdList);
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByCategory);
				}else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
					toCompareAMT = shoppingCartmanager.getComboAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
					//prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByComboId(briefListPrevious, condition.getScopeValue());
					prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(shopCart,briefListPrevious, condition.getScopeValue());
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByCombo);
				}
				if (toCompareAMT.compareTo(condition.getConditionValue()) >= 0) {
					condition.setConditionResult(true);
					matchedPrevious = condition;
					continue;
				} else {
					break;
				}
			}
			// 范围内单品件数
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDPCS)) {
				// 根据范围ID，计算每个Line SKU下Item的金额
				Integer toCompareQty = 0;
				if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
					toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByAll(shopCart);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
					toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByItemId(shopCart, condition.getScopeValue());
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
					toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByCategoryId(shopCart, condition.getScopeValue());
				}else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
					List<Long> itemIdList = new ArrayList<Long>();
					itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
					toCompareQty = shoppingCartmanager.getCustomQuantity(itemIdList, shopCart.getShoppingCartLineCommands());
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
					toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByComboId(shopCart, condition.getScopeValue());
				}
				if (toCompareQty >= condition.getConditionValue().intValue()) {
					condition.setConditionResult(true);
					matchedPrevious = condition;
					continue;
				} else {
					break;
				}
			}
		}
		// 没达到任何档次
		if (matchedPrevious == null)
			return 0;
		else
			return matchedPrevious.getComplexConditionId();
	}

	/*
	 * 检查购物车中是否有主商品和选购商品
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkPromotionConditionCheckingManager#
	 * checkChoiceByAtomicComplexConditionList
	 * (com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
	 * java.util.List)主商品，选购商品prmprd： scpprdpcs(1,pid:21) | scpprdpcs(1，cmb:4)
	 * ，addtprd：ChoiceMark:prmprd,addtprd.逻辑关系式：(至少一个prmprd，多个是或的关系) &&
	 * (至少一个addtprd，多个是或的关系)
	 */
	@Override
	public Integer checkChoiceByAtomicComplexConditionList(ShoppingCartCommand shopCart, List<AtomicCondition> complexConditionList, long shopId, List<PromotionBrief> briefListPrevious) {
		if (complexConditionList == null || complexConditionList.size() == 0) {
			return CHECKFAILURE;
		}
		// 主商品和选购商品条件，分成两个数组集合，主商品之间是或的关系，选购商品之间是或的关系，主选之间是与的关系
		List<AtomicCondition> complexPrimaryConditionList = new ArrayList<AtomicCondition>();
		List<AtomicCondition> complexChoiceConditionList = new ArrayList<AtomicCondition>();
		Integer primaryResult = CHECKFAILURE;
		Integer choiceResult = CHECKFAILURE;
		for (AtomicCondition one : complexConditionList) {
			one.setOperateTag("|");
			if (one.getChoiceMark().equalsIgnoreCase(ConditionType.EXP_CHOICEPRIMPRD)) {
				complexPrimaryConditionList.add(one);
			} else if (one.getChoiceMark().equalsIgnoreCase(ConditionType.EXP_CHOICEADDTPRD)) {
				complexChoiceConditionList.add(one);
			}
		}
		if (complexPrimaryConditionList == null || complexPrimaryConditionList.size() == 0 || complexChoiceConditionList == null || complexChoiceConditionList.size() == 0) {
			return CHECKFAILURE;
		}

		primaryResult = getFactorFromShoppingCartByAtomicConditionList(shopCart, complexPrimaryConditionList, shopId, briefListPrevious);
		choiceResult = getFactorFromShoppingCartByAtomicConditionList(shopCart, complexChoiceConditionList, shopId, briefListPrevious);

		if (primaryResult > CHECKFAILURE && choiceResult > CHECKFAILURE) {
			return primaryResult > choiceResult ? choiceResult : primaryResult;
		}

		return CHECKFAILURE;
	}

	/**
	 * 按循序拼接原子表达式结果
	 */
	@Override
	public Integer getResultShoppingCartByAtomicConditionList(List<AtomicCondition> conditionList) {
		Integer factor = 0;
		// 可以优惠表达式逻辑：true | ***永远是真，***不用再处理
		boolean result = false;
		int firstOne = 0;
		for (AtomicCondition condition : conditionList) {
			firstOne ++;
			if (condition.getConditionResult()) {
				if (firstOne == 1) {
					factor = condition.getMultiplicationFactor();
				} else {
					if (factor > condition.getMultiplicationFactor())
					{
						factor = condition.getMultiplicationFactor();	
					}else
					{
						if (factor==0)
						{
							factor = condition.getMultiplicationFactor();
						}
					}
					//factor = factor > condition.getMultiplicationFactor() ? condition.getMultiplicationFactor() : factor;
				}
			}
			if (condition.getOperateTag().equalsIgnoreCase("&")) {
				result = (firstOne == 1?true:result) && condition.getConditionResult();
			} else if (condition.getOperateTag().equalsIgnoreCase("|")) {
				result = (firstOne == 1?false:result) || condition.getConditionResult();
			} else if (condition.getOperateTag().equalsIgnoreCase("")) {
				result = result || condition.getConditionResult();
			}
		}
		if (result) {
			return factor;
		}
		return CHECKFAILURE;
	}

	@Override
	public List<ItemFactor> getItemFactorListShoppingCartByStepAtomicConditions(ShoppingCartCommand shopCart, List<AtomicCondition> condList, long shopId, List<PromotionBrief> briefListPrevious) {
		List<ItemFactor> listFactor = new ArrayList<ItemFactor>();
		List<ItemFactor> listTempFactor = null;
		Integer multipFactor = 0;

		Set<Long> itemIds = new HashSet<Long>();
		for (AtomicCondition condition : condList) {
			listTempFactor = new ArrayList<ItemFactor>();
			// 根据范围ID，计算每个Line SKU下Item的金额
			if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT))
				itemIds = shoppingCartmanager.getItemIdsFromShoppingCartByCall(shopCart.getShoppingCartLineCommands());
			else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT))
				itemIds.add(condition.getScopeValue());
			else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY))
				itemIds = shoppingCartmanager.getItemIdsFromShoppingCartByCategoryId(shopCart.getShoppingCartLineCommands(), condition.getScopeValue());
			else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM))
			{
				List<Long> itemIdList = new ArrayList<Long>();
				itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
				itemIds = shoppingCartmanager.getItemIdsFromShoppingCartByCustomItemIds(shopCart.getShoppingCartLineCommands(), itemIdList);
			}
			else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO))
				itemIds = shoppingCartmanager.getItemIdsFromShoppingCartByComboId(shopCart.getShoppingCartLineCommands(), condition.getScopeValue());

			// 范围内单品金额
			if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDAMT)) {
				if (itemIds == null || itemIds.size() == 0)
					continue;
				for (Long itemId : itemIds) {
					BigDecimal toCompareAMT = shoppingCartmanager.getNeedToPayAmountInShoppingCartByItemId(shopCart, itemId);
					BigDecimal prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(shopCart,briefListPrevious, itemId);
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByItem);
					if (toCompareAMT.compareTo(condition.getConditionValue()) >= 0) {
						ItemFactor itemFactor = new ItemFactor();
						itemFactor.setItemId(itemId);
						itemFactor.setFactor(1);
						if (toCompareAMT.compareTo(condition.getConditionValue()) > 0 && condition.getMultiplicationMark()) {
							multipFactor = calculateMultiplicationFactor(toCompareAMT, condition.getConditionValue());
							itemFactor.setFactor(multipFactor);
						}
						listTempFactor.add(itemFactor);
					}
				}
			}
			// 范围内单品件数
			if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDPCS)) {
				if (itemIds == null || itemIds.size() == 0)
					continue;
				for (Long itemId : itemIds) {
					Integer toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByItemId(shopCart, itemId);
					if (new BigDecimal(toCompareQty).compareTo(condition.getConditionValue()) >= 0) {
						ItemFactor itemFactor = new ItemFactor();
						itemFactor.setItemId(itemId);
						itemFactor.setFactor(1);
						if (new BigDecimal(toCompareQty).compareTo(condition.getConditionValue()) > 0 && condition.getMultiplicationMark()) {
							multipFactor = calculateMultiplicationFactor(new BigDecimal(toCompareQty), condition.getConditionValue());
							itemFactor.setFactor(multipFactor);
						}
						listTempFactor.add(itemFactor);
					}
				}
			}

			// 合并
			if (listFactor == null || listFactor.size() == 0)
				listFactor = listTempFactor;
			if (listTempFactor == null || listTempFactor.size() == 0)
				break;
			else
				listFactor = listTempFactor;
		}
		return listFactor;
	}

	@Override
	public List<ItemFactor> getItemFactorListShoppingCartByAtomicConditions(ShoppingCartCommand shopCart, List<AtomicCondition> condList, long shopId, List<PromotionBrief> briefListPrevious) {
		List<ItemFactor> listFactor = new ArrayList<ItemFactor>();
		List<ItemFactor> listTempFactor = null;
		Integer multipFactor = 0;

		Set<Long> itemIds = new HashSet<Long>();
		for (AtomicCondition condition : condList) {
			listTempFactor = new ArrayList<ItemFactor>();
			// 根据范围ID，计算每个Line SKU下Item的金额
			if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT))
				itemIds = shoppingCartmanager.getItemIdsFromShoppingCartByCall(shopCart.getShoppingCartLineCommands());
			else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT))
				itemIds.add(condition.getScopeValue());
			else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY))
				itemIds = shoppingCartmanager.getItemIdsFromShoppingCartByCategoryId(shopCart.getShoppingCartLineCommands(), condition.getScopeValue());
			else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM))
			{
				List<Long> itemIdList = new ArrayList<Long>();
				itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
				itemIds = shoppingCartmanager.getItemIdsFromShoppingCartByCustomItemIds(shopCart.getShoppingCartLineCommands(),itemIdList);
			}
			else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO))
				itemIds = shoppingCartmanager.getItemIdsFromShoppingCartByComboId(shopCart.getShoppingCartLineCommands(), condition.getScopeValue());

			// 范围内单品金额
			if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDAMT)) {
				if (itemIds == null || itemIds.size() == 0)
					continue;
				for (Long itemId : itemIds) {
					BigDecimal toCompareAMT = shoppingCartmanager.getNeedToPayAmountInShoppingCartByItemId(shopCart, itemId);
					BigDecimal prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(shopCart,briefListPrevious, itemId);
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByItem);
					if (toCompareAMT.compareTo(condition.getConditionValue()) >= 0) {
						ItemFactor itemFactor = new ItemFactor();
						itemFactor.setItemId(itemId);
						itemFactor.setFactor(1);
						if (toCompareAMT.compareTo(condition.getConditionValue()) > 0 && condition.getMultiplicationMark()) {
							multipFactor = calculateMultiplicationFactor(toCompareAMT, condition.getConditionValue());
							itemFactor.setFactor(multipFactor);
						}
						listTempFactor.add(itemFactor);
					}
				}
			}
			// 范围内单品件数
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDPCS)) {
				if (itemIds == null || itemIds.size() == 0)
					continue;
				for (Long itemId : itemIds) {
					Integer toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByItemId(shopCart, itemId);
					if (new BigDecimal(toCompareQty).compareTo(condition.getConditionValue()) >= 0) {
						ItemFactor itemFactor = new ItemFactor();
						itemFactor.setItemId(itemId);
						itemFactor.setFactor(1);
						if (new BigDecimal(toCompareQty).compareTo(condition.getConditionValue()) > 0 && condition.getMultiplicationMark()) {
							multipFactor = calculateMultiplicationFactor(new BigDecimal(toCompareQty), condition.getConditionValue());
							itemFactor.setFactor(multipFactor);
						}
						listTempFactor.add(itemFactor);
					}
				}
			}
			// 合并
			if (listFactor == null || listFactor.size() == 0)
				listFactor = listTempFactor;
			else
				listFactor = getIntersectItemFactorList(listFactor, listTempFactor);
		}
		return listFactor;
	}

	/**
	 * 获取两个集合的交集By SKU,但被赠因子取最小的
	 * 
	 * @param one
	 * @param another
	 * @return
	 */
	@Override
	public List<ItemFactor> getIntersectItemFactorList(List<ItemFactor> one, List<ItemFactor> another) {
		if (one == null || another == null || one.size() == 0 || another.size() == 0)
			return null;
		List<ItemFactor> unionItemFactor = new ArrayList<ItemFactor>();
		for (ItemFactor outer : one) {
			for (ItemFactor inner : another) {
				if (outer.getItemId() == inner.getItemId()) {
					ItemFactor sku = new ItemFactor();
					sku.setItemId(outer.getItemId());
					sku.setFactor(outer.getFactor() > inner.getFactor() ? inner.getFactor() : outer.getFactor());
					unionItemFactor.add(sku);
				}
			}
		}
		return unionItemFactor;
	}

	/**
	 * 处理原子表达式
	 */
	@Override
	public List<AtomicCondition> manipulateShoppingCartByAtomicConditionList(ShoppingCartCommand shopCart, List<AtomicCondition> condList, long shopId, List<PromotionBrief> briefListPrevious) {
		Integer multipFactor = 1;
		BigDecimal needToPay = BigDecimal.ZERO;

		BigDecimal prevoiusDiscAMTByAll = BigDecimal.ZERO;
		BigDecimal prevoiusDiscAMTByItem = BigDecimal.ZERO;
		BigDecimal prevoiusDiscAMTByCategory = BigDecimal.ZERO;
		BigDecimal prevoiusDiscAMTByCombo = BigDecimal.ZERO;

		for (AtomicCondition condition : condList) {
			// 解析原子表达式
			// 无限制
			if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_NOLIMIT)) {
				condition.setMultiplicationMark(false);
				condition.setMultiplicationFactor(1);
				condition.setConditionResult(true);
			}else
				// 整单优惠最大幅度
				if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDMARGINRATE)) {
					prevoiusDiscAMTByAll = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
					needToPay = shoppingCartmanager.getNeedToPayAmountInShoppingCartByAll(shopCart);
					//整单应付金额不可能为零
					if (null == needToPay || needToPay.compareTo(BigDecimal.valueOf(0))==0)
					{
						condition.setConditionResult(false);
						condition.setMultiplicationFactor(0);
					}else  
					{
						BigDecimal previousMarginRate = prevoiusDiscAMTByAll.divide(needToPay, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
						if (previousMarginRate.compareTo(new BigDecimal(100).subtract(condition.getConditionValue())) > 0)
						{
							condition.setConditionResult(false);
							condition.setMultiplicationFactor(0);
						} else 
						{
							condition.setMultiplicationFactor(1);
							condition.setConditionResult(true);
						}
					}
				}
			else
				// 自定义
				if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_CUSTOM)) {
					int customFactor = 0;
					log.info(String.format("自定义条件%1$s开始加载！",condition.getConditionValue().toString()));
					customFactor = SdkCustomizeConditionLoader.load(condition.getConditionValue().toString(),condition.getPromotionId(), shopCart, briefListPrevious);
					log.info(String.format("自定义条件%1$s开始成功！计算的倍增因子为%2$s！",condition.getConditionValue().toString(),customFactor));
					condition.setMultiplicationMark(condition.getMultiplicationMark());
					condition.setMultiplicationFactor(customFactor);
					if (customFactor > 0)
						condition.setConditionResult(true);
					else
						condition.setConditionResult(false);
				}
			// 整单金额
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDAMT)) {
				prevoiusDiscAMTByAll = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
				needToPay = shopCart.getOriginPayAmount();
				if (prevoiusDiscAMTByAll.compareTo(needToPay) < 0) {
					needToPay = needToPay.subtract(prevoiusDiscAMTByAll);
				}
				if (needToPay.compareTo(condition.getConditionValue()) >= 0) {
					condition.setMultiplicationFactor(1);
					if (condition.getMultiplicationMark()) {
						multipFactor = calculateMultiplicationFactor(shopCart.getOriginPayAmount(), condition.getConditionValue());
						condition.setMultiplicationFactor(multipFactor);
					}
					condition.setConditionResult(true);
				} else {
					condition.setConditionResult(false);
				}
			}
			// 整单件数
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDPCS)) {
				if (shopCart.getOrderQuantity() >= condition.getConditionValue().intValue()) {
					condition.setMultiplicationFactor(1);
					if (condition.getMultiplicationMark()) {
						multipFactor = calculateMultiplicationFactor(new BigDecimal(shopCart.getOrderQuantity()), condition.getConditionValue());
						condition.setMultiplicationFactor(multipFactor);
					}
					condition.setConditionResult(true);
				} else {
					condition.setConditionResult(false);
				}
			}
			// 整单Coupon,范围Coupon。无倍增。
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDCOUPON) || condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPCOUPON)) {
				List<PromotionCouponCodeCommand> coupons = shopCart.getCouponCodeCommands();// getCoupons();
				if (checkCoupon(shopCart.getShoppingCartLineCommands(), condition, coupons, shopId)) {
					condition.setMultiplicationMark(false);
					condition.setMultiplicationFactor(1);
					condition.setConditionResult(true);
				} else {
					condition.setConditionResult(false);
				}
			}
			// 范围内最大优惠幅度
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPMARGINRATE)) {
				// 根据范围ID，计算每个Line SKU下Item的金额
				BigDecimal ordAMT = BigDecimal.ZERO;
				BigDecimal prevoiusDiscAMT = BigDecimal.ZERO;
				if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
					ordAMT = shoppingCartmanager.getAllAmount(shopCart.getShoppingCartLineCommands());

					prevoiusDiscAMTByAll = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
					prevoiusDiscAMT = prevoiusDiscAMTByAll;
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
					ordAMT = shoppingCartmanager.getProductAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());

					prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(shopCart,briefListPrevious, condition.getScopeValue());
					prevoiusDiscAMT = prevoiusDiscAMTByItem;
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
					ordAMT = shoppingCartmanager.getCategoryAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());

					prevoiusDiscAMTByCategory = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(shopCart,briefListPrevious, condition.getScopeValue());
					prevoiusDiscAMT = prevoiusDiscAMTByCategory;
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
					List<Long> itemIdList = new ArrayList<Long>();
					BigDecimal prevoiusDiscAMTByCustom = BigDecimal.ZERO;
					itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
					ordAMT = shoppingCartmanager.getCustomAmount(itemIdList, shopCart.getShoppingCartLineCommands());
	
					prevoiusDiscAMTByCustom = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(shopCart.getShoppingCartLineCommands(),briefListPrevious, itemIdList);
					prevoiusDiscAMT = prevoiusDiscAMTByCustom;
				}
				else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
					ordAMT = shoppingCartmanager.getComboAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
					//prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByComboId(briefListPrevious, condition.getScopeValue());
					prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(shopCart,briefListPrevious, condition.getScopeValue());
					prevoiusDiscAMT = prevoiusDiscAMTByCombo;
				}
				//判断最大优惠幅度
				//应付金额可能为零，当为零时，说明该范围内的商品，购物车中没有，
				//所以需要特殊处理，认为这个条件不考虑，设置为true，返回
				if (null == ordAMT || ordAMT.compareTo(BigDecimal.valueOf(0))==0)
				{
					condition.setMultiplicationFactor(1);
					condition.setConditionResult(true);
				}else  
				{
					BigDecimal previousMarginRate = prevoiusDiscAMT.divide(ordAMT, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
					if (previousMarginRate.compareTo(new BigDecimal(100).subtract(condition.getConditionValue())) > 0)
					{
						condition.setConditionResult(false);
					} else {
						condition.setMultiplicationFactor(1);
						condition.setConditionResult(true);
					}
				}
			}
			// 范围内整单金额
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPORDAMT)) {
				// 根据范围ID，计算每个Line SKU下Item的金额
				BigDecimal ordAMT = BigDecimal.ZERO;
				if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
					ordAMT = shoppingCartmanager.getAllAmount(shopCart.getShoppingCartLineCommands());

					prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
					needToPay = ordAMT.subtract(prevoiusDiscAMTByItem);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
					ordAMT = shoppingCartmanager.getProductAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());

					prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(shopCart,briefListPrevious, condition.getScopeValue());
					needToPay = ordAMT.subtract(prevoiusDiscAMTByItem);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
					ordAMT = shoppingCartmanager.getCategoryAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());

					prevoiusDiscAMTByCategory = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(shopCart,briefListPrevious, condition.getScopeValue());
					needToPay = ordAMT.subtract(prevoiusDiscAMTByCategory);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
					List<Long> itemIdList = new ArrayList<Long>();
					BigDecimal prevoiusDiscAMTByCustom = BigDecimal.ZERO;
					itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
					ordAMT = shoppingCartmanager.getCustomAmount(itemIdList, shopCart.getShoppingCartLineCommands());
	
					prevoiusDiscAMTByCustom = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(shopCart.getShoppingCartLineCommands(),briefListPrevious, itemIdList);
					needToPay = ordAMT.subtract(prevoiusDiscAMTByCustom);
				}
				else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
					ordAMT = shoppingCartmanager.getComboAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
					//prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByComboId(briefListPrevious, condition.getScopeValue());
					prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(shopCart,briefListPrevious, condition.getScopeValue());
					
					needToPay = ordAMT.subtract(prevoiusDiscAMTByCombo);
				}
				if (needToPay.compareTo(condition.getConditionValue()) >= 0) {
					condition.setMultiplicationFactor(1);
					if (condition.getMultiplicationMark()) {
						multipFactor = calculateMultiplicationFactor(needToPay, condition.getConditionValue());
						condition.setMultiplicationFactor(multipFactor);
					}
					condition.setConditionResult(true);
				} else {
					condition.setConditionResult(false);
				}
			}
			// 范围内整单件数
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPORDPCS)) {
				// 根据范围ID，计算每个Line SKU下Item的金额
				int ordQTY = 0;
				if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
					ordQTY = shoppingCartmanager.getQuantityInShoppingCartByAll(shopCart);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
					ordQTY = shoppingCartmanager.getProductQuantity(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
					ordQTY = shoppingCartmanager.getCategoryQuantity(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
					List<Long> itemIdList = new ArrayList<Long>();
					itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
					ordQTY = shoppingCartmanager.getCustomQuantity(itemIdList, shopCart.getShoppingCartLineCommands());
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
					ordQTY = shoppingCartmanager.getComboQuantity(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
				}
				if (ordQTY >= condition.getConditionValue().intValue()) {
					condition.setMultiplicationFactor(1);
					if (condition.getMultiplicationMark()) {
						multipFactor = calculateMultiplicationFactor(new BigDecimal(ordQTY), condition.getConditionValue());
						condition.setMultiplicationFactor(multipFactor);
					}
					condition.setConditionResult(true);
				} else {
					condition.setConditionResult(false);
				}
			}
			// 范围内单品金额
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDAMT)) {
				// 根据范围ID，计算每个Line SKU下Item的金额
				BigDecimal toCompareAMT = BigDecimal.ZERO;
				if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
					toCompareAMT = shoppingCartmanager.getNeedToPayAmountInShoppingCartByAll(shopCart);
					prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByItem);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
					toCompareAMT = shoppingCartmanager.getNeedToPayAmountInShoppingCartByItemId(shopCart, condition.getScopeValue());
					prevoiusDiscAMTByItem = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(shopCart,briefListPrevious, condition.getScopeValue());
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByItem);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
					toCompareAMT = shoppingCartmanager.getNeedToPayAmountInShoppingCartByCategoryId(shopCart, condition.getScopeValue());
					prevoiusDiscAMTByCategory = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(shopCart,briefListPrevious, condition.getScopeValue());
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByCategory);
				}else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
					List<Long> itemIdList = new ArrayList<Long>();
					BigDecimal prevoiusDiscAMTByCustom = BigDecimal.ZERO;
					itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
					toCompareAMT = shoppingCartmanager.getNeedToPayAmountInShoppingCartByCustomItemIds(shopCart.getShoppingCartLineCommands(), itemIdList);
					prevoiusDiscAMTByCustom = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(shopCart.getShoppingCartLineCommands(),briefListPrevious, itemIdList);
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByCustom);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
					toCompareAMT = shoppingCartmanager.getNeedToPayAmountInShoppingCartByComboId(shopCart, condition.getScopeValue());
					//prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByComboId(briefListPrevious, condition.getScopeValue());
					prevoiusDiscAMTByCombo = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(shopCart,briefListPrevious, condition.getScopeValue());
					toCompareAMT = toCompareAMT.subtract(prevoiusDiscAMTByCombo);
				}

				if (toCompareAMT.compareTo(condition.getConditionValue()) >= 0) {
					condition.setMultiplicationFactor(1);
					if (toCompareAMT.compareTo(condition.getConditionValue()) > 0 && condition.getMultiplicationMark()) {
						multipFactor = calculateMultiplicationFactor(toCompareAMT, condition.getConditionValue());
						condition.setMultiplicationFactor(multipFactor);
					}
					condition.setConditionResult(true);
				} else {
					condition.setConditionResult(false);
				}
			}
			// 范围内单品件数
			else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDPCS)) {
				// 根据范围ID，计算每个Line SKU下Item的金额
				Integer toCompareQty = 0;
				if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
					toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByAll(shopCart);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
					toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByItemId(shopCart, condition.getScopeValue());
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
					toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByCategoryId(shopCart, condition.getScopeValue());
				}else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
					List<Long> itemIdList = new ArrayList<Long>();
					itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
					toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByCustomItemIds(shopCart, itemIdList);
				} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
					toCompareQty = shoppingCartmanager.getQuantityInShoppingCartByComboId(shopCart, condition.getScopeValue());
				}
				if (new BigDecimal(toCompareQty).compareTo(condition.getConditionValue()) >= 0) {
					condition.setMultiplicationFactor(1);
					if (new BigDecimal(toCompareQty).compareTo(condition.getConditionValue()) > 0 && condition.getMultiplicationMark()) {
						multipFactor = calculateMultiplicationFactor(new BigDecimal(toCompareQty), condition.getConditionValue());
						condition.setMultiplicationFactor(multipFactor);
					}
					condition.setConditionResult(true);
				} else {
					condition.setConditionResult(false);
				}
			}
		}
		return condList;
	}

	/**
	 * 取倍增因子
	 * 
	 * @param toCompare
	 * @param conditionValue
	 * @return
	 */
	public Integer calculateMultiplicationFactor(BigDecimal toCompare, BigDecimal conditionValue) {
		Integer factor = 1;
		factor = toCompare.divide(conditionValue, 2, BigDecimal.ROUND_HALF_DOWN).intValue();
		return factor;
	}

	/**
	 * 根据检查购物车中的Lines 判断表达式是否满足
	 */
	@Override
	public Integer getFactorFromShoppingCartByConditionExpression(ShoppingCartCommand shopCart, String conditionExpression, long shopId, List<PromotionBrief> briefListPrevious) {
		List<AtomicCondition> condList = parseConditionByExpression(conditionExpression);
		condList = manipulateShoppingCartByAtomicConditionList(shopCart, condList, shopId, briefListPrevious);
		Integer result = getResultShoppingCartByAtomicConditionList(condList);
		return result;
	}

	/**
	 * 由购物车计算调用，直接传Promotion下的原子条件对象列表
	 */
	@Override
	public Integer getFactorFromShoppingCartByAtomicConditionList(ShoppingCartCommand shopCart, List<AtomicCondition> condList, long shopId, List<PromotionBrief> briefListPrevious) {
		condList = manipulateShoppingCartByAtomicConditionList(shopCart, condList, shopId, briefListPrevious);
		Integer result = getResultShoppingCartByAtomicConditionList(condList);
		return result;
	}

	/**
	 * 解析原子表达式，获得原子表达式对象
	 */
	@Override
	public AtomicCondition parseAtomicConditionByExpression(String atomicExpression, String op) {
		AtomicCondition condition = new AtomicCondition();
		condition.setOperateTag(op);
		condition.setConditionExpression(atomicExpression);

		// 不限nolmt
		if (atomicExpression.equalsIgnoreCase(ConditionType.EXP_NOLIMIT)) {
			condition.setConditionTag(ConditionType.EXP_NOLIMIT);
			return condition;
		}

		BigDecimal conditionValue = BigDecimal.ZERO;
		int scopeValue = 0;
		boolean multiplicationMark = false;

		// ordamt(3000,1）和 scpcoupon(1,cid:41,1)两种格式
		String[] atomicExpressions = atomicExpression.split(CONDITIONREGX);
		// ordamt(3000）必定是这种格式
		if (atomicExpressions.length == 2) {
			condition.setConditionTag(atomicExpressions[0].trim());
			if (atomicExpressions[1] == null || atomicExpressions[1].isEmpty())
				conditionValue = BigDecimal.ZERO;
			else
				conditionValue = new BigDecimal(atomicExpressions[1]);

			condition.setConditionValue(conditionValue);
			return condition;
		}
		// ordamt(3000,1)
		if (atomicExpressions.length == 3) {
			condition.setConditionTag(atomicExpressions[0].trim());
			if (atomicExpressions[1] == null || atomicExpressions[1].isEmpty())
				conditionValue = BigDecimal.ZERO;
			else
				conditionValue = new BigDecimal(atomicExpressions[1]);

			condition.setConditionValue(conditionValue);

			if (atomicExpressions[2] == null || atomicExpressions[2].isEmpty())
				multiplicationMark = false;
			else {
				if (atomicExpressions[2].equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT) || atomicExpressions[2].equalsIgnoreCase(MemberTagRule.EXP_PREFIX_ALLMEMBER))
					condition.setScopeTag(atomicExpressions[2]);
				else
					multiplicationMark = Integer.parseInt(atomicExpressions[2]) == 1 ? true : false;
			}

			condition.setMultiplicationMark(multiplicationMark);
			condition.setMultiplicationFactor(1);
			return condition;
		}
		// 限购条件，没有单间计之说，所以还是4
		// scpcoupon(1,cid:41,1)
		if (atomicExpressions.length == 4 || atomicExpressions.length == 5) {
			condition.setConditionTag(atomicExpressions[0].trim());
			if (atomicExpressions[1] == null || atomicExpressions[1].isEmpty())
				conditionValue = BigDecimal.ZERO;
			else
				conditionValue = new BigDecimal(atomicExpressions[1]);

			condition.setConditionValue(conditionValue);

			condition.setScopeTag(atomicExpressions[2]);
			if (atomicExpressions[3] == null || atomicExpressions[3].isEmpty())
				scopeValue = 0;
			else
				scopeValue = Integer.parseInt(atomicExpressions[3]);
			condition.setScopeValue(scopeValue);

			if (atomicExpressions.length == 5) {
				if (atomicExpressions[4] == null || atomicExpressions[4].isEmpty())
					multiplicationMark = false;
				else
					multiplicationMark = Integer.parseInt(atomicExpressions[4]) == 1 ? true : false;
			}

			condition.setMultiplicationMark(multiplicationMark);
			condition.setMultiplicationFactor(1);
			return condition;
		}

		return null;
	}

	/*
	 * 解析表达式，返回原子表达式对象列表 ordamt(3000） | scpordamt(1000,pid:21) |
	 * scpcoupon(1,cid:41) | ordcoupon(2)
	 */
	@Override
	public List<AtomicCondition> parseConditionByExpression(String expression) {
		expression = expression.replaceAll("\\ ", "");
		List<AtomicCondition> list = new ArrayList<AtomicCondition>();
		if (expression == null || expression.isEmpty())
			return list;
		AtomicCondition condition = new AtomicCondition();

		String leftExpression = expression;
		String operate = "";
		// 按照&或|原子分表达式
		String[] expressions = expression.split("[\\&\\|]");
		for (String exp : expressions) {
			condition = parseAtomicConditionByExpression(exp, operate);
			leftExpression = leftExpression.replace(exp, "").trim();// 移除处理过的原子，以获得&
																	// |操作符
			if (leftExpression.startsWith("&")) {
				operate = "&";
				leftExpression = leftExpression.replaceFirst("\\&", "").trim();
			}
			if (leftExpression.startsWith("|")) {
				operate = "|";
				leftExpression = leftExpression.replaceFirst("\\|", "").trim();
			}
			if (condition != null) {
				list.add(condition);
			}
		}

		return list;
	}

	/*
	 * coupon检查
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkPromotionConditionCheckingManager#
	 * getComboQuantity(java.lang.Long, java.util.List)
	 */
	public boolean checkCoupon(List<ShoppingCartLineCommand> shoppingCartLines, AtomicCondition condition, List<PromotionCouponCodeCommand> coupons, long shopId) {
		// 整单Coupon ordcoupon
		if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDCOUPON)) {
			long couponTypeId = condition.getConditionValue().longValue();
			// coupons参数传过来的是优惠券编码，condition.getConditionValue()优惠券类型
			if (shoppingCartmanager.checkCouponByCALL(couponTypeId, coupons, shopId)) {
				return true;
			}
		}
		// 范围Coupon scpcoupon(1,cid:41)
		else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPCOUPON)) {
			// coupons参数传过来的是优惠券编码，condition.getConditionValue()优惠券类型
			if (checkScopeCoupon(shoppingCartLines, condition, coupons, shopId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 范围Coupon检查
	 */
	@Override
	public boolean checkScopeCoupon(List<ShoppingCartLineCommand> shoppingCartLines, AtomicCondition condition, List<PromotionCouponCodeCommand> coupons, long shopId) {
		if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPCOUPON)) {
			long couponTypeID = condition.getConditionValue().longValue();
			if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
				return shoppingCartmanager.checkCouponByCALL(couponTypeID, coupons, shopId);
			} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)) {
				return shoppingCartmanager.checkCouponByItemId(shoppingCartLines, condition.getScopeValue(), couponTypeID, coupons, shopId);
			} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)) {
				return shoppingCartmanager.checkCouponByCategoryId(shoppingCartLines, condition.getScopeValue(), couponTypeID, coupons, shopId);
			}else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)) {
				List<Long> itemIdList = new ArrayList<Long>();
				itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
				return shoppingCartmanager.checkCouponByCustomItemIds(shoppingCartLines, itemIdList, couponTypeID, coupons, shopId);
			} else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)) {
				return shoppingCartmanager.checkCouponByComboId(shoppingCartLines, condition.getScopeValue(), couponTypeID, coupons, shopId);
			}
		}
		return false;
	}

	/**
	 * 转换条件表达式为原子类
	 * 
	 * @see com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationManager#convertComplexConditionToAtomic(java.util.List)
	 */
	@Override
	public List<AtomicCondition> convertComplexConditionToAtomic(List<ConditionComplexCommand> complexList) {
		if (complexList == null || complexList.size() == 0) {
			return null;
		}
		List<AtomicCondition> atomicList = new ArrayList<AtomicCondition>();
		for (ConditionComplexCommand comp : complexList) {
			AtomicCondition atomic = parseAtomicConditionByExpression(comp.getConditionExpress(), "|");
			if (atomic != null) {
				atomic.setPromotionId(comp.getPromotionId() == null ? 0 : comp.getPromotionId());
				atomic.setNormalConditionId(comp.getNormalConditionId() == null ? 0 : comp.getNormalConditionId());
				atomic.setComplexConditionId(comp.getId() == null ? 0 : comp.getId());
				atomic.setStepPriority(comp.getStepPriority() == null ? 0 : comp.getStepPriority());
				atomic.setChoiceMark(comp.getChoiceMark() == null ? "" : comp.getChoiceMark());
				atomic.setComplexType(comp.getComplexType() == null ? "" : comp.getComplexType());
				atomicList.add(atomic);
			}
		}
		return atomicList;
	}
}
