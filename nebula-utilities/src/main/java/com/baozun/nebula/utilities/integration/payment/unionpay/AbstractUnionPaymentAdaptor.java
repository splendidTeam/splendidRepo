package com.baozun.nebula.utilities.integration.payment.unionpay;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;
import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.UnionPayBase;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;
import com.unionpay.acp.sdk.CertUtil;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKUtil;
import com.unionpay.acp.sdk.SecureUtil;

public  abstract class AbstractUnionPaymentAdaptor implements PaymentAdaptor {
	
	private static final String UNIONPAY_RETRUNCODE_NOTEXSITS = "34";

	private static final String UNIONPAY_RETRUNCODE_RECEIVED = "05";

	private static final String UNIONPAY_RETRUNCODE_SUCCESS = "00";

	private static final String USERPAYING = "USERPAYING";

	private static final String SUCCESS = "SUCCESS";

	private static final String ORDERNOTEXIST = "ORDERNOTEXIST";
	
	private static final String OTHERSTATUS = "OTHERSTATUS";
	
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractUnionPaymentAdaptor.class);
	static {
		SDKConfig.getConfig().loadPropertiesFromPath(Thread.currentThread().getContextClassLoader().getResource("").getPath() + ProfileConfigUtil.getProfilePath("config/payment/unionpay/acp_sdk.properties").replace("acp_sdk.properties", ""));
	}

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
	public PaymentRequest newPaymentRequest(String httpType,
			Map<String, String> addition) {
		UnionPaymentRequest unionPaymentRequest = new UnionPaymentRequest();
		String requestUrl = SDKConfig.getConfig().getFrontRequestUrl();
		Map<String, String> submitFromData = UnionPayBase.signData(addition);
		String html = UnionPayBase.createHtml(requestUrl, submitFromData);
		logger.info("html:" + html);
		unionPaymentRequest.setRequestHtml(html);
		unionPaymentRequest.setPaymentParameters(submitFromData);
		return unionPaymentRequest;
	}

	@Override
	public PaymentRequest newPaymentRequestForMobileCreateDirect(
			Map<String, String> addition) {
		UnionPaymentRequest unionPaymentRequest = new UnionPaymentRequest();
		String requestUrl = SDKConfig.getConfig().getFrontRequestUrl();
		Map<String, String> submitFromData = UnionPayBase.signData(addition);
		String html = UnionPayBase.createHtml(requestUrl, submitFromData);
		logger.info("html:" + html);
		unionPaymentRequest.setRequestHtml(html);
		unionPaymentRequest.setPaymentParameters(submitFromData);
		return unionPaymentRequest;
	}

	@Override
	public PaymentRequest newPaymentRequestForMobileAuthAndExecute(
			Map<String, String> addition) {
		return null;
	}

	@Override
	public PaymentResult getPaymentResult(HttpServletRequest request) {
		PaymentResult result = new PaymentResult();

		// 所有参数
		Map<String, String[]> map = RequestUtil.getParameterMap(request);

		// 编码
		String encoding = map.get(CHAR_SET)[0];
		Map<String, String> paramMap = new HashMap<String, String>();
		for (Entry<String, String[]> entry : map.entrySet()){
			String[] value = entry.getValue();
			if (Validator.isNotNullOrEmpty(value) && Validator.isNotNullOrEmpty(value[0])) {
				paramMap.put(entry.getKey(), value[0]);
			}
		}
		logger.info("unionpay Syn doNotify param:", JsonUtil.format(paramMap));
		
		String stringSign = paramMap.get("signature");

		// 从返回报文中获取certId ，然后去证书静态Map中查询对应验签证书对象
		String certId = paramMap.get("certId");
		logger.info("对返回报文串验签使用的验签公钥序列号：[" + certId + "]");
		
		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = SDKUtil.coverMap2String(paramMap);
		
		try {
			// 验证签名需要用银联发给商户的公钥证书.
			boolean success = SecureUtil.validateSignBySoft(
					CertUtil.getValidateKey(certId),
					SecureUtil.base64Decode(stringSign.getBytes(encoding)),
					SecureUtil.sha1X16(stringData, encoding));
			
			if(success){
				if (UNIONPAY_RETRUNCODE_SUCCESS.equals(paramMap.get("respCode"))){
					result.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
					result.setResponseValue(RequestParam.UNIONSUCCESS);
					result.setMessage(paramMap.get("respMsg"));
				}else{
					result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					result.setResponseValue(RequestParam.UNIONFAIL);
					result.setMessage(paramMap.get("respMsg"));
				}
			}else{
				//签名失败
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage("sign not match");
				logger.error("****************************sign not match***********");
			}
			PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
			paymentServiceReturnCommand.setReturnMsg(paramMap.get("respMsg"));
			paymentServiceReturnCommand.setOrderNo(paramMap.get("orderId"));
			paymentServiceReturnCommand.setTradeNo(paramMap.get("queryId"));
			result.setPaymentStatusInformation(paymentServiceReturnCommand);
		} catch (Exception e) {
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			result.setMessage(paramMap.get("respMsg"));
			PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
			paymentServiceReturnCommand.setReturnMsg(paramMap.get("respMsg"));
			paymentServiceReturnCommand.setOrderNo(paramMap.get("orderId"));
			paymentServiceReturnCommand.setTradeNo(paramMap.get("queryId"));
			result.setPaymentStatusInformation(paymentServiceReturnCommand);
			logger.error("getPaymentResult error : {}",e.getMessage());
		}
		return result;
	}

	@Override
	public PaymentResult getPaymentResultForMobileCreateDirect(
			Map<String, String> resultStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResultForMobileAuthAndExecuteSYN(
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResultForMobileAuthAndExecuteASY(
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResultFromNotification(
			HttpServletRequest request) {
		PaymentResult result = new PaymentResult();
		Map<String, String[]> map = RequestUtil.getParameterMap(request);
		// 编码
		String encoding = map.get(CHAR_SET)[0];
		
		Map<String, String> paramMap = new HashMap<String, String>();
		for (Entry<String, String[]> entry : map.entrySet()){
			String[] value = entry.getValue();
			if (Validator.isNotNullOrEmpty(value) && Validator.isNotNullOrEmpty(value[0])) {
				paramMap.put(entry.getKey(), value[0]);
			}
		}
		logger.info("unionpay Asy doNotify param:", JsonUtil.format(paramMap));
		
		String stringSign = paramMap.get("signature");

		// 从返回报文中获取certId ，然后去证书静态Map中查询对应验签证书对象
		String certId = paramMap.get("certId");
		logger.info("对返回报文串验签使用的验签公钥序列号：[" + certId + "]");
		
		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = SDKUtil.coverMap2String(paramMap);
		
		try {
			// 验证签名需要用银联发给商户的公钥证书.
			boolean success = SecureUtil.validateSignBySoft(
					CertUtil.getValidateKey(certId),
					SecureUtil.base64Decode(stringSign.getBytes(encoding)),
					SecureUtil.sha1X16(stringData, encoding));
			
			if(success){
				if (UNIONPAY_RETRUNCODE_SUCCESS.equals(paramMap.get("respCode"))){
					result.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
					result.setResponseValue(RequestParam.UNIONSUCCESS);
					result.setMessage(paramMap.get("respMsg"));
				}else{
					result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
					result.setResponseValue(RequestParam.UNIONFAIL);
					result.setMessage(paramMap.get("respMsg"));
				}
			}else{
				//签名失败
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage("sign not match");
				logger.error("****************************sign not match***********");
			}
			PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
			paymentServiceReturnCommand.setReturnMsg(paramMap.get("respMsg"));
			paymentServiceReturnCommand.setOrderNo(paramMap.get("orderId"));
			paymentServiceReturnCommand.setTradeNo(paramMap.get("queryId"));
			result.setPaymentStatusInformation(paymentServiceReturnCommand);
		} catch (Exception e) {
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			result.setMessage(paramMap.get("respMsg"));
			PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
			paymentServiceReturnCommand.setReturnMsg(paramMap.get("respMsg"));
			paymentServiceReturnCommand.setOrderNo(paramMap.get("orderId"));
			paymentServiceReturnCommand.setTradeNo(paramMap.get("queryId"));
			result.setPaymentStatusInformation(paymentServiceReturnCommand);
			logger.error("getPaymentResultFromNotification error : {}",e.getMessage());
		}
		return result;
	}

	@Override
	public PaymentResult closePaymentRequest(Map<String, String> addition) {
		//银联没有未付款之前的取消接口。所以调用查询接口 如果付款成功/交易已受理 那么取消失败。 其他状态都视为取消成功
		PaymentResult result = new PaymentResult();
		// 查询地址
		String requestUrl = SDKConfig.getConfig().getSingleQueryUrl();
		// 参数签名
		Map<String, String> submitData = UnionPayBase.signData(addition);
		// 查询
		String resultStr  = SDKUtil.send(requestUrl, submitData, UnionPayBase.encoding, UnionPayBase.connectionTimeout, UnionPayBase.readTimeout);
		logger.info(resultStr);
		Map<String,String> resultMap = SDKUtil.convertResultStringToMap(resultStr);
		logger.info("respCode:{},respMsg:{}",resultMap.get("respCode"),resultMap.get("respMsg"));
		Boolean isVlid = SDKUtil.validate(resultMap, UnionPayBase.encoding);
		if (isVlid) {
		    //如果查询交易成功
		    if(UNIONPAY_RETRUNCODE_SUCCESS.equals(resultMap.get("respCode"))){
		        //处理被查询交易的应答码逻辑
                String origRespCode = resultMap.get("origRespCode");
                if("00".equals(origRespCode)){
                    //交易成功
                    result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                    result.setMessage(SUCCESS);
                }else if("03".equals(origRespCode) || "04".equals(origRespCode) || "05".equals(origRespCode)){
                    //不确定支付成功，需要再次查询
                    result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    result.setMessage(OTHERSTATUS);
                }else{
                    //没有支付成功，状态未知
                    result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    result.setMessage(OTHERSTATUS);
                }
		    }else if(UNIONPAY_RETRUNCODE_NOTEXSITS.equals(resultMap.get("respCode"))){
		        //查询不到交易，可以取消
		        result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
	            result.setMessage(ORDERNOTEXIST);
		    }else{
                result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                result.setMessage(OTHERSTATUS);
		    }
		}else{
		    //签名失败，暂时不能取消
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			result.setMessage("sign not match");
		}
		PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
		paymentServiceReturnCommand.setOrderNo(resultMap.get("orderId"));
		result.setPaymentStatusInformation(paymentServiceReturnCommand);
		return result;
	}

	@Override
	public boolean isSupportClosePaymentRequest() {
		return true;
	}

	@Override
	public PaymentResult getOrderInfo(Map<String, String> addition) {
		PaymentResult result = new PaymentResult();
		// 查询地址
		String requestUrl = SDKConfig.getConfig().getSingleQueryUrl();
		// 参数签名
		Map<String, String> submitData = UnionPayBase.signData(addition);
		// 查询
		String resultStr  = SDKUtil.send(requestUrl, submitData, UnionPayBase.encoding, UnionPayBase.connectionTimeout, UnionPayBase.readTimeout);
		logger.info(resultStr);
		Map<String,String> resultMap = SDKUtil.convertResultStringToMap(resultStr);
		logger.info("respCode:{},respMsg:{}",resultMap.get("respCode"),resultMap.get("respMsg"));
		Boolean isVlid = SDKUtil.validate(resultMap, UnionPayBase.encoding);
		if (isVlid) {
		    //如果查询交易成功
		    if(UNIONPAY_RETRUNCODE_SUCCESS.equals(resultMap.get("respCode"))){
		        //处理被查询交易的应答码逻辑
                String origRespCode = resultMap.get("origRespCode");
                if("00".equals(origRespCode)){
                    //交易成功
                    result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                    result.setMessage(SUCCESS);
                }else if("03".equals(origRespCode) || "04".equals(origRespCode) || "05".equals(origRespCode)){
                    //不确定支付成功，需要再次查询
                    result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    result.setMessage(OTHERSTATUS);
                }else{
                    //没有支付成功，状态未知
                    result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    result.setMessage(OTHERSTATUS);
                }
		    }else if(UNIONPAY_RETRUNCODE_NOTEXSITS.equals(resultMap.get("respCode"))){
		        //查询不到交易，可以取消
		        result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
	            result.setMessage(ORDERNOTEXIST);
		    }else{
                result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                result.setMessage(OTHERSTATUS);
		    }
		}else{
		    //签名失败，暂时不能取消
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			result.setMessage("sign not match");
		}
		PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
		paymentServiceReturnCommand.setOrderNo(resultMap.get("orderId"));
		result.setPaymentStatusInformation(paymentServiceReturnCommand);
		return result;
	}

	@Override
	public PaymentResult unifiedOrder(Map<String, String> addition) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.utilities.integration.payment.PaymentAdaptor#getCreateResponseToken()
	 */
	@Override
	public PaymentRequest getCreateResponseToken(PaymentRequest paymentRequest) {
		return paymentRequest;
	}
}
