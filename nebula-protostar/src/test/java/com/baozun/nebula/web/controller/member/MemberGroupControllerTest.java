package com.baozun.nebula.web.controller.member;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.manager.member.MemberGroupManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.web.command.MemberGroupRelationResultCommand;


public class MemberGroupControllerTest{

	
	private IMocksControl 			control;
	
	private MemberGroupController 	memberGroupController;
	
	private MemberManager			memberManager;
	private MemberGroupManager		memberGroupManager;
	
	
	
	
	private HttpServletRequest 		request;
	private HttpServletResponse		response;
	
	private Model					model;
	
	@Before
	public void init(){
		memberGroupController	= new MemberGroupController();
		control = createNiceControl();
		
		memberManager = control.createMock("memberManager", MemberManager.class);
		ReflectionTestUtils.setField(memberGroupController, "memberManager", memberManager);
		
		memberGroupManager = control.createMock("memberGroupManager", MemberGroupManager.class);
		ReflectionTestUtils.setField(memberGroupController, "memberGroupManager", memberGroupManager);
		
		request	= control.createMock("HttpServletRequest",HttpServletRequest.class);
		response = control.createMock("HttpServletResponse",HttpServletResponse.class);
		
		model = control.createMock("model", Model.class);
	}
	
	@Test
	public void testGroupListJson(){
		
		Pagination<MemberGroup> pageMemberGroup = new Pagination<MemberGroup>();
		List<MemberGroup> memberGrList = new ArrayList<MemberGroup>();
		
		MemberGroup memberGroup1 =new MemberGroup();
		memberGroup1.setId(1L);
		memberGroup1.setName("小小");
		memberGrList.add(memberGroup1);
		
		pageMemberGroup.setItems(memberGrList);
		
		
		Sort[] sorts=Sort.parse("mgr.name asc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("name","分组");
		
		EasyMock.expect(memberGroupManager
				.findMemberGroupListByQueryMap(paraMap, sorts)).andReturn(memberGrList);
	}
	@Test
	public void testRemoveGroupByIds(){
		Map<String, Object> mockResult = new HashMap<String, Object>();
		mockResult.put("result", "success");
		String ids ="1,2";
		
		List<Long> idList =new ArrayList<Long>();
		idList.add(1L);
		idList.add(2L);
		
		EasyMock.expect(memberGroupManager.removeGroupByIds(idList)).andReturn(true);
		
		control.replay();
		try{
			assertEquals(mockResult,memberGroupController.removeGroupByIds(ids, model, request, response));
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//?
	@Test
	public void testSaveGroup(){
		Map<String, Object> mockResult = new HashMap<String, Object>();
		mockResult.put("result", "success");
		
		String groupName ="xixixixi";
		
		EasyMock.expect(memberGroupManager.validateGroupName(groupName)).andReturn(false);
		
			
			MemberGroup memberGroup = new MemberGroup();
			memberGroup.setName(groupName);
			
			memberGroupManager.createOrUpdateMemberGroup(memberGroup);
		
		EasyMock.expectLastCall();
		control.replay();
		try{
//			assertEquals(mockResult,memberGroupController.saveGroup(groupName, model, request, response));
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBindMemberGroup(){
		Map<String, Object> mockResult = new HashMap<String, Object>();
		mockResult.put("result", "success");
		
		
		Long[] memberIds =new Long[]{1L,2L};
		Long[] groupIds =new Long[]{10L};
		
		MemberGroupRelationResultCommand mockResultbd =new MemberGroupRelationResultCommand();
		Map<String,List<Member>> successMap = new HashMap<String, List<Member>>();
		List<Member> memberList = new ArrayList<Member>();
		Member member = new Member();
		member.setId(7L);
		member.setLifecycle(1);
		memberList.add(member);
		successMap.put("冰冰冰", memberList);
		mockResultbd.setSuccessMap(successMap);
		mockResultbd.setFailMap(successMap);
		mockResultbd.setRepeatMap(successMap);
		
		EasyMock.expect(memberGroupManager
				.bindMemberGroup(memberIds, groupIds))
				.andReturn(mockResultbd);
		control.replay();
		
		try{
			assertEquals(mockResult,memberGroupController.bindMemberGroup(memberIds, groupIds, model, request, response));
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testUnBindMemberGroup(){
		
		Map<String, Object> mockResult = new HashMap<String, Object>();
		mockResult.put("result", "success");
		
		Long[] memberIds =new Long[]{1L,2L};
		Long groupId =3L;
		
		EasyMock.expect(memberGroupManager.unBindMemberGroup(memberIds, groupId)).andReturn(true);
		
		control.replay();
		
		try{
			assertEquals(mockResult,memberGroupController.unBindMemberGroup(memberIds, groupId, model, request, response));
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
