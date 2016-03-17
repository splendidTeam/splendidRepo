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

package com.baozun.nebula.dao.promotion;
 
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.promotion.PromotionCouponCommand;
import com.baozun.nebula.command.promotion.PromotionCouponQueryCommand;
import com.baozun.nebula.model.promotion.PromotionCoupon;

/**
 * @author -  
 */
public interface PromotionCouponDao extends GenericEntityDao<PromotionCoupon, Long> {

	 
	 
	/**
	 * 获取所有优惠卷的类型
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCommand.class)
	List<PromotionCouponCommand> findAllCouponList();

	/**
	 * 根据ID查询优惠券
	 * @param id
	 * @return
	 */
	@NativeQuery(model = PromotionCouponCommand.class)
	PromotionCouponCommand findPromotionCouponCommandById(@QueryParam("id") Long id);
	
	/**
	 * 根据条件分页查询优惠劵列表
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PromotionCouponQueryCommand.class, pagable = true, withGroupby = true)
	public Pagination<PromotionCouponQueryCommand> queryCouponListByConditionallyWithPage(
			Page page, Sort[] sorts, @QueryParam("paraMap") Map<String, Object> paraMap);
}
