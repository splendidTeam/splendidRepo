package com.baozun.nebula.dao.coupon;

import java.util.List;

import com.baozun.nebula.model.coupon.CouponType;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface CouponTypeDao extends GenericEntityDao<CouponType, Long>{
	
	@NativeQuery(model=CouponType.class)
	public CouponType findById(@QueryParam("id") Long id);

	/**
	 * 查询所有优惠券类型列表
	 * @return
	 */
	@NativeQuery(model=CouponType.class)
	public List<CouponType> findAllCouponTypeList();
}
