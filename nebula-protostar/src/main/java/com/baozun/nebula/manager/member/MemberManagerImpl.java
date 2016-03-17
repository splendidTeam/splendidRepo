package com.baozun.nebula.manager.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.command.MemberGroupRelationCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.dao.member.MemberGroupDao;
import com.baozun.nebula.dao.member.MemberPersonalDataDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemImageDao;
import com.baozun.nebula.dao.system.ChooseOptionDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;


@Transactional
@Service("MemberManager")
public class MemberManagerImpl implements MemberManager {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(MemberManagerImpl.class);

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private MemberPersonalDataDao memberPersonalDataDao;

	@Autowired
	private MemberGroupDao memberGroupDao;
	
	@Autowired
	private ChooseOptionDao chooseOptionDao;
	
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	@Autowired
	private ItemImageDao itemImageDao;
	
    
	/**
	 * 分页获取会员信息，及其分组信息
	 * @param page
	 * @param sorts
	 * @param map
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Pagination<MemberCommand> findMemberListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {
		Pagination<MemberCommand> result = memberDao.findMemberListByQueryMapWithPage(page, sorts, paraMap);

		List<MemberGroupRelationCommand> groupName=memberDao.findAllMemberGroupList();
		
		List<MemberCommand> items = result.getItems();
		
		List<String> groupNameList=null;
		
		List<String> groupCodes=new ArrayList<String>();
		groupCodes.add("MEMBER_SOURCE");
		groupCodes.add("MEMBER_TYPE");
		List<ChooseOption> optionList=chooseOptionDao.findChooseOptionValue(groupCodes);
		
		Map<String,String> optionMap=new HashMap<String,String>();
		for(ChooseOption co:optionList){
			optionMap.put(co.getGroupCode()+"-"+co.getOptionValue(), co.getOptionLabel());
		}
		
		for (int i = 0; i < items.size(); i++){
			groupNameList=new ArrayList<String>();
			
			for (int j = 0; j < groupName.size(); j++){
				if(items.get(i).getId().equals(groupName.get(j).getMemberId())){
					groupNameList.add(groupName.get(j).getGroupName());
				}
			}
			
			items.get(i).setGroupName(groupNameList);
			items.get(i).setTypeName(optionMap.get("MEMBER_TYPE-"+items.get(i).getType()));
			items.get(i).setSourceName(optionMap.get("MEMBER_SOURCE-"+items.get(i).getSource()));
			
		}
		
		result.setItems(items);
		
		return result;
	}


	/**
	 * 通过ids启用禁用会员
	 * @param ids
	 * @param state
	 * @return
	 */
	@Override
	public Integer enableOrDisableMemberByIds(List<Long> ids,Integer state){
		// TODO Auto-generated method stub
		
		//如果要设置为有效,则检查下重命问题
		if(state.equals(Member.LIFECYCLE_ENABLE)){
			List<Member> memberList=memberDao.findMemberListByIds(ids);
			List<String> memberNameList=new ArrayList<String>();
			for(Member member:memberList){
				memberNameList.add(member.getLoginName());
			}
			List<Member> unList=memberDao.findMemberListByLoginNams(memberNameList);
			if(unList.size()>0){
				String strs="";
				for(Member member:unList){
					strs+=member.getLoginName()+" ";
				}
				throw new BusinessException(ErrorCodes.USER_EFFECT_USERNAME_EXISTS, new String[]{strs});
			}
		}
		
		Integer result= memberDao.enableOrDisableMemberByIds(ids, state);
		return result;
	}
	
	
	
	/**
	 * 查找所有会员分组
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MemberGroup> findAllMemberGroup() {
		// TODO Auto-generated method stub
			return memberGroupDao.findAllMemberGroupList();

	}

	


	/**
	 * 通过memberId获得所有会员详细信息,通过groupCode获取ChooseOption里面的字段值
	 * @param memberID
	 * @param groupCode
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public MemberPersonalDataCommand findMemberById(Long memberId){
		// TODO Auto-generated method stub
		MemberPersonalDataCommand memberPersonalDataCommand =memberPersonalDataDao.findById(memberId);
        
		List<String> groupCodes=new ArrayList<String>();
		groupCodes.add("MEMBER_SOURCE");
		groupCodes.add("MEMBER_TYPE");
		groupCodes.add("MEMBER_SEX");
		groupCodes.add("MEMBER_CREDENTIALTYPE");
		groupCodes.add("IS_AVAILABLE");
		List<ChooseOption> optionList=chooseOptionDao.findChooseOptionValue(groupCodes);
		
		Map<String,String> optionMap=new HashMap<String,String>();
		for(ChooseOption co:optionList){
			optionMap.put(co.getGroupCode()+"-"+co.getOptionValue(), co.getOptionLabel());
		}   
		//获取地址
    	memberPersonalDataCommand.setCountry(memberPersonalDataCommand.getCountryId()==null ? "" : 
    		AddressUtil.getAddressById(memberPersonalDataCommand.getCountryId()).getName());
    	memberPersonalDataCommand.setProvince(memberPersonalDataCommand.getProvinceId()==null ? "" :
    		AddressUtil.getAddressById(memberPersonalDataCommand.getProvinceId()).getName());
    	memberPersonalDataCommand.setCity(memberPersonalDataCommand.getCityId()==null ? "" :
    		AddressUtil.getAddressById(memberPersonalDataCommand.getCityId()).getName());
    	memberPersonalDataCommand.setArea(memberPersonalDataCommand.getAreaId()==null ? "":
    		AddressUtil.getAddressById(memberPersonalDataCommand.getAreaId()).getName()); 
    	
		memberPersonalDataCommand.setTypeName(optionMap.get("MEMBER_TYPE-"+memberPersonalDataCommand.getType()));
		memberPersonalDataCommand.setSourceName(optionMap.get("MEMBER_SOURCE-"+memberPersonalDataCommand.getSource()));
		memberPersonalDataCommand.setSexName(optionMap.get("MEMBER_SEX-"+memberPersonalDataCommand.getSex()));
		memberPersonalDataCommand.setCredentialsTypeName(optionMap.get("MEMBER_CREDENTIALTYPE-"+memberPersonalDataCommand.getCredentialsType()));
		memberPersonalDataCommand.setLifeCycleName(optionMap.get("IS_AVAILABLE-"+memberPersonalDataCommand.getLifecycle()));
	    return memberPersonalDataCommand;
	}
    
	
	/**
	 * 通过memberId获得所有会员信息列表
	 * @param memberID
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Member> findMemberListByMemberIds(Long[] memberIds){
		// TODO Auto-generated method stub
		List<Long> memberId = new ArrayList<Long>();
		for (Long id : memberIds){
			memberId.add(id);
		}
		return memberDao.findMemberListByIds(memberId);
	}

	
	
	public Integer updateMemberIsAddGroup(List<Long> ids,Integer state){
		// TODO Auto-generated method stub
		return memberDao.updateMemberIsAddGroup(ids, state);
	}

	 
	@Override
	public Pagination<MemberFavoritesCommand> memberFavoritesList(Page page,
			Sort[] sorts, Map<String, Object> searchParam,Long memberId) {
		searchParam.put("memberId", memberId);
		Pagination<MemberFavoritesCommand> 	memberFavoritesCommandPage=sdkMemberManager.memberFavoritesList(page, sorts, searchParam);
		
		List<Long> itemIds = new ArrayList<Long>();
		for(MemberFavoritesCommand memberFavoritesCommand : memberFavoritesCommandPage.getItems() ){
			itemIds.add(memberFavoritesCommand.getItemId());
		}
		
		List<ItemImage> itemImageList = itemImageDao.findItemImageByItemIds(itemIds, ItemImage.IMG_TYPE_LIST);
		
		Map<Long, String> picUrlMap = new HashMap<Long, String>();
		for(int i=itemImageList.size()-1; i>=0; i--){
			ItemImage itemImage = itemImageList.get(i);
			picUrlMap.put(itemImage.getItemId(), itemImage.getPicUrl());
		}
		
		for(MemberFavoritesCommand memberFavoritesCommand : memberFavoritesCommandPage.getItems() ){
			memberFavoritesCommand.setPicUrl(picUrlMap.get(memberFavoritesCommand.getItemId()));
		}
		
		return memberFavoritesCommandPage;
	}


	@Override
	public Pagination<ContactCommand> findContactCommandByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> searchParam, Long memberId ) { 
		searchParam.put("memberId", memberId);
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
	
}
