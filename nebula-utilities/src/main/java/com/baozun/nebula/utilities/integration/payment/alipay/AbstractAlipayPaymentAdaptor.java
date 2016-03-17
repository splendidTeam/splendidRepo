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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.Md5Encrypt;
import com.baozun.nebula.utilities.common.RequestMapUtil;
import com.baozun.nebula.utilities.common.ResourceUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentAdaptorInitialFailureException;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentException;
import com.baozun.nebula.utilities.io.http.HttpClientUtil;
import com.baozun.nebula.utilities.io.http.HttpMethodType;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnForMobileCommand;
import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.common.condition.ResponseParam;
import com.baozun.nebula.utilities.common.convertor.MapAndStringConvertor;
import com.baozun.nebula.utilities.common.convertor.RequestToCommand;

public abstract class AbstractAlipayPaymentAdaptor implements PaymentAdaptor {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractAlipayPaymentAdaptor.class);

	protected Properties configs;

	private String payMethod;

	private String isDefault_login;

	private String returnUrl;

	private String notifyUrl;

	private String errorNotifyUrl;

	@Override
	public abstract String getServiceProvider();

	@Override
	public String getServiceType() {
		return "jishidaozhang";
	}

	@Override
	public String getServiceVersion() {
		return "4.0";
	}

	@Override
	public PaymentRequest newPaymentRequest(String requestType,
			Map<String, String> addition) {
		AlipayPaymentRequest request = new AlipayPaymentRequest();
		try {
			request.setRequestType(requestType);
			if (Validator.isNotNullOrEmpty(isDefault_login)) {
				addition.put("default_login", isDefault_login);
			}
			request.initPaymentRequestParams(configs, addition,
					this.getPayMethod(), returnUrl, notifyUrl, errorNotifyUrl);
		} catch (PaymentException ex) {
			logger.error("Alipay newPaymentRequest error: " + ex.toString());
		}
		return request;
	}

	@Override
	public PaymentRequest newPaymentRequestForMobileCreateDirect(
			Map<String, String> addition) {
		AlipayPaymentRequest request = new AlipayPaymentRequest();
		try {
			request.initPaymentRequestParamsForMobileCreateDirect(configs,
					addition, returnUrl, notifyUrl, errorNotifyUrl);
		} catch (PaymentException ex) {
			logger.error("Alipay newPaymentRequestForMobileCreateDirect error: "
					+ ex.toString());
		}
		return request;
	}

	@Override
	public PaymentRequest newPaymentRequestForMobileAuthAndExecute(
			Map<String, String> addition) {
		AlipayPaymentRequest request = new AlipayPaymentRequest();
		try {
			request.initPaymentRequestParamsForMobileAuthAndExecute(configs,
					addition);
		} catch (PaymentException ex) {
			logger.error("Alipay newPaymentRequestForMobileAuthAndExecute error: "
					+ ex.toString());
		}
		return request;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaymentResult getPaymentResult(HttpServletRequest request) {
		PaymentResult paymentResult = new PaymentResult();
		Map<String, String> responseMap = new HashMap<String, String>();
		RequestMapUtil.requestConvert(request, responseMap);
		String sign = request.getParameter("sign").toString();
		String resultStr = request.getParameter("trade_status").toString();
		String satus = request.getParameter("is_success").toString();
		String notify_id = request.getParameter("notify_id").toString();

		responseMap.remove("sign");
		responseMap.remove("sign_type");
		String toBeSignedString = MapAndStringConvertor
				.getToBeSignedString(responseMap);
		String localSign = Md5Encrypt.md5(
				toBeSignedString + configs.getProperty("param.key"),
				configs.getProperty("param._input_charset"));

		boolean verifyRequest = isNotifyVerifySuccess(notify_id);

		// 返回函数进行加密比较
		if (verifyRequest) {
			if (sign.equals(localSign)) {
				if (satus.equals("T")) {
					getResult(resultStr, paymentResult);
				} else if (satus.equals("F")) {
					paymentResult
							.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					paymentResult.setMessage("failure");
				} else {
					paymentResult
							.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
					paymentResult.setMessage("undefined");
				}
			} else {
				paymentResult
						.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
				paymentResult.setMessage("sign not match");
			}
		} else {
			paymentResult
					.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
			paymentResult.setMessage("alipay return value error");
		}
		PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
		RequestToCommand requestToCommand = new RequestToCommand();
		paymentServiceReturnCommand = requestToCommand
				.alipaySynRequestToCommand(request);
		paymentResult.setPaymentStatusInformation(paymentServiceReturnCommand);
		return paymentResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaymentResult getPaymentResultForMobileCreateDirect(Map<String,String> resultStr) {
		PaymentResult paymentResult = new PaymentResult();
		Map<String, String> responseMap = new HashMap<String, String>();
		String sign = resultStr.get("sign");
		

		try {
			resultStr.remove("sign");
			Map<String, String> resultMap = MapAndStringConvertor.convertResultToMap(resultStr.get("res_data").toString());
			String toBeSignedString = MapAndStringConvertor
					.getToBeSignedString(resultStr);
			String localSign = Md5Encrypt.md5(
					toBeSignedString + configs.getProperty("param.key"),
					configs.getProperty("param._input_charset"));

			// 返回函数进行加密比较
			if (localSign.equals(sign)) {
				if (null == resultMap.get("request_token")) {
					paymentResult
							.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					paymentResult.setMessage(resultMap.get("code") + "/"
							+ resultMap.get("detail"));
				} else {
					paymentResult
							.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
					paymentResult.setMessage(resultMap.get("request_token"));
				}
			} else {
				paymentResult
						.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
				paymentResult.setMessage("sign not match");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return paymentResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaymentResult getPaymentResultForMobileAuthAndExecuteSYN(
			HttpServletRequest request) {
		PaymentResult paymentResult = new PaymentResult();
		Map<String, String> responseMap = new HashMap<String, String>();
		RequestMapUtil.requestConvert(request, responseMap);
		String sign = request.getParameter("sign").toString();
		String result = request.getParameter("result").toString();

		responseMap.remove("sign");
		responseMap.remove("sign_type");
		String toBeSignedString = MapAndStringConvertor
				.getToBeSignedString(responseMap);
		String localSign = Md5Encrypt.md5(
				toBeSignedString + configs.getProperty("param.key"),
				configs.getProperty("param._input_charset"));

		// 返回函数进行加密比较
		if (sign.equals(localSign)) {
			if (RequestParam.ALIPAYSUCCESS.equals(result)) {
				paymentResult
						.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
			} else {
				paymentResult
						.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			}
		} else {
			paymentResult
					.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
			paymentResult.setMessage("sign not match");
		}

		PaymentServiceReturnForMobileCommand paymentServiceReturnForMobileCommand = new PaymentServiceReturnForMobileCommand();
		RequestToCommand requestToCommand = new RequestToCommand();
		paymentServiceReturnForMobileCommand = requestToCommand.alipaySynRequestToCommandForMobile(request,null);
		paymentResult.setPaymentStatusInformation(paymentServiceReturnForMobileCommand);
		return paymentResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaymentResult getPaymentResultForMobileAuthAndExecuteASY(
			HttpServletRequest request) {
		PaymentResult paymentResult = new PaymentResult();
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> responseMap = new HashMap<String, String>();
		RequestMapUtil.requestConvert(request, responseMap);
		String sign = request.getParameter("sign").toString();
		try {
			resultMap = MapAndStringConvertor.convertResultToMap(request
					.getParameter("notify_data").toString());
			String resultStr = resultMap.get("trade_status").toString();
			String notify_id = resultMap.get("notify_id").toString();

			responseMap.remove("sign");
			String toBeSignedString = "service="+responseMap.get("service")+"&v="+responseMap.get("v")+"&sec_id="+responseMap.get("sec_id")+"&notify_data="+responseMap.get("notify_data");
			String localSign = Md5Encrypt.md5(
					toBeSignedString + configs.getProperty("param.key"),
					configs.getProperty("param._input_charset"));

			boolean verifyRequest = isNotifyVerifySuccess(notify_id);

			if (verifyRequest) {
				if (sign.equals(localSign)) {
					getResult(resultStr, paymentResult);
				} else {
					paymentResult
							.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					paymentResult.setMessage("failure");
				}
			} else {
				paymentResult
						.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
				paymentResult.setMessage("alipay return value error");
			}
			if (verifyRequest) {
				paymentResult.setResponseValue(RequestParam.ALIPAYSUCCESS);
			} else {
				paymentResult.setResponseValue(RequestParam.ALIPAYFAIL);
			}
			PaymentServiceReturnForMobileCommand paymentServiceReturnForMobileCommand = new PaymentServiceReturnForMobileCommand();
			RequestToCommand requestToCommand = new RequestToCommand();
			paymentServiceReturnForMobileCommand = requestToCommand.alipaySynRequestToCommandForMobile(request,resultMap);
			paymentResult.setPaymentStatusInformation(paymentServiceReturnForMobileCommand);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return paymentResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaymentResult getPaymentResultFromNotification(
			HttpServletRequest request) {
		PaymentResult paymentResult = new PaymentResult();
		Map<String, String> responseMap = new HashMap<String, String>();
		RequestMapUtil.requestConvert(request, responseMap);
		String sign = request.getParameter("sign").toString();
		String resultStr = request.getParameter("trade_status").toString();
		String notify_id = request.getParameter("notify_id").toString();

		responseMap.remove("sign");
		responseMap.remove("sign_type");
		String toBeSignedString = MapAndStringConvertor
				.getToBeSignedString(responseMap);
		String localSign = Md5Encrypt.md5(
				toBeSignedString + configs.getProperty("param.key"),
				configs.getProperty("param._input_charset"));

		boolean verifyRequest = isNotifyVerifySuccess(notify_id);

		if (verifyRequest) {
			if (sign.equals(localSign)) {
				getResult(resultStr, paymentResult);
			} else {
				paymentResult
						.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				paymentResult.setMessage("failure");
			}
		} else {
			paymentResult
					.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
			paymentResult.setMessage("alipay return value error");
		}
		if (verifyRequest) {
			paymentResult.setResponseValue(RequestParam.ALIPAYSUCCESS);
		} else {
			paymentResult.setResponseValue(RequestParam.ALIPAYFAIL);
		}
		PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
		RequestToCommand requestToCommand = new RequestToCommand();
		paymentServiceReturnCommand = requestToCommand
				.alipayAsyRequestToCommand(request);
		paymentResult.setPaymentStatusInformation(paymentServiceReturnCommand);
		return paymentResult;
	}

	@Override
	public PaymentResult closePaymentRequest(Map<String, String> parm) {
		PaymentResult paymentResult = new PaymentResult();
		AlipayPaymentRequest request = new AlipayPaymentRequest();
		try {
			request.initPaymentRequestParamsCancel(configs, parm);
			_closeTrade(parm, paymentResult);
		} catch (PaymentException e) {
			logger.error("Alipay closePaymentRequest error: " + e.toString());
			paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			paymentResult.setMessage(e.toString());
			e.printStackTrace();
		}
		return paymentResult;
	}

	@Override
	public boolean isSupportClosePaymentRequest() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public PaymentResult getOrderInfo(Map<String, String> addition) {
		PaymentResult paymentResult = new PaymentResult();
		AlipayPaymentRequest request = new AlipayPaymentRequest();
		try {
			request.initPaymentRequestParamsForQuery(configs, addition);
			queryOrderInfo(addition, paymentResult);
		} catch (PaymentException e) {
			logger.error("Alipay queryOrderInfo error: " + e.toString());
			paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			paymentResult.setMessage(e.toString());
			e.printStackTrace();
		}
		return paymentResult;
	}
	
	

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getIsDefault_login() {
		return isDefault_login;
	}

	public void setIsDefault_login(String isDefault_login) {
		this.isDefault_login = isDefault_login;
	}

	/**
	 * 使用 HttpURLConnection 去alipay 上面 验证 是否确实 校验成功.
	 * 
	 * @param notifyVerifyUrl
	 *            通知验证的url
	 * @return 如果获得的信息是true，则校验成功；如果获得的信息是其他，则校验失败。
	 */
	public boolean isNotifyVerifySuccess(String notifyId) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(configs.getProperty("payment_gateway") + "?");
			sb.append("service=" + configs.getProperty("notify_verify"))
					.append("&");
			sb.append("partner=" + configs.getProperty("param.partner"))
					.append("&");
			sb.append("notify_id=" + notifyId);
			URL url = new URL(sb.toString());
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("POST");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()));

			String notifyVerifyResult = bufferedReader.readLine();
			// 如果获得的信息是true，则校验成功；如果获得的信息是其他，则校验失败。
			return "true".equals(notifyVerifyResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void _closeTrade(Map<String, String> params, PaymentResult result) {
		String alipayUrl = params.get("action").toString();
		params.remove("action");
		String closeTradeUrl = MapAndStringConvertor
				.getToBeSignedString(params);
		String url = alipayUrl + "?" + closeTradeUrl;
		String returnXML = HttpClientUtil.getHttpMethodResponseBodyAsString(
				url, HttpMethodType.GET);

		if (Validator.isNotNullOrEmpty(returnXML)) {
			try {
				Map<String, String> resultMap = MapAndStringConvertor
						.convertResultToMap(returnXML);
				if ("T".equals(resultMap.get("is_success"))) {
					result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
				} else {
					result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					result.setMessage(resultMap.get("error"));
				}
			} catch (DocumentException e) {
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(e.toString());
				e.printStackTrace();
			}
		}
	}

	private void queryOrderInfo(Map<String, String> params, PaymentResult result) {
		String alipayUrl = params.get("action").toString();
		params.remove("action");
		String queryUrl = MapAndStringConvertor.getToBeSignedString(params);
		String url = alipayUrl + "?" + queryUrl;
		String returnXML = HttpClientUtil.getHttpMethodResponseBodyAsString(
				url, HttpMethodType.GET);

		if (Validator.isNotNullOrEmpty(returnXML)) {
			try {
				Map<String, String> resultMap = MapAndStringConvertor
						.convertResultToMap(returnXML);
				if ("T".equals(resultMap.get("is_success"))) {
					result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
					result.setMessage(resultMap.get("trade_status"));

					PaymentServiceReturnCommand resultCommand = new PaymentServiceReturnCommand();
					resultCommand.setTradeNo(resultMap.get("trade_no"));
					resultCommand.setBuyer(resultMap.get("buyer_email"));
					resultCommand.setTradeStatus(resultMap.get("trade_status"));
					result.setPaymentStatusInformation(resultCommand);
				} else {
					result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					result.setMessage(resultMap.get("error"));
				}
			} catch (DocumentException e) {
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(e.toString());
				e.printStackTrace();
			}
		}
	}

	public void getResult(String resultStr, PaymentResult paymentResult) {
		if (ResponseParam.WAIT_BUYER_PAY.equals(resultStr)) {
			paymentResult
					.setPaymentServiceSatus(PaymentServiceStatus.WAIT_BUYER_PAY);
			paymentResult.setMessage("WAIT_BUYER_PAY");
		} else if (ResponseParam.TRADE_CLOSED.equals(resultStr)) {
			paymentResult
					.setPaymentServiceSatus(PaymentServiceStatus.TRADE_CLOSED);
			paymentResult.setMessage("TRADE_CLOSED");
		} else if (ResponseParam.TRADE_FINISHED.equals(resultStr)) {
			paymentResult
					.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
			paymentResult.setMessage("TRADE_FINISHED");
		} else if (ResponseParam.TRADE_PENDING.equals(resultStr)) {
			paymentResult
					.setPaymentServiceSatus(PaymentServiceStatus.UNCONFIRUMED_PAYMENT_SUCCESS);
			paymentResult.setMessage("TRADE_PENDING");
		} else if (ResponseParam.TRADE_SUCCESS.equals(resultStr)) {
			paymentResult
					.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
			paymentResult.setMessage("TRADE_SUCCESS");
		}
	}

	/*
	 * 初始化地址配置参数
	 */
	public void getAddress(String paymentType) {
		Properties configs = new Properties();
		InputStream is = ResourceUtil.getResourceAsStream(
				PaymentFactory.ALIPAYADDRESS, PaymentFactory.class);
		if (is != null) {
			try {
				configs.load(is);
				this.returnUrl = configs.getProperty(paymentType
						+ ".return_url");
				this.notifyUrl = configs.getProperty(paymentType
						+ ".notify_url");
				this.errorNotifyUrl = configs.getProperty(paymentType
						+ ".error_notify_url");
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Error occurs when loading {}",
						PaymentFactory.ALIPAYADDRESS);
			}
		} else {
			logger.error("Could not find {}", PaymentFactory.ALIPAYADDRESS);
		}
	}
	
	@Override
	public PaymentResult unifiedOrder(Map<String, String> addition) {
		PaymentResult paymentResult  = new PaymentResult();
		return paymentResult;
	}

}
