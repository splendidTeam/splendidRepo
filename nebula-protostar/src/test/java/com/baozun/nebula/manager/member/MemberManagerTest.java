package com.baozun.nebula.manager.member;

import java.util.ArrayList;
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
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utils.JsonFormatUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class MemberManagerTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private MemberManager memberManager;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private SdkMemberManager sdkMemberManager;

	private static final Logger	log	= LoggerFactory.getLogger(MemberManagerTest.class);
	
	@Test
	public void testFindMember() {
		Member m = sdkMemberManager.findMember(null);
		System.out.println("==="+m.getLoginName());
	}
	
	@Test
	public void testfindMemberListByQueryMapWithPage(){
		
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		
		Sort[] sorts=Sort.parse("a.login_name desc");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ttttttttttttt", "%20%");
		Pagination<MemberCommand> page = memberManager.findMemberListByQueryMapWithPage(p, sorts, map);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	}
	
	
	@Test
	public void testfindAllMemberGroup() {
		List<MemberGroup> membergroup = memberManager.findAllMemberGroup();
		log.info("findAllMemberList  方法记录数{}条",membergroup.size());
	}
	
	

	    @Test
		public void enableOrDisableMemberByIds(){
			List<Long> ids=new ArrayList<Long>();
			ids.add(1L);
			ids.add(2L);
			try {
				memberManager.enableOrDisableMemberByIds(ids, 1);
			} catch (Exception e) {
				logger.info("必须确保有效的用户名不允许重复!");
				return;
			} 
			logger.info("------------enableOrDisableByIds ------ok");
		}
	  
	  
		@Test
		public final void testfindMemberById(){
			Long memberId = 2L;
			MemberPersonalDataCommand lr = memberManager.findMemberById(memberId);
			

				logger.info("------------------" + lr.getId() + "===================" + lr.getLoginName());
			
		}
	  
		
	
	
	

}
