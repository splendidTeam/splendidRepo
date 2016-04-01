package com.baozun.nebula.manager.member;

import java.util.Map;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.MemberPersonalData;

/**
 * 会员邮件相关业务类
 * 
 * @author yufei.kong 2016.3.23
 *
 */
public interface CommonEmailManager extends BaseManager {
	
	/**
	 * 成功-SUCESS 
	 * 失败-FAILURE
	 * 最大发送次数错误 -MAXSENDNUMBERERROR
	 * 发送时间间隔错误 -INTERVALTIMEERROR
	 * @author yufei.kong
	 *
	 */
	enum SendEmailResultCode {
		SUCESS, FAILURE, MAXSENDNUMBERERROR,INTERVALTIMEERROR;
	}
	
	
	/**
	 * 通用发送邮件接口
	 * 该方法适用于 无主题,附件型邮件  例如:忘记密码 激活邮件 发送成功等
	 * 
	 * 
	 * @param email 接收邮箱
	 * @param emailTemplateCode 邮件模板code
	 * @param dataMap 参数 需自己组装
	 * @return
	 */
	public SendEmailResultCode sendEmail(String email, String emailTemplateCode,Map<String, Object> dataMap);
	
	
}
