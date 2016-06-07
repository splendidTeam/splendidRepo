package com.baozun.nebula.utilities.integration.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor;
import com.baozun.nebula.utilities.common.convertor.PayParamConvertorForAlipayAdaptor;
import com.baozun.nebula.utilities.common.convertor.PayParamConvertorForUnionPayAdaptor;
import com.baozun.nebula.utilities.common.convertor.PayParamConvertorForWechatAdaptor;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayBankPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayCreditCardPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayInternationalCreditCardPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayPaymentRequest;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentAdaptorInitialFailureException;
import com.baozun.nebula.utilities.integration.payment.unionpay.UnionPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.unionpay.UnionPaymentRequest;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatPaymentRequest;

public class PaymentFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(PaymentFactory.class);

	public static final String PAY_TYPE_ALIPAY = "Alipay";

	public static final String PAY_TYPE_ALIPAY_BANK = "Alipay_Bank";

	public static final String PAY_TYPE_ALIPAY_CREDIT = "Alipay_Credit";

	public static final String PAY_TYPE_ALIPAY_CREDIT_INT = "Alipay_Credit_Int";
	
	public static final String PAY_TYPE_UNIONPAY = "unionpay";
	
	public static final String PAY_TYPE_CHINAPNR = "chinaPnR";
	
	public static final String PAY_TYPE_C = "Alipay_Credit_Int";
	
	public static final String PAY_TYPE_WECHAT ="Wechat";
	
	public static final String ALIPAYDEFAULTALIPAYCONFIG = "config/alipay.properties";

	public static final String ALIPAYINTERNATIONALCREDITCARDCONFIG = "configurations/payment/alipay/alipay_InternationalCreditCard.properties";
	
	public static final String ALIPAYADDRESS = "configurations/payment/alipay/alipayAddress.properties";
	
	public static final String CHINAPNRDEFAULTALIPAYCONFIG = "configurations/payment/chinaPnR/chinaPnR.properties";
	
	public static final String UNIONPAYDEFAULTALIPAYCONFIG = "configurations/payment/unionpay/unionpay.properties";
	
	//public static final String WECHATDEFAULTALIPAYCONFIG = "/configurations/payment/wechat/wechat.properties";

	private static PaymentFactory inst = new PaymentFactory();

	private Map<String, PaymentAdaptor> paymentAdaptorMap;

	private Map<String, String> paymentConfigMap;

	private Map<String, PayParamConvertorAdaptor> payParamCommandToMapAdaptorMap;

	private Map<String, PaymentRequest> paymentResultMap;

	public PaymentFactory() {
		initPaymentConfig();
		initPaymentAdaptorMap();
		initPayParamCommandToMapAdaptorMap();
		initPayParamPaymentResultMap();
	}

	public static PaymentFactory getInstance() {
		return inst;
	}
	
	/**
	 * 获得支付适配器
	 * @param payMentType
	 * @return
	 */
	public PaymentAdaptor getPaymentAdaptor(String payMentType) {
		if (null == payMentType || payMentType.length() < 1) {
			return null;
		}
		return paymentAdaptorMap.get(payMentType);

	}

	/**
	 * 获得转换器
	 * @param payMentType
	 * @return
	 */
	public PayParamConvertorAdaptor getPaymentCommandToMapAdaptor(
			String payMentType) {
		if (null == payMentType || payMentType.length() < 1) {
			return null;
		}
		return payParamCommandToMapAdaptorMap.get(payMentType);

	}
	
	/**
	 * 获得支付结果
	 * @param payMentType
	 * @return
	 */
	public PaymentRequest getPaymentResult(
			String payMentType) {
		if (null == payMentType || payMentType.length() < 1) {
			return null;
		}
		return paymentResultMap.get(payMentType);

	}

	/**
	 * 初始化转换器
	 */
	public void initPayParamCommandToMapAdaptorMap() {
		payParamCommandToMapAdaptorMap = new HashMap<String, PayParamConvertorAdaptor>();
		payParamCommandToMapAdaptorMap.put(PAY_TYPE_ALIPAY,
				new PayParamConvertorForAlipayAdaptor());
		payParamCommandToMapAdaptorMap.put(PAY_TYPE_ALIPAY_BANK,
				new PayParamConvertorForAlipayAdaptor());
		payParamCommandToMapAdaptorMap.put(PAY_TYPE_ALIPAY_CREDIT,
				new PayParamConvertorForAlipayAdaptor());
		payParamCommandToMapAdaptorMap.put(PAY_TYPE_ALIPAY_CREDIT_INT,
				new PayParamConvertorForAlipayAdaptor());
		payParamCommandToMapAdaptorMap.put(PAY_TYPE_UNIONPAY,
				new PayParamConvertorForUnionPayAdaptor());
 		payParamCommandToMapAdaptorMap.put(PAY_TYPE_WECHAT,
 				new PayParamConvertorForWechatAdaptor());
	}
	
	/**
	 * 支付结果列表
	 */
	public void initPayParamPaymentResultMap() {
		paymentResultMap = new HashMap<String, PaymentRequest>();
		paymentResultMap.put(PAY_TYPE_ALIPAY, new AlipayPaymentRequest());
		paymentResultMap.put(PAY_TYPE_ALIPAY_BANK,new AlipayPaymentRequest());
		paymentResultMap.put(PAY_TYPE_ALIPAY_CREDIT,new AlipayPaymentRequest());
		paymentResultMap.put(PAY_TYPE_ALIPAY_CREDIT_INT,new AlipayPaymentRequest());
		paymentResultMap.put(PAY_TYPE_UNIONPAY,new UnionPaymentRequest());
 		paymentResultMap.put(PAY_TYPE_WECHAT,new WechatPaymentRequest());
	}

	/*
	 * 初始化配置文件
	 */
	private void initPaymentConfig() {
		paymentConfigMap = new HashMap<String, String>();
		paymentConfigMap.put(PAY_TYPE_ALIPAY, ProfileConfigUtil.getProfilePath(ALIPAYDEFAULTALIPAYCONFIG));
		paymentConfigMap.put(PAY_TYPE_ALIPAY_BANK, ALIPAYDEFAULTALIPAYCONFIG);
		paymentConfigMap.put(PAY_TYPE_ALIPAY_CREDIT, ALIPAYDEFAULTALIPAYCONFIG);
		paymentConfigMap.put(PAY_TYPE_ALIPAY_CREDIT_INT,
				ALIPAYINTERNATIONALCREDITCARDCONFIG);
		paymentConfigMap.put(PAY_TYPE_UNIONPAY,
				UNIONPAYDEFAULTALIPAYCONFIG);
//		paymentConfigMap.put(PAY_TYPE_WECHAT,
//				WECHATDEFAULTALIPAYCONFIG);
	}

	/*
	 * 初始化配置参数
	 */
	public Properties initConfig(String payMentType)
			throws PaymentAdaptorInitialFailureException {

		String configFile = paymentConfigMap.get(payMentType);
		try {
			Properties configs = ProfileConfigUtil.findCommonPro(configFile);
			if (null == configs.getProperty(PaymentAdaptor.PAYMENT_GATEWAY)
					&& null == configs.getProperty("payment_gateway_front"))
				throw new PaymentAdaptorInitialFailureException("No payment gateway address defined.");
			return configs;
		} catch (Exception e) {
			logger.error("Error occurs when loading {}", configFile);
			throw new PaymentAdaptorInitialFailureException("Load Configuration failure", e);
		}

	}

	/*
	 * 初始化适配器Map
	 */
	private Map<String, PaymentAdaptor> initPaymentAdaptorMap() {
		paymentAdaptorMap = new HashMap<String, PaymentAdaptor>();
		paymentAdaptorMap.put(PAY_TYPE_ALIPAY, initAlipayPaymentAdaptor());
		paymentAdaptorMap.put(PAY_TYPE_ALIPAY_BANK,
				initAlipayBankPaymentAdaptor());
		paymentAdaptorMap.put(PAY_TYPE_ALIPAY_CREDIT,
				initAlipayCreditCardPaymentAdaptor());
		paymentAdaptorMap.put(PAY_TYPE_ALIPAY_CREDIT_INT,
				initAlipayInternationalCreditCardPaymentAdaptor());
		paymentAdaptorMap.put(PAY_TYPE_UNIONPAY,
				initUnionPaymentAdaptor());
//		paymentAdaptorMap.put(PAY_TYPE_CHINAPNR,
//				initChinaPnRPaymentAdaptor());
		paymentAdaptorMap.put(PAY_TYPE_WECHAT,
				initWechatPaymentAdaptor());
		return paymentAdaptorMap;
	}

	/*
	 * 初始化Alipay适配器
	 */
	private PaymentAdaptor initAlipayPaymentAdaptor() {
		try {
			return new AlipayPaymentAdaptor(initConfig(PAY_TYPE_ALIPAY));
		} catch (PaymentAdaptorInitialFailureException e) {
			logger.error("initAlipayPaymentAdaptor error: " + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 初始化Alipay银行卡适配器
	 */
	private PaymentAdaptor initAlipayBankPaymentAdaptor() {
		try {
			return new AlipayBankPaymentAdaptor(initConfig(PAY_TYPE_ALIPAY));
		} catch (PaymentAdaptorInitialFailureException e) {
			logger.error("initAlipayBankPaymentAdaptor error: " + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 初始化Alipay国内信用卡适配器
	 */
	private PaymentAdaptor initAlipayCreditCardPaymentAdaptor() {
		try {
			return new AlipayCreditCardPaymentAdaptor(
					initConfig(PAY_TYPE_ALIPAY));
		} catch (PaymentAdaptorInitialFailureException e) {
			logger.error("initAlipayCreditCardPaymentAdaptor error: "
					+ e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 初始化Alipay国外信用卡适配器
	 */
	private PaymentAdaptor initAlipayInternationalCreditCardPaymentAdaptor() {
		try {
			return new AlipayInternationalCreditCardPaymentAdaptor(
					initConfig(PAY_TYPE_ALIPAY_CREDIT_INT));
		} catch (PaymentAdaptorInitialFailureException e) {
			logger.error("initAlipayInternationalCreditCardPaymentAdaptor error: "
					+ e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 初始化银联适配器
	 */
	private PaymentAdaptor initUnionPaymentAdaptor() {
		try {
			return new UnionPaymentAdaptor(
					initConfig(PAY_TYPE_UNIONPAY));
		} catch (PaymentAdaptorInitialFailureException e) {
			logger.error("initUnionPaymentAdaptor error: "
					+ e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 初始化汇付天下适配器
	 */
//	private PaymentAdaptor initChinaPnRPaymentAdaptor() {
//		try {
//			return new ChinaPnRPaymentAdaptor(
//					initConfig(CHINAPNRDEFAULTALIPAYCONFIG));
//		} catch (PaymentAdaptorInitialFailureException e) {
//			logger.error("initChinaPnRPaymentAdaptor error: "
//					+ e.toString());
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/*
	 * 初始化Wechat适配器
	 */
	private PaymentAdaptor initWechatPaymentAdaptor() {
		return new WechatPaymentAdaptor();
	}

}
