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
package com.baozun.nebula.manager.product;

import static org.easymock.EasyMock.createNiceControl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.web.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.product.SkuImportController;


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
public class ItemManagerTest{
	
	private IMocksControl		control;
	private HttpServletRequest	request;
	private HttpServletResponse		response;
	@Autowired
	private ItemManager itemManager;
	
	@Autowired
	private ItemDao				itemDao;
	
	private static final Logger	log	= LoggerFactory.getLogger(ItemManagerTest.class);

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.ItemManagerImpl#findItemListByItemIds(java.lang.Long[])}.
	 */
	
	@Before
	public void init(){
		control = createNiceControl();
		request	= control.createMock("HttpServletRequest",HttpServletRequest.class);
		response = control.createMock("HttpServletResponse",HttpServletResponse.class);
	}
	
	
	@Test
	public void testFindItemListByItemIds(){
		Long[] itemId = {1L,2L,3L};
		List<Item> itemList = itemManager.findItemListByItemIds(itemId);
		
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",itemList.size());
	}
	@Test
	public void testUpdateItemIsAddCategory(){
		List<Long> ids = new ArrayList<Long>();
		ids.add(7L);
		Integer state=0;
		Integer result = itemManager.updateItemIsAddCategory(ids,state);
		
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",result);
	}

	@Test
	public void testValidateItemCode(){
		String code="t1111";
		Long shopId =48L;
		Integer result =itemManager.validateItemCode(code, shopId);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",result);
	}
	@Test
	public void testFindItemInfoByItemId(){
		Long itemId=1566L;
		ItemInfo itemInfo =itemManager.findItemInfoByItemId(itemId);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",itemInfo.getTitle());
		
	}
	@Test
	public void testFindDynamicPropertis(){
		List<DynamicPropertyCommand> dynamicPropertyCommandList =
				itemManager.findDynamicPropertis(48L,327L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",dynamicPropertyCommandList.size());
	}
	@Test
	public void testFindItemById(){
		Item item = itemManager.findItemById(1566L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",item.getCode());
	}
	@Test
	public void testFindItemPropertiesListyByItemId(){
		List<ItemProperties> itemProperties =
				itemManager.findItemPropertiesListyByItemId(1566L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",itemProperties.size());
	}
	@Test
	public void testFindSkuByItemId(){
		List<Sku> list  = itemManager.findSkuByItemId(1566L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",list.size());
	}
	@Test
	public void testCreateOrUpdateItem() throws Exception{
		ItemCommand itemCommand = new ItemCommand();
		itemCommand.setDescription("dd");
		itemCommand.setTitle("test");
		itemCommand.setCode("c");
		itemCommand.setIndustryId("327");
		Long[] categoriesIds={};
		Long[] propertyValueIds={};		
		String[] codes={};
		
		ItemProperties[] iProperties = {};
		Item item=null;
//		item = itemManager.createOrUpdateItem(itemCommand, 48L, categoriesIds, propertyValueIds, codes, iProperties);
		
		//Assert.assertEquals(null, item);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",item);
	}
	
	 @Test
	  public void findItemListByQueryMap(){
		    Page p = new Page();
			p.setSize(5);
			p.setStart(0);
			Sort[] sorts=Sort.parse("tpit.create_time desc");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", "%20%");
			Long shopId =256L;
			Pagination<ItemCommand> page = itemManager.findItemListByQueryMap(p, sorts, map,shopId);
			log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	  }
	 
	 @Test
	  public void removeItemByIds(){
		  List<Long> ids=new ArrayList<Long>();
		  ids.add(1L); 
		  itemManager.removeItemByIds(ids);
		 log.info("removeItemByIds  逻辑删除后状态{000000000000000000}"); 
	  }
	 
	 
	  @Test
	  public void enableOrDisableItemByIds(){
				List<Long> ids=new ArrayList<Long>();
				ids.add(1L);
				ids.add(2L);
				try {
					itemManager.enableOrDisableItemByIds(ids, 1);
				} catch (Exception e) {
					log.info("必须确保有效的用户名不允许重复!");
					return;
				} 
				log.info("------------enableOrDisableByIds ------ok");
	}

	
	@Test
	public void testDownLoadFile(){
		String path = "excel/tplt_sku_import.xls";
		File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
	   
		itemManager.downloadFile(request, response, 256L, 384L);
	}
	
	
	@Test
	public void testImportItemFromFile() throws Exception{
		itemManager.importItemFromFile(new FileInputStream("C:\\Users\\shgz-pc-1486\\Desktop\\uploads\\tplt_sku_import.xls"), 256L);
	}
	

}

