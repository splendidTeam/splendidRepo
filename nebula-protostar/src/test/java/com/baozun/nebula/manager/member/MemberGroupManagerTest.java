/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.manager.member;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.web.command.MemberGroupRelationResultCommand;


/**
 * 
 * @author dongliang.ma
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class MemberGroupManagerTest{
	
	@Autowired
	private MemberGroupManager memberGroupManager;

	private static final Logger	log	= LoggerFactory.getLogger(MemberGroupManagerTest.class);
	
	@Test
	public void testFindMemberGroupListByQueryMap(){
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		
		paraMap.put("groupName", "%10%");
		
		Sort[] sorts = null;
		sorts=Sort.parse("mgr.name asc");
		
		List<MemberGroup> memberGroupList = memberGroupManager.findMemberGroupListByQueryMap(paraMap,sorts);
		
		log.info("~~~~~~~~~~~:{}",JsonFormatUtil.format(memberGroupList));
	}
	
	@Test
	public void testRemoveGroupByIds(){
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		
		paraMap.put("groupName", "%%");
		
		Sort[] sorts = null;
		sorts=Sort.parse("mgr.name asc");
		
		List<MemberGroup> memberGroupList = memberGroupManager.findMemberGroupListByQueryMap(paraMap,sorts);
		
		log.info("before Remove~~~~~~~~~~:{}",JsonFormatUtil.format(memberGroupList));
		
		
		List<Long> ids = new ArrayList<Long>();
		//分组5id：6；分组6id：7
		ids.add(6L);
		ids.add(7L);
		
		
		memberGroupManager.removeGroupByIds(ids);
		
		memberGroupList = memberGroupManager.findMemberGroupListByQueryMap(paraMap,sorts);
		
		log.info("after Remove~~~~~~~~~~:{}",JsonFormatUtil.format(memberGroupList));
		
	}
	
	@Test
	public void testFindMemberGroupListByGroupIds(){
		
		Long[] groupIds = new Long[]{1L,2L,3L};
		
		List<MemberGroup> memberGroupList = memberGroupManager.findMemberGroupListByGroupIds(groupIds);
		
		log.info("~~~~~~~~~~~:{}",JsonFormatUtil.format(memberGroupList));
	}
	
	@Test
	public void testVolidateGroupName(){
		
		String groupName = "分组7";
		
		boolean flag = memberGroupManager.validateGroupName(groupName);
		
		log.info("~~~~~~~~~~~:{}",flag);
	}
	//createOrUpdateMemberGroup
	@Test
	public void testCreateOrUpdateMemberGroup(){
		
		String groupName = "小小小小";
		boolean flag = memberGroupManager.validateGroupName(groupName);
		
		log.info("~~~~~~~~~~~:{}",flag);
		
		MemberGroup memberGroup = new MemberGroup();
		memberGroup.setCreateTime(new Date());
		memberGroup.setLifecycle(1);
		memberGroup.setVersion(new Date());
		memberGroup.setName(groupName);
		memberGroupManager.createOrUpdateMemberGroup(memberGroup);
		
		boolean afterflag = memberGroupManager.validateGroupName(groupName);
		
		log.info("~~~~~~~~~~~:{}",afterflag);
	}
	
	@Test
	public void testBindMemberGroup(){
		Long[] memberIds = {2L};
		Long[] groupIds ={3L};
		MemberGroupRelationResultCommand ic = memberGroupManager.bindMemberGroup(memberIds, groupIds);
		log.info("=_+:success：{};repeat:{}",ic.getSuccessMap().size(),ic.getRepeatMap().size());
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.ItemCategoryManagerImpl#deleteItemCategory(java.lang.Long[], java.lang.Long)}.
	 */
	@Test
	public void testUnBindMemberGroup(){
		Long[] memberIds = {1L,2L};
		Long groupId = 2L;
		boolean flag = memberGroupManager.unBindMemberGroup(memberIds, groupId);
		log.info("+_+:{}",flag);
	}
}

