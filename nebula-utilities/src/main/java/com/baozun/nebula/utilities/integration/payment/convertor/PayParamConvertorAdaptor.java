package com.baozun.nebula.utilities.integration.payment.convertor;

import java.util.Map;

import com.baozun.nebula.utilities.integration.payment.adaptor.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;

public interface PayParamConvertorAdaptor {

	/**
	 * 用于将command转换成支付平台可用map，用于产生支付链接
	 * @param payParamCommand
	 * @return
	 * @throws PaymentParamErrorException
	 */
	public Map<String,String> commandConvertorToMapForCreatUrl(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException;
	
	/**
	 * 
	 * @Description 用于将command转换成支付平台可用map，用于产生mobile端支付链接
	 * @param payParamCommand
	 * @return
	 * @throws PaymentParamErrorException
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-17
	 */
	public Map<String,String> commandConvertorToMapForMobileCreatUrl(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException;
	
	/**
	 * 用于将command转换成支付平台可用map，用于产生取消订单
	 * @param payParamCommand
	 * @return
	 * @throws PaymentParamErrorException
	 */
	public Map<String,String> commandConvertorToMapForCaneclOrder(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException;
	
	/**
	 * 
	 * @Description 用于将command转换成支付平台可用map，用于产生查询订单支付状态
	 * @param payParamCommand
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-17
	 */
	public Map<String,String> commandConvertorToMapForOrderInfo(BasePayParamCommandAdaptor payParamCommand);
	
	/**
	 * 
	 */
	public Map<String,String> extendCommandConvertorMap(Map<String,String> params,Map<String, Object> addition);
}
