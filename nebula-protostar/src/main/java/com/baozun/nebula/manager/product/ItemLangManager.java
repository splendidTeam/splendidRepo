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
 */
package com.baozun.nebula.manager.product;

import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.command.product.BundleElementCommand;
import com.baozun.nebula.command.product.BundleSkuCommand;
import com.baozun.nebula.command.product.ItemInfoCommand;
import com.baozun.nebula.command.product.ItemPropertiesCommand;
import com.baozun.nebula.command.promotion.SkuPropertyMUtlLangCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Item;

/**
 * item 操作接口
 * 
 * @author yi.huang
 * @date 2013-7-1 下午04:08:27
 */
public interface ItemLangManager extends BaseManager {

	Item createOrUpdateItem(ItemInfoCommand itemCommand, Long[] propertyValueIds, Long[] categoriesIds,
			ItemPropertiesCommand[] iProperties, SkuPropertyMUtlLangCommand[] skuPropertyCommand) throws Exception;

	Item createOrUpdateBundleItem(ItemInfoCommand itemCommand, BundleCommand bundleCommand, Long[] categoriesIds)
					throws Exception;

	Integer validateItemCode(String code, Long shopId);

	void saveOrUpdateItemInfoLang(String title, String subTitle, String description, String sketch,
			String seoDescription, String seoKeyword, String seoTitle, String lang, Long itemInfoId);
}
