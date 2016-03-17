package com.baozun.nebula.sdk.command.member;

import java.util.List;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.member.MemberCryptoguard;

public class CryptoguardCommand  implements Command{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4659133410231042306L;
	private List<MemberCryptoguard> memberCryptoguardList;

	public List<MemberCryptoguard> getMemberCryptoguardList() {
		return memberCryptoguardList;
	}

	public void setMemberCryptoguardList(
			List<MemberCryptoguard> memberCryptoguardList) {
		this.memberCryptoguardList = memberCryptoguardList;
	}

}
