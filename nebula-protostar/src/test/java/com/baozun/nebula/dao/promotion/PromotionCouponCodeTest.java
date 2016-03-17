package com.baozun.nebula.dao.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baozun.nebula.sdk.manager.SdkPromotionCouponCodeManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback=false)
public class PromotionCouponCodeTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	SdkPromotionCouponCodeManager sdkPromotionCouponCodeManager;
	@Autowired
	PromotionCouponCodeDao promotionCouponCodeDao;

	@Test
	public void findTimesUsedByCouponCode() {
		try {
			Integer count = sdkPromotionCouponCodeManager
					.findTimesUsedByCouponCode("qw123245238");
			System.out.println("count:" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Test
	public void findAndCheckPromotionCouponCodeCommandListByCodes() {
		try {
			//("c415623frsdfqwe18")
			List<String> codes = new ArrayList<String>();
			codes.add("qw123245238");
			 promotionCouponCodeDao.findAndCheckPromotionCouponCodeCommandListByCodes(codes,
					new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
}
