package com.baozun.nebula.manager.email;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.email.EmailCheck;

/**修改密码邮件*/
public interface EmailCheckManager extends BaseManager{

	/** 加密序列*/
	Long findNextval();
	
	/** 创建或修改修改密码邮件信息*/
	public EmailCheck createEmailCheck(EmailCheck email);
	
	/** 查询密码邮件记录*/
	public EmailCheck findEmailCheckByEncryptedS(String Encrypted_S);
	
	
	/** 获取当天有效的EmailCheck*/
	public EmailCheck findEmailCheckByLoginEmail(String emailAddress,Integer status);
	
	/** 修改记录状态*/
	public Integer updateEmailCheckStatusById(Long id, Integer status);
	
	/** 设置所有过期记录无效*/
	public Integer updateExpireEmailCheckInvalid(Long memberId);
	
	
	/** 获取当天所有EmailCheck列表*/
	List<EmailCheck> findEmailCheckListByDay( Long memberId);
	
	/** 查询当天的申请次数*/
	Integer findCountsByLoginEmail(String emailAddress);
}
