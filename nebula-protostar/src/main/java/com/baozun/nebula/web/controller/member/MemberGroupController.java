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
package com.baozun.nebula.web.controller.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;





import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.manager.member.MemberGroupManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;


@Controller
public class MemberGroupController extends BaseController{
	
	@Autowired
	private MemberManager memberManager;
	
	@Autowired
	private MemberGroupManager memberGroupManager;
	/**
	 * 显示会员分组管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/group/memberGroup.htm")
	public String memberGroup(Model model){
	
		return "/member/group/member-group";
	}
	
	/**
	 * 会员分组列表
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/group/groupList.json")
	@ResponseBody
	public Pagination<MemberGroup> groupListJson(Model model,
			@QueryBeanParam QueryBean queryBean,
			HttpServletRequest request,
			HttpServletResponse response){
		
		
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			sorts=Sort.parse("mgr.name asc");
		}
		
		List<MemberGroup> memberGroupList = memberGroupManager
				.findMemberGroupListByQueryMap(queryBean.getParaMap(),sorts);
		
		Pagination<MemberGroup> page=new Pagination<MemberGroup>(memberGroupList,memberGroupList.size(),1,1,0,Integer.MAX_VALUE);
		page.setSortStr(sorts[0].getField()+" "+sorts[0].getType());
		
		return page;
	}
	
	@RequestMapping("/group/removeGroup.json")
	@ResponseBody
	public Object removeGroupByIds(
			@RequestParam("ids") String ids,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		if (StringUtils.isNotBlank(ids)){

			String[] idArray = ids.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String sid : idArray){

				idList.add(Long.parseLong(sid));
			}
			boolean removeFlag = memberGroupManager.removeGroupByIds(idList);

			if(!removeFlag){
				return FAILTRUE;
			}
		}
		return SUCCESS;
	}
	
	@RequestMapping("/group/saveGroup.Json")
	@ResponseBody
	public Object saveGroup(
			@RequestParam("groupName") String groupName,
			@RequestParam("groupType") Integer groupType,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{


		if (StringUtils.isNotBlank(groupName)||null==groupType||groupType>2||groupType<1){

			
			boolean resultVolidate = memberGroupManager.validateGroupName(groupName);
			
			if (resultVolidate){
				//throw new BusinessException(ErrorCodes.NAME_EXISTS);
				return FAILTRUE;
			}
			
			MemberGroup memberGroup = new MemberGroup();
			memberGroup.setType(groupType);
			memberGroup.setCreateTime(new Date());
			memberGroup.setLifecycle(MemberGroup.LIFECYCLE_ENABLE);
			memberGroup.setVersion(new Date());
			memberGroup.setName(groupName);
			memberGroupManager.createOrUpdateMemberGroup(memberGroup);
			
			return SUCCESS;
		}else{
			throw new Exception();
		}
	}
	
	@RequestMapping("/group/bindMemberGroup.json")
	@ResponseBody
	public Object bindMemberGroup(
			@RequestParam("memberIds") Long[] memberIds,
			@RequestParam("groupIds") Long[] groupIds,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		memberGroupManager.bindMemberGroup(memberIds, groupIds);
		
		return SUCCESS;
	}
	
	@RequestMapping("/group/unBindMemberGroup.json")
	@ResponseBody
	public Object unBindMemberGroup(
			@RequestParam("memberIds") Long[] memberIds,
			@RequestParam("groupId") Long groupId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		
		boolean removeFlag =
				memberGroupManager.unBindMemberGroup(memberIds, groupId);
		
		if(!removeFlag){
			throw new Exception();
		}
		return SUCCESS;
	}
	
	@RequestMapping("/group/validateUnBindByMemberIdsAndGroupId.json")
	@ResponseBody
	public Object validateUnBindSel(
			@RequestParam("memberIds") Long[] memberIds,
			@RequestParam("groupId") Long groupId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		boolean flag=
				memberGroupManager.validateUnBindByMemberIdsAndGroupId(memberIds, groupId);

		if(!flag){
			//throw new Exception();
			return FAILTRUE;
		}
		return SUCCESS;
	}

}
