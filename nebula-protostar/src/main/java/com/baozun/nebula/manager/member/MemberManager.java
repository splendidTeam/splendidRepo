package com.baozun.nebula.manager.member;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.command.MemberGroupRelationCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.command.PropertyCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.auth.UserRole;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.system.ChooseOption;

public interface MemberManager extends BaseManager {
	/**
	 * 分页获取Member列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	Pagination<MemberCommand> findMemberListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	

	
	
	/**
	 * 查找所有会员分组
	 * 
	 * 
	 * @return
	 */
	public List<MemberGroup> findAllMemberGroup();
	
	
	
	/**
	 * 通过ids批量启用或禁用member 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	public Integer enableOrDisableMemberByIds(List<Long> ids,Integer state);
	
	
	
	
	
	/**
	 * 找出会员的全部详细信息
	 * 
	 * @param memberId
	 * @return
	 */

	public MemberPersonalDataCommand findMemberById(Long memberId);
	
	
	
	
	
	/**
	 * 根据会员ids查询会员
	 * @param memberIds
	 * @return
	 */
	public List<Member>findMemberListByMemberIds(Long[] memberIds);
	
	
	
	
	
	/**
	 * 根据会员的ids，更新会员详情表的是否已分类状态为state
	 * @param ids
	 * @param state
	 * @return
	 */
	public Integer updateMemberIsAddGroup(List<Long> ids,Integer state);
	
	
	/**
	 * 查询该会员的所有收藏列表
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<MemberFavoritesCommand> memberFavoritesList(Page page,Sort[] sorts,Map<String, Object> searchParam,Long memberId);
	
	/**
	 * 分页查询联系人地址信息
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<ContactCommand> findContactCommandByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam, Long memberId );

}
