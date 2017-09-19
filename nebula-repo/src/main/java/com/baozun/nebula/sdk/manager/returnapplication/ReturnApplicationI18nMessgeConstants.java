package com.baozun.nebula.sdk.manager.returnapplication;

/**
 * 退换货中使用到的I8n key
 * @author zl.shi
 * @since  2017.8.24
 *
 */
public class ReturnApplicationI18nMessgeConstants {
	
	/*********************** exception message *****************************/
	/** 审核备注不能为空 */
	public static final String EXCEPTION_REVIEW_NOTE_CANNOT_EMPTY 		= "business_exception_200010";
	
	/** 退货地址为空 */
	public static final String EXCEPTION_RETURN_ADDRESS_IS_NULL   		= "business_exception_200011";
	
	/** 对应的申请单已审核，请刷新页面 */
	public static final String EXCEPTION_FORM_IS_AUDIT_FLUSH_PAGE     	= "business_exception_200012";
	
	/** 对应的申请单不存在 */
	public static final String EXCEPTION_FORM_IS_NOT_FOUND   			= "business_exception_200014";
	
	 /** 物流状态异常！*/
	public static final String EXCEPTION_LOGISTICS_STATUS 			    = "business_exception_200015";
	
	
	/*********************** normal message *****************************/
	/** 退货 */
	public static final String MESSAGE_TYPE_SALES_RETURN  			= "order.return.application.type.salesreturn";
	
	/** 换货 */
	public static final String MESSAGE_TYPE_EXCHANGE 				= "order.return.application.type.exchange";
	
	/** 待审核 */
	public static final String MESSAGE_STATUS_CHECK_PENDING			= "order.return.application.status.check_pending";
	
	/** 退回中 */
	public static final String MESSAGE_STATUS_IN_THE_BACK			= "order.return.application.status.in_the_back";
	
	/** 拒绝退换货 */
	public static final String MESSAGE_STATUS_REFUSED  				= "order.return.application.status.refused";
	
	/** 同意退换货 */
	public static final String MESSAGE_STATUS_AGREE 				= "order.return.application.status.agree";
	
	/** 已完成 */
	public static final String MESSAGE_STATUS_COMPLETED				= "order.return.application.status.completed";

}
