//package com.baozun.nebula.manager.coupon;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import loxia.dao.Page;
//import loxia.dao.Pagination;
//import loxia.dao.Sort;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.baozun.nebula.command.coupon.CouponCommand;
//import com.baozun.nebula.dao.coupon.CouponTypeDao;
//import com.baozun.nebula.dao.coupon.MemberCouponDao;
//import com.baozun.nebula.model.coupon.CouponType;
//import com.baozun.nebula.model.coupon.MemberCoupon;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
//		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
//@ActiveProfiles("dev")
//public class MemberCouponManagerTest {
//	private static final Logger log = LoggerFactory.getLogger(MemberCouponManagerTest.class);
//	
//	@Autowired
//	private MemberCouponDao memberCouponDao;
//	
//	@Test
//	public void testSave(){
//		MemberCoupon coupon=new MemberCoupon();
//		coupon.setCreateTime(new Date());
//		coupon.setActCardId(2L);
//		coupon.setMemberId(10L);
//		memberCouponDao.save(coupon);
//	}
//	
//	@Test
//	public void testFindByMemberId(){
//		List<MemberCoupon> memberCoupons=memberCouponDao.findByMemberId(10L);
//		for(MemberCoupon coupon:memberCoupons){
//		System.out.println("couponcouponcouponcoupon"+coupon);
//		log.info("getActCardId====="+coupon.getActCardId());
//		log.info("getMemberId====="+coupon.getMemberId());
//		}
//	}
//	
//	
//	@Test
//	public void testFindCouponCommandList(){
//		Page page=new Page(1,5);
//		Sort[] sorts=Sort.parse(" tacr.create_time desc");
//		Map<String,Object> paraMap=new HashMap<String, Object>();
//		paraMap.put("memberId", 10L);
//		Pagination<CouponCommand> memberCoupons=memberCouponDao.findCouponCommandList(page, sorts, paraMap);
//		for(CouponCommand coupon:memberCoupons.getItems()){
//		System.out.println("couponcouponcouponcoupon"+coupon);
//		log.info("getActCardId====="+coupon.getCardNo());
//		log.info("getMemberId====="+coupon.getMemberId());
//		log.info("getFactorType====="+coupon.getFactorType());
//		log.info("getFactor====="+coupon.getFactor());
//		log.info("getName====="+coupon.getName());
//		log.info("getBeginTime====="+coupon.getBeginTime());
//		}
//	}
//	
//	
//	
//}
