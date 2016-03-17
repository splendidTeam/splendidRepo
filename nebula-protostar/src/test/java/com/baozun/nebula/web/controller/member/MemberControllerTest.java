package com.baozun.nebula.web.controller.member;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.member.MemberGroupManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.command.UserManagerCommand;


public class MemberControllerTest {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(MemberControllerTest.class);
	
	
	private IMocksControl 			control;
	
	private MemberController 	memberController;
	
	private MemberManager			memberManager;
	private MemberGroupManager		memberGroupManager;
	

	
	
	
	private HttpServletRequest 		request;
	private HttpServletResponse		response;
	
	private Model					model;
	
	@Before
	public void init(){
		memberController	= new MemberController();
		control = createNiceControl();
		

		
		memberManager = control.createMock("memberManager", MemberManager.class);
		ReflectionTestUtils.setField(memberController, "memberManager", memberManager);
		
		memberGroupManager = control.createMock("memberGroupManager", MemberGroupManager.class);
		ReflectionTestUtils.setField(memberController, "memberGroupManager", memberGroupManager);
		
		
		
		request	= control.createMock("HttpServletRequest",HttpServletRequest.class);
		response = control.createMock("HttpServletResponse",HttpServletResponse.class);
		
		model = control.createMock("model", Model.class);
	}
	
	
	
	@Test
	public void memberListJson(){
		QueryBean queryBean = new QueryBean();
		Map<String,Object> paraMap	= new HashMap<String, Object>();
		paraMap.put("lifecycle", "1");
		queryBean.setParaMap(paraMap);
		
		Page page = new Page(0, 100);
		queryBean.setPage(page);
		
		Sort[] sorts = null;
		queryBean.setSorts(sorts);
		
		Pagination<MemberCommand> mockResult = new Pagination<MemberCommand>();
		
		mockResult.setCount(7);
		mockResult.setSize(5);
		mockResult.setStart(0);
		List<MemberCommand> list = new ArrayList<MemberCommand>();
		
		
		MemberCommand mc = new MemberCommand();
		mc.setId(7L);
		mc.setLoginEmail("abc@.com");
		mc.setLoginMobile("123456789");
		list.add(mc);
		mockResult.setItems(list);
		
		EasyMock.expect(memberManager.findMemberListByQueryMapWithPage(queryBean.getPage(), queryBean.getSorts(), queryBean.getParaMap())).andReturn(mockResult);
		
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		//assertEquals(mockResult, itemCategoryController.findNoctItemListJson(model, queryBean, request, response));
	}
	@Test
	public void testMemberList(){
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
				// 验证
		assertEquals("member/member-list", memberController.MemberList(model, request, response));
	}
	@Test
	public void viewMember(){
		MemberPersonalDataCommand memberPersonalDataCommand=new MemberPersonalDataCommand();
		memberPersonalDataCommand.setId(1L);
		EasyMock.expect(memberManager.findMemberById(1L)).andReturn(new MemberPersonalDataCommand());
		control.replay();
		try {
			assertEquals("member/member-detail", memberController.viewMember(model, memberPersonalDataCommand.getId(), request, response)); 
		} catch (Exception e) { 
		}
		
	}
	@Test
	public void enableOrDisableManyMemberByIds() throws Exception {
		String ids = "1,2";
		String[] idArray = ids.split(",");
		int length=idArray.length;
		Long[] idArray2=new Long[length];
		List<Long> list = new ArrayList<Long>();
		for(int i=0;i<length;i++){
			idArray2[i]=Long.parseLong(idArray[i]);
			list.add(idArray2[i]);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result","success");
		Integer state=1;
		EasyMock.expect(memberManager.enableOrDisableMemberByIds(list, state)).andReturn(list.size());
		control.replay();
		try {
			assertEquals(map, memberController.enableOrDisableManyMemberByIds(ids, state,model, request, response));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
