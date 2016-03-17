/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.dao.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Sort;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.SkuCommand;

/**
 * skuDao
 * 
 * @author xingyu.liu
 * 
 */

public interface SkuDao extends GenericEntityDao<Sku, Long> {

	/**
	 * 根据itemid查询Sku
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = Sku.class)
	List<Sku> findSkuByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 根据itemids查询可用的Sku, lifecycle = 1
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = Sku.class)
	List<Sku> findEffectSkuByItemIds(@QueryParam("itemIds") List<Long> itemIds);

	/**
	 * 获取第一个满足库存的SKU
	 * 
	 * @param itemIds
	 * @param limitCount
	 * @return
	 */
	@NativeQuery(model = Sku.class)
	List<Sku> findEffectSkuTopOneByItemIdsWithinLimit(@QueryParam("shopId") Long shopId, @QueryParam("itemIds") List<Long> itemIds, @QueryParam("limitCount") Integer limitCount, @QueryParam("displayLimitCount") Integer displayLimitCount);

	/**
	 * 根据itemids查询Sku
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = Sku.class)
	List<Sku> findSkuByItemIds(@QueryParam("itemIds") List<Long> itemIds);
	
	/**
	 * 根据itemCodes查询Sku
	 * 
	 * @param itemCodes
	 * @return
	 */
	@NativeUpdate
	int deleteSkuByItemCodes(@QueryParam("itemCodes") List<String> itemCodes);

	/**
	 * 根据extentionCode查询Sku
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = Sku.class)
	Sku findSkuByExtentionCode(@QueryParam("extentionCode") String extentionCode);

	/**
	 * 根据extentionCode集合查询Sku集合
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = Sku.class)
	List<Sku> findSkuByExtentionCodes(@QueryParam("extentionCodes") List<String> extentionCodes);

	/**
	 * 根据itemid删除Sku
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeUpdate
	Integer deleteSkuByItemId(@QueryParam("itemId") Long itemId);

	@NativeQuery(model = Sku.class)
	Sku findSkuById(@QueryParam("id") Long id);

	/**
	 * @param itemId
	 * @param itemProperties
	 * @return
	 */
	@NativeQuery(model = SkuCommand.class)
	SkuCommand findInventory(@QueryParam Map<String, Object> paramMap);

	/**
	 * 通过itemId获取商品库存
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = SkuCommand.class)
	List<SkuCommand> findInventoryByItemId(@QueryParam("itemId") Long itemId);
	
	/**
	 * 通过itemId获取lifescyle为有效的sku的库存
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = SkuCommand.class)
	List<SkuCommand> findEffectiveSkuInvByItemId(@QueryParam("itemId") Long itemId);

	@NativeUpdate
	Integer deleteSkuBySkuIds(@QueryParam("skuIds") List<Long> skuIds);

	/**
	 * 通过商品id集合, 删除sku信息
	 * 
	 * @param itemIds
	 * @return
	 */
	@NativeUpdate
	Integer removeSkuByItemIds(@QueryParam("itemIds") List<Long> itemIds);

	/**
	 * 同步商品价格(sku级别)
	 * 
	 * @param salesPrice
	 * @param listPrice
	 * @param extentionCode
	 * @return
	 */
	@NativeUpdate
	Integer syncSkuPriceByExtentionCode(@QueryParam("salesPrice") BigDecimal salesPrice, @QueryParam("listPrice") BigDecimal listPrice, @QueryParam("extentionCode") String extentionCode);

	/**
	 * 同步商品的sku价格(item级别)
	 * 
	 * @param salesPrice
	 * @param listPrice
	 * @param itemCode
	 * @return
	 */
	@NativeUpdate
	Integer updateSkuPriceByItemCode(@QueryParam("salesPrice") BigDecimal salesPrice, @QueryParam("listPrice") BigDecimal listPrice, @QueryParam("itemCode") String itemCode);

	/**
	 * 查询在售的sku
	 * 
	 * @return
	 */
	@NativeQuery(model = Sku.class)
	List<Sku> findAllOnSalesSkuList();

	/**
	 * 分页查询在售的sku
	 * 
	 * @param page
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = Sku.class)
	List<Sku> findOnSalesItemListWithPage(Page page, Sort[] sorts);

	/**
	 * 根据skuId查询库存
	 * 
	 * @param skuId
	 * @return
	 */
	@NativeQuery(model = SkuCommand.class)
	SkuCommand findInventoryById(@QueryParam("skuId") Long skuId);
	
	
	/**
	 * 通过paraMap中的条件查询sku信息
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = Sku.class)
	List<Sku> findSkuWithParaMap(@QueryParam Map<String, Object> paraMap);
}
