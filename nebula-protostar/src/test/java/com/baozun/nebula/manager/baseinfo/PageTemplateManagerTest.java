package com.baozun.nebula.manager.baseinfo;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.model.baseinfo.PageItem;
import com.baozun.nebula.model.baseinfo.PageTemplate;
import com.baozun.nebula.sdk.command.PageTemplateCommand;
import com.baozun.nebula.sdk.manager.SdkPageTemplateManager;
import com.baozun.nebula.utils.JsonFormatUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class PageTemplateManagerTest {

	
private static final Logger log = LoggerFactory.getLogger(PageTemplateManagerTest.class);
	
	@Autowired
	private SdkPageTemplateManager sdkpageTemplateManager;
	
	@Autowired
	private PageTemplateManager pageTemplateManager;
	
	@Test
	public void testSdkCreateOrUpdatePageTemplate() {
		PageTemplateCommand con = new PageTemplateCommand();
		con.setId(2L);
		con.setPageCode("11111");
		con.setPageName("s22222g");
		con.setImg("dfg");
		con.setOpeartorId(5L);
		try {
			sdkpageTemplateManager.createOrUpdatePageTemplate(con);
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",
					"++++++++++++++++++++++++++++sucess");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateOrUpdatePageTemplate() {
		PageTemplateCommand con = new PageTemplateCommand();
//		con.setId(1L);
		con.setPageCode("dddddddd");
		con.setPageName("sgggg");
		con.setCreateTime(new Date());
		con.setImg("dfdgg");
		con.setOpeartorId(2L);
		List<PageItem> its = new ArrayList<PageItem>();
		for(int i=0 ;i<5;i++){
			PageItem i1= new PageItem();
			i1.setCode("c1"+i);
			i1.setCreateTime(new Date());
			i1.setName("n1"+i);
			i1.setPageTemplateId(1L);
			its.add(i1);
		}
		con.setPageItems(its);
		try {
			pageTemplateManager.createOrUpdatePageTemplate(con);
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",
					"++++++++++++++++++++++++++++sucess");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@Test
	public void testRemovePageTemplateByIds() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		try {
			pageTemplateManager.removePageTemplateByIds(ids);
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",
					"++++++++++++++++++++++++++++sucess");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testfindPageItemsById() {
		List<PageItem> items = pageTemplateManager.findPageItemByPtid(1L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",
				"++++++++++++++++++++++++++++sucess"+items.size());
	}
	
	@Test
	public void testfindPageItemsByptId() {
		List<PageItem> items = pageTemplateManager.findPageItemByPtid(1L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",
				"++++++++++++++++++++++++++++sucess"+items.size()+"+++");
	}
	
	@Test
	public void testfindPageTemplates() {
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		
		Sort[] sorts=Sort.parse("p.create_time desc");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ttttttttttttt", "%20%");
		Pagination<PageTemplate> page = pageTemplateManager.findPageTemplateListByQueryMapWithPage(p, sorts, map);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	}
	
}
