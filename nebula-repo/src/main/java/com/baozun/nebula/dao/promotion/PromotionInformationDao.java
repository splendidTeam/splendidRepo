/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.dao.promotion;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.promotion.PromotionInformation;

/**
 * @author: pengfei.fang
 * @Description: 促销信息DAO类
 * @date:2016年01月06日
 */
public interface PromotionInformationDao extends GenericEntityDao<PromotionInformation, Long>{

	/**
	 * 根据id更新
	 * 
	 * @param id
	 * @param categoryCode
	 * @param content
	 * @return
	 */
	@NativeUpdate
	Integer updatePromotionInfomationById(
			@QueryParam("id") Long id,
			@QueryParam("categoryCode") String categoryCode,
			@QueryParam("content") String content);

	/**
	 * 根据CategoryCode查询
	 * 
	 * @param categoryCode
	 * @return
	 */
	@NativeQuery(model = PromotionInformation.class)
	PromotionInformation findByCategoryCode(@QueryParam("categoryCode") String categoryCode);
}
