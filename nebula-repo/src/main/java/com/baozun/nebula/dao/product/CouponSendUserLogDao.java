/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.dao.product;
import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.system.CouponSendUserLog;

/**
 * @ClassName: InstationMessageTemplateDao
 * @Description:(优惠券发送记录DAO)
 * @author GEWEI.LU
 * @date 2016年1月15日 下午3:16:25
 */
public interface CouponSendUserLogDao extends GenericEntityDao<CouponSendUserLog, Long> {
	/**
	 * 根据优惠券券码列表查询优惠券列表
	 * 
	 * @param codes
	 * @return
	 */
	@NativeQuery(model = CouponSendUserLog.class)
	List<CouponSendUserLog> findCouponSendUserLog(@QueryParam("promotioncouponid") Long promotioncouponid);
	
	
	/**
	 * 添加发送记录
	 * 
	 * @param codes
	 * @return
	 */
	@NativeUpdate
	void  saveCouponSendUserLog(@QueryParam("memberid") Long memberid,@QueryParam("promotioncouponcodeid") Long promotioncouponcodeid,
			@QueryParam("promotioncouponcode") String promotioncouponcode,@QueryParam("promotioncouponid") Long promotioncouponid,@QueryParam("promotioncouponname") String promotioncouponname);
}
