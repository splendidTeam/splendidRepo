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
	 * 封装购物车行数据
	 * 
	 * @param shoppingCartLineCommand
	 */
	public void packShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand);


	
	/**
	 * 引擎检查, 不对赠品检查
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
	 * 检察
	 * 1, iteminfo.type != 0
	 * 1, sku.lifecycle != 1 
	 * 2, active begin time 是否大于 now() 
	 * 3, chck inventory. 
	 * 4, 检查限购
	 * 
	 * 常规商品的有效性库存时, 用到line中的stock, 
	 * 1, 主买品line中的stock是商品的库存数
	 * 2, 赠品line中的stock是商品的库存数-主买品的qty
	 * 
	 */
	public Integer doEngineCheck(ShoppingCartLineCommand line, boolean flag, ShoppingCartCommand cart, List<LimitCommand> purchaseLimitationList);
	
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
	 * 检察
	 * 1, sku.lifecycle != 1 
	 * 2, active begin time 是否大于 now() 
	 * 3, chck inventory. 
	 */
	public Integer doEngineGiftCheck(ShoppingCartLineCommand line, boolean flag, ShoppingCartCommand cart, List<LimitCommand> purchaseLimitationList);
	
	/**
	 * 限购检察(整个购物车)
	 * @param cart
	 * @param purchaseLimitationList
	 * @return
	 */
	public List<ShoppingCartLineCommand> doEngineCheckLimit(ShoppingCartCommand cart, List<LimitCommand> purchaseLimitationList);
	
	/**
	 * 根据会员ID，会员分组，获取会员组合的Id
	 * 
	 * @param memberId
	 * @param memGroupId
	 * @return
	 */
	public Set<String> getCrowdScopeListByMemberAndGroup(Long memberId, List<Long> memGroupId);

	/**
	 * 根据商品ID，商品分组，获取商品组合的Id
	 * 
	 * @param itemId
	 * @param categoryLists
	 * @return
	 */
	public Set<String> getItemScopeListByItemAndCategory(String itemId, List<ItemCategory> categoryLists);
}
