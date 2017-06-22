package com.baozun.nebula.manager.member;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.constant.ThirdPartyLoginBindConstants;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberBehaviorStatus;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMember;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.feilong.core.Validator;

@Transactional
@Service("thirdPartyMemberManager")
public class ThirdPartyMemberManagerImpl implements ThirdPartyMemberManager{

	private static final Logger	LOG	= LoggerFactory.getLogger(ThirdPartyMemberManagerImpl.class);

	@Autowired
	private SdkMemberManager	sdkMemberManager;

	@Autowired
	private MemberManager		membManager;

	@Autowired
	private MemberDao			memberDao;

	@Override
	public Boolean validFirstLogin(ThirdPartyMember member,Integer source,String ip){

		// 根据uid和来源查询指定会员
		Member dbMember = sdkMemberManager.findTommyThirdMemberByThirdIdAndSource(member.getUid(), source);
		// 当前日期
		Date nowDate = new Date();

		if (dbMember != null){
			// 更新登录日志
			MemberConductCommand memberConductCommand = sdkMemberManager.findMemberConductCommandById(dbMember.getId());

			Integer count = 0;

			if (null == memberConductCommand){
				memberConductCommand = new MemberConductCommand();
			}
			if (null == memberConductCommand.getLoginCount()){
				memberConductCommand.setLoginCount(count);
			}
			// 登录次数加一
			count = memberConductCommand.getLoginCount() + 1;

			memberConductCommand.setLoginCount(count);
			memberConductCommand.setLoginTime(nowDate);
			memberConductCommand.setLoginIp(ip);
			sdkMemberManager.saveMemberConduct(memberConductCommand);

			return Boolean.FALSE;
		}else{
			// 注册会员
			MemberFrontendCommand memberCommand = new MemberFrontendCommand();
			MemberConductCommand conductCommand = null;
			conductCommand = new MemberConductCommand(null, 1, nowDate, nowDate, ip, ip);

			memberCommand.setThirdPartyIdentify(member.getUid());
			memberCommand.setType(Member.MEMBER_TYPE_THIRD_PARTY_MEMBER);
			memberCommand.setSource(source);
			// 设置性别 ，默认男
			if (Validator.isNotNullOrEmpty(member.getSex())){
				try{
					memberCommand.setSex(Integer.valueOf(member.getSex()));
				}catch (NumberFormatException e){
					LOG.error("the method -NumberFormatException .", e);
					memberCommand.setSex(1);
				}
			}else{
				memberCommand.setSex(1);
			}
			// memberCommand.setLoginName(member.getNickName());
			memberCommand.setNickname(member.getNickName());
			memberCommand.setLifecycle(Member.STATUS_ENABLE);
			memberCommand.setMemberConductCommand(conductCommand);

			dbMember = membManager.register(memberCommand);

			return Boolean.TRUE;
		}
	}

	@Override
	public String bindThirdPartyLoginAccount(Long thirdPartyMemberId,Long memberId,String type){
		LOG.info("thirdPartyMemberAccount bind start");
		// 获取第三方登录的用户信息
		Member thirdPartyMember = memberDao.findMemberById(thirdPartyMemberId);
		// 判断用户是否存在
		if (thirdPartyMember == null){
			LOG.error("thirdPartyMember not exist");
			return ThirdPartyLoginBindConstants.MEMBER_NOT_EXIST_ERROR;
		}

		// 获取商城用户信息
		Member storeMember = memberDao.findMemberById(memberId);
		// 判断用户是否存在
		if (storeMember == null){
			LOG.error("storeMember not exist");
			return ThirdPartyLoginBindConstants.MEMBER_NOT_EXIST_ERROR;
		}

		// 判断是否绑定
		if (Validator.isNotNullOrEmpty(thirdPartyMember.getGroupId())){
			LOG.error("thirdPartyMember has bind");
			return ThirdPartyLoginBindConstants.MEMBER_HAS_BIND_ERROR;
		}

		// 将商城用户ID 更新到第三方用户的groupId上
		Integer res = memberDao.updateMemberGroupIdById(thirdPartyMemberId, memberId);

		// 创建用户行为状态
		MemberBehaviorStatus memberBehaviorStatus = new MemberBehaviorStatus();
		memberBehaviorStatus.setMemberId(memberId);
		memberBehaviorStatus.setType(type);
		memberBehaviorStatus.setLifecycle(1);

		Integer bindResult = sdkMemberManager.saveMemberBehaviorStatus(memberBehaviorStatus);
		if (res < 0){
			LOG.error("thirdPartyMember bind error");
			return ThirdPartyLoginBindConstants.BIND_ACCOUNT_FAILED;
		}

		if (bindResult < 0){
			LOG.error("memberBehaviorStatus create error");
			return ThirdPartyLoginBindConstants.CREATE_MEMBERBEHAVIORSTATUS_ERROR;
		}

		LOG.info("thirdPartyMemberAccount bind success");

		return ThirdPartyLoginBindConstants.BIND_ACCOUNT_SUCCESS;
	}

}
