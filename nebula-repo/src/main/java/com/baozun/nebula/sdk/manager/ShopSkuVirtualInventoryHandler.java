package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.handler.HandlerBase;
/**
 * 获取QS虚拟库存在PDP以及购物车起到限流作用
 * @author jumbo
 *
 */
public interface ShopSkuVirtualInventoryHandler extends HandlerBase{
	/**
	 * 获取虚拟库存
	 * 1，检查虚拟库存开关，是否是开着的（从Redis中去取）;
	 * 2，获取虚拟库存，从Redis的计数器中去取，如果没有从数据库查一遍后放到计数器中;
	 * 3，库存扣去，使用cacheManager中的Decr(key,avt);
	 * @param skuId
	 * @return
	 */
	public SkuCommand findSkuQSVirtualInventoryById(Long skuId,String extCode);
	
	/**
	 * 扣去虚拟库存
	 * @param skuId
	 * @param skuQtyOccupy
	 * @return
	 */
	public long decreaseSkuQSVirtualInventoryById(Long skuId,String extCode,int skuQtyOccupy);
	/**
	 * 增加虚拟库存
	 * @param skuId
	 * @param skuQty
	 * @return
	 */	
	public long increaseSkuQSVirtualInventoryById(Long skuId,String extCode,int skuQty);
}
