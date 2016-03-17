package com.baozun.nebula.utilities.common.convertor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.common.condition.RequestCurrencyUnionPay;
import com.baozun.nebula.utilities.integration.payment.UnionPayBase;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;

public class PayParamConvertorForUnionPayAdaptor implements PayParamConvertorAdaptor{
	
	/**
	 * 此方法将COMMAND中的参数转换成银联能使用的map，并在此期间进行参数校验
	 */
	public Map<String,String> commandConvertorToMapForCreatUrl(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException{
		Map<String,String> requestParam = new HashMap<String,String>();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		java.util.Date currentTime = new java.util.Date();// 得到当前系统时间
		String pDate = formatter.format(currentTime);

		// 固定填写
		requestParam.put("version", UnionPayBase.version);// M

		// 默认取值：UTF-8
		requestParam.put("encoding", UnionPayBase.encoding);// M

		requestParam.put("signMethod", "01");// M

		// 取值：01
		requestParam.put("txnType", "01");// M

		// 01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付）03：分期付款
		requestParam.put("txnSubType", "01");// M

		//产品类型
		requestParam.put("bizType", "000000");// M

		requestParam.put("channelType", "07");// M

		Properties properties = ProfileConfigUtil.findPro("config/payment/unionpay/acp_sdk.properties");
		// 前台返回商户结果时使用，前台类交易需上送
		requestParam.put("frontUrl", properties.getProperty("frontUrl").replace("{subOrdinate}", payParamCommand.getOrderNo()));// C

		// 后台返回商户结果时使用，如上送，则发送商户后台交易结果通知
		requestParam.put("backUrl", properties.getProperty("backUrl"));// M

		// 0：普通商户直连接入2：平台类商户接入
		requestParam.put("accessType", "0");// M

		// 商户端生成
		requestParam.put("orderId", payParamCommand.getOrderNo());// M

		// 商户发送交易时间
		requestParam.put("txnTime", pDate);// M
		//订单接收超时时间 当距离交易发送时间超过该时间时，银联全渠道系统不再为该笔交易提供支付服务 （此参数传任何值报错400 可能需要开通该服务）
		//requestParam.put("orderTimeout","");
		// 后台类交易且卡号上送；跨行收单且收单机构收集银行卡信息时上送01：银行卡02：存折03：C卡默认取值：01取值“03”表示以IC终端发起的IC卡交易，IC作为普通银行卡进行支付时，此域填写为“01”
		requestParam.put("accType", "01");// C
		// 交易单位为分
		requestParam.put("txnAmt", String.valueOf(payParamCommand.getTotalFee().multiply(new BigDecimal(100)).intValue()));// M

		// 默认为156交易 参考公参
		requestParam.put("currencyCode", "156");// M
		
		requestParam.put("merId", properties.getProperty("merchantId"));
		
		
		return requestParam;
	}

	/**
	 * 
	 */
	@Override
	public Map<String, String> commandConvertorToMapForCaneclOrder(
			BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException {
		return new HashMap<String,String>();
	}
	
	
}
