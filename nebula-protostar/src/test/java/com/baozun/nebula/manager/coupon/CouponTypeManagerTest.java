package com.baozun.nebula.manager.coupon;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.dao.coupon.CouponTypeDao;
import com.baozun.nebula.model.coupon.CouponType;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class CouponTypeManagerTest {
	private static final Logger log = LoggerFactory.getLogger(CouponTypeManagerTest.class);
	
	@Autowired
	private CouponTypeDao couponTypeDao;
	
	@Test
	public void testSave(){
		CouponType coupon=new CouponType();
		coupon.setBeginTime(new Date());
		coupon.setEndTime(new Date());
		coupon.setFactor(0.9);
		coupon.setFactorType("discount");
		coupon.setName("9折");
		coupon.setUsenum(true);
		
		couponTypeDao.save(coupon);
	}
	
	@Test
	public void testFindById(){
		CouponType coupon=couponTypeDao.findById(3L);
		System.out.println("couponcouponcouponcoupon"+coupon);
		log.info("getFactor====="+coupon.getFactor());
		log.info("getFactorType====="+coupon.getFactorType());
		log.info("getName====="+coupon.getName());
		log.info("getEndTime====="+coupon.getEndTime());
		log.info("getId====="+coupon.getId());
	}
	
}
