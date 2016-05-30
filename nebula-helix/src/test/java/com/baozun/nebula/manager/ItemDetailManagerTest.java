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
 */
package com.baozun.nebula.manager;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.feilong.tools.jsonlib.JsonUtil;


/**
 *
 * @author yi.huang
 *
 * @date 2013-7-1 下午04:19:55 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ItemDetailManagerTest{
	
	
	@Autowired
	private ItemDetailManager itemDetailManager;
	
	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(ItemDetailManagerTest.class);

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.ItemManagerImpl#findItemListByItemIds(java.lang.Long[])}.
	 */
	
	private static final Long itemId =78L;
	
	@Before
	public void setM(){
		ProfileConfigUtil.setMode("dev");
	}
	
	@Test
	public void testFindDynamicProperty() {
		LangUtil.setCurrentLang("zh_CN");
		Map<String, Object> dyp = itemDetailManager.findDynamicProperty(itemId);
		if(dyp != null) {
			System.out.println("===========" + dyp.size());
			Set<String> keys = dyp.keySet();
			for(String key : keys) {
				System.out.println("=========" + key + ":" + dyp.get(key));
			}
		}
	}
	
	@Test
	public void testGatherDy(){
		LangUtil.setCurrentLang("zh_CN");
		Map<String, Object> dyp = itemDetailManager.gatherDynamicProperty(itemId);
		log.debug("sssssss:{}", JsonUtil.format(dyp));
	}
	

}

