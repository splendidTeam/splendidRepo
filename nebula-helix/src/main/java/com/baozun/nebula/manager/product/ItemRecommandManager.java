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
package com.baozun.nebula.manager.product;

import java.util.List;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.RecommandItem;

/**
 * @author Tianlong.Zhang
 *
 */
public interface ItemRecommandManager extends BaseManager{
	
	/**
	 * 获取推荐的item列表
	 * @param type
	 * @param param
	 * @return
	 */
	public List<ItemCommand> getRecommandItemByTypeAndParam(Integer type, Long param, String imgType);
	
	public List<ItemCommand> getRecommandItemByItemIds(List<Long> itemIdList, String type);
	
	/**
	 * 通过商品Id查询该商品搭配的商品
	 * @param itemId
	 * @return
	 */
	public List<ItemCommand> getRecommandItemByItemId(Long itemId, String type);

	/**
	 * 根据类型和参数查询推荐列表
	 * 
	 * @param type
	 * @param param
	 */
	public List<RecommandItem> findRecommandItemByTypeAndParam(Integer type, Long param);
}
