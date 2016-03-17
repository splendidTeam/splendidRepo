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
package com.baozun.nebula.dao.product;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.utils.JsonFormatUtil;


/**
 *
 * @author yi.huang
 *
 * @date 2013-7-1 上午09:46:54 
 */
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback=false)
public class ItemDaoTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;
	
	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(ItemDaoTest.class);

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ItemDao#findItemById(java.lang.Long)}.
	 */
	@Test
	public void testFindItemById(){
		Item item = itemDao.findItemById(2L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",item.getCode());
	}
	
	
	@Test
	public void testFindItemListByIds(){
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(3L);
		List<Item> list = itemDao.findItemListByIds(ids);
		
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",list.size());
	}
	
	@Test
	public void testUpdateItemIsAddCategory(){
		List<Long> ids = new ArrayList<Long>();
		ids.add(22L);
		Integer state=1;
		Integer result = itemDao.updateItemIsAddCategory(ids,state);
		
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@::::{}",result);
	}
	
	
	@Test
	  public void enableOrDisableItemByIds(){
		  List<Long> ids=new ArrayList<Long>();
		  ids.add(1303L);
		  
		List<Item> itemlist = new ArrayList<Item>();
		itemlist= itemDao.findItemListByIds(ids);
		
		Item item = (Item) ((itemlist==null||itemlist.size()==0)?null:itemlist.get(0));
		
		log.info("enableOrDisableItemByIds  禁用前状态{}", item==null?"null":item.getLifecycle()); 
		String updateListTimeFlag = sdkMataInfoManager.findValue(MataInfo.UPDATE_ITEM_LISTTIME);		 
		 itemDao.enableOrDisableItemByIds(ids, 0, updateListTimeFlag);
		 
		 itemlist= itemDao.findItemListByIds(ids);
			
		 item = (Item) ((itemlist==null||itemlist.size()==0)?null:itemlist.get(0));
			
		 log.info("enableOrDisableItemByIds000000000000000  禁用后状态{}", item==null?"null":item.getLifecycle()); 
	 }
	
	
	 @Test
	  public void removeItemByIds(){
		  List<Long> ids=new ArrayList<Long>();
		  ids.add(6L); 
		  ids.add(7L);
		  itemDao.removeItemByIds(ids);
		  Item item= itemDao.findItemById(1303L);
		 log.info("removeItemByIds  逻辑删除后状态{}",item==null?"null":item.getLifecycle()); 
	  }
	 
	 
	 @Test
	  public void findItemCommandByCode(){
		 ItemCommand itemCommand = itemDao.findItemCommandByCode("dd");
		 System.out.println(itemCommand.getCode()+"::::"+itemCommand.getTitle());
	  }
	 
	 @Test
	  public void findItemListByQueryMap(){
		    Page p = new Page();
			p.setSize(5);
			p.setStart(0);
			Sort[] sorts=Sort.parse("tpit.create_time desc");
			Map<String, Object> map = new HashMap<String, Object>();
			Pagination<ItemCommand> page = itemDao.findItemListByQueryMap(p, sorts, map,256L);
			log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	  }
	 
}

