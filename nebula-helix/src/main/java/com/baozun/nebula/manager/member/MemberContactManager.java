package com.baozun.nebula.manager.member;

import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.manager.BaseManager;

public interface MemberContactManager extends BaseManager{

	/**
	 * 分页查询联系人地址信息
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<ContactCommand> findContactCommandByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam);
	
	/**
	 * 
	 * 查询收货人地址列表
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<ContactCommand> findContactList(Page page,Sort[] sorts,Map<String, Object> searchParam);
	
	/**
	 * 
	 * 创建或修改收货人地址
	 * @param contact
	 * @return
	 */
	public ContactCommand createOrUpdateContact(ContactCommand contact);
	
	/**
	 * 删除收货人地址
	 * @param contactId
	 * @param memberId
	 * @return
	 */
	public Integer removeContactById(Long contactId,Long memberId);
	
	/**
	 * 查询单个收货人地址
	 * @param contactId
	 * @return
	 * @deprecated {@link com.baozun.nebula.manager.member.MemberContactManager#findContactById(Long, Long)}
	 */
	public ContactCommand findContactById(Long contactId);
	
	/**
	 * 查询单个收货人地址
	 * @param contactId
	 * @param memberId
	 * @return
	 */
	public ContactCommand findContactById(Long contactId, Long memberId);
	
	/**
	 * 设置默认收货人地址
	 * @param contactId
	 * @return
	 */
	public Integer updateDefaultById(Long contactId,Long memberId);
}
