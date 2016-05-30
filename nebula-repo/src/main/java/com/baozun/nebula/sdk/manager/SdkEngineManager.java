package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Set;

import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

public interface SdkEngineManager extends BaseManager{

    /**
     * 创建订单的引擎检查.
     * 
     * @param memberId
     * @param memCombos
     * @param shoppingCartCommand
     * @since 5.3.1
     */
    void createOrderDoEngineChck(Long memberId,Set<String> memCombos,ShoppingCartCommand shoppingCartCommand);

    /**
     * 引擎检查, 不对赠品检查
     * 
     * @param flag
     *            添加或减少
     * @param skuId
     * @param extentionCode
     * @param qty
     *            数量
     * @since 5.3.1
     */
    Integer doEngineCheck(ShoppingCartLineCommand line,boolean flag);

    /**
     * 引擎检查, 只对赠品而言
     * 
     * @param skuId
     * @param extentionCode
     * @param qty
     *            数量
     * @param flag
     *            添加或减少
     * @param cart
     *            购物车
     * @param purchaseLimitationList
     *            限购规则
     *            检察
     *            1, sku.lifecycle != 1
     *            2, active begin time 是否大于 now()
     *            3, chck inventory.
     */
    Integer doEngineGiftCheck(ShoppingCartLineCommand line,boolean flag,ShoppingCartCommand cart,List<LimitCommand> purchaseLimitationList);

    /**
     * 限购检察(整个购物车)
     * 
     * @param cart
     * @param purchaseLimitationList
     * @return
     */
    List<ShoppingCartLineCommand> doEngineCheckLimit(ShoppingCartCommand cart,List<LimitCommand> purchaseLimitationList);

    /**
     * 根据会员ID，会员分组，获取会员组合的Id
     * 
     * @param memberId
     * @param memGroupId
     * @return
     */
    Set<String> getCrowdScopeListByMemberAndGroup(Long memberId,List<Long> memGroupId);

    /**
     * 根据商品ID，商品分组，获取商品组合的Id
     * 
     * @param itemId
     * @param categoryLists
     * @return
     */
    Set<String> getItemScopeListByItemAndCategory(Long itemId,List<ItemCategory> categoryLists);
}
