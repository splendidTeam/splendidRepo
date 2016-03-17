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
package com.baozun.nebula.sdk.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.sdk.manager.SdkCategoryManager;

/**
 * 商品分类sdkManager实现类
 * 
 * @author chenguang.zhou
 * @date 2014年6月13日 下午2:52:37
 */
@Service("sdkCategoryManager")
@Transactional
public class SdkCategoryManagerImpl implements SdkCategoryManager{

	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private ItemCategoryDao itemCategoryDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Category> findCategoryListByCodes(List<String> codes) {
		return categoryDao.findCategoryListByCodes(codes);
	}

	@Override
	public ItemCategory saveItemCategory(ItemCategory itemCategory) {
		return itemCategoryDao.save(itemCategory);
	}

}
