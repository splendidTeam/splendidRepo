package com.baozun.nebula.sdk.manager.shoppingcart;

import java.util.List;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionConditionSKU;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

public interface SdkShoppingCartGroupManager extends BaseManager{

    /**
     * 根据促销引擎计算的结果，把原购物车行数据重新分组。
     * 添加Caption行，按lineGroup分组排列好相应的行。
     * jsp负责显示。
     */
    List<ShoppingCartLineCommand> groupShoppingCartLinesToDisplayByLinePromotionResult(
                    ShoppingCartCommand oneShoppingCart,
                    List<PromotionBrief> promotionBriefList);

    /**
     * 根据促销Id列表，获取活动的条件行SKU List
     * 
     * @param shoppingLines
     * @param promotionIdList
     * @return
     */
    List<PromotionConditionSKU> getPromotionConditionSKUListFromShoppingCartLines(
                    List<ShoppingCartLineCommand> shoppingLines,
                    List<Long> promotionIdList);

    /**
     * 根据SKU ID获取，优惠行。
     * 包括行上减金额，赠品行，套餐行
     * 
     * @param skuListSetting
     * @param skuId
     * @return
     */
    List<PromotionCommand> getPromotionListBySKU(List<PromotionSKUDiscAMTBySetting> skuListSetting,Long skuId);

    /**
     * 根据活动ID获取，条件行。
     * 
     * @param listSKUCondition
     * @param promotionId
     * @return
     */
    List<PromotionConditionSKU> getConditionSKUListByPromotionId(List<PromotionConditionSKU> listSKUCondition,Long promotionId);

    /**
     * 根据活动ID获取，优惠行。
     * 包括行上减金额，赠品行，套餐行
     * 
     * @param skuListSetting
     * @param promotionId
     * @return
     */
    List<PromotionSKUDiscAMTBySetting> getSettingSKUListByPromotionId(List<PromotionSKUDiscAMTBySetting> skuListSetting,Long promotionId);

    /**
     * 检查一个活动是否有赠品行优惠
     * 
     * @param onePromotionSettingSKUList
     * @param promotion
     * @return
     */
    Boolean checkPromotionHasLineGift(List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList,PromotionCommand onePromotion);

    /**
     * 获取行赠品行，包括参与行
     * 显示后排除这些行，待后续优惠显示
     * 
     * @param oneShopCartLineList
     * @param lineGiftPromotionSettingSKUList
     * @param lineGiftPromotionConditionSKUList
     * @return
     */
    List<ShoppingCartLineCommand> getLinesOfLineGiftPromotion(
                    List<ShoppingCartLineCommand> oneShopCartLineList,
                    List<PromotionSKUDiscAMTBySetting> lineGiftPromotionSettingSKUList,
                    List<PromotionConditionSKU> lineGiftPromotionConditionSKUList);

    /**
     * 获取整单赠品行
     * 显示后排除这些行，待后续优惠显示
     * 
     * @param oneShopCartLineList
     * @param onePromotionSettingSKUList
     * @return
     */
    Boolean checkPromotionOrderGift(List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList,PromotionCommand onePromotion);

    List<ShoppingCartLineCommand> getLinesOfOrderGiftPromotion(
                    List<ShoppingCartLineCommand> oneShopCartLineList,
                    List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList);

    /**
     * 检查一个活动是否有套餐行优惠
     * 
     * @param onePromotionSettingSKUList
     * @param promotion
     * @return
     */
    Boolean checkPromotionHasSuitKits(PromotionCommand promotion);

    /**
     * 获取有套餐活动的所有的行，包括主选商品
     * 显示后排除这些行，待后续优惠显示
     * 
     * @param oneShopCartLineList
     * @param kitPromotionConditionSKUList
     * @param kitPromotionSettingSKUList
     * @return
     */
    List<ShoppingCartLineCommand> getLinesOfSuitKitsPromotion(
                    List<ShoppingCartLineCommand> oneShopCartLineList,
                    List<PromotionSKUDiscAMTBySetting> kitPromotionSettingSKUList,
                    List<PromotionConditionSKU> kitPromotionConditionSKUList);

    /**
     * 获取非套餐、非行赠品类型优惠，购物车行
     * 显示后排除这些行，待无优惠行显示
     * 
     * @param oneShopCartLineList
     * @param onePromotionConditionSKUList
     * @param onePromotionSettingSKUList
     * @return
     */
    List<ShoppingCartLineCommand> getLinesOfNormalPromotion(
                    List<ShoppingCartLineCommand> oneShopCartLineList,
                    List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList,
                    List<PromotionConditionSKU> onePromotionConditionSKUList);
}
