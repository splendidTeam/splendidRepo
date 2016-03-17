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
package com.baozun.nebula.wormhole.scm.manager;

import java.util.List;

import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.wormhole.mq.entity.ProductPriceV5;
import com.baozun.nebula.wormhole.mq.entity.sku.SkuInfoV5;

/**
 * 同步商品Manager
 * 
 * @author chenguang.zhou
 * @date 2014年5月16日 下午3:59:26
 */
public interface ItemManager {

	/**
	 * 同步商品的基本修改
	 * @param skuInfoV5List
	 * @param msgReceive
	 */
	public void syncBaseInfo(List<SkuInfoV5> skuInfoV5List, MsgReceiveContent msgReceive);

	/**
	 * 同步商品价格(item级别)
	 * @param pdpriceV5List
	 * @param msgReceive
	 */
	public void syncItemPrice(List<ProductPriceV5> pdpriceV5List, MsgReceiveContent msgReceive);
	
}
