package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.command.promotion.SettingComplexCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

public interface SdkPromotionCalculationSettingManager extends BaseManager{
	public List<AtomicSetting> parseSettingByExpression(String expression);
	public AtomicSetting parseAtomicSettingByExpression(String atomicExpression);
	public List<PromotionBrief> summarizeSettingToBrief(List<PromotionBrief> briefList);
	public BigDecimal getSKUSettingDiscountAmount(List<PromotionSKUDiscAMTBySetting> list,Long skuId);
	public BigDecimal getSKUSettingFreeShipping(List<PromotionSKUDiscAMTBySetting> list,Long skuId);
	public List<AtomicSetting> convertComplexSettingToAtomic(List<SettingComplexCommand> complexList);
	public List<PromotionSKUDiscAMTBySetting> compressMultipSKUSetting2One(List<PromotionSKUDiscAMTBySetting> list);
	public BigDecimal getDiscAMTFromPromotionResultBriefsByCall(List<PromotionBrief> briefListOnePromotion);
	public BigDecimal getDiscAMTFromPromotionResultBriefsBaseOrder(List<PromotionBrief> briefListOnePromotion);

	public Map<Long,List<PromotionCouponCodeCommand>> seperateCouponCodesByShopID(List<String> coupons);
	public List<PromotionCouponCodeCommand> getLeftCouponsKickoffPrevious(List<PromotionCouponCodeCommand> currentCoupons,List<PromotionBrief> kickCoupons);
	public List<String> getCouponCodeFromCommandList(List<PromotionCouponCodeCommand> couponCommandList);
	public List<PromotionCouponCodeCommand> filterCouponsByCouponTypeId(List<PromotionCouponCodeCommand> couponCommandList, Long couponTypeId);
	public Integer getGiftQtyFromPromotionResultBriefsByItemId(List<PromotionBrief> briefListOnePromotion, long itemId);
	
	//public BigDecimal getDiscAMTFromOrderBasePromotionResultBriefsByCall(ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious);
	public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious,long ItemId);	
	public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious,long CategoryId);
	public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious,long comboId);
	public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious,long skuId);
	
	public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(List<ShoppingCartLineCommand> shopCartLines,List<PromotionBrief> briefListPrevious,long ItemId);	
	public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(List<ShoppingCartLineCommand> shopCartLines,List<PromotionBrief> briefListPrevious,long CategoryId);
	public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(List<ShoppingCartLineCommand> shopCartLines,List<PromotionBrief> briefListPrevious,long comboId);
	public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(List<ShoppingCartLineCommand> shopCartLines,List<PromotionBrief> briefListPrevious,long skuId);

	public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(List<ShoppingCartLineCommand> lines,List<PromotionBrief> briefListPrevious, List<Long> customItemIdList);
	
	// 自定义组合优惠，计算该组合下的累计金额
	public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerOrderByAMT(List<ShoppingCartLineCommand> lines,
			List<Long> customItemIdList, BigDecimal discAmount, Integer factor, List<PromotionBrief> briefListPrevious);
	
	// 自定义组合折扣，计算该组合下的累计金额
	public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerOrderByRate(List<ShoppingCartLineCommand> lines,
			List<Long> customItemIdList, BigDecimal discRate, List<PromotionBrief> briefListPrevious);

	// 自定义组合按Item优惠，计算该Item下的累计金额
	public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerItemByAMT(List<ShoppingCartLineCommand> lines,
			List<Long> customItemIdList, BigDecimal discAmount, Integer factor, List<PromotionBrief> briefListPrevious);

	// 自定义组合按Item折扣，计算该Item下的累计金额
	public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerItemByRate(List<ShoppingCartLineCommand> lines,
			List<Long> customItemIdList, BigDecimal discRate, boolean onePieceMark, List<PromotionBrief> briefListPrevious);

	// 自定义按组合按件优惠，计算该Combo下的累计金额
	public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerPCSByAMT(List<ShoppingCartLineCommand> lines,
			List<Long> customItemIdList, BigDecimal discAmount, Integer factor, List<PromotionBrief> briefListPrevious);
	// 按自定义组合按件折扣，计算该自定义ID下的累计金额
	public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerPCSByRate(List<ShoppingCartLineCommand> lines,
			List<Long> customItemIdList, BigDecimal discRate, boolean onePieceMark, List<PromotionBrief> briefListPrevious);

	// 计算礼品的累计金额,按customId。一个自定义下多个SKU，按不定顺序取足QTY返回优惠金额
	public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByCustomItemIds(long shopId, AtomicSetting setting,
			Integer qty, Integer displayCountLimited);
	
	// 根据CouponCodes List，检查当前Type的优惠券，计算出该Type下customId的优惠金额
	public List<PromotionSKUDiscAMTBySetting> getDiscountAMTByCustomItemIdsCoupon(
			List<ShoppingCartLineCommand> shoppingCartLines, List<Long> customItemIdList, long couponTypeId,
			List<PromotionCouponCodeCommand> couponCodes, boolean onePieceMark, long shopId,
			List<PromotionBrief> briefListPrevious);
}
