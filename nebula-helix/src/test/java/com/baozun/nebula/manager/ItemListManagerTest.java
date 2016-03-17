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
package com.baozun.nebula.manager;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.product.SearchResultCommand;
import com.baozun.nebula.manager.product.ItemListManager;
import com.baozun.nebula.manager.product.SearchConditionManager;
import com.baozun.nebula.sdk.command.CategoryCommand;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;

/**
 * @author Tianlong.Zhang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ItemListManagerTest {
	private static final Logger log = LoggerFactory.getLogger(ItemListManagerTest.class);
	
	@Autowired
	private ItemListManager itemListManager;
	
	@Autowired
	private SearchConditionManager searchConditionManager;
	
	@Test
	public void findItemList() throws JsonGenerationException, JsonMappingException, IOException{
		List<SearchConditionItemCommand> cmdList = searchConditionManager.findItemByPropertyId(123L);
		cmdList.size();
		//		SearchResultCommand cmd = itemListManager.findItemList();
//		ObjectMapper mapper = new ObjectMapper();
//		String result= mapper.writeValueAsString(cmd);
//		log.error("start ====");
//		log.error(result);
	}
	
	@Test
	public void findCategoryList(){
		Long categoryId = 39L;
		List<CategoryCommand> result = itemListManager.findCategoryList(categoryId,19L);
		log.error("&&&&&&&&&&&&&&&&&");
		for(CategoryCommand cmd :result){
			log.error(" "+cmd.getId()+" \t name: "+cmd.getName()+" \t pid: "+cmd.getParentId());
		}
	}
	
	@Test
	public void findCurmbList(){
		Long categoryId = 161L;
		List<CurmbCommand> result = itemListManager.findCurmbList(categoryId);
		log.error("&&&&&&&&&&&&&&&&&");
		for(CurmbCommand cmd : result ){
			log.error(" "+cmd.getId()+" \t name: "+cmd.getName()+" \t pid: "+cmd.getParentId()+" \t url: "+cmd.getUrl()+" \t type: "+cmd.getType());
		}
	}
}
