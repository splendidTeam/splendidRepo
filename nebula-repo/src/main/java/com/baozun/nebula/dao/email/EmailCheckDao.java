package com.baozun.nebula.dao.email;


import java.util.Date;
import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.email.EmailCheck;

public interface EmailCheckDao extends GenericEntityDao<EmailCheck,Long>{


	@NativeQuery(clazzes=Long.class,alias="seqValue")
	Long findNextval();
	
	/**
	 * 获取EmailCheck
	 * @return
	 */
	@NativeQuery(model = EmailCheck.class)
	EmailCheck findEmailCheckByMidAndEncryptedS(@QueryParam("memberId") Long memberId,@QueryParam("EncryptedS") String Encrypted_S);
	
	/**
	 * 获取EmailCheck
	 * @return
	 */
	@NativeQuery(model = EmailCheck.class)
	EmailCheck findEmailCheckByEncryptedS(@QueryParam("EncryptedS") String Encrypted_S);
	
	/**
	 * 获取当天有效的EmailCheck
	 * @return
	 */
	@NativeQuery(model = EmailCheck.class)
	EmailCheck findEmailCheckByLoginEmail(@QueryParam("emailAddress") String emailAddress,
			@QueryParam("status") Integer status);
	
	/** 设置记录无效*/
	@NativeUpdate
	Integer updateEmailCheckStatusByMemberid(@QueryParam("memberId") Long memberId,
			@QueryParam("status") Integer status, @QueryParam("createtime") Date createtime);
	
	/** 设置记录无效*/
	@NativeUpdate
	Integer updateEmailCheckStatusById(@QueryParam("id") Long id,@QueryParam("status") Integer status);
	
	
	/**
	 * 获取当天所有EmailCheck列表
	 * @return
	 */
	@NativeQuery(model = EmailCheck.class)
	List<EmailCheck> findEmailCheckListByDay(@QueryParam("memberId") Long memberId);
	
	/** 查询当天的申请次数*/
	@NativeQuery(alias="total",clazzes=Integer.class)
	Integer findCountsByLoginEmail(@QueryParam("emailAddress") String emailAddress);
	
}
