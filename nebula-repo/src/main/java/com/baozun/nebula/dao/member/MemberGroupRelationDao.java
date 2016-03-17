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

import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.model.product.ItemCategory;

/**
 * MemberGroupRelationDao
 * @author  Justin
 *
 */
public interface MemberGroupRelationDao extends GenericEntityDao<MemberGroupRelation,Long>{

	/**
	 * 获取所有MemberGroupRelation列表
	 * @return
	 */
	@NativeQuery(model = MemberGroupRelation.class)
	List<MemberGroupRelation> findAllMemberGroupRelationList();
	
	/**
	 * 通过ids获取MemberGroupRelation列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MemberGroupRelation.class)
	List<MemberGroupRelation> findMemberGroupRelationListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取MemberGroupRelation列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberGroupRelation.class)
	List<MemberGroupRelation> findMemberGroupRelationListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取MemberGroupRelation列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MemberGroupRelation.class)
	Pagination<MemberGroupRelation> findMemberGroupRelationListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用MemberGroupRelation
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableMemberGroupRelationByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除MemberGroupRelation
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeMemberGroupRelationByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的MemberGroupRelation列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MemberGroupRelation.class)
	List<MemberGroupRelation> findAllEffectMemberGroupRelationList();
	
	/**
	 * 通过参数map获取有效的MemberGroupRelation列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberGroupRelation.class)
	List<MemberGroupRelation> findEffectMemberGroupRelationListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的MemberGroupRelation列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MemberGroupRelation.class)
	Pagination<MemberGroupRelation> findEffectMemberGroupRelationListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 查询出所有 会员id 在memberIds和 分组groupid 在groupIds中的MemberGroupRelation
	 * 
	 * @param memberIds
	 *            会员id 数组
	 * @param groupIds
	 *            分组id 数组
	 * @return
	 */
	@NativeQuery(model = MemberGroupRelation.class)
	List<MemberGroupRelation> findMemberGroupRelationByMemberIdAndGroupId(
			@QueryParam("memberIds") Long[] memberIds,
			@QueryParam("groupIds") Long[] groupIds);
	
	/**
	 * 将一个会员关联到一个分组下
	 * 
	 * @param memberId
	 *            会员id
	 * @param groupId
	 *            分组id
	 * @return
	 */
	@NativeUpdate
	Integer bindMemberGroup(@QueryParam("memberId") Long memberId,@QueryParam("groupId") Long groupId);
	
	/**
	 * 把一个或者多个会员从一个分组下解除关联(关系表物理删除)
	 * 
	 * @param memberIds
	 *            会员id 数组
	 * @param groupId
	 *            分组id
	 * @return
	 */
	@NativeUpdate
	Integer unBindMemberGroup(@QueryParam("memberIds") Long[] memberIds,@QueryParam("groupId") Long groupId);
	
	/**
	 * 查找当前memberId所有分类条目
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = MemberGroupRelation.class)
	List<MemberGroupRelation> findMemberGroupRelationListByMemberId(@QueryParam("memberId") Long memberId);
	 
}
