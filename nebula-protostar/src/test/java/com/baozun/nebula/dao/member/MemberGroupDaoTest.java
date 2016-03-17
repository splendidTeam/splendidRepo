package com.baozun.nebula.dao.member;

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
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.utils.JsonFormatUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class MemberGroupDaoTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	private static final Logger log = LoggerFactory.getLogger(MemberGroupDaoTest.class);
	
	@Autowired
	private MemberGroupDao memberGroupDao;
	
	
	
	
	@Test
	public void findAllMemberGroupList() {
		List<MemberGroup> membergroup = memberGroupDao.findAllMemberGroupList();
		log.info("findAllMemberGroupList  方法记录数{}条",membergroup.size());
	}
	
	
	
	
	@Test
	public void testfindMemberGroupListByIds() {
		List<Long> ids=new ArrayList<Long>();
		 ids.add(new Long(6L));
		 ids.add(new Long(7L));
		 List<MemberGroup> membergroup= memberGroupDao.findMemberGroupListByIds(ids);
		 log.info("findMemberListByIds  方法记录数{}条", membergroup.size());
	}
	
	
	
	/*@Test
	public void findMemberGroupListByQueryMapWithPage(){
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		Sort[] sorts=Sort.parse("a.login_name desc");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dddddddddddd", "%20%");
		Pagination<MemberGroup> page = memberGroupDao.findMemberGroupListByQueryMapWithPage(p, sorts, map);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	}*/
	

}
