package com.baozun.nebula.manager.system;

import static org.junit.Assert.fail;

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

import com.baozun.nebula.command.OptionGroupCommand;
import com.baozun.nebula.manager.system.option.OptionManager;
import com.baozun.nebula.model.system.ChooseOption;


/**
 * test
 * @author xingyu.liu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")  
public class ChooseOptionManagerTest{
	Logger	logger	= LoggerFactory.getLogger(ChooseOptionManagerTest.class);
	
	@Autowired
	private ChooseOptionManager chooseOptionManager;
	@Test
	public final void testFindOptionGroupListByQueryMapWithPage() {
		Sort[] sorts=Sort.parse("t.group_code desc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("groupCode", "%E%");
		Page page=new Page(1,10);
		List<OptionGroupCommand> pageOptionCommand = chooseOptionManager.findOptionGroupListByQueryMapWithPage( sorts, paraMap);
		logger.info("-----------------------------{}",pageOptionCommand.size());
	}
	
	@Test
	public final void testValidateOptionGroupCode() {
		Boolean flag = chooseOptionManager.validateOptionGroupCode("MEMBER_SOURCE");
		logger.info("-----------------------------{}",flag);
	}
	
	@Test
	public void testCreateOrUpdateOptionGroup() {
		
		Boolean flag = chooseOptionManager.validateOptionGroupCode("OPTION_TESTaaa");
		Boolean flag2 = chooseOptionManager.validateOptionGroupCode("OPTION_TESTbbb");
		logger.info("-----------------新增前------------{}",flag+"|||"+flag2);
		if(!flag){
			ChooseOption chooseOption1 =new ChooseOption();
			chooseOption1.setGroupCode("OPTION_TESTaaa");
			chooseOption1.setGroupDesc("OPTION_TESTaaa");
			chooseOption1.setLifecycle(1);
			chooseOption1.setOptionLabel("有效");
			chooseOption1.setOptionValue("1");
			ChooseOption chooseOption2 =new ChooseOption();
			chooseOption2.setGroupCode("OPTION_TESTbbb");
			chooseOption2.setGroupDesc("OPTION_TESTbbb");
			chooseOption2.setLifecycle(1);
			chooseOption2.setOptionLabel("有效");
			chooseOption2.setOptionValue("1");
			
			ChooseOption[] cop = new ChooseOption[2];
			cop[0]=chooseOption1;
			cop[1]=chooseOption2;
			
			chooseOptionManager.createOrUpdateOptionGroup(cop);
			
			flag = chooseOptionManager.validateOptionGroupCode("OPTION_TESTaaa");
			flag2 = chooseOptionManager.validateOptionGroupCode("OPTION_TESTbbb");
			logger.info("--------------新增后---------------{}",flag+"|||"+flag2);
		}
		
	}
	@Test
	public final void validateOptionValue() {
		Boolean flag = chooseOptionManager.validateOptionValue("MEMBER_SOURCE", "2");
		logger.info("-----------------------------{}",flag);
	}
	@Test
	public final void findOptionListByQueryMapWithPage() {
		Sort[] sorts=Sort.parse("group_code desc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("groupCode", "%E%");
		Page page=new Page(1,10);
		Pagination<ChooseOption> chooseOption = chooseOptionManager.findOptionListByQueryMapWithPage("groupCode", page, sorts, paraMap);
		logger.info("-----------------------------{}",chooseOption.getCount());
	}
	@Test
	public void enableOrDisableOptionByIds(){
	    List<Long> ids=new ArrayList<Long>();
		ids.add(4L);
		try {
			   chooseOptionManager.enableOrDisableOptionByIds(ids, 1);
			}catch (Exception e) {
					logger.info("------------------");
					return;
			} 
		
		logger.info("------------enableOrDisableOptionByIds ------ok");
		}
	@Test
	public final void findChooseOptionById() {

		 ChooseOption CO= chooseOptionManager.findChooseOptionById(99L);
		 logger.info("findChooseOptionById  {}", CO.getGroupCode());
	}
	@Test
	public void testCreateOrUpdateOption() {
		
		Boolean flag = chooseOptionManager.validateOptionValue("OPTION_TTTTTTTTTTTTTTTTTTTT", "4");
		logger.info("-----------------新增前------------{}",flag+"|||");
		if(!flag){
			ChooseOption chooseOption =new ChooseOption();
			chooseOption.setGroupCode("OPTION_TTTTTTTTTTTTTTTTTTTT");
			chooseOption.setGroupDesc("OPTION_TTTTTTTTTTTTTTTTTTTT");
			chooseOption.setLifecycle(1);
			chooseOption.setOptionLabel("有效");
			chooseOption.setOptionValue("1");
			
			chooseOptionManager.createOrUpdateOption(chooseOption);
			
			flag = chooseOptionManager.validateOptionValue("OPTION_TTTTTTTTTTTTTTTTTTTT", "1");
			logger.info("--------------新增后---------------{}",flag+"|||");
		}
		
	}
	
	
	

}
