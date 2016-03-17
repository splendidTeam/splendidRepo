package com.baozun.nebula.utilities.common.convertor;

import java.util.HashMap;
import java.util.Map;

import com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;

public class PayParamConvertorForWechatAdaptor implements
		PayParamConvertorAdaptor {

	@Override
	public Map<String, String> commandConvertorToMapForCreatUrl(
			BasePayParamCommandAdaptor payParamCommand)
			throws PaymentParamErrorException {
		return new HashMap<String, String>();
	}

	@Override
	public Map<String, String> commandConvertorToMapForCaneclOrder(
			BasePayParamCommandAdaptor payParamCommand)
			throws PaymentParamErrorException {
		return new HashMap<String, String>();
	}

}
