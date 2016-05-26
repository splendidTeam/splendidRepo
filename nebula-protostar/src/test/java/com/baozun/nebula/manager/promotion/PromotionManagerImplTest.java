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

package com.baozun.nebula.manager.promotion;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.promotion.AudienceCommand;
import com.baozun.nebula.command.promotion.ConditionNormalCommand;
import com.baozun.nebula.command.promotion.HeadCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.ScopeCommand;
import com.baozun.nebula.command.promotion.SettingNormalCommand;
import com.baozun.nebula.dao.promotion.PromotionAudiencesDao;
import com.baozun.nebula.model.promotion.PromotionAudiences;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;

/**
 * @author - 项硕
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:loxia-hibernate-context.xml",
			"classpath*:loxia-service-context.xml",
			"classpath*:spring.xml"})
@ActiveProfiles("dev")
public class PromotionManagerImplTest {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(PromotionManagerImplTest.class);
	
	@Autowired
	private PromotionManager promotionManager;

	@Autowired
	private SdkPromotionManager sdkPromotionManager;
	@Autowired
	private PromotionAudiencesDao promotionAudiencesDao;

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionManagerImpl#queryPromotionListByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)}.
	 */
	@Test
	public void testQueryPromotionListByQueryMapWithPage() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> lifecycles = new ArrayList<Integer>();
		lifecycles.add(0);
		lifecycles.add(1);
		map.put("lifecycles", lifecycles);
		//Pagination<PromotionCommand> pagination = promotionManager.queryPromotionListByQueryMapWithPage(new Page(0, 3), null,  map, false);
		//System.out.println(JsonFormatUtil.format(pagination.getItems()));
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionManagerImpl#savePromotionHead(com.baozun.nebula.command.promotion.HeadCommand)}.
	 */
	@Test
	public void testSavePromotionHead() {
		HeadCommand cmd = new HeadCommand();
		cmd.setId(9L);
		cmd.setStartTime(new Date());
		cmd.setEndTime(new Date());
		cmd.setPromotionName("项硕测试大促");
		assertTrue(promotionManager.savePromotionHead(cmd, 1L) != null);
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionManagerImpl#savePromotionAudience(com.baozun.nebula.command.promotion.AudienceCommand)}.
	 */
	@Test
	public void testSavePromotionAudience() {
		PromotionAudiences audiences = new PromotionAudiences();
		promotionAudiencesDao.save(audiences);
		audiences.setComboName("hello");
		//assertTrue(promotionManager.savePromotionAudience(cmd, 1L));
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionManagerImpl#savePromotionScope(com.baozun.nebula.command.promotion.ScopeCommand)}.
	 */
	@Test
	public void testSavePromotionScope() {
		ScopeCommand cmd = new ScopeCommand();
		cmd.setId(13L);
		cmd.setPromotionId(9L);
		//assertTrue(promotionManager.savePromotionScope(cmd, 1L));
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionManagerImpl#savePromotionCondition(com.baozun.nebula.command.promotion.ConditionNormalCommand)}.
	 */
	@Test
	public void testSavePromotionCondition() {
		ConditionNormalCommand cmd = new ConditionNormalCommand();
		cmd.setId(10L);
		//cmd.setPromotionId(9L);
		//assertTrue(promotionManager.savePromotionCondition(cmd, 1L));
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionManagerImpl#savePromotionSetting(com.baozun.nebula.command.promotion.SettingNormalCommand)}.
	 */
	@Test
	public void testSavePromotionSetting() {
		SettingNormalCommand cmd = new SettingNormalCommand();
		cmd.setId(10L);
		//cmd.setPromotionId(9L);
		//cmd.setPromotionConditionId(10L);
		//assertTrue(promotionManager.savePromotionSetting(cmd, 1L));
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionManagerImpl#publishPromotion(com.baozun.nebula.command.promotion.PromotionCommand)}.
	 */
	@Test
	public void testPublishPromotion() {
		PromotionCommand cmd = promotionManager.findPromotionById(6L);
		//promotionManager.enablePromotion(1L, 1L);
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionManagerImpl#cancelPromotion(java.lang.Long)}.
	 */
	@Test
	public void testCancelPromotion() {

	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionManagerImpl#copyPromotion(java.lang.Long)}.
	 */
	/*@Test
	public void testCopyPromotion() {
		//	Long id = promotionManager.copyPromotion(6L, 1L);
			PromotionCommand cmd = new PromotionCommand();
			cmd.setId(id);
			promotionManager.publishPromotion(cmd, 1L);
		//System.out.println(JsonFormatUtil.format(promotionManager.findPromotionById(id)));
		for (int i = 66; i < 73; i++) {
			PromotionCommand cmd = new PromotionCommand();
			cmd.setId(new Long(i));
			promotionManager.enablePromotion(1L, 1L);
		}
	}*/
	/**
	 * 获得所有活动有效期包含在当前时间的以启用的活动
	 * @return
	 */
 	/**
	 * 获得所有活动有效期包含在当前时间的以启用的活动
	 * @return
	 */
    @Test
	public  void testpublishPromotion(){  
		List<PromotionCommand>  pr = sdkPromotionManager.publishPromotion(new Date());  
		log.info("publishPromotion  方法记录数{}条",pr.size());
		
	}
	  
		/**
	 * 启用活动前检查。人群的重叠性
	 * @return
	 */ 
    @Test
	public  void checkPromtionAudienceOverlappingByPid(){  
    	//List<PromotionCommand> ff = sdkPromotionManager.checkPromtionAudienceOverlappingByPid(149l);   
		
	} 
     
    
	/**
	 * 启用活动前检查。商品的重叠性
	 * @return
	 */
    @Test
	public  void checkPromtionScopeOverlappingByPid(){  
    	//List<PromotionCommand> ff = sdkPromotionManager.checkPromtionInforOverlappingByPid(149l);   
		
	} 
}
