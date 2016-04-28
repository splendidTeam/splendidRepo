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
package com.baozun.nebula.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.web.controller.product.converter.ItemImageViewCommandConverter;
import com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolver;
import com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolver;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyValueViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyViewCommand;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年4月25日 下午3:17:37 
 * @version   
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ResolverTest {
	
	private static final Logger	LOG	= LoggerFactory.getLogger(ResolverTest.class);
	
	@Autowired
	private ItemPropertyViewCommandResolver							itemPropertyViewCommandResolver;
	
	@Autowired
	private ItemColorSwatchViewCommandResolver						itemColorSwatchViewCommandResolver;
	
	@Autowired
	@Qualifier("itemImageViewCommandConverter")
	private ItemImageViewCommandConverter 							itemImageViewCommandConverter;
	
	@Before
	public void setM(){
		ProfileConfigUtil.setMode("dev");
	}
	
	@Test
	public void testItemPropertyViewCommandResolver(){
		
		Map<String, Object> returnMap =new HashMap<String, Object>();
		ItemBaseInfoViewCommand baseInfoViewCommand =new ItemBaseInfoViewCommand();
		List<ItemImageViewCommand> images =new ArrayList<ItemImageViewCommand>();
		
		constrData(returnMap,
				baseInfoViewCommand, images);
		
		
		
		LOG.debug("sss:{}", JsonUtil.format(itemPropertyViewCommandResolver.resolve(baseInfoViewCommand, images)));
		
	}
	
/*	@Test
	public void testItemColorSwatchViewCommandResolver(){
		
		ItemBaseInfoViewCommand baseInfoViewCommand =new ItemBaseInfoViewCommand();
		baseInfoViewCommand.setId(14027L);
		baseInfoViewCommand.setCode("42611PO033");
		LOG.debug("sss:{}", JsonUtil.format(itemColorSwatchViewCommandResolver.resolve(baseInfoViewCommand,
				itemImageViewCommandConverter)));
	}
	
	*/
	
	/**
	 * 
	 * @param returnMap
	 * @param baseInfoViewCommand
	 * @param images
	 * @return
	 */
	private ItemPropertyViewCommand constrData(Map<String, Object> returnMap,
			ItemBaseInfoViewCommand baseInfoViewCommand,
			List<ItemImageViewCommand> images) {
		ItemPropertyViewCommand itemPropertyViewCommand =new ItemPropertyViewCommand();
		Map<String, List<PropertyElementViewCommand>> nonSalesProperties = new HashMap<String, List<PropertyElementViewCommand>>();
		List<PropertyElementViewCommand> elementViewCommands =new ArrayList<PropertyElementViewCommand>();
		PropertyElementViewCommand elementViewCommand =new PropertyElementViewCommand();
		PropertyViewCommand propertyViewCommand =new PropertyViewCommand();
		propertyViewCommand.setId(1l);
		propertyViewCommand.setGroupName("123");
		propertyViewCommand.setIsColorProp(false);
		propertyViewCommand.setIsSaleProp(false);
		propertyViewCommand.setName("上市年份");
		elementViewCommand.setProperty(propertyViewCommand);
		List<PropertyValueViewCommand> propertyValueViewCommands =new ArrayList<PropertyValueViewCommand>();
		PropertyValueViewCommand propertyValueViewCommand =new PropertyValueViewCommand();
		propertyValueViewCommand.setPropertyId(1l);
		propertyValueViewCommand.setPropertyName("上市年份");
		propertyValueViewCommand.setPropertyValue("1909-09-09");
		propertyValueViewCommand.setItemPropertiesId(101l);
		propertyValueViewCommands.add(propertyValueViewCommand);
		elementViewCommand.setPropertyValues(propertyValueViewCommands);
		elementViewCommands.add(elementViewCommand);
		nonSalesProperties.put("123", elementViewCommands);
		itemPropertyViewCommand.setNonSalesProperties(nonSalesProperties);
		
		List<PropertyElementViewCommand> salesProperties =new ArrayList<PropertyElementViewCommand>();
		PropertyElementViewCommand elementViewCommand1 =new PropertyElementViewCommand();
		PropertyViewCommand propertyViewCommand1 =new PropertyViewCommand();
		propertyViewCommand1.setId(2l);
		propertyViewCommand1.setGroupName("1233");
		propertyViewCommand1.setIsColorProp(false);
		propertyViewCommand1.setIsSaleProp(true);
		propertyViewCommand1.setName("尺码");
		elementViewCommand1.setProperty(propertyViewCommand1);
		List<PropertyValueViewCommand> propertyValueViewCommands1 =new ArrayList<PropertyValueViewCommand>();
		PropertyValueViewCommand propertyValueViewCommand1 =new PropertyValueViewCommand();
		propertyValueViewCommand1.setPropertyId(1l);
		propertyValueViewCommand1.setPropertyName("尺码");
		propertyValueViewCommand1.setPropertyValue("xxl");
		propertyValueViewCommand1.setItemPropertiesId(101l);
		propertyValueViewCommands1.add(propertyValueViewCommand1);
		elementViewCommand.setPropertyValues(propertyValueViewCommands1);
		salesProperties.add(elementViewCommand);
		
		itemPropertyViewCommand.setSalesProperties(salesProperties);
		
		
		baseInfoViewCommand.setId(14060L);
		baseInfoViewCommand.setCode("42611WP364B33");
		
		
		ItemImageViewCommand itemImageViewCommand =new ItemImageViewCommand();
		itemImageViewCommand.setColorItemPropertyId(57571l);
		itemImageViewCommand.setItemId(14060L);
		Map<String, List<ImageViewCommand>> imagesMap =new HashMap<String, List<ImageViewCommand>>();
		List<ImageViewCommand> imageViewCommands =new ArrayList<ImageViewCommand>();
		ImageViewCommand imageViewCommand =new ImageViewCommand();
		imageViewCommand.setUrl("http://www.baidu.com");
		imageViewCommands.add(imageViewCommand);
		imagesMap.put("IMG_TYPE_COLOR", imageViewCommands);
		itemImageViewCommand.setImages(imagesMap);
		images.add(itemImageViewCommand);
		
		
		List<DynamicPropertyCommand> commands =new ArrayList<DynamicPropertyCommand>();
		Map<String, Object> gMap =new HashMap<String, Object>();
		gMap.put("ABC", commands);
		returnMap.put("salePropCommandList", commands);
		returnMap.put("generalGroupPropMap", gMap);
		return itemPropertyViewCommand;
	}

}
