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

package com.baozun.nebula.manager.promotion;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.web.multipart.MultipartFile;

import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.command.promotion.PromotionCouponQueryCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.promotion.PromotionCoupon;

/**
 * @author - 项硕
 */
public interface PromotionCouponManager extends BaseManager {

	public List<String> importCouponCode(MultipartFile file,
			PromotionCouponCodeCommand coupon);

	/**
	 * 根据条件分页查询优惠劵列表
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	 Pagination<PromotionCouponQueryCommand> queryCouponListByConditionallyWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap);

	/**
	 * 根据优惠劵id删除优惠劵
	 * @param ids
	 */
	 void delCouponsByIds(List<Long> ids);
	
	/**
	 * 新建 或修改
	 */
	 PromotionCoupon createOrupdate(PromotionCoupon model);
	 
	 /**
	  * 新建 或修改
	  */
	 PromotionCoupon queryByid(Long id);
	 
	 /**
	  * 修改状态
	  */
	 PromotionCoupon updateActive(Long id ,int active);

}
