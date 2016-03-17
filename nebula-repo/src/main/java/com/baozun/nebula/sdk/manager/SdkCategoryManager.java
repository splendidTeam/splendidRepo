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
package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemCategory;

/**
 * 商品分类sdkManager
 * 
 * @author chenguang.zhou
 * @date 2014年6月13日 下午2:52:05
 */
public interface SdkCategoryManager extends BaseManager{

	/**
	 * 通过商品分类code查询商品分类 
	 * @param codes
	 * @return
	 */
	public List<Category> findCategoryListByCodes(List<String> codes);
	
	
	/**
	 * 保存商品分类
	 * @param itemCategory
	 * @return
	 */
	public ItemCategory saveItemCategory(ItemCategory itemCategory);
}
