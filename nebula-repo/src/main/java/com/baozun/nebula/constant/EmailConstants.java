package com.baozun.nebula.constant;

/**
 * 邮件通用模板配置
 * @author lxy
 * @date  
 * @version
 */
public class EmailConstants {
    /**支付成功*/
    public static final String PAY_ORDER_SUCCESS ="EMAIL_PAY_ORDER_SUCCESS" ;
	/**下单成功 */
    public static final String CREATE_ORDER_SUCCESS ="EMAIL_CREATE_ORDER_SUCCESS" ;
    /**取消订单 */
    public static final String CANCEL_ORDER_SUCCESS ="EMAIL_CANCEL_ORDER_SUCCESS" ;
    /**订单发货 */
    public static final String ORDER_DELIVERY ="EMAIL_ORDER_DELIVERY" ;
    /**交易完成 */
    public static final String ORDER_DEAL_COMPLATE ="EMAIL_ORDER_DEAL_COMPLATE" ;
    /**忘记密码 */
	public static final String FORGET_PASSWORD="EMAIL_FORGET_PASSWORD";
    /** 取消失败预警*/
    public static final String SYS_CANCEL_ORDER_FAILURE ="EMAIL_SYS_CANCEL_ORDER_FAILURE" ;
    
    /**激活邮件模板 */
    public static final String EMAIL_ACTIVE_TEMPLATE="EMAIL_ACTIVE_TEMPLATE";
    
    /**发送成功邮件模板 */
    public static final String EMAIL_SENDSUCCESS_TEMPLATE="EMAIL_SENDSUCCESS_TEMPLATE";
    
	/**郵件發送次數*/
	public static final Integer SEND_EMAIL_NUMBER = 99999;
	
	/** 发送邮件时间间隔（分鐘） */
	public static final Integer NEBULA_SEND_EMAIL_AGAIN_TIME_SPAN = 2;
	
	/**註冊緩存key拼接字符*/
	public static final String NEBULA_MEMBER_REGISTER = "_register";
	
	/**最大发送数 cache key*/
	public static final String NEBULA_EMAIL_SENDMAXNUMBER_KEY="nebula_email_sendMaxNumber_key";
	
	/**发送时间间隔 cache key*/
	public static final String NEBULA_EMAIL_INTERVALTIME_KEY="nebula_email_intervalTime_key";
	
	/**最大发送次数验证错误码*/
	public static final String NEBULA_EMAIL_SENDMAXNUMBER_ERRORCODE = "sendMaxNumberError";
	
	/**发送时间间隔验证错误码*/
	public static final String NEBULA_EMAIL_INTERVALTIME_ERRORCODE = "intervalTimeError";
	
	/**发送成功响应码*/
	public static final String NEBULA_EMAIL_SEND_SUCCESSCODE = "sendSuccess";
	
	/** 邮箱已激活*/
	public static final String EMAIL_ACTIVE_YES = "Y";
	
	/** 邮箱未激活*/
	public static final String EMAIL_ACTIVE_NO = "N";

}
