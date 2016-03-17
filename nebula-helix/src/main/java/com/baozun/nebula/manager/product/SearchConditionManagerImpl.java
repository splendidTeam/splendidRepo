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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;

/**
 * @author Tianlong.Zhang
 *
 */
@Transactional
@Service("searchConditionManager")
public class SearchConditionManagerImpl implements SearchConditionManager{
	
	@Autowired
	private SdkSearchConditionItemManager sdkSearchConditionItemManager;
	
	@Autowired
	private SdkSearchConditionManager sdkSearchConditionManager;

	@Override
	public List<SearchConditionItemCommand> findItemByPropertyValueId(
			Long pValueId) { 
		return sdkSearchConditionItemManager.findItemByPropertyValueId(pValueId);
	}

	@Override
	public List<SearchConditionCommand> findConditionByCategoryId(Long cid) {
		
		return sdkSearchConditionManager.findConditionByCategoryId(cid);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.SearchConditionManager#findItemBySId(java.lang.Long)
	 */
	@Override
	public List<SearchConditionItemCommand> findItemBySId(Long sId) {
		return sdkSearchConditionItemManager.findItemBySId(sId);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.SearchConditionManager#findItemById(java.lang.Long)
	 */
	@Override
	public SearchConditionItemCommand findItemById(Long id) {
		return sdkSearchConditionItemManager.findItemById(id);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.SearchConditionManager#findItemByPropertyId(java.lang.Long)
	 */
	@Override
	public List<SearchConditionItemCommand> findItemByPropertyId(Long propertyId) {
		return sdkSearchConditionItemManager.findItemByPropertyId(propertyId);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.SearchConditionManager#findConditionByCategoryIds(java.util.List)
	 */
	@Override
	public List<SearchConditionCommand> findConditionByCategoryIds(
			List<Long> cids) {
		return sdkSearchConditionManager.findConditionByCategoryIdList(cids);
	}

}
