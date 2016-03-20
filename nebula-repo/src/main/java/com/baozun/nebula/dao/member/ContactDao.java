package com.baozun.nebula.dao.member;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.model.member.Contact;

public interface ContactDao extends GenericEntityDao<Contact,Long>{
	/**
	 * 获取所有Contact列表
	 * @return
	 */
	@NativeQuery(model = Contact.class)
	List<Contact> findAllContactListByMemberId(@QueryParam("memberId") Long memberId);
	
	/**
	 * 根据会员ID获取地址列表，可分页可排序
	 * 
	 * @param page
	 * @param sorts
	 * @param memberId
	 * @return
	 */
	@NativeQuery(model = Contact.class, value="findAllContactListByMemberId")
	Pagination<Contact> findContactsByMemberId(Page page,Sort[] sorts, @QueryParam("memberId") Long memberId);
	
	/**
	 * 查询单个收货人地址
	 * @param id
	 * @return
	 * @deprecated {@link com.baozun.nebula.dao.member.ContactDao#getByPrimaryKey(Long)}
	 */
	@NativeQuery(model = Contact.class)
	Contact findContactById(@QueryParam("id") Long id);

	/**
	 * 查询单个收货人地址
	 * @param contactId
	 * @param memberId
	 * @return
	 */
	@NativeQuery(model = Contact.class)
	Contact findContactByCantactIdAndMemberId(@QueryParam("contactId") Long contactId, @QueryParam("memberId") Long memberId);
	
	@NativeUpdate
	Integer updateContactByMemberId(@QueryParam("memberId")Long memberId,@QueryParam("isDefault")boolean isDefault);
	
	@NativeUpdate
	Integer updateContactByContactId(@QueryParam("contactId")Long contactId,@QueryParam("isDefault")boolean isDefault,
									 @QueryParam("memberId")Long memberId);
	
	/**
	 * 分页查询CommentLog
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	@NativeQuery(model=ContactCommand.class)
	public Pagination<ContactCommand> findContactByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> searchParam);
	
	
	/**
	 * 保存或修改收货人地址
	 * @return
	 */
	@NativeUpdate
	Integer updateContact(@QueryParam("id")Long id,
							@QueryParam("name")String name,
							@QueryParam("countryId")Long countryId,
							@QueryParam("provinceId")Long provinceId, 
							@QueryParam("cityId")Long cityId,
							@QueryParam("areaId")Long areaId,
							@QueryParam("townId")Long townId,
							@QueryParam("postcode")String postcode,
							@QueryParam("address")String address, 
							@QueryParam("telphone")String telphone,
							@QueryParam("mobile")String mobile,
							@QueryParam("ifDefault")boolean ifDefault,
							@QueryParam("memberId")Long memberId);
	
	/**
	 * (物理删除)
	 * @param id
	 */
	@NativeUpdate
	Integer removeContactById(@QueryParam("id") Long id, @QueryParam("memberId") Long memberId);
	
}
