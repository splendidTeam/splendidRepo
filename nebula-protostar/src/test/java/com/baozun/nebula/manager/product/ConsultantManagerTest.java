/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.product;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.command.product.ConsultantStatusCommand;

import ch.qos.logback.classic.Logger;

/**
 * @author Tianlong.Zhang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ConsultantManagerTest {
	private static final Logger	logger	= (Logger) LoggerFactory.getLogger(ConsultantManagerTest.class);

	@Autowired
	private ConsultantManager consultantManager;
	
	@Test
	public void findConsultantListByQueryMapWithPage(){
		Page page = new Page();
		page.setSize(3);
		Map<String,Object> paraMap = new HashMap<String,Object>();
		consultantManager.findConsultantListByQueryMapWithPage(page, null, paraMap);
	}
	
	@Test
	public void findConsultantStatus(){
		List<ConsultantStatusCommand> cmdList = consultantManager.findConsultantStatus();
		logger.info("-------------");
		for(ConsultantStatusCommand cmd : cmdList){
			logger.info(cmd.getLabel()+"  "+cmd.getValue());
		}
	}
	
	@Test
	public void responseConsultant(){
		ConsultantCommand cmd = new ConsultantCommand();
		cmd.setId(18L);
		cmd.setResolveNote("this is resolveNote  @ unit Test");
		cmd.setPublishMark(0);
		cmd.setResolveId(13L);
		boolean result = consultantManager.resolveConsultant(cmd);
		assertTrue(result);
	}
	
	@Test
	public void updateConsultant(){
		ConsultantCommand cmd = new ConsultantCommand();
		cmd.setId(18L);
		cmd.setResolveNote("updated this is resolveNote  @ unit Test updated");
		cmd.setPublishMark(1);
		cmd.setLastUpdateId(13L);
		boolean result = consultantManager.updateConsultant(cmd);
		assertTrue(result);
	}
	
	@Test
	public void publishConsultant(){
		ConsultantCommand cmd = new ConsultantCommand();
		cmd.setId(18L);
		cmd.setPublishMark(1);
		cmd.setPublishId(13L);
		boolean result = consultantManager.publishConsultant(cmd);
		assertTrue(result);
	}
	
	@Test 
	public void unpublishConsultant(){
		ConsultantCommand cmd = new ConsultantCommand();
		cmd.setId(18L);
		cmd.setPublishMark(0);
		cmd.setUnPublishId(13L);
		boolean result = consultantManager.unpublishConsultant(cmd);
		assertTrue(result);
	}
	
	@Test
	public void findConsultantById(){
		ConsultantCommand cmd = consultantManager.findConsultantById(18L);
		logger.info("cmd toString :");
		logger.info(cmd.getContent());
		logger.info(cmd.getResolveNote());
	}
}
