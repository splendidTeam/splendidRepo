package com.baozun.nebula.manager.member;
import java.sql.Timestamp;
import java.util.HashMap;
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

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.dao.member.ContactDao;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utils.JsonFormatUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ContactManagerTest {
	private static final Logger log = LoggerFactory.getLogger(ContactManagerTest.class);
	
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	@Autowired
	private MemberContactManager contactManager;
	
	@Test
	public void testSdkCreateOrUpdateContact() {
		ContactCommand con = new ContactCommand();
//		con.setId(2L);
		con.setName("ZHANGSAN");
		con.setAddress("JFADLG;L");
		con.setAreaId(2L);
		con.setCityId(2L);
		con.setCountryId(2L);
		//con.setIfDefault(0);
		con.setMobile("54213659");
		con.setPostcode("165326");
		con.setProvinceId(2L);
		con.setTelphone("15234563");
		con.setTownId(2L);
		con.setMemberId(1L);
		try {
			sdkMemberManager.createOrUpdateContact(con);
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",
					"++++++++++++++++++++++++++++sucess");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRemoveContactById(){
		Integer count = sdkMemberManager.removeContactById(83L, 1L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",
				"++++++++++++++++++++++++++++count ="+count);
	}
	
	@Test
	public void testfindPageItemsById() {
		ContactCommand item = sdkMemberManager.findContactById(83L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",
				"++++++++++++++++++++++++++++name = "+item.getName());
	}
	
	
	@Test
	public void testfindContacts() {
		Page page = new Page();
		page.setSize(5);
		page.setStart(0);
		
		Sort[] sorts=Sort.parse("t.version desc");
		
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("startDate", new Timestamp(0));
//		map.put("endDate", request.getAttribute("endDate"));
		
		Pagination<ContactCommand>args =contactManager.findContactCommandByQueryMapWithPage(page, sorts, map);
	}
	
}