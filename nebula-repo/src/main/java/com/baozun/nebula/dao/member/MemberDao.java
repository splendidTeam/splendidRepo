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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.command.MemberGroupRelationCommand;
import com.baozun.nebula.command.member.SimpleMemberCombo;
import com.baozun.nebula.model.member.Member;

/**
 * MemberDao
 * 
 * @author Justin
 */
public interface MemberDao extends GenericEntityDao<Member, Long>{

	/**
	 * 获取所有Member列表
	 * 
	 * @return
	 */
	@NativeQuery(model = MemberCommand.class)
	List<MemberCommand> findAllMemberList();

	/**
	 * 通过ids获取Member列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = Member.class)
	List<Member> findMemberListByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过ids获取Member列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = Member.class)
	List<Member> findMemberList(@QueryParam("synthesize") List<Long> synthesize,@QueryParam("type") String type);

	/**
	 * 通过ids获取Member列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = Member.class)
	List<Member> findMembertypeList(@QueryParam("synthesize") List<String> synthesize,@QueryParam("type") String type);

	@NativeQuery(model = MemberGroupRelationCommand.class)
	List<MemberGroupRelationCommand> findAllMemberGroupList();

	/**
	 * 通过参数map获取Member列表
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberCommand.class)
	List<MemberCommand> findMemberListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取Member列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = MemberCommand.class)
	Pagination<MemberCommand> findMemberListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 通过ids批量启用或禁用Member 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */

	@NativeUpdate
	Integer enableOrDisableMemberByIds(@QueryParam("ids") List<Long> ids,@QueryParam("state") Integer state);

	/**
	 * 通过多个用户名称查询会员列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = Member.class)
	List<Member> findMemberListByLoginNams(@QueryParam("loginNames") List<String> loginNames);

	/**
	 * 根据分类id 查询分类下面正在启用的会员
	 * 
	 * @param groupIds
	 * @return
	 */
	@NativeQuery(model = Member.class)
	List<Member> findMemberEffectiveMemberIdByGroupId(@QueryParam("groupIds") List<Long> groupIds);

	/**
	 * 获取有效的Member列表 lifecycle =1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MemberCommand.class)
	List<MemberCommand> findAllEffectMemberList();

	/**
	 * 通过参数map获取有效的Member列表 强制加上lifecycle =1
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberCommand.class)
	List<MemberCommand> findEffectMemberListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取有效的Member列表 强制加上lifecycle =1
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = MemberCommand.class)
	Pagination<MemberCommand> findEffectMemberListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 根据itemIds,更新是否加入分类状态为state
	 * 
	 * @param ids
	 */
	@NativeUpdate
	Integer updateMemberIsAddGroup(@QueryParam("memberIds") List<Long> memberIds,@QueryParam("state") Integer state);

	@NativeQuery(model = Member.class)
	Member findMemberByLoginEmail(@QueryParam("loginEmail") String loginEmail);

	@NativeQuery(model = Member.class)
	Member findMemberByLoginNameAndPasswd(@QueryParam("loginName") String loginName,@QueryParam("password") String password);

	@NativeQuery(model = Member.class)
	Member findMemberByLoginName(@QueryParam("loginName") String loginName);

	@NativeQuery(model = Member.class)
	Member findMemberById(@QueryParam("id") Long id);

	@NativeQuery(model = Member.class)
	Member findMemberByLoginMobile(@QueryParam("loginMobile") String loginMobile);

	@NativeUpdate
	Integer updatePasswd(@QueryParam("memberId") Long memberId,@QueryParam("newPwd") String newPwd);

	/** 查询第三方会员 */
	@NativeQuery(model = Member.class)
	Member findThirdMemberByUidAndSource(
			@QueryParam("thirdPartyIdentify") String thirdPartyIdentify,
			@QueryParam("source") Integer source,
			@QueryParam("type") Integer type);

	/**
	 * 根据分组ID列表查询有效的会员ID
	 * 
	 * @param gidList
	 * @return
	 */
	@NativeQuery(alias = "id",clazzes = Long.class)
	List<Long> findEffectMemberIdListByGroupIdList(@QueryParam("groupIds") List<Long> gidList);

	/**
	 * 查询所有有效会员ID列表
	 * 
	 * @param idList
	 * @return
	 */
	@NativeQuery(alias = "id",clazzes = Long.class)
	List<Long> findAllEffectMemberIdList();

	/**
	 * 根据id列表，查询会员的简单信息
	 * 
	 * @param inIdList
	 * @return
	 */
	@NativeQuery(model = SimpleMemberCombo.class)
	List<SimpleMemberCombo> findSimpleMemberComboListByIdList(@QueryParam("ids") List<Long> inIdList);

	@NativeQuery(alias = "id",clazzes = Long.class)
	List<Long> findMemberIdsByComsumeeAmtPromotionId(
			@QueryParam("shopId") Long shopId,
			@QueryParam("amt") BigDecimal amt,
			@QueryParam("prmId") String prmId);

	/**
	 * 根据会员id更新他对应的groupId
	 * 
	 * @param memberId
	 * @param groupId
	 * @return
	 */
	@NativeUpdate
	int updateMemberGroupIdById(@QueryParam("memberId") Long memberId,@QueryParam("groupId") Long groupId);
}
