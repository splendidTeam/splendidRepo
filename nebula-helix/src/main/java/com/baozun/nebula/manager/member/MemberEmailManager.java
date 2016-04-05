package com.baozun.nebula.manager.member;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.manager.member.CommonEmailManager.SendEmailResultCode;

/**
 * 会员相关邮件发送接口 实现以下方法
 * <ol>
 * 	<li>发送激活邮件 {@link #sendActiveEmail}</li>
 *  <li>发送注册成功的邮件 {@link #sendRegsiterSuccessEmail}</li>
 *  <li>激活用户 {@link #activeMemberAccount}</li>
 *  <li>发送邮件 {@link #sendEmailValidateCode}</li>
 * </ol>
 * 
 * @author yufei.kong 2016年4月5日 16:18:38
 */
public interface MemberEmailManager extends BaseManager {
	
	
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
	enum returnResultCode {
		SUCESS, FAILURE, MAXSENDNUMBERERROR,INTERVALTIMEERROR;
	}
	
	/**
	 * 发送激活邮件
	 * @param memberId
	 * @param path 拼接发送内容中的 前缀地址
	 * @param email 接收邮箱
	 */
	public SendEmailResultCode sendActiveEmail(Long memberId, String path,String email);
	
	/**
	 * 发送注册成功的邮件
	 * @param email 接收地址
	 * @param nickName用户昵称
	 * @return
	 */
	public SendEmailResultCode sendRegsiterSuccessEmail(String email,String nickName);
	
	
	/**
	 * 解析url 适用于发送激活邮件,忘记密码邮件链接返回之后的相关解析
	 * @param memberId
	 * @return List<String> 参数列表
	 */
	public List<String>  analysisTheUrl(String url);
	
	
	/**
	 * 激活用户
	 * @param memberId
	 * @return
	 */
	public String activeMemberAccount(Long memberId);
	
	
	/**
	 * 发送忘记密码邮件
	 * @param code 验证码
	 * @param email
	 */
    public void sendEmailValidateCode(String code, String email);
}
