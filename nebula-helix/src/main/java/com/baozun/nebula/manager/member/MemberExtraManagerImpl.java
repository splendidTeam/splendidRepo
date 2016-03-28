package com.baozun.nebula.manager.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.member.MemberPersonalDataDao;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.utils.RegulareExpUtils;
import com.feilong.core.Validator;

@Transactional
@Service("memberExtraManager")
public class MemberExtraManagerImpl implements MemberExtraManager{

	@Autowired
	private SdkMemberManager		sdkMemberManager;

	@Autowired
	private MemberPersonalDataDao	memberPersonalDataDao;

	@Override
	@Transactional(readOnly = true)
	public MemberPersonalData findMemPersonalDataByLoginName(String loginName){
		// 查询用户
		MemberCommand member = null;
		if (RegulareExpUtils.isMobileNO(loginName)){
			member = sdkMemberManager.findMemberByLoginMobile(loginName);
		}else if (RegulareExpUtils.isSureEmail(loginName)){
			member = sdkMemberManager.findMemberByLoginEmail(loginName);
		}else{
			member = sdkMemberManager.findMemberByLoginName(loginName);
		}

		// 根据用户id查询用户的个人信息
		if (Validator.isNotNullOrEmpty(member)){
			MemberPersonalData memberPersonalData = memberPersonalDataDao.findMemberPersonalDataByMemberId(member.getId());
			return memberPersonalData;
		}

		return null;
	}

	@Override
	public boolean rememberPwd(Long memberId,String short4){
		return memberPersonalDataDao.rememberPwd(memberId, short4) > 0;
	}

}
