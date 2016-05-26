package com.baozun.nebula.sdk.manager.promotion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.promotion.PromotionCouponCommand;
import com.baozun.nebula.dao.promotion.PromotionCouponCodeDao;
import com.baozun.nebula.dao.promotion.PromotionCouponDao;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.dao.salesorder.SdkOrderPromotionDao;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Transactional
@Service("sdkPromotionCouponManager")
public class SdkPromotionCouponManagerImpl implements SdkPromotionCouponManager {

	@Autowired
	private PromotionCouponCodeDao promotionCouponCodeDao;
	
	@Autowired
	private SdkOrderDao sdkOrderDao;
	
	@Autowired
	private SdkOrderPromotionDao sdkOrderPromotionDao;
	
	@Autowired
	private PromotionCouponDao  couponDao;
	
	@Override
	public void updatePromCouponUsedStatusByOrderCode(String code,Integer used) {
		SalesOrderCommand so = sdkOrderDao.findOrderByCode(code, null);
		
		List<OrderPromotionCommand> orderPromots = sdkOrderPromotionDao.findOrderProsInfoByOrderId(so.getId());
		
		Set<String> couponCodes = new HashSet<String>();
		
		if(null != orderPromots && orderPromots.size() > 0){
			for(OrderPromotionCommand opc : orderPromots){
				if(StringUtils.isNotBlank(opc.getCoupon())){
					List<String> coupons = new Gson().fromJson(opc.getCoupon(),new TypeToken<List<String>>(){}.getType());
					for(String coupon:coupons){
						couponCodes.add(coupon);
					}
				}
			}
			List<String> couponList = new ArrayList<String>();
			for(String coupon : couponCodes){
				couponList.add(coupon);
			}
			promotionCouponCodeDao.useOrUnusePromotionCouponCodeByCouponCodes(couponList,used);
		}
		
	}
	@Override
	@Transactional(readOnly=true)
	public List<PromotionCouponCommand> findAllcouponList() { 
		return couponDao.findAllCouponList(); 
	}
	
	@Override
	@Transactional(readOnly=true)
	public PromotionCouponCommand findPromotionCouponCommandById(Long id) {
		return couponDao.findPromotionCouponCommandById(id);
	}

}
