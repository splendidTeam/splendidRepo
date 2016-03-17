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
 */
package com.baozun.nebula.wormhole.scm.handler;

import java.util.List;

import com.baozun.nebula.wormhole.mq.entity.ProductPriceV5;
import com.baozun.nebula.wormhole.mq.entity.sku.SkuInfoV5;

/**
 * 同步商品信息相关的handler
 * 
 * @author chenguang.zhou
 * @date 2014年5月9日 下午1:45:09
 */
public interface SyncItemHandler extends HandlerBase{

	/**
	 * 同步商品基础信息
	 * scm推送商品信息到商城
	 */
	public void syncBaseInfo(List<SkuInfoV5> skuInfoV5List);
	
	/**
	 * 同步商品价格
	 * SCM推送商品价格到商城
	 */
	public List<ProductPriceV5> syncItemPrice(List<ProductPriceV5> pdPriceV5List);
}
