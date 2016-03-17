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
 *
 */
package com.baozun.nebula.dao.member;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.command.member.SimpleMemberCombo;
import com.baozun.nebula.model.member.MemberGroup;

/**
 * MemberGroupDao
 * @author  Justin
 *
 */
public interface MemberGroupDao extends GenericEntityDao<MemberGroup,Long>{

	/**
	 * 获取所有MemberGroup列表
	 * @return
	 */
	@NativeQuery(model = MemberGroup.class)
	List<MemberGroup> findAllMemberGroupList();
	
	/**
	 * 通过ids获取MemberGroup列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MemberGroup.class)
	List<MemberGroup> findMemberGroupListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取MemberGroup列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberGroup.class)
	List<MemberGroup> findMemberGroupListByQueryMap(@QueryParam Map<String, Object> paraMap ,Sort[] sorts);
	
	/**
	 * 分页获取MemberGroup列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MemberGroup.class)
	Pagination<MemberGroup> findMemberGroupListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用MemberGroup
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableMemberGroupByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除MemberGroup
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer removeMemberGroupByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的MemberGroup列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MemberGroup.class)
	List<MemberGroup> findAllEffectMemberGroupList();
	
	/**
	 * 通过参数map获取有效的MemberGroup列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberGroup.class)
	List<MemberGroup> findEffectMemberGroupListByQueryMap(@QueryParam Map<String, Object> paraMap ,Sort[] sorts);
	
	/**
	 * 分页获取有效的MemberGroup列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MemberGroup.class)
	Pagination<MemberGroup> findEffectMemberGroupListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 验证分组名称获取分组
	 * @param groupName
	 * @return
	 */
	@NativeQuery(model = MemberGroup.class)
	MemberGroup validateGroupName(@QueryParam("groupName")String groupName);

	/**
	 * 根据id列表，查询会员分组的简单信息
	 * @param inIdList
	 * @return
	 */
	@NativeQuery(model = SimpleMemberCombo.class)
	List<SimpleMemberCombo> findSimpleMemberComboListByIdList(
			@QueryParam("ids") List<Long> inIdList);
}
