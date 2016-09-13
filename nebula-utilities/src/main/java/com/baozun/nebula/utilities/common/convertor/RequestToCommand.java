package com.baozun.nebula.utilities.common.convertor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONObject;

import com.baozun.nebula.utilities.common.ReflectionUtil;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnForMobileCommand;

public class RequestToCommand {

	private static final Logger logger = LoggerFactory.getLogger(RequestToCommand.class);

	/**
	 * 支付宝同步数组
	 */
	public final static Map<String, String> getAlipaySyn() {
		Map<String, String> AlipayMap = new HashMap<String, String>();
		AlipayMap.put("is_success", "setReturnStatus");
		AlipayMap.put("sign_type", "setSingType");
		AlipayMap.put("sign", "setSign");
		AlipayMap.put("out_trade_no", "setOrderNo");
		AlipayMap.put("subject", "setSubject");
		AlipayMap.put("payment_type", "setPaymentType");
		AlipayMap.put("exterface", "setExterface");
		AlipayMap.put("trade_no", "setTradeNo");
		AlipayMap.put("trade_status", "setTradeStatus");
		AlipayMap.put("notify_id", "setNotifyId");
		AlipayMap.put("notify_time", "setNotiftTime");
		AlipayMap.put("notify_type", "setNotifyType");
		AlipayMap.put("seller_email", "setSeller");
		AlipayMap.put("buyer_email", "setBuyer");
		AlipayMap.put("seller_id", "setSellerId");
		AlipayMap.put("buyer_id", "setBuyerId");
		AlipayMap.put("total_fee", "setTotalFee");
		AlipayMap.put("body", "setBody");
		AlipayMap.put("extra_common_param", "setExtraCommonParam");
		AlipayMap.put("agent_user_id", "setAgentUserId");
		return AlipayMap;
	}

	/**
	 * 支付宝异步数组
	 */
	public final static Map<String, String> getAlipayAsy() {
		Map<String, String> AlipayMap = new HashMap<String, String>();
		AlipayMap.put("sign_type", "setSingType");
		AlipayMap.put("sign", "setSign");
		AlipayMap.put("out_trade_no", "setOrderNo");
		AlipayMap.put("subject", "setSubject");
		AlipayMap.put("payment_type", "setPaymentType");
		AlipayMap.put("trade_no", "setTradeNo");
		AlipayMap.put("trade_status", "setTradeStatus");
		AlipayMap.put("notify_id", "setNotifyId");
		AlipayMap.put("notify_time", "setNotiftTime");
		AlipayMap.put("notify_type", "setNotifyType");
		AlipayMap.put("seller_email", "setSeller");
		AlipayMap.put("buyer_email", "setBuyer");
		AlipayMap.put("seller_id", "setSellerId");
		AlipayMap.put("buyer_id", "setBuyerId");
		AlipayMap.put("total_fee", "setTotalFee");
		AlipayMap.put("body", "setBody");
		AlipayMap.put("extra_common_param", "setExtraCommonParam");
		AlipayMap.put("gmt_create", "setGmtCreate");
		AlipayMap.put("gmt_payment", "setGmtPayment");
		AlipayMap.put("gmt_close", "setGmtClose");
		AlipayMap.put("refund_status", "setRefundStatus");
		AlipayMap.put("gmt_refund", "setGmtRefund");
		AlipayMap.put("price", "setPrice");
		AlipayMap.put("quantity", "setQuantity");
		AlipayMap.put("discount", "setDiscount");
		AlipayMap.put("is_total_fee_adjust", "setIsTotalFeeAdjust");
		AlipayMap.put("use_coupon", "setUseCoupon");
		AlipayMap.put("out_channel_type", "setOutChannelType");
		AlipayMap.put("out_channel_amount", "setOutChannelAmount");
		AlipayMap.put("out_channel_inst", "setOutChannelInst");
		AlipayMap.put("business_scene", "setBusinessScene");
		return AlipayMap;
	}

	/**
	 * 银联数组(同步与异步返回值相同)
	 */
	public final static Map<String, String> getUnion() {
		Map<String, String> UnionMap = new HashMap<String, String>();
		UnionMap.put("version", "setVersion");
		UnionMap.put("charset", "setCharset");
		UnionMap.put("signMethod", "setSingType");
		UnionMap.put("signature", "setSign");
		UnionMap.put("transType", "setPaymentType");
		UnionMap.put("respCode", "setTradeStatus");
		UnionMap.put("respMsg", "setRespMsg");
		UnionMap.put("merAbbr", "setMerAbbr");
		UnionMap.put("merId", "setMerId");
		UnionMap.put("orderNumber", "setOrderNo");
		UnionMap.put("traceNumber", "setTraceNumber");
		UnionMap.put("traceTime", "setTraceTime");
		UnionMap.put("qid", "setTradeNo");
		UnionMap.put("orderAmount", "setTotalFee");
		UnionMap.put("orderCurrency", "setOrderCurrency");
		UnionMap.put("respTime", "setGmtClose");
		UnionMap.put("settleAmount", "setSettleAmount");
		UnionMap.put("settleCurrency", "setSettleCurrency");
		UnionMap.put("settleDate", "setSettleDate");
		UnionMap.put("exchangeRate", "setExchangeRate");
		UnionMap.put("exchangeDate", "setExchangeDate");
		UnionMap.put("cupReserved", "setCupReserved");
		return UnionMap;
	}

	public PaymentServiceReturnCommand alipaySynRequestToCommand(HttpServletRequest request) {
		PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
		Map<String, String[]> map = request.getParameterMap();
		Map<String, String> result = new HashMap<String, String>();
		for (Object objectkey : map.keySet()) {
			result.put(objectkey.toString(),
					Arrays.toString((String[]) map.get(objectkey)).replace("[", "").replace("]", ""));
		}

		setParamValue(getAlipaySyn(), result, paymentServiceReturnCommand);
		paymentServiceReturnCommand.setAllMessage(JSONObject.fromObject(result).toString());
		return paymentServiceReturnCommand;
	}

	public PaymentServiceReturnForMobileCommand alipaySynRequestToCommandForMobile(HttpServletRequest request,
			Map<String, String> otherValue) {
		PaymentServiceReturnForMobileCommand paymentServiceReturnForMobileCommand = new PaymentServiceReturnForMobileCommand();
		Map<String, String[]> map = request.getParameterMap();
		Map<String, String> result = new HashMap<String, String>();
		for (Object objectkey : map.keySet()) {
			result.put(objectkey.toString(),
					Arrays.toString((String[]) map.get(objectkey)).replace("[", "").replace("]", ""));
		}
		if (null != otherValue && otherValue.size() > 0) {
			result.putAll(otherValue);
		}
		setParamValue(getAlipaySyn(), result, paymentServiceReturnForMobileCommand);
		paymentServiceReturnForMobileCommand.setAllMessage(JSONObject.fromObject(result).toString());
		return paymentServiceReturnForMobileCommand;
	}

	public PaymentServiceReturnCommand alipayAsyRequestToCommand(HttpServletRequest request) {
		PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
		Map<String, String[]> map = request.getParameterMap();
		Map<String, String> result = new HashMap<String, String>();
		for (Object objectkey : map.keySet()) {
			result.put(objectkey.toString(),
					Arrays.toString((String[]) map.get(objectkey)).replace("[", "").replace("]", ""));
		}
		setParamValue(getAlipayAsy(), result, paymentServiceReturnCommand);
		paymentServiceReturnCommand.setAllMessage(JSONObject.fromObject(result).toString());
		return paymentServiceReturnCommand;
	}

	public PaymentServiceReturnCommand unionRequestToCommand(HttpServletRequest request) {
		PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
		Map<String, String[]> map = request.getParameterMap();
		Map<String, String> result = new HashMap<String, String>();
		for (Object objectkey : map.keySet()) {
			result.put(objectkey.toString(),
					Arrays.toString((String[]) map.get(objectkey)).replace("[", "").replace("]", ""));
		}
		setParamValue(getUnion(), result, paymentServiceReturnCommand);
		paymentServiceReturnCommand.setAllMessage(JSONObject.fromObject(result).toString());
		return paymentServiceReturnCommand;
	}

	private void setParamValue(Map<String, String> keyMap, Map<String, String> valueMap,
			PaymentServiceReturnCommand paymentServiceReturnCommand) {
		for (String key : valueMap.keySet()) {
			try {
				String mapKey = keyMap.get(key);
				if (null != mapKey) {
					Method m = ReflectionUtil.getMethod(paymentServiceReturnCommand.getClass(), mapKey, String.class);
					if (m != null)
						ReflectionUtil.invoke(paymentServiceReturnCommand, m, valueMap.get(key));
					else
						ReflectionUtil.setFieldValue(paymentServiceReturnCommand, key, valueMap.get(key));
				}
			} catch (Exception e) {
				logger.error("Error when setting Param " + key + "'s value with " + valueMap.get(key), e);
			}
		}
	}

}
