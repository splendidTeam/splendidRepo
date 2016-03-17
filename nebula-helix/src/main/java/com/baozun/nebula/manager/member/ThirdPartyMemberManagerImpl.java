package com.baozun.nebula.manager.member;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMember;
import com.baozun.nebula.web.command.MemberFrontendCommand;

@Transactional
@Service("ThirdPartyMemberManager")
public class ThirdPartyMemberManagerImpl implements ThirdPartyMemberManager {

	@Autowired
	private SdkMemberManager sdkMemberManager;
	@Autowired
	private MemberManager membManager;

	@Override
	public Boolean validFirstLogin(ThirdPartyMember member, Integer source, String ip
			,List<ShoppingCartLineCommand> lines) {
		

		Member dbMember = sdkMemberManager.findThirdMemberByThirdIdAndSource(member.getUid(), source);
		if(dbMember != null){
			MemberConductCommand condCommand = sdkMemberManager.findMemberConductCommandById(dbMember.getId());
			Integer count = 0;
			if(null != condCommand){
				if(null == condCommand.getLoginCount())
					condCommand.setLoginCount(count);
				count = condCommand.getLoginCount() + 1;
			}else{
				condCommand = new MemberConductCommand();
				count = count + 1;
			}
			condCommand.setLoginCount(count);
			condCommand.setLoginTime(new Date());
			condCommand.setLoginIp(ip);
			sdkMemberManager.saveMemberConduct(condCommand);
			return false;
		}else{
			MemberFrontendCommand memberCommand = new MemberFrontendCommand();
			MemberConductCommand conductCommand = null;
			conductCommand = new MemberConductCommand(null,1,new Date(),new Date(),ip,ip);
			
			memberCommand.setThirdPartyIdentify(member.getUid());
			memberCommand.setType(Member.MEMBER_TYPE_THIRD_PARTY_MEMBER);
			memberCommand.setSource(source);
			//memberCommand.setLoginName(member.getNickName());
			memberCommand.setLifecycle(Member.STATUS_ENABLE);
			memberCommand.setMemberConductCommand(conductCommand);
			
			dbMember = membManager.register(memberCommand,lines);
			return true;
		}
	}
	
	



}
