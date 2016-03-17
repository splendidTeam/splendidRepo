package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.manager.BaseManager;

/**
 * 有效性检查
 * @author 阳羽
 * @createtime 2014-4-4 下午06:12:52
 */
public interface SdkEffectiveManager extends BaseManager{
	
	/**
	 * 检查库存
	 * @param extentionCode 
	 * @param qty 该sku购买商品的总数量
	 */
	public boolean chckInventory(String extentionCode,Integer qty);
	
	/**
	 * 检查商品是否有效
	 * @param valid true有效 false无效
	 * @return 
	 */
	public Integer checkItemIsValid(boolean valid);
	
	
}
