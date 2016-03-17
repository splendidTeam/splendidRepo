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
package com.baozun.nebula.dao.product;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.ItemInfoLang;

/**
 * 商品国际化信息
 * 
 */
public interface ItemInfoLangDao extends GenericEntityDao<ItemInfoLang, Long> {


	/**
	 * 通过语言标识和itemInfoId查询商品信息
	 * @param itemInfoId
	 * @param lang
	 * @return
	 */
	@NativeQuery(model = ItemInfoLang.class)
	ItemInfoLang findItemInfoLangByItemInfoIdAndLang(@QueryParam("itemInfoId") Long itemInfoId, @QueryParam("lang") String lang);

}
