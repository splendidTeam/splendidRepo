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
import com.baozun.nebula.model.member.MemberCryptoguard;

/**
 * MemberCryptoguardDao
 * @author  Justin
 *
 */
public interface MemberCryptoguardDao extends GenericEntityDao<MemberCryptoguard,Long>{

	/**
	 * 获取所有MemberCryptoguard列表
	 * @return
	 */
	@NativeQuery(model = MemberCryptoguard.class)
	List<MemberCryptoguard> findAllMemberCryptoguardList();
	
	/**
	 * 通过ids获取MemberCryptoguard列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MemberCryptoguard.class)
	List<MemberCryptoguard> findMemberCryptoguardListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取MemberCryptoguard列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberCryptoguard.class)
	List<MemberCryptoguard> findMemberCryptoguardListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取MemberCryptoguard列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MemberCryptoguard.class)
	Pagination<MemberCryptoguard> findMemberCryptoguardListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用MemberCryptoguard
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableMemberCryptoguardByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除MemberCryptoguard
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeMemberCryptoguardByIds(@QueryParam("ids")List<Long> ids);
	

	/**
	 * 删除密保通过会员Id
	 * @param memberId
	 */
	@NativeUpdate
	void removeMemberCryptoguardByMemberId(@QueryParam("memberId")Long memberId);
	
	/**
	 * 获取有效的MemberCryptoguard列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MemberCryptoguard.class)
	List<MemberCryptoguard> findAllEffectMemberCryptoguardList();
	
	/**
	 * 通过参数map获取有效的MemberCryptoguard列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MemberCryptoguard.class)
	List<MemberCryptoguard> findEffectMemberCryptoguardListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的MemberCryptoguard列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MemberCryptoguard.class)
	Pagination<MemberCryptoguard> findEffectMemberCryptoguardListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	


	@NativeQuery(model = MemberCryptoguard.class)
	List<MemberCryptoguard> findByMemberId(@QueryParam("memberId") Long memberId);
	
	@NativeQuery(model = MemberCryptoguard.class)
	MemberCryptoguard findByQa(@QueryParam("q") String q, @QueryParam("a") String a);
}
