package com.baozun.nebula.web.controller.order.resolver;

/**
 * 购物车操作的结果
 * 
 * @author jumbo
 *
 */
public enum ReturnApplicationResult {

	/**
	 * 成功
	 */
	SUCCESS,

	/**
	 * 订单物流状态异常，订单物流状态为完成（15）时才可申请退款
	 */
	SALES_ORDER_STATUS_EXCEPTION,
	
	/**
	 * 申请的退货数量超出可退数量
	 */
	RETURN_COUNT_OUT_OF_RANGE,
	
	/**
	 * 不支持退货
	 */
	UNSUPPORT_RETURN,
	
	/**
	 * 还有未处理的退货
	 */
	UNPROCESS_RETURN,
	
	/**
	 * 描述超出字数限制
	 */
	DESCRIBE_TO_LONG,
	
	/**
	 * 退货超出时间限制
	 */
	OUT_OF_DATE,

}
