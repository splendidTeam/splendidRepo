/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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

import java.util.List;

import com.baozun.nebula.command.product.BundleSkuPriceCommand;
import com.baozun.nebula.model.bundle.BundleSku;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

/**
 * BundleSku Dao
 * 
 * @author yue.ch
 *
 */
public interface BundleSkuDao extends GenericEntityDao<BundleSku, Long> {
	
	@NativeQuery(model = BundleSku.class)
	List<BundleSku> findByBundleId(@QueryParam("bundleId")Long bundleId);
	
	@NativeQuery(model = BundleSku.class)
	List<BundleSku> findByBundleElementId(@QueryParam("bundleElementId")Long bundleElementId);
	
	@NativeQuery(model = BundleSkuPriceCommand.class)
	List<BundleSkuPriceCommand> getBundleSkusPrice(@QueryParam("bundleItemId")Long bundleItemId, @QueryParam("skuIds") Long[] skuIds);
	
	@NativeQuery(model = BundleSkuPriceCommand.class)
	BundleSkuPriceCommand getBundleSkuPrice(@QueryParam("bundleItemId")Long bundleItemId, @QueryParam("skuId") Long skuId);
	
	@NativeUpdate
	void deleteByBundleId(@QueryParam("bundleId") Long bundleId);
}
