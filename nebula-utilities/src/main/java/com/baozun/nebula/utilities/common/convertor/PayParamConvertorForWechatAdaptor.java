package com.baozun.nebula.utilities.common.convertor;

import java.util.HashMap;
import java.util.Map;

import com.baozun.nebula.utilities.common.WechatUtil;
import com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatConfig;
import com.feilong.core.Validator;

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
		Map<String,String> addition = new HashMap<String, String>();
		addition.put("appid", WechatConfig.APP_ID);
		addition.put("mch_id", WechatConfig.PARTNER_ID);
		addition.put("nonce_str", WechatUtil.generateRandomString());
		
		if(Validator.isNotNullOrEmpty(payParamCommand.getRequestParams().get("code"))){
			addition.put("out_trade_no", String.valueOf(payParamCommand.getRequestParams().get("code")));
 		}
		return addition;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor#commandConvertorToMapForMobileCreatUrl(com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor)
	 */
	@Override
	public Map<String, String> commandConvertorToMapForMobileCreatUrl(
			BasePayParamCommandAdaptor payParamCommand)
			throws PaymentParamErrorException {
		return new HashMap<String, String>();
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor#commandConvertorToMapForOrderInfo(com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor)
	 */
	@Override
	public Map<String, String> commandConvertorToMapForOrderInfo(
			BasePayParamCommandAdaptor payParamCommand) {
		Map<String, String> addition = new HashMap<String, String>();
		addition.put("appid", WechatConfig.APP_ID);
		addition.put("mch_id", WechatConfig.PARTNER_ID);
		addition.put("nonce_str", WechatUtil.generateRandomString());

		if(Validator.isNotNullOrEmpty(payParamCommand.getRequestParams().get("code"))){
			addition.put("out_trade_no", String.valueOf(payParamCommand.getRequestParams().get("code")));
 		}
		
		if(Validator.isNotNullOrEmpty(payParamCommand.getOrderNo())){
			addition.put("transaction_id", payParamCommand.getOrderNo());
 		}
		return addition;
	}

}
