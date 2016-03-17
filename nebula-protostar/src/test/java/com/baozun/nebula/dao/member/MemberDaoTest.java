package com.baozun.nebula.dao.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
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

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.command.MemberGroupRelationCommand;
import com.baozun.nebula.command.RoleCommand;
import com.baozun.nebula.dao.auth.RoleDao;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.utils.JsonFormatUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:loxia-hibernate-context.xml",
			"classpath*:loxia-service-context.xml",
			"classpath*:spring.xml"})
@ActiveProfiles("dev")
public class MemberDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final Logger log = LoggerFactory.getLogger(MemberDaoTest.class);
	
	@Autowired
	private MemberDao memberDao;
	
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.auth.RoleDao#findAllList()}.
	 * 获取所有会员
	 */
	@Test
	public void testfindAllMemberList() {
		List<MemberCommand> member = memberDao.findAllMemberList();
		log.info("findAllMemberList  方法记录数{}条",member.size());
	}
	
	
	@Test
	public void testfindMemberListByIds() {
		List<Long> ids=new ArrayList<Long>();
		 ids.add(new Long(6L));
		 ids.add(new Long(7L));
		 List<Member> members= memberDao.findMemberListByIds(ids);
		 log.info("findMemberListByIds  方法记录数{}条", members.size());
	}
	
	@Test
	public void testfindAllMemberGroupList() {
		List<MemberGroupRelationCommand> MGR=memberDao.findAllMemberGroupList();
		log.info("findAllMemberGroupList  方法记录数{}条",MGR.size());
	}
	
	
	
	
	
	@Test
	public void findMemberListByQueryMapWithPage(){
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		Sort[] sorts=Sort.parse("a.login_name desc");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "%20%");
		Pagination<MemberCommand> page = memberDao.findMemberListByQueryMapWithPage(p, sorts,map);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	}
	
	
	
	  @Test
	  public void enableOrDisableMemberByIds() {
		  List<Long> ids=new ArrayList<Long>();
		  ids.add(1L);
		  
		List<Member> memberlist = new ArrayList<Member>();
		memberlist= memberDao.findMemberListByIds(ids);
		
		Member member = (Member) ((memberlist==null||memberlist.size()==0)?null:memberlist.get(0));
		
		log.info("enableOrDisableMemberByIds  禁用前会员状态{}", member==null?"null":member.getLifecycle()); 
			 
		 memberDao.enableOrDisableMemberByIds(ids, 0);
		 /*List<Long> ids1=new ArrayList<Long>();
		 ids1.add(new Long(6L));*/
		 
		 memberlist= memberDao.findMemberListByIds(ids);
			
		 member = (Member) ((memberlist==null||memberlist.size()==0)?null:memberlist.get(0));
			
		 log.info("enableOrDisableMemberByIds  禁用后会员状态{}", member==null?"null":member.getLifecycle()); 
	 }
	  
	  
	  
	  @Test
	  public void findMemberListByLoginNams() {
		  List<String> loginNams=new ArrayList<String>();
		  loginNams.add("wyd");
		  loginNams.add("mdl");
		  List<Member> member=memberDao.findMemberListByLoginNams(loginNams);
		  log.info("findMemberListByLoginNams  方法返回{}", loginNams.size());
	 }
	  
	  
	  
	  
	
	

	

}
