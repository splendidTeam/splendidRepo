/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.utilities.integration.payment.alipay;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ReflectionUtil;
import com.baozun.nebula.utilities.common.URIUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentParam;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentException;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentInitialFailureException;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;
import com.baozun.nebula.utilities.integration.payment.exception.RequestTypeNotSupporttedException;
import com.baozun.nebula.utilities.integration.payment.param.PaymentRequestParam;
import com.baozun.nebula.utilities.common.Md5Encrypt;
import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.common.convertor.MapAndStringConvertor;

public class AlipayPaymentRequest implements PaymentRequest, Serializable {

	private static final long serialVersionUID = -7441188005486354584L;

	private static final Logger logger = LoggerFactory
			.getLogger(AlipayPaymentRequest.class);

	private Map<String, String> paymentParameters;

	/**
	 * 支付网关地址
	 */
	private String payment_gateway;

	/**
	 * 账户相关加密文件
	 */
	private String key;
	
	/**
	 * 防钓鱼时间戳服务名称
	 */
	private String anti_phishing_key_service_name;

	/**
	 * 请求类型
	 */
	private String requestType;
	
	/**
	 * 同步回传地址(pc)
	 */
	private String return_url;
	
	/**
	 * 同步回传地址(mobile)
	 */
	private String call_back_url;
	
	
	/**
	 * 用户付款中途退出返回商户的地址(mobile)
	 */
	private String merchant_url;
	
	/**
	 * 异步回传地址
	 */
	private String notify_url;
	
	/**
	 * 异步回传出错地址
	 */
	private String error_notify_url;

	/**
	 * 接口名称
	 */
	@PaymentParam(mandatory = true)
	private String service;
	
	/**
	 * 商品名称
	 */
	@PaymentParam(mandatory = true)
	private String subject;

	/**
	 * 合作者身份
	 */
	@PaymentParam(mandatory = true, sampleValue = "2088101011913539")
	private String partner;
	

	/**
	 * 参数编码字符集合
	 */
	@PaymentParam(mandatory = true, sampleValue = "utf-8")
	private String _input_charset;

	/**
	 * 签名方式，默认为MD5，因为其他方式需要交换密钥，暂不实现
	 */
	@PaymentParam(mandatory = true)
	private static final String sign_type = "MD5";

	@PaymentParam(mandatory = true, sampleValue = "7d314d22efba4f336fb187697793b9d2")
	private String sign;


	/**
	 * 支付类型
	 */
	@PaymentParam(mandatory = true, sampleValue = "1")
	private String payment_type;

	/**
	 * 卖家支付宝账号
	 */
	@PaymentParam(sampleValue = "alipay-test01@alipay.com")
	private String seller_email;

	/**
	 * 买家支付宝账号
	 */
	@PaymentParam(sampleValue = "tstable01@alipay.com")
	private String buyer_email;

	/**
	 * 卖家支付宝账户号
	 */
	@PaymentParam(sampleValue = "2088002007018966")
	private String seller_id;

	/**
	 * 买家支付宝账户号
	 */
	@PaymentParam(sampleValue = "2088002007018955")
	private String buyer_id;

	/**
	 * 卖家别名支付宝账号
	 */
	@PaymentParam(sampleValue = "tstable02@alipay.com")
	private String seller_account_name;

	/**
	 * 买家别名支付宝账号
	 */
	@PaymentParam(sampleValue = "tstable03@alipay.com")
	private String buyer_account_name;

	/**
	 * 商品单价
	 */
	@PaymentParam
	private BigDecimal price;

	/**
	 * 交易金额
	 */
	@PaymentParam
	private BigDecimal total_fee;

	/**
	 * 购买数量
	 */
	@PaymentParam
	private Integer quantity;

	/**
	 * 默认支付方式
	 */
	@PaymentParam(sampleValue = "directPay")
	private String paymethod;

	/**
	 * 支付渠道
	 */
	@Deprecated
	@PaymentParam
	private String enable_paymethod;

	/**
	 * 网银支付时是否做CTU校验
	 */
	@PaymentParam(sampleValue = "Y")
	private String need_ctu_check;

	/**
	 * 提成类型
	 */
	@PaymentParam(sampleValue = "10")
	private String royalty_type;

	/**
	 * 分润账号集
	 */
	@PaymentParam
	private String royalty_parameters;

	/**
	 * 防钓鱼时间戳
	 */
	@PaymentParam(sampleValue = "587FE3D2858E6B01E30104656E7805E2")
	private String anti_phishing_key;

	/**
	 * 客户端IP
	 */
	@PaymentParam(sampleValue = "128.214.222.111")
	private String exter_invoke_ip;

	/**
	 * 公用回传参数
	 */
	@PaymentParam(sampleValue = "你好,这是测试商户的广告。")
	private String extra_common_param;

	/**
	 * 公用业务扩展参数
	 */
	@PaymentParam(sampleValue = "pnr^MFGXDW|start_ticket_no^123|end_ticket _no^234|b2b_login_name^abc")
	private String extend_param;

	/**
	 * 超时时间
	 */
	@PaymentParam(sampleValue = "1h")
	private String it_b_pay;
	
	/**
	 *  支付宝扫码支付方式
	 * 	0：订单码-简约前置模式，对应iframe宽度不能小于600px，高度不能小于300px；
	 *	1：订单码-前置模式，对应iframe宽度不能小于300px，高度不能小于600px；
	 *	3：订单码-迷你前置模式，对应iframe宽度不能小于75px，高度不能小于75px。
	 *	跳转模式下，用户的扫码界面是由支付宝生成的，不在商户的域名下。
	 *	2：订单码-跳转模式
	 *
	 */
	@PaymentParam(sampleValue = "1")
	private String qr_pay_mode;

	/**
	 * 商户申请的产品类型
	 */
	@PaymentParam(sampleValue = "CHANNEL_FAST_PAY")
	private String product_type;

	/**
	 * 快捷登录授权令牌
	 */
	@PaymentParam(sampleValue = "201103290c9f9f2c03db4267a4c8e1bfe3adfd52")
	private String token;

	/**
	 * 商户回传业务参数
	 */
	@PaymentParam
	private String item_orders_info;

	/**
	 * 商户买家签约号
	 */
	@PaymentParam(sampleValue = "ZHANGSAN")
	private String sign_id_ext;

	/**
	 * 商户买家签约名
	 */
	@PaymentParam(sampleValue = "张三")
	private String sign_name_ext;
	
	/**
	 * 验证支付宝接口
	 */
	@PaymentParam(mandatory = true)
	private String notify_verify;
	
	/**
	 * 取消订单接口
	 */
	@PaymentParam(mandatory = true)
	private String close_trade;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() throws PaymentException {
		if (anti_phishing_key_service_name == null) {
			logger.info("No anti_phishing_key_service defined, so this parameter will be ignored.");
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(payment_gateway);
		sb.append("?");
		sb.append("service=" + anti_phishing_key_service_name);
		sb.append("&");
		sb.append("partner=" + this.partner);

		URL url = null;
		Document doc = null;
		try {
			url = new URL(sb.toString());
			SAXReader reader = new SAXReader();
			doc = reader.read(url.openStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (url == null || doc == null)
			throw new PaymentInitialFailureException(
					"Read service response error.");

		if (logger.isDebugEnabled()) {
			logger.debug("Invoke anti_phishing_key_service with url: {}",
					url.toString());
			logger.debug("==================== Result ====================");
			logger.debug(doc.toString());
		}

		List<Node> resultNodes = doc.selectNodes("/alipay/is_success");
		if (resultNodes == null || resultNodes.size() == 0) {
			logger.error("Get anti_phishing_key_service failure.");
			throw new PaymentInitialFailureException(
					"Get anti_phishing_key_service failure.");
		}
		if (resultNodes.iterator().next().getText().equalsIgnoreCase("T")) {
			// true
			StringBuffer result = new StringBuffer();
			List<Node> keys = doc.selectNodes("//response/timestamp/*");
			for (Node key : keys) {
				result.append(key.getText());
			}
			logger.info("anti_phishing_key is {}", result.toString());
			this.anti_phishing_key = result.toString();
		} else {
			// false
			logger.error("Get anti_phishing_key_service failure with return message: "
					+ doc.toString());
			throw new PaymentInitialFailureException(
					"Get anti_phishing_key_service failure.");
		}
	}

	@Override
	public boolean supportRequestType(String requestType) {
		if (RequestParam.HTTP_TYPE_GET.equalsIgnoreCase(requestType)
				|| RequestParam.HTTP_TYPE_POST.equalsIgnoreCase(requestType))
			return true;
		return false;
	}

	@Override
	public void setRequestType(String requestType)
			throws RequestTypeNotSupporttedException {
		if (supportRequestType(requestType))
			this.requestType = requestType;
		else
			throw new RequestTypeNotSupporttedException(
					(requestType == null ? "null" : requestType)
							+ " is not one supported request type.");
	}

	@Override
	public String getRequestURL() {
		return URIUtil.getEncodedUrl(this.getPaymentParameters(),
				_input_charset);
	}

	@Override
	public Map<String, String> getPaymentParameters() {
		// TODO Auto-generated method stub
		return paymentParameters;
	}

	/**
	 * 设置PC端支付初始化参数并检查
	 * 
	 * @param prop
	 * @param orderNo
	 * @param amt
	 * @param addition
	 * @throws PaymentException
	 */
	public void initPaymentRequestParams(Properties prop, Map<String, String> addition, String payMethod,String returnUrl,String notifyUrl,String errorNotifyUrl)
			throws PaymentException {
		this.payment_gateway = prop.getProperty(PaymentAdaptor.PAYMENT_GATEWAY);
		this.anti_phishing_key_service_name = prop
				.getProperty("anti_phishing_key_service_name");
		this.paymethod = payMethod;
		this.return_url = returnUrl;
		this.notify_url = notifyUrl;
		this.error_notify_url = errorNotifyUrl;
		loadRequestParams(prop);
		prepare();// 防钓鱼网站检查
		setParamMap(addition);
		String toBeSignedString = MapAndStringConvertor.getToBeSignedString(addition);
		String sign = Md5Encrypt.md5(toBeSignedString + key, _input_charset);
		addition.put("sign", sign);
		addition.put("sign_type", sign_type);
		addition.put("action", payment_gateway);
		this.paymentParameters = addition;
	}
	

	/**
	 *  只设置MD5加密的字段(sign与sign_type不在加密范围内)
	 *  该设置只转换配置文件中的参数
	 *  PC端
	 * @param addition
	 * @throws PaymentParamErrorException
	 */
	public void setParamMap(Map<String, String> addition)
			throws PaymentParamErrorException {
		if (Validator.isNotNullOrEmpty(service)) {
			addition.put("service", service);// 服务名称(不可为空)
		} else {
			throw new PaymentParamErrorException("service can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(payment_type)) {
			addition.put("payment_type", payment_type);// 支付类型(不可为空)
		} else {
			throw new PaymentParamErrorException("service can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(_input_charset)) {
			addition.put("_input_charset", _input_charset);// 字符串编码(不可为空)
		} else {
			throw new PaymentParamErrorException(
					"_input_charset can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(subject)) {
			addition.put("subject", subject);// 商品名称(不可为空)
		} else {
			throw new PaymentParamErrorException("subject can't be null/empty!");
		}
		if(Validator.isNotNullOrEmpty(return_url)){//支付宝参数中虽然可以为空，但业务需求允许为空
			addition.put("return_url", return_url);
		}else{
			throw new PaymentParamErrorException("return_url can't be null/empty!");
		}
		if(Validator.isNotNullOrEmpty(notify_url)){
			addition.put("notify_url", notify_url);//支付宝参数中虽然可以为空，但业务需求允许为空
		}else{
			throw new PaymentParamErrorException("notify_url can't be null/empty!");
		}
		if(Validator.isNotNullOrEmpty(error_notify_url)){
			addition.put("error_notify_url", error_notify_url);
		}
		if (Validator.isNotNullOrEmpty(partner)) {
			addition.put("partner", partner);// 合作方(不可为空)
		} else {
			throw new PaymentParamErrorException("partner can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(seller_email)) {
			addition.put("seller_email", seller_email);// 卖家支付宝账号(可为空)
		}
		if (Validator.isNotNullOrEmpty(buyer_email)) {
			addition.put("buyer_email", buyer_email);// 买家支付宝账号(可为空)
		}
		if (Validator.isNotNullOrEmpty(seller_id)) {
			addition.put("seller_id", seller_id);// 卖家支付宝账户号(可为空)
		}
		if (Validator.isNotNullOrEmpty(buyer_id)) {
			addition.put("buyer_id", buyer_id);// 买家支付宝账户号(可为空)
		}
		if (Validator.isNotNullOrEmpty(seller_account_name)) {
			addition.put("seller_account_name", seller_account_name);// 卖家别名支付宝账号(可为空)
		}
		if (Validator.isNotNullOrEmpty(buyer_account_name)) {
			addition.put("buyer_account_name", buyer_account_name);// 买家别名支付宝账号(可为空)
		}
		if (Validator.isNotNullOrEmpty(enable_paymethod)) {
			addition.put("enable_paymethod", enable_paymethod);// 支付渠道(可为空)(例：directPay^bankPay^cartoon^cash)
		}
		if (Validator.isNotNullOrEmpty(paymethod)) {
			addition.put("paymethod", paymethod);// 支付方式
		}
		if (Validator.isNotNullOrEmpty(need_ctu_check)) {
			addition.put("need_ctu_check", need_ctu_check);// 网银支付时是否做CTU校验(可为空)
		}
		if (Validator.isNotNullOrEmpty(royalty_type)) {
			addition.put("royalty_type", royalty_type);// 提成类型(可为空)一旦传递了这个参数则值不能为空
		}
		if (Validator.isNotNullOrEmpty(royalty_parameters)) {
			addition.put("royalty_parameters", royalty_parameters);// 分润账号集(可为空)
		}
		if (Validator.isNotNullOrEmpty(anti_phishing_key)) {
			addition.put("anti_phishing_key", anti_phishing_key);// 防钓鱼时间戳(可为空)
		}
		if (Validator.isNotNullOrEmpty(exter_invoke_ip)) {
			addition.put("exter_invoke_ip", exter_invoke_ip);// 客户端IP(可为空)
		}
		if (Validator.isNotNullOrEmpty(extra_common_param)) {
			addition.put("extra_common_param", extra_common_param);// 公用回传参数(可为空)
		}
		if (Validator.isNotNullOrEmpty(extend_param)) {
			addition.put("extend_param", extend_param);// 公用业务扩展参数(可为空)
		}
		if (Validator.isNotNullOrEmpty(it_b_pay)) {
			addition.put("it_b_pay", it_b_pay);// 超时时间(可为空)参数不接受小数点
		}
		if (Validator.isNotNullOrEmpty(qr_pay_mode)) {
			addition.put("qr_pay_mode", qr_pay_mode);// 支付宝扫描支付方式
		}	
		if (Validator.isNotNullOrEmpty(product_type)) {
			addition.put("product_type", product_type);// 商户申请的产品类型(可为空)
		}
		if (Validator.isNotNullOrEmpty(token)) {
			addition.put("token", token);// 快捷登录授权令牌(可为空)
		}
		if (Validator.isNotNullOrEmpty(item_orders_info)) {
			addition.put("item_orders_info", item_orders_info);// 商户回传业务参数(可为空)
		}
		if (Validator.isNotNullOrEmpty(sign_id_ext)) {
			addition.put("sign_id_ext", sign_id_ext);// 商户买家签约号(可为空)
		}
		if (Validator.isNotNullOrEmpty(sign_id_ext)) {
			if (Validator.isNotNullOrEmpty(sign_name_ext)) {
				addition.put("sign_name_ext", sign_name_ext);// 商户买家签约名(可为空)注：如果sign_id_ext不为空则该参数不为空
			} else {
				throw new PaymentParamErrorException(
						"sign_id_ext has value!sign_name_ext can't be null/empty!");
			}
		}
	}
	
	/**
	 * 设置MOBILE端授权初始化参数并检查
	 * 
	 * @param prop
	 * @param orderNo
	 * @param amt
	 * @param addition
	 * @throws PaymentException
	 */
	public void initPaymentRequestParamsForMobileCreateDirect(Properties prop, Map<String, String> addition,String call_back_url,String notifyUrl,String merchant_url)
			throws PaymentException {
		this.payment_gateway = prop.getProperty("payment_gateway_mobile");
		this.call_back_url = call_back_url;
		this.notify_url = notifyUrl;
		this.merchant_url = merchant_url;
		loadRequestParams(prop);
		this.service = prop.getProperty(PaymentAdaptor.PAYMENT_GATEWAY_MOBILE_C);
		String requestStr = PaymentRequestParam.MOBILE_CREATE_DIRECT_STR;
		setParamMapForMobileC(addition,requestStr,prop);
		String toBeSignedString = MapAndStringConvertor.getToBeSignedString(addition);
		String sign = Md5Encrypt.md5(toBeSignedString + key, _input_charset);
		addition.put("sign", sign);
		addition.put("action", payment_gateway);
		this.paymentParameters = addition;
	}
	
	/**
	 *  只设置MD5加密的字段(sign与sign_type不在加密范围内)
	 *  该设置只转换配置文件中的参数
	 *  MOBILE端
	 * @param addition
	 * @throws PaymentParamErrorException
	 */
	public void setParamMapForMobileC(Map<String, String> addition,String requestStr,Properties prop)
			throws PaymentParamErrorException {
		String reqStr = "";
		if (Validator.isNotNullOrEmpty(service)) {
			addition.put("service", service);// 服务名称(不可为空)
		} else {
			throw new PaymentParamErrorException("service can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(prop.get("format"))) {
			addition.put("format", prop.get("format").toString());// 请求参数格式(不可为空)
		} else {
			throw new PaymentParamErrorException("format can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(prop.get("v"))) {
			addition.put("v", prop.get("v").toString());// 接口版本号(不可为空)
		} else {
			throw new PaymentParamErrorException("v can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(prop.get("sec_id"))) {
			addition.put("sec_id", prop.get("sec_id").toString());// 签名方式(不可为空)
		} else {
			throw new PaymentParamErrorException("service can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(addition.get("req_id"))) {
			addition.put("req_id", addition.get("req_id"));// 用于关联请求与响应，防止请求重播。支付宝限制来自同一个partner的请求号必须唯一。(不可为空)
		} else {
			throw new PaymentParamErrorException("req_id can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(subject)) {
			reqStr = requestStr.replace("${subject}", subject);// 商品名称(不可为空)
		} else {
			throw new PaymentParamErrorException("subject can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(addition.get("out_trade_no"))) {
			reqStr = reqStr.replace("${out_trade_no}", addition.get("out_trade_no"));// 支付宝合作商户网站唯一订单号(不可为空)
		} else {
			throw new PaymentParamErrorException("out_trade_no can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(addition.get("total_fee"))) {
			reqStr = reqStr.replace("${total_fee}", addition.get("total_fee").toString());// 交易金额(不可为空)
			addition.remove("total_fee");
		} else {
			throw new PaymentParamErrorException("total_fee can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(seller_email)) {
			reqStr = reqStr.replace("${seller_account_name}", seller_email);// 卖家支付宝账号(不可为空)
		} else {
			throw new PaymentParamErrorException("seller_account_name can't be null/empty!");
		}
		if(Validator.isNotNullOrEmpty(call_back_url)){//支付宝参数不可为空
			reqStr = reqStr.replace("${call_back_url}", call_back_url);
		}else{
			throw new PaymentParamErrorException("call_back_url can't be null/empty!");
		}
		if(Validator.isNotNullOrEmpty(notify_url)){
			reqStr = reqStr.replace("${notify_url}", notify_url);//支付宝参数中虽然可以为空，但业务需求允许为空
		}else{
			throw new PaymentParamErrorException("notify_url can't be null/empty!");
		}
		if(Validator.isNotNullOrEmpty(merchant_url)){
			reqStr = reqStr.replace("${merchant_url}", merchant_url);
		}else{
			throw new PaymentParamErrorException("merchant_url can't be null/empty!");
		}
		if(Validator.isNotNullOrEmpty(addition.get("pay_expire"))){//交易自动关闭时间
			reqStr = reqStr.replace("${pay_expire}", addition.get("pay_expire"));
		}else{
			reqStr = reqStr.replace("${pay_expire}", "21600");
		}
		if (Validator.isNotNullOrEmpty(partner)) {
			addition.put("partner", partner);// 合作方(不可为空)
		} else {
			throw new PaymentParamErrorException("partner can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(addition.get("out_user"))) {
			reqStr = reqStr.replace("${out_user}", addition.get("out_user"));
		} else {
			reqStr = reqStr.replace("<out_user>${out_user}</out_user>", "");
		}
		addition.put("req_data", reqStr);
	}
	
	/**
	 * 设置MOBILE端交易初始化参数并检查
	 * 
	 * @param prop
	 * @param orderNo
	 * @param amt
	 * @param addition
	 * @throws PaymentException
	 */
	public void initPaymentRequestParamsForMobileAuthAndExecute(Properties prop, Map<String, String> addition)
			throws PaymentException {
		this.payment_gateway = prop.getProperty("payment_gateway_mobile");
		loadRequestParams(prop);
		this.service = prop.getProperty(PaymentAdaptor.PAYMENT_GATEWAY_MOBILE_P);
		String requestStr = PaymentRequestParam.MOBILE_AUTH_EXECUTE_STR;
		setParamMapForMobileP(addition,requestStr,prop);
		String toBeSignedString = MapAndStringConvertor.getToBeSignedString(addition);
		String sign = Md5Encrypt.md5(toBeSignedString + key, _input_charset);
		addition.put("sign", sign);
		addition.put("action", payment_gateway);
		this.paymentParameters = addition;
	}
	
	/**
	 *  只设置MD5加密的字段(sign与sign_type不在加密范围内)
	 *  该设置只转换配置文件中的参数
	 *  MOBILE端
	 * @param addition
	 * @throws PaymentParamErrorException
	 */
	public void setParamMapForMobileP(Map<String, String> addition,String requestStr,Properties prop)
			throws PaymentParamErrorException {
		String reqStr = "";
		if (Validator.isNotNullOrEmpty(service)) {
			addition.put("service", service);// 服务名称(不可为空)
		} else {
			throw new PaymentParamErrorException("service can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(prop.get("format"))) {
			addition.put("format", prop.get("format").toString());// 请求参数格式(不可为空)
		} else {
			throw new PaymentParamErrorException("format can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(prop.get("v"))) {
			addition.put("v", prop.get("v").toString());// 接口版本号(不可为空)
		} else {
			throw new PaymentParamErrorException("service can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(prop.get("sec_id"))) {
			addition.put("sec_id", prop.get("sec_id").toString());// 签名方式(不可为空)
		} else {
			throw new PaymentParamErrorException("service can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(partner)) {
			addition.put("partner", partner);// 合作方(不可为空)
		} else {
			throw new PaymentParamErrorException("partner can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(addition.get("request_token"))) {
			reqStr = requestStr.replace("${request_token}", addition.get("request_token"));;// 请求业务参数(不可为空)
		} else {
			throw new PaymentParamErrorException("request_token can't be null/empty!");
		}
		addition.put("req_data", reqStr);
	}

	/**
	 * 初始化退单参数
	 * @param prop
	 * @param amt
	 * @param addition
	 * @throws PaymentException
	 */
	public void initPaymentRequestParamsCancel(Properties prop,
			Map<String, String> addition) throws PaymentException {
		this.payment_gateway = prop.getProperty(PaymentAdaptor.PAYMENT_GATEWAY);
		loadRequestParams(prop);
		this.close_trade = prop.getProperty("close_trade");
		addition.put("service", close_trade);
		addition.put("partner", partner);
		addition.put("_input_charset", _input_charset);
		String toBeSignedString = MapAndStringConvertor.getToBeSignedString(addition);
		String sign = Md5Encrypt.md5(toBeSignedString + key, _input_charset);
		addition.put("sign", sign);
		addition.put("sign_type", sign_type);
		addition.put("action", payment_gateway);
	}
	
	/**
	 * 初始化订单查询参数
	 * @param prop
	 * @param amt
	 * @param addition
	 * @throws PaymentException
	 */
	public void initPaymentRequestParamsForQuery(Properties prop,
			Map<String, String> addition) throws PaymentException {
		this.payment_gateway = prop.getProperty(PaymentAdaptor.PAYMENT_GATEWAY);
		loadRequestParams(prop);
		String query = prop.getProperty("single_trade_query");
		addition.put("service", query);
		addition.put("partner", partner);
		addition.put("_input_charset", _input_charset);
		String toBeSignedString = MapAndStringConvertor.getToBeSignedString(addition);
		String sign = Md5Encrypt.md5(toBeSignedString + key, _input_charset);
		addition.put("sign", sign);
		addition.put("sign_type", sign_type);
		addition.put("action", payment_gateway);
	}

	private void loadRequestParams(Properties prop)
			throws PaymentParamErrorException {
		if (prop == null) {
			logger.warn("No Payment Configuration File found for Alipay");
			return;
		}
		Enumeration<?> e = prop.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (!key.startsWith(PaymentAdaptor.PAYMENT_PARAM_PREFIX))
				continue;
			// set value
			String value = prop.getProperty(key);
			key = key.substring(6);
			setParamValue(key, value);
		}
	}

	private void setParamValue(String key, String value)
			throws PaymentParamErrorException {
		try {
			Method m = ReflectionUtil.getMethod(this.getClass(), key,
					String.class);
			if (m != null)
				ReflectionUtil.invoke(this, m, value);
			else
				ReflectionUtil.setFieldValue(this, key, value);
		} catch (Exception e) {
			throw new PaymentParamErrorException("Error when setting Param "
					+ key + "'s value with " + value, e);
		}
	}

	@Override
	public String getRequestType() {
		return this.requestType;
	}

	@Override
	public String getRequestHtml() {
		return null;
	}
}
