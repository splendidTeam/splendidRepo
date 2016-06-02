package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.command.product.BundleSkuPriceCommand;
import com.baozun.nebula.sdk.manager.product.SdkBundleManager;
import com.baozun.nebula.web.command.BundleValidateResult;
import com.feilong.tools.jsonlib.JsonUtil;

import loxia.dao.Page;
import loxia.dao.Pagination;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml",
		"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class NebulaBundleManagerTest {

	@Autowired
	private NebulaBundleManager bundleManager;
	
	@Autowired
	private SdkBundleManager sdkBundleManager;
	
	private static final Logger log = LoggerFactory.getLogger(NebulaBundleManagerTest.class);
	
	@Test
	public void testFindBundleCommandByMainItemId(){
		List<BundleCommand> bundles = bundleManager.findBundleCommandByMainItemId(4L ,false);
		
		log.debug("result : {}" ,JsonUtil.format(bundles));
	}
	
	@Test
	public void testFindBundleCommandByMainStyle() {
		List<BundleCommand> bundles = bundleManager.findBundleCommandByMainStyle("1" ,false);
		
		log.debug("result : {}" ,JsonUtil.format(bundles));
	}
	
	@Test
	public void testFindBundleCommandByBundleItemCode(){
		BundleCommand bundle = bundleManager.findBundleCommandByBundleItemCode("111111114", true);
		
		log.debug("result : {}" ,JsonUtil.format(bundle));
	}
	
	@Test
	public void testFindBundleCommandByPage(){
		Page page = new Page();
		page.setSize(8);
		page.setStart(0);
		Pagination<BundleCommand> bundles =	bundleManager.findBundleCommandByPage(page,null, true);
		
		log.debug("result : {}" ,JsonUtil.format(bundles));
	}
	@Test
	public void testValidateBundle(){
		List<Long> skuIds = new ArrayList<Long>();
		skuIds.add(9L);
		skuIds.add(10L);
		skuIds.add(13L);
		skuIds.add(18L);
		skuIds.add(20L);
//		skuIds.add(2L);
		BundleValidateResult result = bundleManager.validateBundle(3L, skuIds, 10);
		System.out.println(result.getType());

	}
	
	@Test
	public void testGetBundleSkusPrice(){
		List<BundleSkuPriceCommand> result = sdkBundleManager.getBundleSkusPrice(3L, new Long[]{1L, 2L});
		for(BundleSkuPriceCommand command : result) {
			System.out.println(command.getListPrice().intValueExact() + "***" + command.getSalesPrice().intValueExact());
		}
	}
}
