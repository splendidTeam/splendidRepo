package com.baozun.nebula.utilities.common.convertor;

import java.util.Map;

import com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor;
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
	 * 用于将command转换成支付平台可用map，用于产生取消订单
	 * @param payParamCommand
	 * @return
	 * @throws PaymentParamErrorException
	 */
	public Map<String,String> commandConvertorToMapForCaneclOrder(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException;
}
