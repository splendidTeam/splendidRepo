package com.baozun.nebula.utilities.integration.payment.unionpay;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.RequestMapUtil;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.UnionPayBase;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKUtil;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentRequest newPaymentRequestForMobileAuthAndExecute(
			Map<String, String> addition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult getPaymentResult(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
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
		Map<String, String> responseMap = new HashMap<String, String>();
		RequestMapUtil.requestConvert(request, responseMap);
		Boolean isValid = SDKUtil.validate(responseMap, UnionPayBase.encoding);
		if(isValid){
			if (UNIONPAY_RETRUNCODE_SUCCESS.equals(responseMap.get("respCode"))){
				result.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
				result.setMessage(responseMap.get("respMsg"));
			}else{
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(responseMap.get("respMsg"));
			}
			PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
			paymentServiceReturnCommand.setReturnMsg(responseMap.get("respMsg"));
			paymentServiceReturnCommand.setOrderNo(responseMap.get("orderId"));
			paymentServiceReturnCommand.setTradeNo(responseMap.get("queryId"));
			result.setPaymentStatusInformation(paymentServiceReturnCommand);
		}else{
			//签名失败
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			result.setMessage("sign not match");
			logger.error("****************************sign not match***********");
		}
		return result;
	}

	@Override
	public PaymentResult closePaymentRequest(Map<String, String> addition) {
		//银联没有未付款之前的取消接口。所以调用查询接口 如果付款成功/交易已受理 那么取消失败。 其他状态都视为取消成功
		PaymentResult result = new PaymentResult();
		String requestUrl = SDKConfig.getConfig().getSingleQueryUrl();
		Map<String, String> submitData = UnionPayBase.signData(addition);
		String resultStr  = SDKUtil.send(requestUrl, submitData, UnionPayBase.encoding, UnionPayBase.connectionTimeout, UnionPayBase.readTimeout);
		logger.info(resultStr);
		Map<String,String> resultMap = SDKUtil.convertResultStringToMap(resultStr);
		logger.info("respCode:{},respMsg:{}",resultMap.get("respCode"),resultMap.get("respMsg"));
		Boolean isVlid = SDKUtil.validate(resultMap, UnionPayBase.encoding);
		if(isVlid){
			if(UNIONPAY_RETRUNCODE_SUCCESS.equals(resultMap.get("respCode")) || UNIONPAY_RETRUNCODE_RECEIVED.equals(resultMap.get("respCode"))){
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(OTHERSTATUS);
			}else{
				result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
				result.setMessage(ORDERNOTEXIST);
			}
		}else{
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
		String requestUrl = SDKConfig.getConfig().getSingleQueryUrl();
		Map<String, String> submitData = UnionPayBase.signData(addition);
		String resultStr  = SDKUtil.send(requestUrl, submitData, UnionPayBase.encoding, UnionPayBase.connectionTimeout, UnionPayBase.readTimeout);
		logger.info(resultStr);
		Map<String,String> resultMap = SDKUtil.convertResultStringToMap(resultStr);
		logger.info("respCode:{},respMsg:{}",resultMap.get("respCode"),resultMap.get("respMsg"));
		Boolean isVlid = SDKUtil.validate(resultMap, UnionPayBase.encoding);
		if(isVlid){
			if(UNIONPAY_RETRUNCODE_SUCCESS.equals(resultMap.get("respCode"))){
				result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
				result.setMessage(SUCCESS);
			}else if(UNIONPAY_RETRUNCODE_RECEIVED.equals(resultMap.get("respCode"))){
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(USERPAYING);
			}else if(UNIONPAY_RETRUNCODE_NOTEXSITS.equals(resultMap.get("respCode"))){
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(ORDERNOTEXIST);
			}else{
				result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
				result.setMessage(OTHERSTATUS);
			}
		}else{
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
	

}
