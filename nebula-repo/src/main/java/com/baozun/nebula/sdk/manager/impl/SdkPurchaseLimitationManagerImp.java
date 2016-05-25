package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.param.PurchaseLimitConditionType;
import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitationManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;

/*
 * 购物车中，添加商品时的限购功能
 * 根据skuId和QTY，检查限购规则，通过检查的才生成订单行，
 * 否则提示用户不能超过购买数量提示
 */
@Transactional
@Service("SdkPurchaseLimitationManager")
public class SdkPurchaseLimitationManagerImp implements SdkPurchaseLimitationManager{

    @Autowired
    private SdkShoppingCartManager sdkShoppingCartManager;

    /*
     * 检查过程。变更前，检查当前SKU下的QTY，检查对象当前购物车或者历史订单中。
     * 注意：当检查历史订单的数据时，其实是包含了当前的购物车和历史数据的。
     * 获取当前用户购买限制，检查每个限制是否针对当前SKU的商品。一个item可能有多个限购，优先级
     */
    @Override
    public List<ShoppingCartLineCommand> checkSKUPurchaseByLimitaionList(List<LimitCommand> limitationList,ShoppingCartCommand shopCart){
        List<ShoppingCartLineCommand> errorLineList = null;
        // 获取引擎解析结果
        for (LimitCommand one : limitationList){
            errorLineList = checkSKUPurchaseByLimitaion(one, shopCart);
            if (null == errorLineList || errorLineList.size() <= 0){
                continue;
            }else{
                return errorLineList;
            }
        }
        return null;
    }

    /*
     * 检查一个限购
     */
    public List<ShoppingCartLineCommand> checkSKUPurchaseByLimitaion(LimitCommand oneLimitation,ShoppingCartCommand shopCart){
        List<AtomicCondition> atomicListOneLimitation = new ArrayList<AtomicCondition>();
        atomicListOneLimitation = oneLimitation.getAtomicLimitationList();
        return checkSKUPurchaseByAtomicLimitaion(atomicListOneLimitation, shopCart);
    }

    /*
     * 根据一个限购中，多个条件，检查是否违反限购
     */
    public List<ShoppingCartLineCommand> checkSKUPurchaseByAtomicLimitaion(
                    List<AtomicCondition> atomicListOneLimitation,
                    ShoppingCartCommand shopCart){
        List<ShoppingCartLineCommand> errorLineList = null;
        for (AtomicCondition atomic : atomicListOneLimitation){
            errorLineList = checkSKUPurchaseByAtomicLimitaion(atomic, shopCart);
            if (null == errorLineList || errorLineList.size() <= 0)
                continue;
            else
                return errorLineList;
        }
        return null;
    }

    /*
     * 根据一个限购中，多个条件，检查是否违反限购。根据6个维度统计，判断之
     */
    public List<ShoppingCartLineCommand> checkSKUPurchaseByAtomicLimitaion(AtomicCondition condition,ShoppingCartCommand shopCart){
        Integer qtyLimited = 0;
        long scopeValue = 0L;
        // 超过限制数量的购物车行的集合
        List<ShoppingCartLineCommand> errorLineList = null;

        qtyLimited = condition.getConditionValue().intValue();
        scopeValue = condition.getScopeValue();

        if (condition.getConditionTag().equalsIgnoreCase(PurchaseLimitConditionType.EXP_ORDSKUQTY)){// 订单内单款件数
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
                errorLineList = sdkShoppingCartManager.getOrderSKUQtyByItemId(shopCart, scopeValue, qtyLimited);
            }

            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
                errorLineList = sdkShoppingCartManager.getOrderSKUQtyByCategoryId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
                errorLineList = sdkShoppingCartManager.getOrderSKUQtyByCustomId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
                errorLineList = sdkShoppingCartManager.getOrderSKUQtyByComboId(shopCart, scopeValue, qtyLimited);
            }
        }
        if (condition.getConditionTag().equalsIgnoreCase(PurchaseLimitConditionType.EXP_ORDITEMQTY)){ // 订单内商品样数
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
                errorLineList = sdkShoppingCartManager.getOrderItemQtyByItemId(shopCart, scopeValue, qtyLimited);
            }

            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
                errorLineList = sdkShoppingCartManager.getOrderItemQtyByCategoryId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
                errorLineList = sdkShoppingCartManager.getOrderItemQtyByCustomId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
                errorLineList = sdkShoppingCartManager.getOrderItemQtyByComboId(shopCart, scopeValue, qtyLimited);
            }
        }
        if (condition.getConditionTag().equalsIgnoreCase(PurchaseLimitConditionType.EXP_ORDQTY)){ // 订单内件数
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
                errorLineList = sdkShoppingCartManager.getOrderQtyByItemId(shopCart, scopeValue, qtyLimited);
            }

            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
                errorLineList = sdkShoppingCartManager.getOrderQtyByCategoryId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
                errorLineList = sdkShoppingCartManager.getOrderQtyByCustomId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
                errorLineList = sdkShoppingCartManager.getOrderQtyByComboId(shopCart, scopeValue, qtyLimited);
            }
        }
        // 注意：当检查历史订单的数据时，其实是包含了当前的购物车和历史数据的。
        if (condition.getConditionTag().equalsIgnoreCase(PurchaseLimitConditionType.EXP_HISORDSKUQTY)){ // 历史购买单款件数
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderSKUQtyByItemId(shopCart, scopeValue, qtyLimited);
            }

            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderSKUQtyByCategoryId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderSKUQtyByCustomId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderSKUQtyByComboId(shopCart, scopeValue, qtyLimited);
            }
        }
        if (condition.getConditionTag().equalsIgnoreCase(PurchaseLimitConditionType.EXP_HISORDITEMQTY)){ // 历史购买商品样数(一个item是一样)
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderItemQtyByItemId(shopCart, scopeValue, qtyLimited);
            }

            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderItemQtyByCategoryId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderItemQtyByCustomId(shopCart, scopeValue, qtyLimited);
            }

            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderItemQtyByComboId(shopCart, scopeValue, qtyLimited);
            }
        }
        if (condition.getConditionTag().equalsIgnoreCase(PurchaseLimitConditionType.EXP_HISORDQTY)){ // 历史购买订单数
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderQtyByItemId(shopCart, scopeValue, qtyLimited);
            }

            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderQtyByCategoryId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderQtyByCustomId(shopCart, scopeValue, qtyLimited);
            }
            if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
                errorLineList = sdkShoppingCartManager.getHistoryOrderQtyByComboId(shopCart, scopeValue, qtyLimited);
            }
        }
        return errorLineList;
    }
}
