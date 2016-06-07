package com.baozun.nebula.utilities.common.convertor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;

public class PayParamConvertorForAlipayAdaptor implements PayParamConvertorAdaptor{
	
	/**
	 * 此方法将COMMAND中的参数转换成支付宝能使用的map，并在此期间进行参数校验
	 * 支付宝在此处有个讨厌的设置默认选中的银行参数是有区别的<p>
	 * 国际卡中使用此参数使用:default_bank<p>
	 * 国内卡中使用此参数:defaultbank<p>
	 */
	public Map<String,String> commandConvertorToMapForCreatUrl(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException{
		Map<String,String> requestParam = new HashMap<String,String>();
		if(Validator.isNotNullOrEmpty(payParamCommand.getOrderNo())){//铁定不能为空的
			requestParam.put("out_trade_no", payParamCommand.getOrderNo());
		}else{
			throw new PaymentParamErrorException("out_trade_no can't be null/empty!");
		}
		
		if(Validator.isNotNullOrEmpty(payParamCommand.getDefault_bank())){
			if(payParamCommand.isInternationalCard()){
				requestParam.put("default_bank", payParamCommand.getDefault_bank());
			}else{
				requestParam.put("defaultbank", payParamCommand.getDefault_bank());
			}
		}

		BigDecimal minPay = new BigDecimal(0.01f);
		BigDecimal maxPay = new BigDecimal(100000000);
		
		if (Validator.isNotNullOrEmpty(payParamCommand.getTotalFee())) {
			requestParam.put("total_fee", String.valueOf(payParamCommand.getTotalFee()));// 交易金额(可为空)
		} else if (payParamCommand.getTotalFee().compareTo(minPay) == -1
				|| payParamCommand.getTotalFee().compareTo(maxPay) == 1) {
			throw new PaymentParamErrorException("total_fee:" + payParamCommand.getTotalFee()
					+ " can't < " + minPay + " or > " + maxPay);
		} else {
			throw new PaymentParamErrorException(
					"total_fee can't be null/empty!");
		}
		
		//可为空，但项目不允许传空的超时时间
		if(Validator.isNotNullOrEmpty(payParamCommand.getIt_b_pay())){
			requestParam.put("it_b_pay", payParamCommand.getIt_b_pay());// 超时时间(可为空)
		}else {
			throw new PaymentParamErrorException(
					"it_b_pay can't be null/empty!");
		}
		//支付宝扫码支付方式
		if(Validator.isNotNullOrEmpty(payParamCommand.getQrPayMode())){
			requestParam.put("qr_pay_mode", payParamCommand.getQrPayMode());
		}
		
		if (Validator.isNotNullOrEmpty(payParamCommand.getBody())) {
			requestParam.put("body", payParamCommand.getBody());// 商品描述(可为空)
		}
		return requestParam;
	}

	/**
	 * 支付宝取消订单可以不填订单号，但支付宝订单流水必须存在
	 */
	@Override
	public Map<String, String> commandConvertorToMapForCaneclOrder(
			BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException {
		Map<String,String> requestParam = new HashMap<String,String>();
		if(Validator.isNullOrEmpty(payParamCommand.getOrderNo())){
			throw new PaymentParamErrorException(
					"orderNo can't be null/empty!");
		}
		
		if(Validator.isNotNullOrEmpty(payParamCommand.getTrade_no())){
			requestParam.put("trade_no", payParamCommand.getTrade_no());
		}

		if(Validator.isNotNullOrEmpty(payParamCommand.getOrderNo())){
			requestParam.put("out_order_no", payParamCommand.getOrderNo());
		}
		if(Validator.isNotNullOrEmpty(payParamCommand.getTrade_role())){
			requestParam.put("trade_role", payParamCommand.getTrade_role());
		}else{
			throw new PaymentParamErrorException(
					"trade_role can't be null/empty!");
		}
		
		return requestParam;
	}
	
	
}
