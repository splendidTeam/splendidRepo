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
 *
 */
package com.baozun.nebula.wormhole.scm.manager;

import java.util.List;

import com.baozun.nebula.wormhole.mq.entity.SkuInventoryV5;

/**
 * @author yfxie
 *
 */
public interface InventoryManager {
	
	/**
	 * @deprecated
	 * 
	 * 处理全量同步
	 * 
	 * @param siList
	 * @param msgIdList
	 */
	public void syncFullInventory(List<SkuInventoryV5> siList,List<Long> msgIdList);
	
	/**
	 * 处理增量同步
	 * 
	 * @param siList
	 * @param msgIdList
	 */
	public void syncIncrementInventory(List<SkuInventoryV5> siList,List<Long> msgIdList);

}
