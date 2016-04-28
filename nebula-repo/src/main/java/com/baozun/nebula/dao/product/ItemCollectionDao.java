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
package com.baozun.nebula.dao.product;

import com.baozun.nebula.model.product.ItemCollection;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

/**
 * IndustryDao
 * 
 * @author chen.kf
 */
public interface ItemCollectionDao extends GenericEntityDao<ItemCollection, Long>{
	
	@NativeQuery(model=ItemCollection.class)
	public ItemCollection findItemCollectionById(@QueryParam("id")Long id);

	/**
	 * 根据导航id查询商品指定排序
	 * 
	 * @return List<ItemCollection>
	 * @author 冯明雷
	 * @time 2016年4月27日下午2:22:09
	 */
	@NativeQuery(model = ItemCollection.class)
	ItemCollection findItemCollectionByNavigationId(@QueryParam("navigationId") Long navigationId);
}
