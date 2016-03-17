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
package com.baozun.nebula.utilities.integration.payment.wechat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.WechatUtil;
import com.baozun.nebula.utilities.common.XMLUtils;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;

public abstract class AbstractWechatPaymentAdaptor implements PaymentAdaptor {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractWechatPaymentAdaptor.class);
	
	protected Properties configs;

	@Override
	public String getServiceProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentRequest newPaymentRequest(String httpType, Map<String, String> addition) {
		return null;
	}

	@Override
	public PaymentRequest newPaymentRequestForMobileCreateDirect(Map<String, String> addition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentRequest newPaymentRequestForMobileAuthAndExecute(Map<String, String> addition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResult(HttpServletRequest request) {
		
		return null;
	}


	@Override
	public PaymentResult getPaymentResultForMobileCreateDirect(Map<String, String> resultStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResultForMobileAuthAndExecuteSYN(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResultForMobileAuthAndExecuteASY(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResultFromNotification(HttpServletRequest request) {
		PaymentResult result = new PaymentResult();
		try {
			String requestBody = WechatUtil.getRequestBody(request);
		
			PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
	
			Map<String, String> responseMap = XMLUtils.parserXml(requestBody);
			
			Set<String> excludes = new HashSet<String>();
			excludes.add(WechatResponseKeyConstants.SIGN);
			
			String subOrdinate = responseMap.get(WechatResponseKeyConstants.OUT_TRADE_NO);
			String partner = responseMap.get(WechatResponseKeyConstants.MCH_ID);// 商户号
			String billNo = responseMap.get(WechatResponseKeyConstants.TRANSACTION_ID);//微信支付订单号
			
			if("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RETURN_CODE))){
				if("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RESULT_CODE))){
					String resSign =  WechatUtil.makeSign(responseMap, excludes, WechatConfig.PAY_SIGN_KEY);
					
					//验证签名
					if(!responseMap.get(WechatResponseKeyConstants.SIGN).equals(resSign)){
						result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
						result.setMessage("sign not match");
						logger.error("getPaymentResultFromNotification sign not match, parameter sign : {}, local sign : {}", 
								responseMap.get(WechatResponseKeyConstants.SIGN),
								resSign);
						return result;
						
					}
					
					// 验证商户号是否一致
					if(!WechatConfig.PARTNER_ID.equals(partner)){
						result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
						result.setMessage("partner not match");
						logger.error("getPaymentResultFromNotification partner not match, parameter partner : {}, local partner : {}", 
								partner,
								WechatConfig.PARTNER_ID);
						return result;
					}
					
					result.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
					result.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));
					
				}else{
					result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					result.setMessage(responseMap.get(WechatResponseKeyConstants.ERR_CODE));
					logger.error("getPaymentResultFromNotification failure , result_code : {}, err_code : {},err_code_des : {}", 
							responseMap.get(WechatResponseKeyConstants.RESULT_CODE),
							responseMap.get(WechatResponseKeyConstants.ERR_CODE),
							responseMap.get(WechatResponseKeyConstants.ERR_CODE_DES));
					
				}
				
			}else{
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));
				logger.error("getPaymentResultFromNotification failure , return_code : {}, return_msg : {}", 
						responseMap.get(WechatResponseKeyConstants.RETURN_CODE),
						responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
			}
			paymentServiceReturnCommand.setReturnMsg(responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
			paymentServiceReturnCommand.setOrderNo(subOrdinate);
			paymentServiceReturnCommand.setTradeNo(billNo);
			result.setPaymentStatusInformation(paymentServiceReturnCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return result;
	}

	@Override
	public PaymentResult closePaymentRequest(Map<String, String> parm) {
		PaymentResult result = new PaymentResult();
	
		String sign = WechatUtil.makeSign(parm, null, WechatConfig.PAY_SIGN_KEY);
		parm.put(WechatResponseKeyConstants.SIGN, sign);
			
		String xmlInfo = WechatUtil.getXmlInfo(parm);
		String response = WechatUtil.post(WechatConfig.CLOSE_ORDER_URL, xmlInfo);
		Set<String> excludes = new HashSet<String>();
		excludes.add(WechatResponseKeyConstants.SIGN);
		Map<String, String> responseMap = XMLUtils.parserXml(response);
		if("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RETURN_CODE))){
			if("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RESULT_CODE))){
				String resSign =  WechatUtil.makeSign(responseMap, excludes, WechatConfig.PAY_SIGN_KEY);
				
				if(responseMap.get(WechatResponseKeyConstants.SIGN).equals(resSign)){
					result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
					result.setMessage(PaymentServiceStatus.SUCCESS.toString());
				}else{
					result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					result.setMessage("sign not match");
					logger.error("closePaymentRequest sign not match, parameter sign : {}, local sign : {}", 
							responseMap.get(WechatResponseKeyConstants.SIGN),
							resSign);
					
				}
				
			}else{
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(responseMap.get(WechatResponseKeyConstants.ERR_CODE));
				logger.error("closePaymentRequest failure , result_code : {}, err_code : {},err_code_des : {}", 
						responseMap.get(WechatResponseKeyConstants.RESULT_CODE),
						responseMap.get(WechatResponseKeyConstants.ERR_CODE),
						responseMap.get(WechatResponseKeyConstants.ERR_CODE_DES));
				
			}
			
		}else{
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			result.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));
			logger.error("closePaymentRequest failure , return_code : {}, return_msg : {}", 
					responseMap.get(WechatResponseKeyConstants.RETURN_CODE),
					responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
		}
		return result;
	}

	@Override
	public boolean isSupportClosePaymentRequest() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public PaymentResult getOrderInfo(Map<String, String> addition) {
		PaymentResult result = new PaymentResult();
		PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
	 
		String sign = WechatUtil.makeSign(addition, null, WechatConfig.PAY_SIGN_KEY);
		addition.put(WechatResponseKeyConstants.SIGN, sign);
		 
		String xmlInfo = WechatUtil.getXmlInfo(addition);
		String response = WechatUtil.post(WechatConfig.ORDER_QUERY_URL, xmlInfo);
		Set<String> excludes = new HashSet<String>();
		excludes.add(WechatResponseKeyConstants.SIGN);
		Map<String, String> responseMap = XMLUtils.parserXml(response);
		
		
		if("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RETURN_CODE))){
			if("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RESULT_CODE))){
				String resSign =  WechatUtil.makeSign(responseMap, excludes, WechatConfig.PAY_SIGN_KEY);
				
				if(responseMap.get(WechatResponseKeyConstants.SIGN).equals(resSign)){
					result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
					result.setMessage(responseMap.get(WechatResponseKeyConstants.TRADE_STATE));
				}else{
					result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					result.setMessage("sign not match");
					logger.error("orderquery sign not match, parameter sign : {}, local sign : {}", 
							responseMap.get(WechatResponseKeyConstants.SIGN),
							resSign);
					
				}
				
			}else{
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(responseMap.get(WechatResponseKeyConstants.ERR_CODE));
				logger.error("orderquery failure , result_code : {}, err_code : {},err_code_des : {}", 
						responseMap.get(WechatResponseKeyConstants.RESULT_CODE),
						responseMap.get(WechatResponseKeyConstants.ERR_CODE),
						responseMap.get(WechatResponseKeyConstants.ERR_CODE_DES));
				
			}
		}else{
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			result.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));
			logger.error("orderquery failure , return_code : {}, return_msg : {}", 
					responseMap.get(WechatResponseKeyConstants.RETURN_CODE),
					responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
		}
		paymentServiceReturnCommand.setOrderNo(responseMap.get(WechatResponseKeyConstants.OUT_TRADE_NO));
		paymentServiceReturnCommand.setTradeNo(responseMap.get(WechatResponseKeyConstants.TRANSACTION_ID));
		result.setPaymentStatusInformation(paymentServiceReturnCommand);
		return result;
	}

	@Override
	public PaymentResult unifiedOrder(Map<String, String> addition) {
		PaymentResult result = new PaymentResult();

		String sign =  WechatUtil.makeSign(addition, null, WechatConfig.PAY_SIGN_KEY);
		
		addition.put(WechatResponseKeyConstants.SIGN, sign);
		String xmlInfo = WechatUtil.getXmlInfo(addition);
		// 调用微信的统一下单接口
		// @see http://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
		String response = WechatUtil.post(WechatConfig.PAY_UNIFIED_ORDER_URL, xmlInfo);
		logger.debug("response xml info is [{}]", response);
		Map<String, String> responseMap= XMLUtils.parserXml(response);
		
		Set<String> excludes = new HashSet<String>();
		excludes.add(WechatResponseKeyConstants.SIGN);


		if("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RETURN_CODE))){
			if("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RESULT_CODE))){
				String resSign =  WechatUtil.makeSign(responseMap, excludes, WechatConfig.PAY_SIGN_KEY);
				
				if(responseMap.get(WechatResponseKeyConstants.SIGN).equals(resSign)){
					result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
					result.setMessage(responseMap.get(WechatResponseKeyConstants.TRADE_STATE));
					//封装返回参数
					getReturnCommand(result,responseMap);
				}else{
					result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					result.setMessage("sign not match");
					logger.error("unifiedOrder sign not match, parameter sign : {}, local sign : {}", 
							responseMap.get(WechatResponseKeyConstants.SIGN),
							resSign);
				}
				
			}else{
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(responseMap.get(WechatResponseKeyConstants.ERR_CODE));
				logger.error("unifiedOrder failure , result_code : {}, err_code : {},err_code_des : {}", 
						responseMap.get(WechatResponseKeyConstants.RESULT_CODE),
						responseMap.get(WechatResponseKeyConstants.ERR_CODE),
						responseMap.get(WechatResponseKeyConstants.ERR_CODE_DES));
				//封装返回参数
				getReturnCommand(result,responseMap);
				
			}
			
		}else{
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			result.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));
			logger.error("unifiedOrder failure , return_code : {}, return_msg : {}", 
					responseMap.get(WechatResponseKeyConstants.RETURN_CODE),
					responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
		}

		return result;
	}
	
	private void getReturnCommand(PaymentResult paymentResult,
			Map<String, String> resMap) {
		PaymentServiceReturnCommand resultCommand = new PaymentServiceReturnCommand();
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.APPID))){
			resultCommand.setAppId(resMap.get(WechatResponseKeyConstants.APPID));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.MCH_ID))){
			resultCommand.setMchId(resMap.get(WechatResponseKeyConstants.MCH_ID));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.DEVICE_INFO))){
			resultCommand.setDeviceInfo(resMap.get(WechatResponseKeyConstants.DEVICE_INFO));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.NONCE_STR))){
			resultCommand.setNonceStr(resMap.get(WechatResponseKeyConstants.NONCE_STR));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.SIGN))){
			resultCommand.setSign(resMap.get(WechatResponseKeyConstants.SIGN));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.RESULT_CODE))){
			resultCommand.setResultCode(resMap.get(WechatResponseKeyConstants.RESULT_CODE));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.ERR_CODE))){
			resultCommand.setErrCode(resMap.get(WechatResponseKeyConstants.ERR_CODE));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.ERR_CODE_DES))){
			resultCommand.setErrCodeDes(resMap.get(WechatResponseKeyConstants.ERR_CODE_DES));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.TRADE_TYPE))){
			resultCommand.setTradeType(resMap.get(WechatResponseKeyConstants.TRADE_TYPE));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.PREPAY_ID))){
			resultCommand.setPrepayId(resMap.get(WechatResponseKeyConstants.PREPAY_ID));
		}
		if(StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.CODE_URL))){
			resultCommand.setCodeUrl(resMap.get(WechatResponseKeyConstants.CODE_URL));
		}
		paymentResult.setPaymentStatusInformation(resultCommand);
	}

}
