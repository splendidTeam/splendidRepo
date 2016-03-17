//package com.baozun.nebula.pay.manager;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import com.baozun.nebula.utilities.common.URIUtil;
//import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
//import com.baozun.nebula.utilities.integration.payment.PaymentResult;
//import com.baozun.nebula.utilities.integration.payment.PaymentService;
//import com.baozun.nebula.utilities.integration.payment.alipay.Alipay4PaymentAdaptor;
//import com.baozun.nebula.utilities.integration.payment.alipay.Alipay4PaymentRequest;
//import com.baozun.nebula.utilities.integration.payment.exception.PaymentAdaptorInitialFailureException;
//import com.baozun.nebula.utilities.integration.payment.exception.PaymentException;
//
//@Service("AplipayManager")
//public class AplipayManagerImpl implements AplipayManager {
//
//	private static final Logger	logger	= LoggerFactory.getLogger(AplipayManagerImpl.class);
//	@Override
//	/*
//	 * 支付宝对于金额只能支持到小数点后两位
//	 * codeType：编码类型
//	 * (non-Javadoc)
//	 * @see com.baozun.nebula.utilities.integration.payment.manager.AplipayManager#newPaymentRequest(java.lang.String, java.lang.String, java.math.BigDecimal, java.util.Map, java.lang.String)
//	 */
//	public Map<String,String> newPaymentRequest(String requestType, String orderNo,
//			BigDecimal amt, Map<String, String> addition,String codeType) {
//		Map<String,String> result = new HashMap<String,String>();
//		PaymentService paymentService = PaymentService.getInstance();
//		PaymentAdaptor paymentAdaptor = paymentService.getPaymentAdaptor("Aplipay");
//		String payMentRequestURL = "";
//		try {
//			paymentAdaptor.newPaymentRequest(Alipay4PaymentRequest.HTTP_TYPE_POST, orderNo, amt, addition);
//			payMentRequestURL = URIUtil.getEncodedUrl(addition, codeType);
//			result.put("url", payMentRequestURL);
//			result.put("isSuccess", "true");
//		} catch (PaymentAdaptorInitialFailureException e) {
//			logger.error("AplipayManagerImpl's newPaymentRequest: "+e.toString());
//			e.printStackTrace();
//			result.put("fail", e.toString());
//		} catch (PaymentException e) {
//			logger.error("AplipayManagerImpl's newPaymentRequest: "+e.toString());
//			e.printStackTrace();
//			result.put("fail", e.toString());
//		}
//		return result;
//	}
//	
//	@Override
//	public Map<String, Object> getPaymentResult(HttpServletRequest request) {
//		PaymentService paymentService = PaymentService.getInstance();
//		PaymentAdaptor paymentAdaptor = paymentService.getPaymentAdaptor("Aplipay");
//		Map<String, Object> result = new HashMap<String,Object>();
//		PaymentResult paymentResult = new PaymentResult();
//		paymentResult = paymentAdaptor.getPaymentResult(request);
//		result.put("satus", paymentResult.getPaymentServiceSatus().toString());
//		result.put("message", paymentResult.getMessage());
//		result.put("resultMap", request.getParameterMap());
//		return result;
//	}
//
//	@Override
//	public Map<String,Object> getPaymentResultFromNotification(
//			HttpServletRequest request) {
//		PaymentService paymentService = PaymentService.getInstance();
//		PaymentAdaptor paymentAdaptor = paymentService.getPaymentAdaptor("Aplipay");
//		Map<String, Object> result = new HashMap<String,Object>();
//		String resultStr = "";
//		PaymentResult paymentResult = new PaymentResult();
//		paymentResult = paymentAdaptor.getPaymentResultFromNotification(request);
//		resultStr = paymentResult.getPaymentServiceSatus().toString();
//		result.put("satus",resultStr);
//		result.put("resultMap", request.getParameterMap());
//		return result;
//	}
//
//	@Override
//	public Map<String,String> closePaymentRequest(String paymentReqId,String orderNum,
//			BigDecimal amt, boolean isBuyerClose) {
//		PaymentService paymentService = PaymentService.getInstance();
//		PaymentAdaptor paymentAdaptor = paymentService.getPaymentAdaptor("Aplipay");
//		Map<String,String> parm = new HashMap<String,String>();
//		Map<String,String> resultMap = new HashMap<String,String>();
//		try {
//			parm.put("trade_no", paymentReqId);
//			parm.put("out_order_no", orderNum);
//			resultMap = paymentAdaptor.closePaymentRequest(paymentReqId,orderNum,amt,isBuyerClose,parm);
//		} catch (PaymentException e) {
//			logger.error("AplipayManagerImpl's closePaymentRequest: "+e.toString());
//			e.printStackTrace();
//			resultMap.put("fail", e.toString());
//		}
//		return resultMap;
//	}
//
//	@Override
//	public boolean isSupportClosePaymentRequest() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//}
