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
package com.baozun.nebula.manager.sdk.manger;

import java.util.List;

import javax.annotation.Resource;

import loxia.dao.Pagination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.sdk.command.CategoryCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkItemListManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.solr.command.SolrGroup;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utilities.common.LangUtil;

/**
 * @author Tianlong.Zhang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SdkItemListManagerTest {
	private static final Logger log = LoggerFactory.getLogger(SdkItemListManagerTest.class);
	
	@Autowired
	private SdkItemListManager sdkItemListManager;
	
	@Test
	public void findCategoryList(){
//		Long categoryId = 26L;
//		List<CategoryCommand> cmdList = sdkItemListManager.findCategoryList(categoryId);
//		log.info("result is =========== " +cmdList.size() );
//		for(CategoryCommand cmd : cmdList){
//			log.info(cmd.getId()+"  "+cmd.getName()+"  "+cmd.getParentId());
//		}
	}
	
	@Test
	public void findItemList(){
//		 Pagination<ProdcutItemCommand> p=sdkItemListManager.findItemList();
//		log.info("&&&&&&&&&&&&&&&&&&&&&&&&&");
//		for(ProdcutItemCommand c : p.getItems()){
//				log.info(c.getId()+"   "+c.getCode()+"  "+c.getPicUrl());
//		}
	}
	
	@Resource(name = "sdkSkuManager")
	private SdkSkuManager sdkSkuManager;
	

	@Test
	public void testGetSkuPros() {
		String properties = "[1918,1919,1920]";
		//LangUtil.setCurrentLang("zh_CN");
		LangUtil.setCurrentLang("en_GB");
		//LangUtil.setCurrentLang("zh_HK");
		List<SkuProperty> skuPropertyList = sdkSkuManager.getSkuPros(properties);
		if(Validator.isNotNullOrEmpty(skuPropertyList)) {
			for(SkuProperty sp : skuPropertyList) {
				System.out.println("===================" + sp.getValue());
			}
		}
	}
}
