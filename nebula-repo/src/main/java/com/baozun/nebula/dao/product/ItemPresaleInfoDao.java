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

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.PresellItem;
import com.baozun.nebula.model.product.PresellItemPriceInfo;

/**
 * 商品预售处理Dao
 * 
 * @author jinbao.ji
 * @date 2016年2月3日 上午11:28:37
 */
public interface ItemPresaleInfoDao extends GenericEntityDao<PresellItem, Long>{

	/**
	 * 根据itemId获取该商品具有的属性值(如白色,32G,联通版),以属性值排序号升序
	 * 
	 * @param itemId
	 *            map(key:itempropertyId,value:propertyvalueName)
	 */
	@NativeQuery(alias = { "itempropertyId", "propertyvalueName" },clazzes = { String.class, String.class })
	List<Map<String, String>> findpropertvaluenameByitempropertyIds(@QueryParam("itemId") Long itemId);

	/**
	 * 根据itemId获取该商品的预售基本信息
	 * 
	 * @param itemCode
	 * @return
	 */
	@NativeQuery(model = PresellItem.class)
	PresellItem findPresaleInfoByitemId(@QueryParam("itemId") Long itemId);

	

}
