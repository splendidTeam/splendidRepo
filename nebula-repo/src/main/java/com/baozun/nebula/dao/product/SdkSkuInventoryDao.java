/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
 *
 */
package com.baozun.nebula.dao.product;

import java.util.Date;
import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.SkuInventory;

/**
 * @author Tianlong.Zhang
 * 
 */
public interface SdkSkuInventoryDao extends GenericEntityDao<SkuInventory, Long> {

	/**
	 * 根据extentionCode查询Sku
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = SkuInventory.class)
	SkuInventory findSkuInventoryByExtentionCode(@QueryParam("extentionCode") String extentionCode);

	/**
	 * 减少库存
	 * 
	 * @param extentionCode
	 * @param count
	 *            减少的
	 * @return
	 */
	@NativeUpdate
	public Integer liquidateSkuInventory(@QueryParam("extentionCode") String extentionCode, @QueryParam("count") Integer count);

	/**
	 * 根据extentionCode集合查询Sku库存
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = SkuInventory.class)
	List<SkuInventory> findSkuInventoryByExtentionCodes(@QueryParam("extentionCodeList") List<String> extentionCodeList);

	/**
	 * 增加库存
	 * 
	 * @param extentionCode
	 * @param count
	 *            增加的
	 * @return
	 */
	@NativeUpdate
	public Integer addSkuInventory(@QueryParam("extentionCode") String extentionCode, @QueryParam("count") Integer count);

	/**
	 * 保存库存
	 * 
	 * @author jumbo
	 * 
	 */
	@NativeUpdate
	public void insertSkuInventory(SkuInventory skuInventory);

	/**
	 * @param skuInv
	 */
	@NativeUpdate
	void updateInventoryById(@QueryParam("id") Long id, @QueryParam("availableQty") Integer availableQty, @QueryParam("lastSyncTime") Date lastSyncTime, @QueryParam("baselineTime") Date baselineTime);

	/**
	 * @param id
	 * @param availableQty
	 * @param lastSyncTime
	 */
	@NativeUpdate
	void addSkuInventoryById(@QueryParam("id") Long id, @QueryParam("availableQty") Integer availableQty, @QueryParam("lastSyncTime") Date lastSyncTime);

}
