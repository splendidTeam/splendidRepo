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

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;

/**
 * @author Tianlong.Zhang
 *
 */
public interface SearchConditionManager extends BaseManager{
	public List<SearchConditionItemCommand> findItemByPropertyValueId(Long pValueId);
	
	public List<SearchConditionCommand> findConditionByCategoryId(Long cid);
	
	public List<SearchConditionCommand> findConditionByCategoryIds(List<Long> cids);
	
	/**
	 * 根据 searchConditionId 查找对应的 SearchConditionItem
	 * @param sId
	 * @return
	 */
	public List<SearchConditionItemCommand> findItemBySId(Long sId);
	
	/**
	 * 根据 propertyId 查找对应的 SearchConditionItemCommand
	 * @param propertyId
	 * @return
	 */
	public List<SearchConditionItemCommand> findItemByPropertyId(Long propertyId);
	
	public SearchConditionItemCommand findItemById(Long id);
}
