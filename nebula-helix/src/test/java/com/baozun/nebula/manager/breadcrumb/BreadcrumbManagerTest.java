/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.breadcrumb;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年5月19日 下午2:01:17 
 * @version   
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml",
		"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class BreadcrumbManagerTest {
	
	private static final Logger log = LoggerFactory.getLogger(BreadcrumbManagerTest.class);
	
	@Autowired
	private BreadcrumbManager breadcrumbManager;
	
	//测试时在spring-manager.xml里加
	//<bean id="facetFilterHelper"
	//	class="com.baozun.nebula.search.FacetFilterHelperImpl">
	//</bean>
	
	@Before
	public void setM(){
		ProfileConfigUtil.setMode("dev");
	}
	
	@Test
	public void test(){
		doTest(14l, 58l, null);
	}
	
    //	@Test
    //	public void test2(){
    //		HttpServletRequest request =getRequest(); 
    //		doTest(null, 84l, request);
    //	}
	
	public void doTest(Long navId, Long itemId,HttpServletRequest request){
		LangUtil.setCurrentLang("zh_CN");
		List<CurmbCommand> commands = null;
		try {
			commands = breadcrumbManager.findCurmbCommands(navId, itemId, request);
		} catch (IllegalItemStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.debug("sss:{}", JsonUtil.format(commands));
	}
	
	
	 
	
	

}
