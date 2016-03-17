package com.baozun.nebula.web.controller.system;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.utils.query.bean.QueryBean;

public class ChooseOptionControllerTest {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(ChooseOptionControllerTest.class);

	private ChooseOptionController chooseOptionController;
	private IMocksControl control;
	private Model model;
	private ChooseOptionManager chooseOptionManager;
	@Before
	public void init() {
		control = createNiceControl();
		chooseOptionController = new ChooseOptionController();
		chooseOptionManager= control.createMock("chooseOptionManager", ChooseOptionManager.class);
		ReflectionTestUtils.setField(chooseOptionController, "chooseOptionManager",
				chooseOptionManager);
		
		model = control.createMock("model", Model.class);
		
	}
	@Test
	public final void testOptionGroupList(){
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testOptionGroupListJson() {
		QueryBean queryBean = new QueryBean();
		queryBean.setParaMap(new HashMap<String, Object>());
		chooseOptionController.optionGroupListJson(queryBean);
		
	}
	
	@Test
	public void testValidateOptionGroupCode(){
		String groupCode ="MEMBER_TYPE";
		EasyMock.expect(chooseOptionManager.validateOptionGroupCode(groupCode)).andReturn(true);
		
	}
	
	@Test
	public void testSaveOptionGroup(){
	
	//	EasyMock.expect(chooseOptionManager.validateOptionGroupCode("OPTION_TEST4")).andReturn(false);

		ChooseOption chooseOption1 =new ChooseOption();
		chooseOption1.setGroupCode("OPTION_TESTccc");
		chooseOption1.setGroupDesc("OPTION_TESTccc");
		chooseOption1.setLifecycle(1);
		chooseOption1.setOptionLabel("有效");
		chooseOption1.setOptionValue("1");
		ChooseOption chooseOption2 =new ChooseOption();
		chooseOption2.setGroupCode("OPTION_TESTddd");
		chooseOption2.setGroupDesc("OPTION_TESTddd");
		chooseOption2.setLifecycle(1);
		chooseOption2.setOptionLabel("有效");
		chooseOption2.setOptionValue("1");
		
		ChooseOption[] cop = new ChooseOption[2];
		cop[0]=chooseOption1;
		cop[1]=chooseOption2;
			
			chooseOptionManager.createOrUpdateOptionGroup(cop);
			
		//	EasyMock.expect(chooseOptionManager.validateOptionGroupCode("OPTION_TEST4")).andReturn(true);

	}
	@Test
	public void testOptionList(){
		control.replay();
		// 验证
        assertEquals("/system/option/choose-option-list", chooseOptionController.optionList("groupCode", "groupDesc", model));

	}
	
	@Test
	public void testOptionListJson(){
		QueryBean queryBean = new QueryBean();
		Map<String,Object> paraMap	= new HashMap<String, Object>();
		paraMap.put("lifecycle", "1");
		queryBean.setParaMap(paraMap);
		
		Page page = new Page(0, 100);
		queryBean.setPage(page);
		
		Sort[] sorts = null;
		queryBean.setSorts(sorts);
		
		Pagination<ChooseOption> mockResult = new Pagination<ChooseOption>();
		
		mockResult.setCount(7);
		mockResult.setSize(5);
		mockResult.setStart(0);
		List<ChooseOption> list = new ArrayList<ChooseOption>();
		
		
		ChooseOption mc = new ChooseOption();
		mc.setId(7L);
		mc.setGroupCode("dfdfdfd");
		list.add(mc);
		mockResult.setItems(list);
		
		EasyMock.expect(chooseOptionManager.findOptionListByQueryMapWithPage("groupCode",queryBean.getPage(), queryBean.getSorts(), queryBean.getParaMap())).andReturn(mockResult);
		control.replay();
		// 验证
		assertEquals(mockResult, chooseOptionController.chooseOptionList("groupCode", model, queryBean));
	
	}
	
	@Test
	public void testenableOrDisableOptionByIds(){
		Long groupId= 2088L;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result","success");
		List<Long> list = new ArrayList<Long>();
		list.add(groupId);
		Integer state=2;
		Integer mockResult = 1;
		EasyMock.expect(chooseOptionManager.enableOrDisableOptionByIds(list, state)).andReturn(mockResult);
		control.replay();
		try {
			assertEquals(map, chooseOptionController.enableOrDisableOptionByIds(groupId, state));
		} catch (Exception e) {
			e.printStackTrace();
		}
		control.verify();
	}
	
	@Test
	public void testSaveOption(){
		ChooseOption chooseOption1 =new ChooseOption();
		chooseOption1.setGroupCode("OPTION_TEST aaaa");
		chooseOption1.setGroupDesc("OPTION_TESTbbbb");
		chooseOption1.setLifecycle(1);
		chooseOption1.setOptionLabel("有效");
		chooseOption1.setOptionValue("1");
		ChooseOption chooseOption2 =new ChooseOption();
		chooseOption2.setGroupCode("OPTION_TESTccccc");
		chooseOption2.setGroupDesc("OPTION_TESTddddd");
		chooseOption2.setLifecycle(1);
		chooseOption2.setOptionLabel("有效");
		chooseOption2.setOptionValue("1");
		
		ChooseOption[] cop = new ChooseOption[2];
		cop[0]=chooseOption1;
		cop[1]=chooseOption2;
			
			chooseOptionManager.createOrUpdateOptionGroup(cop);
			
		//	EasyMock.expect(chooseOptionManager.validateOptionGroupCode("OPTION_TEST4")).andReturn(true);


	}
	@Test
	public void testvalidateOptionValue(){
		String groupCode ="MEMBER_TYPE";
		String optionValue="333";
		EasyMock.expect(chooseOptionManager.validateOptionValue(groupCode, optionValue)).andReturn(true);
	}

}
