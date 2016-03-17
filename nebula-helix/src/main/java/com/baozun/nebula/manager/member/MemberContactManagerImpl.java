package com.baozun.nebula.manager.member;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.member.Contact;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;

@Transactional
@Service("memberContactManager")
public class MemberContactManagerImpl implements MemberContactManager{

	private Integer ISDEFAULT = 1;
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	@Transactional(readOnly=true)
	@Override
	public Pagination<ContactCommand> findContactCommandByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> searchParam) {
		
		Pagination<ContactCommand> contactCommand =sdkMemberManager.findContactCommandByQueryMapWithPage(page, sorts, searchParam);
		if(contactCommand !=null){
			List<ContactCommand> contactList=contactCommand.getItems();
	        for(ContactCommand contact:contactList){ 
	        	Address country=AddressUtil.getAddressById(contact.getCountryId());
	        	Address province=AddressUtil.getAddressById(contact.getProvinceId());
	        	Address city=AddressUtil.getAddressById(contact.getCityId());
	        	Address area=AddressUtil.getAddressById(contact.getAreaId());
	        	Address town=AddressUtil.getAddressById(contact.getTownId());
	        	contact.setCountry(country==null ? "" : country.getName());
	        	contact.setProvince(province==null ? "" :province.getName());
	        	contact.setCity(city==null ? "" :city.getName());
	        	contact.setArea(area==null ? "":area.getName());
	        	contact.setTown(town==null ? "":town.getName());
	        } 
		} 
		return contactCommand;
	}

	@Transactional(readOnly=true)
	@Override
	public Pagination<ContactCommand> findContactList(Page page, Sort[] sorts,
			Map<String, Object> searchParam) {
		Pagination<ContactCommand> contactCommand =sdkMemberManager.findContactList(page, sorts, searchParam);
		if(contactCommand !=null){
			List<ContactCommand> contactList=contactCommand.getItems();
	        for(ContactCommand contact:contactList){ 
	        	Address country=AddressUtil.getAddressById(contact.getCountryId());
	        	Address province=AddressUtil.getAddressById(contact.getProvinceId());
	        	Address city=AddressUtil.getAddressById(contact.getCityId());
	        	Address area=AddressUtil.getAddressById(contact.getAreaId());
	        	Address town=AddressUtil.getAddressById(contact.getTownId());
	        	contact.setCountry(country==null ? "" : country.getName());
	        	contact.setProvince(province==null ? "" :province.getName());
	        	contact.setCity(city==null ? "" :city.getName());
	        	contact.setArea(area==null ? "":area.getName());
	        	contact.setTown(town==null ? "":town.getName());
	        } 
		} 
		return contactCommand;
	}

	@Override
	public ContactCommand createOrUpdateContact(ContactCommand contact) {
		// 验证用户是否存在
		MemberCommand memberCommand = sdkMemberManager
				.findMemberById(contact.getMemberId());
		if (memberCommand == null) {
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);
		}
		return sdkMemberManager.createOrUpdateContact(contact);
	}

	@Override
	public Integer removeContactById(Long contactId, Long memberId) {
		// 验证用户是否存在
		MemberCommand memberCommand = sdkMemberManager
				.findMemberById(memberId);
		if (memberCommand == null) {
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);
		}
		return sdkMemberManager.removeContactById(contactId, memberId);
	}

	/**
	 * @deprecated {@link com.baozun.nebula.manager.member.MemberContactManager#findContactById(Long, Long)}
	 */
	@Override
	@Transactional(readOnly=true)
	public ContactCommand findContactById(Long contactId) {
		ContactCommand contact =sdkMemberManager.findContactById(contactId);
		if(contact !=null){
			Address country=AddressUtil.getAddressById(contact.getCountryId());
			Address province=AddressUtil.getAddressById(contact.getProvinceId());
			Address city=AddressUtil.getAddressById(contact.getCityId());
			Address area=AddressUtil.getAddressById(contact.getAreaId());
			Address town=AddressUtil.getAddressById(contact.getTownId());
			contact.setCountry(country==null ? "" : country.getName());
			contact.setProvince(province==null ? "" :province.getName());
			contact.setCity(city==null ? "" :city.getName());
			contact.setArea(area==null ? "":area.getName());
			contact.setTown(town==null ? "":town.getName());
		} 
		return contact;
	}
	
	@Override
	@Transactional(readOnly=true)
	public ContactCommand findContactById(Long contactId, Long memberId) {
		ContactCommand contact =sdkMemberManager.findContactById(contactId, memberId);
		if(contact !=null){
			Address country=AddressUtil.getAddressById(contact.getCountryId());
			Address province=AddressUtil.getAddressById(contact.getProvinceId());
			Address city=AddressUtil.getAddressById(contact.getCityId());
			Address area=AddressUtil.getAddressById(contact.getAreaId());
			Address town=AddressUtil.getAddressById(contact.getTownId());
			contact.setCountry(country==null ? "" : country.getName());
			contact.setProvince(province==null ? "" :province.getName());
			contact.setCity(city==null ? "" :city.getName());
			contact.setArea(area==null ? "":area.getName());
			contact.setTown(town==null ? "":town.getName());
		} 
		return contact;
	}

	@Override
	public Integer updateDefaultById(Long contactId,Long memberId) {
		// 验证用户是否存在
		MemberCommand memberCommand = sdkMemberManager
				.findMemberById(memberId);
		if (memberCommand == null) {
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);
		}
		return sdkMemberManager.updateContactIsDefault(memberId, contactId, Contact.ISDEFAULT);
	}

}
