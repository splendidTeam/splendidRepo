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
    /**
     * 邮件注册验证模板
     */
    public static final String EMAIL_REGISTER_VALIDATE="EMAIL_REGISTER_VALIDATE";
    
	/**郵件發送次數*/
	public static final Integer SEND_EMAIL_NUMBER = 99999;
	
	/** 发送邮件时间间隔（分鐘） */
	public static final Integer NEBULA_SEND_EMAIL_AGAIN_TIME_SPAN = 2;
	
	/**註冊緩存key拼接字符*/
	public static final String NEBULA_MEMBER_REGISTER = "_register";
	
	/**注册邮件发送时间 key字符*/
	public static final String EMAIL_SEND_TIME_KEY = "email_send_time_key";
	
	/**激活邮件发送时间 key拼接字符*/
	public static final String SEND_ACTIVE_EMAIL_EXPIRED_TIME = "send_active_email_expired_time";
	
	/**
	 * 邮箱已激活
	 */
	public static final String EMAIL_ACTIVE_YES = "Y";
	/**
	 * 邮箱未激活
	 */
	public static final String EMAIL_ACTIVE_NO = "N";

}
