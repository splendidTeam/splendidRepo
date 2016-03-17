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

import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.member.MemberPersonalData;

/**
 * MemberPersonalDataDao
 * @author  Justin
 *
 */
public interface MemberPersonalDataDao extends GenericEntityDao<MemberPersonalData,Long>{

	
	
	/**
	 * 通过ids获取MemberPersonalData列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MemberPersonalData.class)
	List<MemberPersonalData> findMemberPersonalDataListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取MemberPersonalData列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberPersonalData.class)
	List<MemberPersonalData> findMemberPersonalDataListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取MemberPersonalData列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MemberPersonalData.class)
	Pagination<MemberPersonalData> findMemberPersonalDataListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过id获取用户
	 * @param loginName
	 * @return
	 */
	@NativeQuery(model = MemberPersonalDataCommand.class)
	MemberPersonalDataCommand findById(@QueryParam("memberId") Long memberId);

	
	/**
	 * 通过ids批量启用或禁用MemberPersonalData
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	
	@NativeUpdate
	void enableOrDisableMemberPersonalDataByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除MemberPersonalData
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeMemberPersonalDataByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的MemberPersonalData列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MemberPersonalData.class)
	List<MemberPersonalData> findAllEffectMemberPersonalDataList();
	
	/**
	 * 通过参数map获取有效的MemberPersonalData列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberPersonalData.class)
	List<MemberPersonalData> findEffectMemberPersonalDataListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的MemberPersonalData列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MemberPersonalData.class)
	Pagination<MemberPersonalData> findEffectMemberPersonalDataListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	
	
	/**
	 * 通过id获取用户
	 * @param memberId
	 * @return
	 */
	@NativeQuery(model = MemberPersonalData.class)
	MemberPersonalData findByMemberId(@QueryParam("memberId") Long memberId);
	
	/**
	 * 根据手机号码获取用户详细信息
	 * @param mobile
	 * @return
	 */
	@NativeQuery(model = MemberPersonalData.class)
	MemberPersonalData findMemberPersonDataByMobile(@QueryParam("mobile") String mobile);
	/**
	 * 通过昵称获取用户
	 * @param nickname
	 * @return
	 */
	@NativeQuery(model = MemberPersonalData.class)
	MemberPersonalData findByNickname(@QueryParam("nickname") String nickname);
	
	
}
