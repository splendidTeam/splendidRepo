package com.baozun.nebula.sdk.manager.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.utils.PropListCopyable;
import loxia.utils.PropertyUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.command.coupon.CouponCommand;
import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.command.product.ItemRateListCommand;
import com.baozun.nebula.dao.coupon.CouponDao;
import com.baozun.nebula.dao.member.ContactDao;
import com.baozun.nebula.dao.member.MemberConductDao;
import com.baozun.nebula.dao.member.MemberCryptoguardDao;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.dao.member.MemberFavoritesDao;
import com.baozun.nebula.dao.member.MemberGroupDao;
import com.baozun.nebula.dao.member.MemberGroupRelationDao;
import com.baozun.nebula.dao.member.MemberPersonalDataDao;
import com.baozun.nebula.dao.product.ItemRateDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.dao.sns.ConsultantsDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.member.Contact;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberConduct;
import com.baozun.nebula.model.member.MemberCryptoguard;
import com.baozun.nebula.model.member.MemberFavorites;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.model.product.ItemRate;
import com.baozun.nebula.model.sns.Consultants;
import com.baozun.nebula.sdk.command.ItemRateCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkSecretManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.utils.RegulareExpUtils;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;

@Transactional
@Service("sdkMemberService")
public class SdkMemberManagerImpl implements SdkMemberManager {

	final Logger					log	= LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberDao				memberDao;

	@Autowired
	private MemberGroupDao			memberGroupDao;

	@Autowired
	private CouponDao				couponDao;

	@Autowired
	private ContactDao				contactDao;

	@Autowired
	private MemberFavoritesDao		memberFavoritesDao;

	@Autowired
	private MemberConductDao		memberConductDao;

	@Autowired
	private MemberPersonalDataDao	memberPersonalDataDao;

	@Autowired
	private ItemRateDao				itemRateDao;

	@Autowired
	private ConsultantsDao			consultantsDao;

	@Autowired
	private MemberGroupRelationDao	memberGroupRelationDao;
	@Autowired
	private SdkOrderLineDao			sdkOrderLineDao;

	@Autowired
	private MemberCryptoguardDao	memberCryptoguardDao;

	@Autowired
	private SdkEngineManager		sdkEngineManager;

	@Autowired
	private SdkSkuManager			sdkSkuManager;

	@Autowired
	private SdkSecretManager		sdkSecretManager;

	private void encrypt(MemberPersonalData mpd) {

		sdkSecretManager.encrypt(mpd, new String[] { "nickname", "localRealName", "intelRealName", "bloodType",
				"marriage", "country", "province", "city", "area", "town", "address", "credentialsNo", "email",
				"mobile", "qq", "weibo", "weixin", "edu", "industy", "position", "salary", "workingLife", "company",
				"interest", "postCode" });
	}

	private void decrypt(MemberPersonalData mpd) {

		sdkSecretManager.decrypt(mpd, new String[] { "nickname", "localRealName", "intelRealName", "bloodType",
				"marriage", "country", "province", "city", "area", "town", "address", "credentialsNo", "email",
				"mobile", "qq", "weibo", "weixin", "edu", "industy", "position", "salary", "workingLife", "company",
				"interest", "postCode" });
	}

	private void encrypt(MemberPersonalDataCommand mpdc) {

		sdkSecretManager.encrypt(mpdc, new String[] { "nickname", "localRealName", "intelRealName", "bloodType",
				"marriage", "country", "province", "city", "area", "address", "credentialsNo", "postCode" });
	}

	private void decrypt(MemberPersonalDataCommand mpdc) {

		sdkSecretManager.decrypt(mpdc, new String[] { "nickname", "localRealName", "intelRealName", "bloodType",
				"marriage", "country", "province", "city", "area", "address", "credentialsNo", "postCode" });
	}

	private void encryptContact(ContactCommand contact) {

		sdkSecretManager.encrypt(contact, new String[] { "name", "country", "province", "city", "area", "town",
				"address", "postcode", "telphone", "mobile", "email" });
	}

	private void decryptContact(ContactCommand contact) {

		sdkSecretManager.decrypt(contact, new String[] { "name", "country", "province", "city", "area", "town",
				"address", "postcode", "telphone", "mobile", "email" });
	}

	private void encryptContact(Contact contact) {

		sdkSecretManager.encrypt(contact, new String[] { "name", "country", "province", "city", "area", "town",
				"address", "postcode", "telphone", "mobile", "email" });
	}

	private void decryptContact(Contact contact) {

		sdkSecretManager.decrypt(contact, new String[] { "name", "country", "province", "city", "area", "town",
				"address", "postcode", "telphone", "mobile", "email" });
	}

	@Override
	@Transactional(readOnly=true)
	public MemberCommand findMemberById(Long id) {
		Member member = memberDao.findMemberById(id);
		if (null != member)
			return convertMemberToMemberCommand(member);
		return null;
	}

	@Override
	public MemberCommand saveMember(MemberCommand memberCommand) {
		Member member = null;
		if (memberCommand.getId() == null || memberCommand.getId() == 0) {
			// 保存
			member = new Member();
		} else {
			// 更新
			member = memberDao.findMemberById(memberCommand.getId());
		}
		member = convertMemberCommandToMember(memberCommand, member);
		member = memberDao.save(member);
		if (null != member) {
			return convertMemberToMemberCommand(member);
		}
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public MemberCommand findMemberByLoginName(String loginName) {
		Member member = null;
		if (StringUtils.isNotBlank(loginName))
			member = memberDao.findMemberByLoginName(loginName.toUpperCase());
		if (null != member)
			return convertMemberToMemberCommand(member);
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public MemberCommand findMemberByLoginEmail(String loginName) {
		Member member = null;
		if (StringUtils.isNotBlank(loginName))
			member = memberDao.findMemberByLoginEmail(loginName.toUpperCase());
		if (null != member)
			return convertMemberToMemberCommand(member);
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public MemberCommand findMemberByLoginNameAndPasswd(String loginName, String password) {
		Member member = memberDao.findMemberByLoginNameAndPasswd(loginName, password);
		if (null != member)
			return convertMemberToMemberCommand(member);
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<CouponCommand> findCouponCommandList(Page page, Sort[] sorts, Map<String, Object> searchParam) {
		return couponDao.findCouponCommandList(page, sorts, searchParam);
	}

	/**
	 * @deprecated {@link com.baozun.nebula.sdk.manager.SdkMemberManager#findContactById(Long, Long)}
	 */
	@Override
	@Transactional(readOnly=true)
	public ContactCommand findContactById(Long contactId) {
		Contact contact = contactDao.findContactById(contactId);
		decryptContact(contact);
		if (null != contact)
			return (ContactCommand) ConvertUtils.convertTwoObject(new ContactCommand(), contact);
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public ContactCommand findContactById(Long contactId, Long memberId) {
		Contact contact = contactDao.findContactByCantactIdAndMemberId(contactId, memberId);
		decryptContact(contact);
		if (null != contact)
			return (ContactCommand) ConvertUtils.convertTwoObject(new ContactCommand(), contact);
		return null;
	}

	@Override
	public ContactCommand saveContactCommand(ContactCommand contactCommand) {
		encryptContact(contactCommand);
		Contact contact = null;
		if (contactCommand.getId() == null || contactCommand.getId() == 0) {
			// 保存
			contact = new Contact();
		} else {
			// 更新
			contact = contactDao.getByPrimaryKey(contactCommand.getId());
		}
		contactCommand.setModifyTime(new Date());
		contact = (Contact) ConvertUtils.convertTwoObject(contact, contactCommand);
		contact = contactDao.save(contact);
		if (null != contact)
			return (ContactCommand) ConvertUtils.convertTwoObject(new ContactCommand(), contact);
		return null;
	}
	
	/**
	 * 重写联系人信息中的区划的国际化信息
	 * @param contactCommand
	 */
	private void rewriteIntlDistrictInfo(ContactCommand contactCommand){
		if(contactCommand == null) return;
		Address country = AddressUtil.getAddressById(contactCommand.getCountryId());
		Address province = AddressUtil.getAddressById(contactCommand.getProvinceId());
		Address city = AddressUtil.getAddressById(contactCommand.getCityId());
		Address area = AddressUtil.getAddressById(contactCommand.getAreaId());
		Address town = AddressUtil.getAddressById(contactCommand.getTownId());
		contactCommand.setCountry(country == null ? "" : country.getName());
		contactCommand.setProvince(province == null ? "" : province.getName());
		contactCommand.setCity(city == null ? "" : city.getName());
		contactCommand.setArea(area == null ? "" : area.getName());
		contactCommand.setTown(town == null ? "" : town.getName());
	}

	@Override
	@Transactional(readOnly=true)
	public List<ContactCommand> findAllContactListByMemberId(Long memberId) {
		List<Contact> contacts = contactDao.findAllContactListByMemberId(memberId);
		List<ContactCommand> contactCommands = new ArrayList<ContactCommand>();
		for (Contact contact : contacts) {
			decryptContact(contact);
			ContactCommand contactCommand = (ContactCommand) ConvertUtils.convertTwoObject(new ContactCommand(),
					contact);
			// 用id获取中文名称
			rewriteIntlDistrictInfo(contactCommand);			
			contactCommands.add(contactCommand);
		}
		return contactCommands;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Pagination<ContactCommand> findContactsByMemberId(Page page, Sort[] sorts, Long memberId){
		Pagination<Contact> contacts = contactDao.findContactsByMemberId(page, sorts, memberId);
		Pagination<ContactCommand> resultContacts = new Pagination<ContactCommand>();
		try {
			PropertyUtil.copyProperties(contacts, resultContacts, 
					new PropListCopyable("count","currentPage","totalPages","start","size","sortStr"));
		} catch (Exception e) {
			//should not occur
			e.printStackTrace();
		}
		resultContacts.setItems(new ArrayList<ContactCommand>());
		for(Contact contact : contacts.getItems()){
			decryptContact(contact);
			ContactCommand contactCommand = (ContactCommand) ConvertUtils.convertTwoObject(new ContactCommand(),
					contact);
			// 用id获取中文名称
			rewriteIntlDistrictInfo(contactCommand);
			resultContacts.getItems().add(contactCommand);			
		}
		return resultContacts;
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<MemberFavoritesCommand> memberFavoritesList(Page page, Sort[] sorts,
			Map<String, Object> searchParam) {
		return memberFavoritesDao.memberFavoritesList(page, sorts, searchParam);
	}

	@Override
	@Transactional(readOnly=true)
	public MemberFavoritesCommand findMemberFavoritesByMemberIdAndItemId(Long itemId, Long memberId, Long skuId) {
		Map<String, Long> paramMap = new HashMap<String, Long>();
		paramMap.put("itemId", itemId);
		paramMap.put("memberId", memberId);
		if (null != skuId) {
			paramMap.put("skuId", skuId);
		}
		MemberFavorites memberFavorites = memberFavoritesDao.findMemberFavoritesByMemberIdAndItemId(paramMap);
		if (null != memberFavorites)
			return (MemberFavoritesCommand) ConvertUtils
					.convertTwoObject(new MemberFavoritesCommand(), memberFavorites);
		return null;
	}

	@Override
	public void deleteMemberFavorites(MemberFavoritesCommand memberFavoritesCommand) {
		Map<String, Long> paramMap = new HashMap<String, Long>();
		paramMap.put("itemId", memberFavoritesCommand.getItemId());
		paramMap.put("memberId", memberFavoritesCommand.getMemberId());
		Long skuId = memberFavoritesCommand.getSkuId();
		if (null != skuId) {
			paramMap.put("skuId", skuId);
		}
		MemberFavorites memberFavorites = memberFavoritesDao.findMemberFavoritesByMemberIdAndItemId(paramMap);
		memberFavoritesDao.delete(memberFavorites);
	}

	private MemberCommand convertMemberToMemberCommand(Member member) {
		MemberCommand memberCommand = new MemberCommand();

		memberCommand.setLoginName(member.getLoginName());
		memberCommand.setPassword(member.getPassword());
		memberCommand.setOldPassword(member.getOldPassword());// BrandStore迁移的历史密码
		memberCommand.setLoginEmail(member.getLoginEmail());

		// memberCommand.setIsaddgroup(member.getIsaddgroup());//未加入组
		memberCommand.setLifecycle(member.getLifecycle());

		memberCommand.setLoginMobile(member.getLoginMobile());
		memberCommand.setThirdPartyIdentify(member.getThirdPartyIdentify());
		memberCommand.setId(member.getId());

		memberCommand.setSource(member.getSource());
		memberCommand.setReceiveMail(member.getReceiveMail());
		memberCommand.setType(member.getType());
		return memberCommand;
	}

	private Member convertMemberCommandToMember(MemberCommand memberCommand, Member member) {
		member.setLoginName(memberCommand.getLoginName());
		member.setPassword(memberCommand.getPassword());
		member.setLoginEmail(memberCommand.getLoginEmail());

		// member.setIsaddgroup(memberCommand.getIsaddgroup());//未加入组
		member.setLifecycle(memberCommand.getLifecycle());

		member.setLoginMobile(memberCommand.getLoginMobile());
		member.setThirdPartyIdentify(memberCommand.getThirdPartyIdentify());
		member.setId(memberCommand.getId());

		member.setSource(memberCommand.getSource());
		member.setReceiveMail(memberCommand.getReceiveMail());
		member.setType(memberCommand.getType());
		return member;
	}

	@Override
	public MemberConductCommand saveMemberConduct(MemberConductCommand memberConductCommand) {
		MemberConduct conduct = memberConductDao.findMemberConductById(memberConductCommand.getId());
		if (null == conduct)
			conduct = new MemberConduct();
		conduct = (MemberConduct) ConvertUtils.convertTwoObject(conduct, memberConductCommand);
		conduct = memberConductDao.save(conduct);
		if (null != conduct)
			return (MemberConductCommand) ConvertUtils.convertTwoObject(new MemberConductCommand(), conduct);
		return null;
	}

	@Override
	public MemberPersonalDataCommand saveMemberPersonDataCommand(MemberPersonalDataCommand memberPersonalDataCommand) {
		encrypt(memberPersonalDataCommand);
		MemberPersonalData memberPersonalData = memberPersonalDataDao
				.getByPrimaryKey(memberPersonalDataCommand.getId());
		if (null == memberPersonalData) {
			memberPersonalData = new MemberPersonalData();
		}
		memberPersonalData = (MemberPersonalData) ConvertUtils.convertTwoObject(memberPersonalData,
				memberPersonalDataCommand);
		memberPersonalData = memberPersonalDataDao.save(memberPersonalData);
		if (null != memberPersonalData)
			return (MemberPersonalDataCommand) ConvertUtils.convertTwoObject(new MemberPersonalDataCommand(),
					memberPersonalData);
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public MemberConductCommand findMemberConductCommandById(Long id) {
		return memberConductDao.findMemberConductCommandById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public MemberPersonalDataCommand findMemberPersonDataCommandById(Long id) {
		MemberPersonalDataCommand mpdc = memberPersonalDataDao.findById(id);

		decrypt(mpdc);
		return mpdc;
	}

	@Override
	public Integer updateContactIsDefault(Long memberId, Long contactId, boolean isDefault) {
		contactDao.updateContactByMemberId(memberId, Contact.NOTDEFAULT);
		return contactDao.updateContactByContactId(contactId, isDefault, memberId);
	}

	@Override
	@Transactional(readOnly=true)
	public CouponCommand findByMemberIdAndCardNo(Long memberId, String couponNo) {
		return couponDao.findCouponCommandByMemberIdAndCardNo(memberId, couponNo);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<ContactCommand> findContactCommandByQueryMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> searchParam) {
		Pagination<ContactCommand> contactPage = contactDao.findContactByQueryMapWithPage(page, sorts, searchParam);

		for (ContactCommand cc : contactPage.getItems()) {
			decryptContact(cc);
		}

		return contactPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#findContactList(loxia. dao.Page, loxia.dao.Sort[],
	 * java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<ContactCommand> findContactList(Page page, Sort[] sorts, Map<String, Object> searchParam) {

		Pagination<ContactCommand> contactPage = contactDao.findContactByQueryMapWithPage(page, sorts, searchParam);

		for (ContactCommand cc : contactPage.getItems()) {
			decryptContact(cc);
		}

		return contactPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#createOrUpdateContact(
	 * com.baozun.nebula.command.ContactCommand)
	 */
	@Override
	public ContactCommand createOrUpdateContact(ContactCommand command) {

		Contact con = null;
		if (command.getId() == null || command.getId() == 0) {
			// 保存
			con = new Contact();
			con = convertContactCommandToContact(command, con);
			encryptContact(con);
			if (command.getIsDefault()) {// 设置其他为非默认
				contactDao.updateContactByMemberId(command.getMemberId(), Contact.NOTDEFAULT);
			}
			con = contactDao.save(con);

		} else {
			// 更新
			con = contactDao.getByPrimaryKey(command.getId());
			con = convertContactCommandToContact(command, con);
			encryptContact(con);
			if (command.getIsDefault()) {// 设置其他为非默认
				contactDao.updateContactByMemberId(command.getMemberId(), Contact.NOTDEFAULT);
			}

		}
		if (null != con) {
			return convertContactToContactCommand(con);
		}
		return null;
	}

	private ContactCommand convertContactToContactCommand(Contact con) {
		ContactCommand command = new ContactCommand();
		
		ConvertUtils.convertTwoObject(command, con);
		rewriteIntlDistrictInfo(command);
		
		return command;
	}

	//TODO 这个方法的地址转换需要检查逻辑是否正确。从我个人理解来看，数据转换为展现的时候要转，但是存储的时候也转是否有必要和合适
	//是为了匹配历史不用ID而直接用名字时的兼容性么？
	private Contact convertContactCommandToContact(ContactCommand command, Contact con) {
		// con.setId(command.getId());
		con.setName(command.getName());
		con.setAddress(command.getAddress());
		con.setAreaId(command.getAreaId());
		con.setCityId(command.getCityId());
		con.setCountryId(command.getCountryId());
		con.setIsDefault(command.getIsDefault());
		con.setMobile(command.getMobile());
		con.setPostcode(command.getPostcode());
		con.setProvinceId(command.getProvinceId());
		con.setTelphone(command.getTelphone());
		con.setTownId(command.getTownId());
		con.setEmail(command.getEmail());
		con.setMemberId(command.getMemberId());
		Address country = AddressUtil.getAddressById(command.getCountryId());
		Address province = AddressUtil.getAddressById(command.getProvinceId());
		Address city = AddressUtil.getAddressById(command.getCityId());
		Address area = AddressUtil.getAddressById(command.getAreaId());
		Address town = AddressUtil.getAddressById(command.getTownId());
		con.setCountry(country == null ? "" : country.getName());
		con.setProvince(province == null ? "" : province.getName());
		con.setCity(city == null ? "" : city.getName());
		con.setArea(area == null ? "" : area.getName());
		con.setTown(town == null ? "" : town.getName());
		return con;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#removeContactById(java .lang.Long, java.lang.Long)
	 */
	@Override
	public Integer removeContactById(Long contactId, Long memberId) {
		return contactDao.removeContactById(contactId, memberId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#findFavoritesList(loxia .dao.Page, loxia.dao.Sort[],
	 * java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<MemberFavoritesCommand> findFavoritesList(Page page, Sort[] sorts, Map<String, Object> searchParam) {

		Pagination<MemberFavoritesCommand> favoritesPage = memberFavoritesDao.findFavoritesList(page, sorts, searchParam);
		if (favoritesPage != null && favoritesPage.getItems() != null) {
			for (MemberFavoritesCommand favortiesCommand : favoritesPage.getItems()) {
				if(favortiesCommand != null && favortiesCommand.getSkuId() != null){
					List<SkuProperty> skuPros = sdkSkuManager.getSkuPros(favortiesCommand.getProperties());
					if (null != skuPros && !skuPros.isEmpty()) {
						favortiesCommand.setSkuPropertys(skuPros);
					}
				}
			}
		}

		return favoritesPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#createFavorites(java.lang .Long, java.lang.Long,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public MemberFavoritesCommand createFavorites(Long memberId, Long itemId, Long skuId) {
		MemberFavorites meberFavorites = new MemberFavorites();
		meberFavorites.setItemId(itemId);
		meberFavorites.setSkuId(skuId);
		meberFavorites.setMemberId(memberId);
		meberFavorites.setCreateDate(new Date());
		meberFavorites.setVersion(new Date());
		MemberFavorites newMeberFavorites = memberFavoritesDao.save(meberFavorites);
		MemberFavoritesCommand memberFavoritesCommand = new MemberFavoritesCommand();
		memberFavoritesCommand = (MemberFavoritesCommand) ConvertUtils.convertTwoObject(memberFavoritesCommand,
				newMeberFavorites);
		return memberFavoritesCommand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#removeFavoritesByIds(java .util.List, java.lang.Long)
	 */
	@Override
	public Integer removeFavoritesByIds(List<Long> itemIds, List<Long> skuIds, Long memberId) {
		// TODO Auto-generated method stub
		return memberFavoritesDao.removeFavoritesByIds(itemIds, skuIds, memberId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#findItemRateList(loxia .dao.Page, loxia.dao.Sort[],
	 * java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<ItemRateCommand> findItemRateList(Page page, Sort[] sorts, Map<String, Object> searchParam) {
		Pagination<ItemRateCommand> ItemRateCommandList = new Pagination<ItemRateCommand>();
		List<ItemRateCommand> itemRateList = new ArrayList<ItemRateCommand>();
		Pagination<ItemRateListCommand> itemteList = itemRateDao.findeStatusNndOrderListQueryMapWithPage(page, sorts,
				searchParam);
		if (itemteList != null && itemteList.getItems().size() > 0) {
			for (ItemRateListCommand itemRate : itemteList.getItems()) {
				ItemRateCommand itemRateCommand = new ItemRateCommand();
				OrderLineCommand orderLine = new OrderLineCommand();
				RateCommand rateCommand = new RateCommand();
				orderLine.setItemPic(itemRate.getItemPic());
				orderLine.setItemId(itemRate.getItemId());
				orderLine.setItemName(itemRate.getItemName());
				orderLine.setEvaluationStatus(itemRate.getEvaluationStatus());
				orderLine.setOrderId(itemRate.getOrderId());
				orderLine.setId(itemRate.getSoLineId());
				rateCommand.setId(itemRate.getRateId());
				rateCommand.setReply(itemRate.getReply());
				rateCommand.setMemberId(itemRate.getMemberId());
				rateCommand.setLifecycle(itemRate.getLifecycle());

				itemRateCommand.setRateCommand(rateCommand);
				itemRateCommand.setOrderLineCommand(orderLine);
				itemRateCommand.setCreateTime(itemRate.getCreateTime());
				itemRateList.add(itemRateCommand);
			}
		}
		ItemRateCommandList.setItems(itemRateList);
		ItemRateCommandList.setCount(itemteList.getCount());
		ItemRateCommandList.setSize(itemteList.getSize());
		ItemRateCommandList.setTotalPages(itemteList.getTotalPages());
		ItemRateCommandList.setSortStr(itemteList.getSortStr());
		ItemRateCommandList.setCurrentPage(itemteList.getCurrentPage());
		ItemRateCommandList.setStart(itemteList.getStart());
		return ItemRateCommandList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#findItemRateById(java. lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public RateCommand findItemRateById(Long rateId) {
		ItemRate newItemRate = itemRateDao.getByPrimaryKey(rateId);
		RateCommand newItemRateCommand = new RateCommand();
		if (null != newItemRate)
			newItemRateCommand = (RateCommand) ConvertUtils.convertTwoObject(newItemRateCommand, newItemRate);
		return newItemRateCommand;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#createRate(com.baozun. nebula.command.RateCommand)
	 */
	@Override
	public ItemRate createRate(RateCommand itemRateCommand) {
		ItemRate newItemRate = new ItemRate();
		newItemRate.setId(itemRateCommand.getId());
		newItemRate.setItemId(itemRateCommand.getItemId());
		newItemRate.setContent(itemRateCommand.getContent());
		newItemRate.setCreateTime(new Date());
		newItemRate.setMemberId(itemRateCommand.getMemberId());
		newItemRate.setScore(itemRateCommand.getScore());
		newItemRate.setLifecycle(BaseModel.LIFECYCLE_INIT.longValue());
		newItemRate.setOrderLineId(itemRateCommand.getOrderLineId());
		ItemRate itemRate = itemRateDao.save(newItemRate);
		return itemRate;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#findConsultantsList(loxia .dao.Page, loxia.dao.Sort[],
	 * java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<ConsultantCommand> findConsultantsList(Page page, Sort[] sorts, Map<String, Object> searchParam) {
		return consultantsDao.findConsultants(page, sorts, searchParam);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkMemberManager#createConsultants(com.
	 * baozun.nebula.command.product.ItemConsultantCommand)
	 */
	@Override
	public Consultants createConsultants(Consultants consultants) {
		return consultantsDao.save(consultants);
	}

	@Override
	public Member register(Member sourceMember) {
		sourceMember.setLifecycle(BaseModel.LIFECYCLE_ENABLE);// 有效
		// sourceMember.setIsaddgroup(0);//未加入组
		String encodePassword = EncryptUtil.getInstance().hash(sourceMember.getPassword(), sourceMember.getLoginName());
		sourceMember.setPassword(encodePassword);
		sourceMember.setLoginName(sourceMember.getLoginName());
		Member member = memberDao.save(sourceMember);
		return member;
	}

	@Override
	@Transactional(readOnly=true)
	public boolean validUserName(String username) {
		boolean flag = false;
		Member member = memberDao.findMemberByLoginName(username);
		if (null == member) {
			flag = true;
		}
		return flag;
	}

	@Override
	@Transactional(readOnly=true)
	public boolean validEmail(String email) {
		boolean flag = false;
		Member member = memberDao.findMemberByLoginEmail(email);
		if (null == member) {
			flag = true;
		}
		return flag;
	}

	@Override
	@Transactional(readOnly=true)
	public boolean validMobile(String mobile) {
		boolean flag = false;
		Member member = memberDao.findMemberByLoginMobile(mobile);
		if (null == member) {
			flag = true;
		}
		return flag;
	}

	@Override
	@Transactional(readOnly=true)
	public List<MemberCryptoguard> findCryptoguardList(Long memberId) {
		return memberCryptoguardDao.findByMemberId(memberId);
	}

	@Override
	public String sendPassResetCodeByEmail(Long memberId) {
		String genericRandomCode = genericRandomCode();
		log.info(genericRandomCode);
		return genericRandomCode;
	}

	@Override
	public String sendPassResetCodeBySms(Long memberId) {
		String genericRandomCode = genericRandomCode();
		log.info(genericRandomCode);
		return genericRandomCode;
	}

	private String genericRandomCode() {
		Random random = new Random();
		String result = "";
		for (int i = 0; i < 4; i++) {
			result += String.valueOf(random.nextInt(10));
		}
		return result;
	}

	@Override
	@Transactional(readOnly=true)
	public boolean checkCryptoguard(List<MemberCryptoguard> cryptoguardList) {
		boolean result = true;
		for (MemberCryptoguard c : cryptoguardList) {
			MemberCryptoguard newCry = memberCryptoguardDao.findByQa(c.getQuestion(), c.getAnswer());
			result = result && (newCry != null);
		}
		return result;
	}

	@Override
	public boolean checkEmailUrl(String code, Long memberId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional(readOnly=true)
	public MemberPersonalData findMemberPersonData(Long memberId) {
		MemberPersonalData mpd = memberPersonalDataDao.findByMemberId(memberId);

		decrypt(mpd);

		return mpd;

	}

	@Override
	public boolean resetPasswd(Long memberId, String newPwd) {
		Integer res = memberDao.updatePasswd(memberId, newPwd);
		if (res > 0)
			return true;
		return false;
	}

	@Override
	public boolean updatePasswd(Long memberId, String pwd, String newPwd, String reNewPwd) {
		Member member = memberDao.findMemberById(memberId);

		String encodePassword = EncryptUtil.getInstance().hash(pwd, member.getLoginName());
		String encodeNewPassword = EncryptUtil.getInstance().hash(newPwd, member.getLoginName());
		if (!encodePassword.equals(member.getPassword())) {
			throw new BusinessException(Constants.OLDPASSWORD_ISWRONG_ERROR);
		}
		if (pwd.equals(newPwd)) {
			throw new BusinessException(Constants.OLDPASSWORD_ASSAMEAS_NEWPASSWORD_ERROR);
		}
		if (!newPwd.equals(reNewPwd)) {
			throw new BusinessException(Constants.RENEWPASSWORD_NOTSAMEAS_NEWPASSWORD_ERROR);
		}
		Integer res = memberDao.updatePasswd(memberId, encodeNewPassword);
		if (res > 0)
			return true;
		return false;
	}

	@Override
	public void saveCryptoguard(MemberCryptoguard memberCryptoguard) {
		memberCryptoguardDao.save(memberCryptoguard);

	}

	@Override
	public void removeCryptoguardByMemberId(Long memberId) {
		memberCryptoguardDao.removeMemberCryptoguardByMemberId(memberId);

	}

	@Override
	public MemberPersonalData savePersonData(MemberPersonalData personData) {

		encrypt(personData);

		MemberPersonalData oldMemberPersonalData = memberPersonalDataDao.getByPrimaryKey(personData.getId());
		MemberPersonalData memberPersonalData = null;
		if (oldMemberPersonalData != null) {
			Date ver = oldMemberPersonalData.getVersion();
			personData.setVersion(ver);
			oldMemberPersonalData = personData;
			memberPersonalData = memberPersonalDataDao.save(oldMemberPersonalData);
		} else {
			memberPersonalData = memberPersonalDataDao.save(personData);
		}

		return memberPersonalData;
	}

	@Override
	@Transactional(readOnly=true)
	public boolean checkNickname(String nickname) {
		MemberPersonalData memberPersonalData = memberPersonalDataDao.findByNickname(nickname);
		if (memberPersonalData == null)
			return false;
		return true;
	}

	@Override
	@Transactional(readOnly=true)
	public void sendBindEmailUrl(Long memberId, String email, String path) {
		String SEND_MAIL_KEY = ProfileConfigUtil.findPro("config/metainfo.properties").getProperty("send.mail.key");
		Member member = memberDao.findMemberById(memberId);
		MemberConductCommand conduct = findMemberConductCommandById(member.getId());
		StringBuffer emailContent = new StringBuffer();
		emailContent.append(member.getId()).append(member.getId()).append(email).append(member.getType())
				.append(member.getSource()).append(conduct.getRegisterTime());
		String checksum = EncryptUtil.getInstance().hash(emailContent.toString(), SEND_MAIL_KEY);
		StringBuffer activeEmailUrl = new StringBuffer();

		try {
			StringBuffer encryptParams = new StringBuffer();
			encryptParams.append("member_id=").append(member.getId()).append("&checksum=").append(checksum)
					.append("&sendTime=").append(new Date().getTime()).append("&email=").append(email);
			String encrypt = EncryptUtil.getInstance().encrypt(encryptParams.toString());
			activeEmailUrl.append(path).append("?registerComfirm=").append(URLEncoder.encode(encrypt, "UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("activeEmailUrl-----------------------: " + activeEmailUrl);
	}

	@Override
	public boolean bindEmail(Long memberId, String checkSum, String email) {
		String SEND_MAIL_KEY = ProfileConfigUtil.findPro("config/metainfo.properties").getProperty("send.mail.key");
		Member member = memberDao.findMemberById(memberId);
		MemberConductCommand conduct = memberConductDao.findMemberConductCommandById(memberId);
		StringBuffer emailContent = new StringBuffer();
		emailContent.append(member.getId()).append(member.getId()).append(email).append(member.getType())
				.append(member.getSource()).append(conduct.getRegisterTime());
		String encodeChckSum = EncryptUtil.getInstance().hash(emailContent.toString(), SEND_MAIL_KEY);

		if (!encodeChckSum.equals(checkSum)) {
			throw new BusinessException(Constants.VALIDEMAIL_ERROR);
		}
		Member mem = memberDao.getByPrimaryKey(memberId);
		mem.setLoginEmail(email);
		Member memRes = memberDao.save(mem);

		MemberPersonalData personData = memberPersonalDataDao.getByPrimaryKey(memberId);
		personData.setEmail(email);
		MemberPersonalData personDataRes = memberPersonalDataDao.save(personData);
		if (personDataRes == null || memRes == null) {
			throw new BusinessException(Constants.BINDEMAIL_ERROR);
		}
		return true;
	}

	@Override
	public String sendBindMobileCode(String mobile, Long memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean bindMobile(String mobile, Long memberId) {
		Member member = memberDao.getByPrimaryKey(memberId);
		member.setLoginMobile(mobile);
		Member res = memberDao.save(member);
		if (res == null) {
			return false;
		}
		return true;
	}

	/**
	 * 获取随机用户名
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getUsername() {
		StringBuffer sb = new StringBuffer();
		sb.append("bz").append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())).append(getRandomNumber());
		return sb.toString();
	}

	/**
	 * 获取一个四位随机数，并且四位数不重复
	 * 
	 * @return Set<Integer>
	 */
	private static Set<Integer> getRandomNumber() {
		// 使用SET以此保证写入的数据不重复
		Set<Integer> set = new HashSet<Integer>();
		// 随机数
		Random random = new Random();

		while (set.size() < 4) {
			// nextInt返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）
			// 和指定值（不包括）之间均匀分布的 int 值。
			set.add(random.nextInt(10));
		}
		return set;
	}

	@Override
	@Transactional(readOnly=true)
	public MemberFavorites findMemberFavoritesByIdAndMemberId(Long id, Long memberId) {
		// TODO Auto-generated method stub
		return memberFavoritesDao.findMemberFavoritesByIdAndMemberId(id, memberId);
	}

	@Override
	@Transactional(readOnly=true)
	public Member findMember(String loginName) {
		Member member = null;
		if (StringUtils.isNotBlank(loginName)) {
			String name = loginName.toUpperCase();
			// 先检查loginName
			member = memberDao.findMemberByLoginName(name);
			if (null == member) {
				// 如果loginName没有对应的用户，则检查该loginName是否符合手机号码或邮箱,查看是否有对应的用户
				if (RegulareExpUtils.isMobileNO(loginName)) {
					member = memberDao.findMemberByLoginMobile(loginName);
				} else if (RegulareExpUtils.isSureEmail(loginName)) {
					member = memberDao.findMemberByLoginEmail(name);
				}
			}
		}
		return member;
	}

	@Override
	@Transactional(readOnly=true)
	public MemberPersonalData findMemberPersonDataByMobile(String mobile) {

		MemberPersonalData mpd = memberPersonalDataDao.findMemberPersonDataByMobile(mobile);

		decrypt(mpd);

		return mpd;
	}

	@Override
	@Transactional(readOnly=true)
	public void sendActiveByEmail(Long memberId, String path) {
		String SEND_MAIL_KEY = ProfileConfigUtil.findPro("config/metainfo.properties").getProperty("send.mail.key");
		Member member = memberDao.findMemberById(memberId);
		MemberPersonalData personData = memberPersonalDataDao.findByMemberId(memberId);
		decrypt(personData);
		MemberConductCommand conduct = findMemberConductCommandById(member.getId());
		StringBuffer emailContent = new StringBuffer();
		emailContent.append(member.getId()).append(member.getId()).append(personData.getEmail())
				.append(member.getType()).append(member.getSource()).append(conduct.getRegisterTime());
		String checksum = EncryptUtil.getInstance().hash(emailContent.toString(), SEND_MAIL_KEY);
		StringBuffer activeEmailUrl = new StringBuffer();

		try {
			StringBuffer encryptParams = new StringBuffer();
			encryptParams.append("member_id=").append(member.getId()).append("&checksum=").append(checksum)
					.append("&sendTime=").append(new Date().getTime());
			String encrypt = EncryptUtil.getInstance().encrypt(encryptParams.toString());
			activeEmailUrl.append(path).append("?registerComfirm=")
					.append(URLEncoder.encode(encrypt, Constants.CHARSET));

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("activeEmailUrl-----------------------: " + activeEmailUrl);
	}

	@Override
	public void validEmailActiveUrl(Long memberId, String checkSum) {
		String SEND_MAIL_KEY = ProfileConfigUtil.findPro("config/metainfo.properties").getProperty("send.mail.key");
		Member member = memberDao.findMemberById(memberId);
		MemberPersonalData personData = memberPersonalDataDao.findByMemberId(memberId);
		decrypt(personData);
		MemberConductCommand conduct = memberConductDao.findMemberConductCommandById(memberId);
		StringBuffer emailContent = new StringBuffer();
		emailContent.append(member.getId()).append(member.getId()).append(personData.getEmail())
				.append(member.getType()).append(member.getSource()).append(conduct.getRegisterTime());
		String encodeChckSum = EncryptUtil.getInstance().hash(emailContent.toString(), SEND_MAIL_KEY);
		if (!encodeChckSum.equals(checkSum)) {
			throw new BusinessException(Constants.VALIDEMAIL_ERROR);
		}
		member.setLoginEmail(personData.getEmail());
		Member res = memberDao.save(member);
		if (res == null) {
			throw new BusinessException(Constants.BINDEMAIL_ERROR);
		}

	}

	@Override
	@Transactional(readOnly=true)
	public Member findThirdMemberByThirdIdAndSource(String thirdPartyIdentify, Integer source) {
		Member member = memberDao.findThirdMemberByUidAndSource(thirdPartyIdentify, source,
				Member.MEMBER_TYPE_THIRD_PARTY_MEMBER);
		if (null != member)
			return member;
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public List<MemberGroupRelation> findMemberGroupRelationListByMemberId(Long memberId) {
		return memberGroupRelationDao.findMemberGroupRelationListByMemberId(memberId);
	}

	@Override
	@Transactional(readOnly=true)
	public List<MemberGroup> findMemberGroupListByIds(List<Long> ids) {

		return memberGroupDao.findMemberGroupListByIds(ids);
	}

	@Override
	@Transactional(readOnly=true)
	public List<OrderLineCommand> findOrderLineCompletionByItemIdOrUserId(Long memberId, Long itemId) {

		return sdkOrderLineDao.findOrderLineCompletionByItemIdOrUserId(memberId, itemId);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<RateCommand> findItemRateListByMemberId(Page page, Sort[] sorts, Long memberId) {
		Pagination<RateCommand> itemRateCommand = itemRateDao.findItemRateListByMemberId(page, sorts, memberId);
		return itemRateCommand;
	}

	@Override
	@Transactional(readOnly=true)
	public Set<String> getMemComboIdsByGroupIdMemberId(List<Long> groupIds, Long memberId) {
		return sdkEngineManager.getCrowdScopeListByMemberAndGroup(memberId, groupIds);
	}

	@Override
	@Transactional(readOnly=true)
	public MemberCommand findMemberByLoginMobile(String loginMobile) {
		// TODO Auto-generated method stub
		Member member = memberDao.findMemberByLoginMobile(loginMobile);

		if (null != member)
			return convertMemberToMemberCommand(member);

		return null;
	}
}
