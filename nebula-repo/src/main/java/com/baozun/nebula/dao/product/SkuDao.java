/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
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
 * skuDao.
 *
 * @author xingyu.liu
 */
public interface SkuDao extends GenericEntityDao<Sku, Long>{

    /**
     * Find sku by id.
     *
     * @param id
     *            the id
     * @return the sku
     */
    @NativeQuery(model = Sku.class)
    Sku findSkuById(@QueryParam("id") Long id);

    /**
     * 根据skuId查询库存.
     *
     * @param skuId
     *            the sku id
     * @return the sku command
     */
    @NativeQuery(model = SkuCommand.class)
    SkuCommand findInventoryById(@QueryParam("skuId") Long skuId);

    /**
     * 根据extentionCode查询Sku.
     *
     * @param extentionCode
     *            the extention code
     * @return the sku
     */
    @NativeQuery(model = Sku.class)
    Sku findSkuByExtentionCode(@QueryParam("extentionCode") String extentionCode);

    /**
     * 根据itemid查询Sku.
     *
     * @param itemId
     *            the item id
     * @return the list< sku>
     */
    @NativeQuery(model = Sku.class)
    List<Sku> findSkuByItemId(@QueryParam("itemId") Long itemId);

    /**
     * 根据itemids查询可用的Sku, lifecycle = 1.
     *
     * @param itemIds
     *            the item ids
     * @return the list< sku>
     */
    @NativeQuery(model = Sku.class)
    List<Sku> findEffectSkuByItemIds(@QueryParam("itemIds") List<Long> itemIds);

    /**
     * 获取第一个满足库存的SKU.
     *
     * @param shopId
     *            the shop id
     * @param itemIds
     *            the item ids
     * @param limitCount
     *            the limit count
     * @param displayLimitCount
     *            the display limit count
     * @return the list< sku>
     */
    @NativeQuery(model = Sku.class)
    List<Sku> findEffectSkuTopOneByItemIdsWithinLimit(
                    @QueryParam("shopId") Long shopId,
                    @QueryParam("itemIds") List<Long> itemIds,
                    @QueryParam("limitCount") Integer limitCount,
                    @QueryParam("displayLimitCount") Integer displayLimitCount);

    /**
     * 根据itemids查询Sku.
     *
     * @param itemIds
     *            the item ids
     * @return the list< sku>
     */
    @NativeQuery(model = Sku.class)
    List<Sku> findSkuByItemIds(@QueryParam("itemIds") List<Long> itemIds);

    /**
     * 根据itemCodes查询Sku.
     *
     * @param itemCodes
     *            the item codes
     * @return the int
     */
    @NativeUpdate
    int deleteSkuByItemCodes(@QueryParam("itemCodes") List<String> itemCodes);

    /**
     * 根据extentionCode集合查询Sku集合.
     *
     * @param extentionCodes
     *            the extention codes
     * @return the list< sku>
     */
    @NativeQuery(model = Sku.class)
    List<Sku> findSkuByExtentionCodes(@QueryParam("extentionCodes") List<String> extentionCodes);

    /**
     * 根据 skuids 查询 lifecycle=1 sku list.
     *
     * @param skuIds
     *            the sku ids
     * @return the list< sku>
     * @since 5.3.1
     */
    @NativeQuery(model = Sku.class)
    List<Sku> findEnableSkuListBySkuIds(@QueryParam("skuIds") Long[] skuIds);

    /**
     * 根据itemid删除Sku.
     *
     * @param itemId
     *            the item id
     * @return the integer
     */
    @NativeUpdate
    Integer deleteSkuByItemId(@QueryParam("itemId") Long itemId);

    /**
     * Find inventory.
     *
     * @param paramMap
     *            the param map
     * @return the sku command
     */
    @NativeQuery(model = SkuCommand.class)
    SkuCommand findInventory(@QueryParam Map<String, Object> paramMap);

    /**
     * 通过itemId获取商品库存.
     *
     * @param itemId
     *            the item id
     * @return the list< sku command>
     */
    @NativeQuery(model = SkuCommand.class)
    List<SkuCommand> findInventoryByItemId(@QueryParam("itemId") Long itemId);

    /**
     * 通过itemId获取lifescyle为有效的sku的库存.
     *
     * @param itemId
     *            the item id
     * @return the list< sku command>
     */
    @NativeQuery(model = SkuCommand.class)
    List<SkuCommand> findEffectiveSkuInvByItemId(@QueryParam("itemId") Long itemId);

    /**
     * Delete sku by sku ids.
     *
     * @param skuIds
     *            the sku ids
     * @return the integer
     */
    @NativeUpdate
    Integer deleteSkuBySkuIds(@QueryParam("skuIds") List<Long> skuIds);

    /**
     * 通过商品id集合, 删除sku信息.
     *
     * @param itemIds
     *            the item ids
     * @return the integer
     */
    @NativeUpdate
    Integer removeSkuByItemIds(@QueryParam("itemIds") List<Long> itemIds);

    /**
     * 同步商品价格(sku级别).
     *
     * @param salesPrice
     *            the sales price
     * @param listPrice
     *            the list price
     * @param extentionCode
     *            the extention code
     * @return the integer
     */
    @NativeUpdate
    Integer syncSkuPriceByExtentionCode(
                    @QueryParam("salesPrice") BigDecimal salesPrice,
                    @QueryParam("listPrice") BigDecimal listPrice,
                    @QueryParam("extentionCode") String extentionCode);

    /**
     * 同步商品的sku价格(item级别).
     *
     * @param salesPrice
     *            the sales price
     * @param listPrice
     *            the list price
     * @param itemCode
     *            the item code
     * @return the integer
     */
    @NativeUpdate
    Integer updateSkuPriceByItemCode(
                    @QueryParam("salesPrice") BigDecimal salesPrice,
                    @QueryParam("listPrice") BigDecimal listPrice,
                    @QueryParam("itemCode") String itemCode);

    /**
     * 查询在售的sku.
     *
     * @return the list< sku>
     */
    @NativeQuery(model = Sku.class)
    List<Sku> findAllOnSalesSkuList();

    /**
     * 分页查询在售的sku.
     *
     * @param page
     *            the page
     * @param sorts
     *            the sorts
     * @return the list< sku>
     */
    @NativeQuery(model = Sku.class)
    List<Sku> findOnSalesItemListWithPage(Page page,Sort[] sorts);

    /**
     * 通过paraMap中的条件查询sku信息.
     *
     * @param paraMap
     *            the para map
     * @return the list< sku>
     */
    @NativeQuery(model = Sku.class)
    List<Sku> findSkuWithParaMap(@QueryParam Map<String, Object> paraMap);
    
    /**
	 * 根据itemids查询有效的sku列表 lifecycle !=2
	 * @param itemIds
	 * @return
	 */
    @NativeQuery(model = Sku.class)
	List<Sku> findAllOnSalesSkuListByItemIds(@QueryParam("itemIds") List<Long> itemIds);
}
