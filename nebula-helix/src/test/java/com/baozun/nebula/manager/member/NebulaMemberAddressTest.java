package com.baozun.nebula.manager.member;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.controller.PageForm;

import loxia.dao.Pagination;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml",
		"classpath*:spring.xml" })
public class NebulaMemberAddressTest {

	@Resource(name = "sdkMemberService")
	private SdkMemberManager sdkMemberManager;

	@Test
	public void findContactsByMemberIdTest() {

		PageForm pageForm = new PageForm();

		Pagination<ContactCommand> contacts = sdkMemberManager.findContactsByMemberId(pageForm.getPage(),
				pageForm.getSorts(), 1L);

	}

}
