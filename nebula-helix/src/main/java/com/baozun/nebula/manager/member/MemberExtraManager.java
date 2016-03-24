package com.baozun.nebula.manager.member;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.MemberPersonalData;

/**
 * 用户信息接口
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016-3-23 下午2:43:57
 */
public interface MemberExtraManager extends BaseManager{

	/**
	 * 根据用户的登录名查询用户个人信息 MemberPersonalData
	 * 
	 * @author 冯明雷
	 * @time 2016-3-23下午2:45:00
	 */
	MemberPersonalData findMemPersonalDataByLoginName(String loginName);
	
	/**
	 * 根据用户id更新用户的Short4字段(记住密码时使用)
	 * boolean
	 * @author 冯明雷
	 * @time 2016-3-23下午3:04:32
	 */
	boolean rememberPwd(Long memberId,String short4);

}
