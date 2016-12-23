/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.product;

import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.SkuInventoryChangeLogCommand;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年6月6日 上午10:04:26 
 * @version   
 */
public interface SkuInventoryChangeLogManager {
	/**
	 * 分页获取SkuInventoryChangeLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<SkuInventoryChangeLogCommand> findSkuInventoryChangeLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
}
