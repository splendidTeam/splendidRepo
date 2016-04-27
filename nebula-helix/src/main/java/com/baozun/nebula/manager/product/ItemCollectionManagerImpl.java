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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.product.ItemCollection;
import com.baozun.nebula.sdk.manager.SdkItemCollectionManager;

@Transactional
@Service("itemCollectionManager")
public class ItemCollectionManagerImpl implements ItemCollectionManager{

	private static final Logger	LOG	= LoggerFactory.getLogger(ItemCollectionManagerImpl.class);

	@Autowired
	private SdkItemCollectionManager	sdkItemCollectionManager;

	@Transactional(readOnly = true)
	@Override
	public ItemCollection findItemCollectionByNavigationId(Long navigationId){
		return sdkItemCollectionManager.findItemCollectionByNavigationId(navigationId);
	}

}
