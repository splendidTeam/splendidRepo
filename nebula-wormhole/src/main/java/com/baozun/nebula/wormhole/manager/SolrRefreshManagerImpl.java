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
package com.baozun.nebula.wormhole.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.utilities.common.Validator;

/**
 * @author Tianlong.Zhang
 *
 */
@Service("solrRefreshManager")
@Transactional
public class SolrRefreshManagerImpl implements SolrRefreshManager{
	
	private static final Logger log = LoggerFactory.getLogger(SolrRefreshManager.class);
	
	@Autowired
	private SdkItemManager itemManager;
	
	@Autowired
	private ItemSolrManager itemSolrManager;
	
	@Override
	public void refresh() {
		log.info(" SolrRefreshManagerImpl refresh begin");
		List<Item> deletedItems = itemManager.findAllDeletedItemList();
		List<Item> onSaleItems = itemManager.findAllOnSalesItemList();
		List<Long> ids = null;
		if(Validator.isNotNullOrEmpty(deletedItems)){
			ids = new ArrayList<Long>(deletedItems.size());
			for(Item item : deletedItems){
				ids.add(item.getId());
			}
			itemSolrManager.deleteItem(ids);
			log.info(" SolrRefreshManagerImpl deleteItem end");
		}
		
		if(Validator.isNotNullOrEmpty(onSaleItems)){
			ids = new ArrayList<Long>(onSaleItems.size());
			for(Item item : onSaleItems){
				ids.add(item.getId());
			}
			
			boolean i18nOnOff = LangProperty.getI18nOnOff();
			if(i18nOnOff) {
				itemSolrManager.saveOrUpdateItemI18n(ids);
			} else {
				itemSolrManager.saveOrUpdateItem(ids);
			}
			log.info(" SolrRefreshManagerImpl saveOrUpdateItem end");
		}
		
		log.info(" SolrRefreshManagerImpl refresh end");
	}

}
