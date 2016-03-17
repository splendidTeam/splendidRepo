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

package com.baozun.nebula.manager.promotion;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Sort;

import com.baozun.nebula.command.promotion.PriorityCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * @author - 项硕
 */
public interface PromotionPriorityManager extends BaseManager {
	
	/**
	 * 按条件查询促销渠道列表
	 * @param page
	 * @param sorts
	 * @param queryMap
	 * @return
	 */
	public List<PriorityCommand> findPromotionPriorityChanelAdjustListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object>queryMap);
}
