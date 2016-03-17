package com.baozun.nebula.sdk.manager;


import java.math.BigDecimal;
import java.util.List;

import loxia.annotation.QueryParam;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;

public interface SdkSkuManager extends BaseManager{

	/**
	 * 通过skuId查询SKU
	 * @param skuId
	 * @return	:Sku
	 * @date 2014-2-17 下午08:15:56
	 */
	Sku findSkuById(Long skuId);
	
	/**
	 * 动态属性转换
	 * @param properties
	 * @return
	 */
	public List<SkuProperty> getSkuPros(String properties);
	
	/**
	 * 增加sku的库存
	 * @param extentionCode
	 * @param count
	 * @return
	 */
	void addSkuInventory(String extentionCode,Integer count);
	
	/**
	 * 通过outId查询sku集合(lifecycle=1)
	 * @param outIdList
	 * @return
	 */
	public List<Sku> findSkuByOutIds(List<String> outIdList);
	
	
	/**
	 * 同步商品价格(sku级别)
	 * @param salesPrice
	 * @param listPrice
	 * @param extentionCode
	 * @return
	 */
	Integer syncSkuPriceByExtentionCode(BigDecimal salesPrice, BigDecimal listPrice, String extentionCode);
	
	/**
	 * 同步商品的sku价格(item级别)
	 * @param salesPrice
	 * @param listPrice
	 * @param itemCode
	 * @return
	 */
	public Integer updateSkuPriceByItemCode(BigDecimal salesPrice, BigDecimal listPrice, String itemCode);
	
	/**
	 * 通过extentionCode查询sku信息
	 * @param extentionCode
	 * @return
	 */
	public Sku findSkuByExtentionCode(String extentionCode);
	
	/**
	 * 获取该尺寸下的虚拟库存
	 * @param extentionCode
	 * @return
	 */
	public SkuCommand findSkuQSVirtualInventoryById(Long skuId,String extCode);
}
