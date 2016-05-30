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
package com.baozun.nebula.sdk.manager.product;

import java.util.List;

import com.baozun.nebula.command.product.BundleSkuPriceCommand;

/**
 * 捆绑类商品接口
 * 
 * @author yue.ch
 * @time 2016年5月30日 上午11:28:10
 */
public interface SdkBundleManager {

	/**
	 * 获取bundle中某个sku的价格
	 * 
	 * @param bundleItemId bundle的itemId
	 * @param skuId bundle中的某个sku
	 * @return
	 */
	public BundleSkuPriceCommand getBundleSkuPrice(Long bundleItemId, Long skuId);
	
	/**
	 * 获取bundle中某些sku的价格
	 * 
	 * @param bundleItemId bundle的itemId
	 * @param skuIds bundle中选中的一组sku
	 * @return
	 */
	public List<BundleSkuPriceCommand> getBundleSkusPrice(Long bundleItemId, List<Long> skuIds);
}
