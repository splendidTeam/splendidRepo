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

import com.baozun.nebula.dao.coupon.CouponDao;
import com.baozun.nebula.model.coupon.Coupon;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class CouponManagerTest {
	private static final Logger log = LoggerFactory.getLogger(CouponManagerTest.class);
	
	@Autowired
	private CouponDao couponDao;
	
	@Test
	public void testSave(){
		Coupon coupon=new Coupon();
		coupon.setCardNo("bbb");
		coupon.setCardPassword("aaaaa");
		coupon.setCardTypeId(new Long(1));
		coupon.setUsedTime(new Date());
		
		couponDao.save(coupon);
	}
	
	@Test
	public void testFindById(){
		Coupon coupon=couponDao.findById(1L);
		log.info("getCardNo====="+coupon.getCardNo());
		log.info("getCardPassword====="+coupon.getCardPassword());
		log.info("getCardTypeId====="+coupon.getCardTypeId());
		log.info("getUsedTime====="+coupon.getUsedTime());
		log.info("getId====="+coupon.getId());
	}
	
}
