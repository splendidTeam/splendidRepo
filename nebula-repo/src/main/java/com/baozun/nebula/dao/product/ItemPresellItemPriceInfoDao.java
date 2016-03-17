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

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.PresellItemPriceInfo;

/**
 * 预售商品价格信息处理Dao
 * 
 * @author jinbao.ji
 * @date 2016年2月3日 上午11:28:37
 */
public interface ItemPresellItemPriceInfoDao extends GenericEntityDao<PresellItemPriceInfo, Long>{

	/**
	 * 根据itemId和extentionCode查询预售商品价格信息
	 * 
	 * @param itemId
	 * @param extentionCode
	 * @return
	 */
	@NativeQuery(model = PresellItemPriceInfo.class)
	PresellItemPriceInfo finditemPresellItemPriceInfoByitemIdAndExtentionCode(
			@QueryParam("itemId") Long itemId,
			@QueryParam("extentionCode") String extentionCode);

	/**
	 * 根据itemId获取该商品的预售SKU信息
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = PresellItemPriceInfo.class)
	List<PresellItemPriceInfo> findItemPresalseSkuInfoByItemId(@QueryParam("itemId") Long itemId);
}
