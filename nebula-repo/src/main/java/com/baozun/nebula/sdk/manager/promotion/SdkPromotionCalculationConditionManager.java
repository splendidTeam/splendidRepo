package com.baozun.nebula.sdk.manager.promotion;

import java.util.List;
import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.calculateEngine.condition.ItemFactor;
import com.baozun.nebula.command.promotion.ConditionComplexCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

public interface SdkPromotionCalculationConditionManager extends BaseManager{

	public Integer getFactorFromShoppingCartByConditionExpression(ShoppingCartCommand shopCart,String expression,long shopId,List<PromotionBrief> briefListPrevious);
	
	public Integer getFactorFromShoppingCartByAtomicConditionList(ShoppingCartCommand shopCart,List<AtomicCondition> conditionList,long shopId,List<PromotionBrief> briefListPrevious);
	
	public long getStepByAtomicComplexConditionList(ShoppingCartCommand shopCart,List<AtomicCondition> complexConditionList,long shopId,List<PromotionBrief> briefListPrevious);
	
	public List<AtomicSetting> getStepAtomicComplexSetting(List<AtomicSetting> settingList,long complexConditionId);
	
	public Integer checkChoiceByAtomicComplexConditionList(ShoppingCartCommand shopCart,List<AtomicCondition> complexConditionList,long shopId,List<PromotionBrief> briefListPrevious);
	
	public List<AtomicCondition> parseConditionByExpression(String expression);
	
	public AtomicCondition parseAtomicConditionByExpression(String AtomicExpression,String op);
	
	public List<AtomicCondition> manipulateShoppingCartByAtomicConditionList(ShoppingCartCommand shopCart, List<AtomicCondition> condList,long shopId,List<PromotionBrief> briefListPrevious);
	
	public Integer getResultShoppingCartByAtomicConditionList(List<AtomicCondition> conditionList);
	
	public boolean checkScopeCoupon(List<ShoppingCartLineCommand> shoppingCartLines,AtomicCondition condition, List<PromotionCouponCodeCommand> coupons,long shopId);
	
	public List<AtomicCondition> convertComplexConditionToAtomic(List<ConditionComplexCommand> complexList);

	public List<ItemFactor> getIntersectItemFactorList(List<ItemFactor> one,List<ItemFactor> another);
	
	public List<ItemFactor> getItemFactorListShoppingCartByAtomicConditions(ShoppingCartCommand shopCart,List<AtomicCondition> condList,long shopId,List<PromotionBrief> briefListPrevious);
	
	public List<ItemFactor> getItemFactorListShoppingCartByStepAtomicConditions(ShoppingCartCommand shopCart,List<AtomicCondition> condList,long shopId,List<PromotionBrief> briefListPrevious);
}
