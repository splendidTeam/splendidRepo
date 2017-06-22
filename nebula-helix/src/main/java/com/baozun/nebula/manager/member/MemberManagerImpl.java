package com.baozun.nebula.manager.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.dao.member.MemberPersonalDataDao;
import com.baozun.nebula.event.EmailEvent;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.exception.PasswordNotMatchException;
import com.baozun.nebula.exception.UserExpiredException;
import com.baozun.nebula.exception.UserNotExistsException;
import com.baozun.nebula.manager.email.EmailCheckManager;
import com.baozun.nebula.model.email.EmailCheck;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberCryptoguard;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.utils.RegulareExpUtils;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.baozun.nebula.utils.EmailParamEnciphermentUtil;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.feilong.core.Alphabet;
import com.feilong.core.RegexPattern;
import com.feilong.core.Validator;
import com.feilong.core.util.RandomUtil;
import com.feilong.core.util.RegexUtil;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

@Transactional
@Service("membManager")
public class MemberManagerImpl implements MemberManager{

	private static final Logger		LOGGER		= LoggerFactory.getLogger(MemberManagerImpl.class);

	@Autowired
	private SdkMemberManager		sdkMemberManager;

	@Autowired
	private SdkItemManager			sdkItemManager;

	@Autowired
	private EmailCheckManager		emailCheckManager;

	@Autowired
	private MemberPersonalDataDao	memberPersonalDataDao;

	@Autowired
	private EventPublisher			eventPublisher;

	@Autowired
	private MemberDao				memberDao;

	@Value("#{meta['page.base']}")
	private String					pageUrlBase	= "";

	@Override
	public MemberPersonalData findMemberPersonData(Long memberId){
		return sdkMemberManager.findMemberPersonData(memberId);
	}

	@Override
	public boolean updatePasswd(Long memberId,String pwd,String newPwd,String reNewPwd){
		return sdkMemberManager.updatePasswd(memberId, pwd, newPwd, reNewPwd);
	}

	@Override
	public void saveCryptoguard(List<MemberCryptoguard> memberCryptoguardList){
		if (memberCryptoguardList != null){
			Long memberId = memberCryptoguardList.get(0).getMemberId();
			List<MemberCryptoguard> res = sdkMemberManager.findCryptoguardList(memberId);
			if (res != null)
				sdkMemberManager.removeCryptoguardByMemberId(memberId);
			for (MemberCryptoguard memberCryptoguard : memberCryptoguardList){
				sdkMemberManager.saveCryptoguard(memberCryptoguard);
			}
		}
	}

	@Override
	public List<MemberCryptoguard> findCryptoguardList(Long memberId){
		return sdkMemberManager.findCryptoguardList(memberId);
	}

	@Override
	public MemberPersonalData savePersonData(MemberPersonalData personData){

		if (personData.getCountryId() != null){
			Address address = AddressUtil.getAddressById(personData.getCountryId());
			personData.setCountry(address.getName());
		}

		if (personData.getProvinceId() != null){
			Address address = AddressUtil.getAddressById(personData.getProvinceId());
			personData.setProvince(address.getName());
		}

		if (personData.getCityId() != null){
			Address address = AddressUtil.getAddressById(personData.getCityId());
			personData.setCity(address.getName());
		}

		if (personData.getAreaId() != null){
			Address address = AddressUtil.getAddressById(personData.getAreaId());
			personData.setArea(address.getName());
		}

		if (personData.getTownId() != null){
			Address address = AddressUtil.getAddressById(personData.getTownId());
			personData.setTown(address.getName());
		}

		return sdkMemberManager.savePersonData(personData);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkNickname(String nickname){
		return sdkMemberManager.checkNickname(nickname);
	}

	@Override
	public void sendBindEmailUrl(Long memberId,String email,String path){
		sdkMemberManager.sendBindEmailUrl(memberId, email, path);
	}

	@Override
	public boolean bindEmail(Long memberId,String cryptCode,String email){
		return sdkMemberManager.bindEmail(memberId, cryptCode, email);
	}

	@Override
	public String sendBindMobileCode(String mobile,Long memberId){
		return sdkMemberManager.sendBindMobileCode(mobile, memberId);
	}

	@Override
	public boolean bindMobile(String mobile,Long memberId){
		return sdkMemberManager.bindMobile(mobile, memberId);
	}

	private void saveLoginMemberConduct(MemberConductCommand codunctCommand,Long memberId){
		MemberConductCommand condCommand = sdkMemberManager.findMemberConductCommandById(memberId);
		Integer count = 0;
		if (null != condCommand){
			if (null == condCommand.getLoginCount())
				condCommand.setLoginCount(count);
			count = condCommand.getLoginCount() + 1;
		}else{
			condCommand = new MemberConductCommand();
			count = count + 1;
		}
		condCommand.setLoginCount(count);
		if (codunctCommand != null){
			condCommand.setLoginTime(codunctCommand.getLoginTime());
			condCommand.setLoginIp(codunctCommand.getLoginIp());
		}
		sdkMemberManager.saveMemberConduct(condCommand);
	}

	@Transactional(readOnly = true)
	@Override
	public Member findMember(String loginName){
		return sdkMemberManager.findMember(loginName);
	}

	@Transactional(readOnly = true)
	@Override
	public MemberCommand findMemberByUsername(String username){
		return sdkMemberManager.findMemberByLoginName(username);
	}

	@Transactional(readOnly = true)
	@Override
	public MemberPersonalData findMemberPersonDataByMobile(String mobile){
		return sdkMemberManager.findMemberPersonDataByMobile(mobile);
	}

	/**
	 * 通过memberId获得所有会员详细信息,通过groupCode获取ChooseOption里面的字段值
	 * 
	 * @param memberID
	 * @param groupCode
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public MemberCommand findMemberById(Long memberId){
		MemberCommand member = sdkMemberManager.findMemberById(memberId);
		return member;
	}

	@Override
	public MemberCommand saveMember(MemberCommand memberCommand){
		return sdkMemberManager.saveMember(memberCommand);
	}

	@Override
	public void sendActiveByEmail(Long memberId,String path){
		// sdkMemberManager.sendActiveByEmail(memberId,path);
		setEncryptedMsg(memberId, path);
	}

	private void setEncryptedMsg(Long memberId,String path){
		Properties properties = ProfileConfigUtil.findPro("config/email.properties");
		String key = properties.getProperty("param.register.key");
		String action = properties.getProperty("param.register.action");
		Date date = new Date();
		Random random = new Random();
		int sequence = random.nextInt();
		String t_q_s = EmailParamEnciphermentUtil.enciphermentParam(null, date, action, key, String.valueOf(sequence));
		String token = t_q_s.substring(t_q_s.indexOf("t=") + 2, t_q_s.indexOf("&q="));
		String s = t_q_s.substring(t_q_s.indexOf("s=") + 2, t_q_s.length());
		// 获取用户信息
		MemberPersonalData personData = memberPersonalDataDao.findByMemberId(memberId);
		// 用户邮件
		String email = personData.getEmail();
		// 加密信息
		EmailCheck ec = new EmailCheck();
		ec.setMemberId(memberId);
		ec.setCreateTime(date);
		ec.setEncrypted_S(s);
		ec.setEmailAddress(email);
		ec.setStatus(EmailCheck.STATUS_VALID_NOTCLICK);
		ec.setCount(1);
		ec.setModifyTime(date);
		ec.setToken(token);
		// 保存加密信息供验证时使用
		emailCheckManager.createEmailCheck(ec);
		// 邮件内容
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("pageUrlBase", pageUrlBase);
		String url = path + "?" + t_q_s;
		LOGGER.info("激活邮件url:{}", url);
		dataMap.put("link", "<a href='" + url + "'>" + url + "</a>");
		// 发送邮件
		EmailEvent emailEvent = new EmailEvent(this, email, EmailConstants.EMAIL_ACTIVE_TEMPLATE, dataMap);
		eventPublisher.publish(emailEvent);
	}

	@Transactional(readOnly = true)
	@Override
	public Pagination<RateCommand> findItemRateListByMemberId(Page page,Sort[] sorts,Long memberId){
		Pagination<RateCommand> ratePage = sdkMemberManager.findItemRateListByMemberId(page, sorts, memberId);

		List<Long> itemIds = new ArrayList<Long>();
		if (ratePage != null){
			List<RateCommand> reateList = ratePage.getItems();
			for (RateCommand rate : reateList){
				itemIds.add(rate.getItemId());

			}

			Map<Long, List<ItemImage>> map = new HashMap<Long, List<ItemImage>>();
			List<ItemImageCommand> itemImageCommandList = sdkItemManager.findItemImagesByItemIds(itemIds, ItemImage.IMG_TYPE_LIST);
			for (ItemImageCommand itemImageCommand : itemImageCommandList){
				map.put(itemImageCommand.getItemId(), itemImageCommand.getItemIamgeList());

			}

			for (RateCommand rate : reateList){
				rate.setItemPicUrlList(map.get(rate.getItemId()));
			}
			ratePage.setItems(reateList);
		}

		return ratePage;
	}

	@Override
	public MemberCommand login(MemberFrontendCommand memberCommand) throws UserNotExistsException,UserExpiredException,
			PasswordNotMatchException{
		MemberCommand member = findMemberCommandByLoginName(memberCommand.getLoginName());

		if (member == null){
			throw new UserNotExistsException();
		}
		// 盐值为空时走原来验证逻辑，验证通过使用新的加密算法生成新的密码，然后保存盐值和新密码 add by ruichao.gao
		if (Validator.isNullOrEmpty(member.getSalt())){
			String encodePassword = EncryptUtil.getInstance().hash(memberCommand.getPassword(), member.getLoginName());
			if (!encodePassword.equals(member.getPassword())){
				throw new PasswordNotMatchException();
			}

			// 生成新的盐值，用新的加密算法进行加密，
			String salt = RandomUtil.createRandomFromString(Alphabet.DECIMAL_AND_LETTERS, 88);
			String pwd = EncryptUtil.getInstance().hashSalt(memberCommand.getPassword(), salt);
			// 保存密码和盐值
			Member mem = memberDao.findMemberById(member.getId());
			mem.setSalt(salt);
			mem.setPassword(pwd);
			memberDao.save(mem);
		}else{
			String encodePassword = EncryptUtil.getInstance().hashSalt(memberCommand.getPassword(), member.getSalt());
			if (!encodePassword.equals(member.getPassword())){
				throw new PasswordNotMatchException();
			}
		}

		// 保存用户行为信息
		saveLoginMemberConduct(memberCommand.getMemberConductCommand(), member.getId());
		return member;
	}

	@Override
	public MemberCommand findMemberCommandByLoginName(String loginName){
		MemberCommand member;
		if (RegexUtil.matches(RegexPattern.MOBILEPHONE, loginName)){
			member = sdkMemberManager.findMemberByLoginMobile(loginName);
		}else if (RegexUtil.matches(RegexPattern.EMAIL, loginName)){
			member = sdkMemberManager.findMemberByLoginEmail(loginName);
		}else{
			member = sdkMemberManager.findMemberByLoginName(loginName);
		}
		return member;
	}

	@Deprecated
	@Override
	public Member register(MemberFrontendCommand memberCommand){
		// 保存会员信息
		String thirdPartyIdentify = memberCommand.getThirdPartyIdentify();
		// 第三方标识 member表字段置空
		memberCommand.setThirdPartyIdentify(null);

		Member member = sdkMemberManager.register((Member) ConvertUtils.convertTwoObject(new Member(), memberCommand));
		if (null == member){
			LOGGER.info("member is null");
			throw new BusinessException(ErrorCodes.SYSTEM_ERROR);
		}
		MemberPersonalData personData = new MemberPersonalData();
		personData.setId(member.getId());
		if (memberCommand.getType() != Member.MEMBER_TYPE_THIRD_PARTY_MEMBER){
			if (RegulareExpUtils.isMobileNO(memberCommand.getLoginName())){
				personData.setMobile(memberCommand.getLoginName());
			}else if (RegulareExpUtils.isSureEmail(memberCommand.getLoginName())){
				personData.setEmail(memberCommand.getLoginName());
			}
		}else{
			// 根据会员来源，保存uid
			if (Member.MEMBER_SOURCE_QQ == memberCommand.getSource()){
				personData.setShort3(thirdPartyIdentify);
			}else if (Member.MEMBER_SOURCE_ALIPAY == memberCommand.getSource()){
				personData.setShort4(thirdPartyIdentify);
			}else if (Member.MEMBER_SOURCE_WECHAT == memberCommand.getSource()){
				personData.setShort5(thirdPartyIdentify);
			}
		}
		// loginMobile不为null,则写入persondata
		if (StringUtils.isNotBlank(memberCommand.getLoginMobile())){
			personData.setMobile(memberCommand.getLoginMobile());
		}

		// loginEmail不为null,则写入persondata
		if (StringUtils.isNotBlank(memberCommand.getLoginEmail())){
			personData.setEmail(memberCommand.getLoginEmail());
		}
		personData.setNickname(memberCommand.getNickname());
		personData.setSex(memberCommand.getSex());
		// 保存会员个人资料信息
		personData = sdkMemberManager.savePersonData(personData);
		if (null == personData){
			LOGGER.info("personData is null");
			throw new BusinessException(ErrorCodes.SYSTEM_ERROR);
		}
		// 保存用户行为信息
		memberCommand.getMemberConductCommand().setId(member.getId());
		MemberConductCommand conductCommand = sdkMemberManager.saveMemberConduct(memberCommand.getMemberConductCommand());
		if (null == conductCommand){
			LOGGER.info("conductCommand is null");
			throw new BusinessException(ErrorCodes.SYSTEM_ERROR);
		}

		return member;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.member.MemberManager#rewriteRegister(com.baozun.nebula.web.command.MemberFrontendCommand)
	 */
	@Override
	public Member rewriteRegister(MemberFrontendCommand memberCommand){
		// 保存会员信息
		Member targetMember = (Member) ConvertUtils.convertTwoObject(new Member(), memberCommand);
		Member member = sdkMemberManager.rewriteRegister(targetMember);
		Long memberId = member.getId();

		LOGGER.info("[save Member entity suc!] [{}] ID:{} \"\"", new Date(), memberId);

		MemberPersonalData personData = new MemberPersonalData();
		personData = (MemberPersonalData) ConvertUtils.convertTwoObject(personData, memberCommand.getMemberPersonalDataCommand());
		if (null == personData){
			personData = new MemberPersonalData();
		}
		personData.setId(memberId);

		if (memberCommand.getType() != Member.MEMBER_TYPE_THIRD_PARTY_MEMBER){
			if (Validator.isNotNullOrEmpty(memberCommand.getLoginName())){
				if (RegexUtil.matches(RegexPattern.MOBILEPHONE, memberCommand.getLoginName())){
					personData.setMobile(memberCommand.getLoginName());
				}else if (RegexUtil.matches(RegexPattern.EMAIL, memberCommand.getLoginName())){
					personData.setEmail(memberCommand.getLoginName());
				}
			}
		}

		if (StringUtils.isNotBlank(memberCommand.getMobile())){
			personData.setMobile(memberCommand.getMobile());
		}

		if (StringUtils.isNotBlank(memberCommand.getEmail())){
			personData.setEmail(memberCommand.getEmail());
		}

		// loginMobile不为null,则写入persondata
		if (StringUtils.isNotBlank(memberCommand.getLoginMobile())){
			personData.setMobile(memberCommand.getLoginMobile());
		}

		// loginEmail不为null,则写入persondata
		if (StringUtils.isNotBlank(memberCommand.getLoginEmail())){
			personData.setEmail(memberCommand.getLoginEmail());
		}
		// 保存会员个人资料信息
		sdkMemberManager.savePersonData(personData);

		LOGGER.info("[save MemberPersonalData entity suc!] [{}] ID:{} \"\"", new Date(), memberId);

		// 保存用户行为信息
		memberCommand.getMemberConductCommand().setId(memberId);
		MemberConductCommand memberConduct = sdkMemberManager.saveMemberConduct(memberCommand.getMemberConductCommand());

		LOGGER.info("[save MemberConduct entity suc!] [{}] conduct_id:{} \"\"", new Date(), memberConduct.getId());

		return member;
	}

	@Override
	public void validEmailActiveUrl(Long memberId,String checkSum){
		sdkMemberManager.validEmailActiveUrl(memberId, checkSum);
	}

	@Override
	public boolean bindMobileAndSynShopppingCart(String mobile,Long memberId){
		return sdkMemberManager.bindMobile(mobile, memberId);
	}

	@Override
	public boolean validEmailActiveUrl(String t,String q,String s){
		Properties properties = ProfileConfigUtil.findPro("config/email.properties");
		String key = properties.getProperty("param.register.key");
		String action = properties.getProperty("param.register.action");
		return EmailParamEnciphermentUtil.checkParam(action, key, t, s, q);
	}

	@Override
	public void bindMemberEmail(Long memberId,String email){
		Member member = memberDao.findMemberById(memberId);
		member.setLoginEmail(email);
		Member res = memberDao.save(member);
		if (res == null){
			throw new BusinessException(Constants.BINDEMAIL_ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.member.MemberManager#checkRegisterData(com.baozun.nebula.web.command.MemberFrontendCommand,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public NebulaReturnResult checkRegisterData(MemberFrontendCommand mfc){
		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
		defaultReturnResult.setResult(true);

		Map<String, String> returnObject = new HashMap<String, String>();

		// 验证email
		String loginEmail = mfc.getLoginEmail();
		if (Validator.isNotNullOrEmpty(loginEmail)){
			MemberCommand findMemberByLoginEmail = sdkMemberManager.findMemberByLoginEmail(loginEmail);
			if (Validator.isNotNullOrEmpty(findMemberByLoginEmail)){
				defaultReturnResult.setResult(false);
				returnObject.put("loginEmail", "register.loginemail.unavailable");
			}
		}
		// 验证mobile
		String loginMobile = mfc.getLoginMobile();
		if (Validator.isNotNullOrEmpty(loginMobile)){
			MemberCommand findMemberByLoginMobile = sdkMemberManager.findMemberByLoginMobile(loginMobile);
			if (Validator.isNotNullOrEmpty(findMemberByLoginMobile)){
				defaultReturnResult.setResult(false);
				returnObject.put("loginMobile", "register.loginmobile.unavailable");
			}
		}

		// 验证 LoginName
		String loginName = mfc.getLoginName();
		if (Validator.isNotNullOrEmpty(loginName)){
			MemberCommand findMemberByLoginName = sdkMemberManager.findMemberByLoginName(loginName);
			if (Validator.isNotNullOrEmpty(findMemberByLoginName)){
				defaultReturnResult.setResult(false);
				returnObject.put("loginName", "register.loginname.unavailable");
			}
		}

		defaultReturnResult.setReturnObject(returnObject);

		return defaultReturnResult;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.member.MemberManager#setupMemberReference(com.baozun.nebula.web.command.MemberFrontendCommand,
	 * java.lang.String)
	 */
	@Override
	public void setupMemberReference(MemberFrontendCommand memberFrontendCommand,String clientIp){
		// 生命周期：未激活状态
		memberFrontendCommand.setLifecycle(Member.LIFECYCLE_UNACTIVE);
		// 来源：自注册
		memberFrontendCommand.setSource(Member.MEMBER_SOURCE_SINCE_REG_MEMBERS);
		// 类型：自注册会员
		memberFrontendCommand.setType(Member.MEMBER_TYPE_SINCE_REG_MEMBERS);

		int loginCount = 0;
		Date registerTime = new Date();

		MemberConductCommand conductCommand = new MemberConductCommand(loginCount, registerTime, clientIp);

		memberFrontendCommand.setMemberConductCommand(conductCommand);

	}

	@Override
	@Transactional(readOnly = true)
	public List<MemberCommand> findMembersByIds(List<Long> ids){
		return sdkMemberManager.findMembersByIds(ids);
	}

}