package com.baozun.nebula.manager.promotion;
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


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.coupon.CouponTypeDao;
import com.baozun.nebula.model.coupon.CouponType;

/**
 * 
 * CouponTypeManagerImpl.java
 *
 * @author - 項邵瀧の父
 * @version  2014-3-12
 */
@Transactional
@Service("couponTypeManager")
public class CouponTypeManagerImpl implements CouponTypeManager {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CouponTypeManagerImpl.class);
	
	@Autowired
	private CouponTypeDao couponTypeDao;

	@Override
	public List<CouponType> findAllCouponTypeList() {
		return couponTypeDao.findAllCouponTypeList();
	}
	

}