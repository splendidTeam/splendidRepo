package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.command.promotion.PromotionCouponCommand;
import com.baozun.nebula.manager.BaseManager;


/**
 * 
 * @author 阳羽
 * @createtime 2014-3-6 下午04:38:09
 */
public interface SdkPromotionCouponManager extends BaseManager{

	/**
	 * 根据订单code修改couponCode的isused状态
	 * @param code
	 */
	public void updatePromCouponUsedStatusByOrderCode(String code,Integer used);
	
	 /***
	  * 查询优惠卷类型
	  * @return
	  */
	public List<PromotionCouponCommand> findAllcouponList();
	
	/**
	 * 根据ID查询优惠券
	 * @param id
	 * @return
	 */
	public PromotionCouponCommand findPromotionCouponCommandById(Long id);
}
