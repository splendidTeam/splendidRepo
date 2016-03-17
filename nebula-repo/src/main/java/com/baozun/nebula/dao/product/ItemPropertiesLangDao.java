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

import com.baozun.nebula.model.product.ItemPropertiesLang;

/**
 * 
* @Description: 商品属性国际化
* @author 何波
* @date 2014年12月18日 上午9:34:47 
*
 */
public interface ItemPropertiesLangDao extends GenericEntityDao<ItemPropertiesLang, Long> {
	
	/**
	 * 通过ItemPropertiesId和语言标识查询商品属性(国际化方法)
	 * @param itemPropertiesId
	 * @param langKey
	 * @return
	 */
	@NativeQuery(model = ItemPropertiesLang.class)
	ItemPropertiesLang findItemPropertiesLangByItemPropertiesIdAndLang(@QueryParam("itemPropertiesId") Long itemPropertiesId, @QueryParam("langKey") String langKey);

}
