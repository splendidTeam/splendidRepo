package com.baozun.nebula.dao.coupon;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.coupon.CouponCommand;
import com.baozun.nebula.model.coupon.Coupon;

/***
 * 优惠劵的增删改查
 * @author qiang.yang
 *
 */
public interface CouponDao  extends GenericEntityDao<Coupon, Long>{
	/**
	 * 根据优惠卷的id进行查询优惠劵
	 * @param id
	 * @return
	 */
	@NativeQuery(model = Coupon.class)
	public Coupon findById(@QueryParam("id") Long id);
	
	/**
	 * 根据优惠卷的代码进行查询优惠劵
	 * @param id
	 * @return
	 */
	@NativeQuery(model = Coupon.class)
	public Coupon findByCardNo(@QueryParam("cardNo") String cardNo);

	/**
	 * 根据优惠卷的memberId进行查询优惠劵
	 * @param memberId
	 * @return
	 */
	@NativeQuery(model = Coupon.class)
	public List<Coupon> findByMemberId(@QueryParam("memberId") Long memberId);
	

	/**
	 * 根据优惠劵id和会员id进行查找
	 * @param memberId
	 * @param actCardId
	 * @return
	 */
	@NativeQuery(model = Coupon.class)
	public Coupon findByMemberIdAndActCardId(@QueryParam("memberId") Long memberId,@QueryParam("actCardId") Long actCardId);
	/***
	 * 根据会员id联合查询所有的优惠劵的信息
	 * @param memberId
	 * @return
	 */
	@NativeQuery(model=CouponCommand.class)
	public Pagination<CouponCommand> findCouponCommandList(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 根据优惠劵id和会员id进行查找
	 * @param memberId
	 * @param actCardId
	 * @return
	 */
	@NativeQuery(model = Coupon.class)
	public Coupon findByMemberIdAndCardNo(@QueryParam("memberId") Long memberId,@QueryParam("cardNo") String cardNo);
	
	
	@NativeUpdate
	public Coupon updateCouponByMemberIdAndCardNo(@QueryParam("memberId") Long memberId,@QueryParam("cardNo") String cardNo,@QueryParam("usedTime") Date usedTime);

	/**
	 * 根据会员id和优惠券couponNo查询优惠券信息
	 * @param memberId
	 * @param cardNo
	 * @return
	 */
	@NativeQuery(model = CouponCommand.class)
	public CouponCommand findCouponCommandByMemberIdAndCardNo(@QueryParam("memberId") Long memberId,@QueryParam("cardNo") String cardNo);
}
