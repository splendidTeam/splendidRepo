package com.baozun.nebula.dao.system;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.OptionGroupCommand;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.utils.JsonFormatUtil;
/**
 * test
 * @author xingyu.liu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ChooseOptionDaoTest {
	private static final Logger log = LoggerFactory
			.getLogger(ChooseOptionDaoTest.class);
	@Autowired
	private ChooseOptionDao chooseOptionDao;
	@Test
	public final void testFindOptionGroupList() {
		Sort[] sorts=Sort.parse("t.group_code desc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("groupCode", "ORDER_PAYTYPE_TYPE");
		Page page=new Page(1,10);
		List<OptionGroupCommand> pageOptionCommand = chooseOptionDao.findOptionGroupList( sorts, paraMap);
		log.info("-----------------------------{}",pageOptionCommand.size());
	}
	@Test
	public final void testvalidateOptionGroupCode() {
		ChooseOption cOption = chooseOptionDao.validateOptionGroupCode("MEMBER_TYPE");
		log.info("-----------------------------{}",JsonFormatUtil.format(cOption));
	}
	@Test
	public final void validateOptionValue() {
		ChooseOption Option = chooseOptionDao.validateOptionValue("MEMBER_TYPE", "1");
		log.info("-----------------------------{}",JsonFormatUtil.format(Option));
	}
	@Test
	public final void findChooseOptionById() {

		 ChooseOption CO= chooseOptionDao.findChooseOptionById(99L);
		 log.info("findChooseOptionById  {}", CO.getGroupCode());
	}

}
