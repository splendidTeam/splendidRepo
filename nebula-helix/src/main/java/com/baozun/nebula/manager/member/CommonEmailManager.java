package com.baozun.nebula.manager.member;

import java.util.Map;

import com.baozun.nebula.manager.BaseManager;


/**
 * 通用邮件发送接口 实现以下方法
 * <ol>
 * 
 * 	<li>发送邮件 {@link #sendEmail}</li>
 * 
 * </ol>
 * @author yufei.kong 2016年4月5日 16:18:38
 */
public interface CommonEmailManager extends BaseManager {
	
	/**
	 * 返回结果枚举类
	 * 
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
	  SendEmailResultCode sendEmail(String email, String emailTemplateCode,Map<String, Object> dataMap);
	
	
}
