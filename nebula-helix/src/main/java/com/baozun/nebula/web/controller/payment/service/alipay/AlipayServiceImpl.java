package com.baozun.nebula.web.controller.payment.service.alipay;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.baozun.nebula.utilities.common.convertor.MapAndStringConvertor;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.io.http.HttpClientUtil;
import com.baozun.nebula.utilities.io.http.HttpMethodType;
import com.baozun.nebula.web.controller.payment.service.common.CommonPayServiceImpl;
import com.baozun.nebula.web.controller.payment.service.common.command.CommonPayParamCommand;
import com.feilong.core.Validator;

@Service
public class AlipayServiceImpl extends CommonPayServiceImpl implements AlipayService {
	
	/** The Constant LOGGER. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(AlipayServiceImpl.class);
    
	@Override
	public PaymentRequest createPayment(CommonPayParamCommand payParamCommand, Map<String, String> extraParams, Device device) {
		try {
			if (device.isMobile()){
			    return createPaymentForWap(payParamCommand, extraParams);
			} else {
				return createPayment(payParamCommand, extraParams);
			}
		} catch (Exception e) {
			LOGGER.error("[CREATEPAYMENT] create payment error.", e);
		}
		return null;
	}
    
	
	@Override
	public PaymentResult getPaymentResultForSynOfWap(
			HttpServletRequest request, String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);//获得支付适配器
		return paymentAdaptor.getPaymentResultForMobileAuthAndExecuteSYN(request);
	}

	@Override
	public PaymentResult getPaymentResultForAsyOfWap(
			HttpServletRequest request, String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);//获得支付适配器
		return paymentAdaptor.getPaymentResultForMobileAuthAndExecuteASY(request);
	}
	
	private PaymentRequest createPaymentForWap(CommonPayParamCommand payParamCommand, Map<String, String> additionParams) throws Exception {
		//获得支付适配器
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommand.getPaymentType());
		LOGGER.info("[CREATE_PAYMENT_FORWAP] get reserved pay type. orderCode:[{}], storePayType:[{}], reservedPayType:[{}]", payParamCommand.getTrade_no(), payParamCommand.getPaymentType());
		
		//创建一个新的MOBILE端授权请求
		Map<String, String> createDirectParaMap = getCreateDirectParaMap(payParamCommand);
		//将支付所需的定制参数赋值给createDirectParaMap
        if (null != additionParams) {
        	createDirectParaMap.putAll(additionParams);
        } 
        
		PaymentRequest paymentRequest = paymentAdaptor.newPaymentRequestForMobileCreateDirect(createDirectParaMap);
		String result = HttpClientUtil.getHttpMethodResponseBodyAsStringIgnoreCharSet(paymentRequest.getRequestURL(), HttpMethodType.GET, INPUT_CHARSET);
		LOGGER.info("[CREATE_PAYMENT_FORWAP] alipay wap pay create direct interface return, result:[{}]", result);
		Map<String,String> map = queryString2Map(URLDecoder.decode(result, INPUT_CHARSET));
		
		//手机WAP端授权结果
		PaymentResult paymentResult = paymentAdaptor.getPaymentResultForMobileCreateDirect(map);
		
		if(paymentResult.getPaymentServiceSatus().equals(PaymentServiceStatus.SUCCESS)){
			Map<String, String> resultMap = MapAndStringConvertor.convertResultToMap(map.get("res_data").toString());
			//创建一个新的MOBILE端交易请求
			paymentRequest = paymentAdaptor.newPaymentRequestForMobileAuthAndExecute(resultMap);
			LOGGER.info("[CREATE_PAYMENT_FORWAP] get alipay wap pay redirect url. result:[{}]", paymentRequest.getRequestURL());
			return paymentRequest;
		}
		
		return null;
	}
	
    private Map<String, String> getCreateDirectParaMap(CommonPayParamCommand payParamCommand) {
    	Map<String, String> paraMap = new HashMap<String, String>();
    	paraMap.put("out_trade_no", payParamCommand.getTrade_no());
    	paraMap.put("req_id", String.valueOf(System.currentTimeMillis()));
    	paraMap.put("total_fee", String.valueOf(payParamCommand.getTotalFee()));
    	paraMap.put("pay_expire", payParamCommand.getIt_b_pay());
		return paraMap;
	}
    
    /**
     * 将类似于url中的queryString转化为map
     * @param queryString
     * @return
     */
    private Map<String, String> queryString2Map(String queryString) {
    	Map<String,String> map = new HashMap<String,String>();
    	
    	String[] params = queryString.split("&");
		if(Validator.isNotNullOrEmpty(params)){
			for(String param : params){
				String key = param.split("=")[0].toString();
				String val = param.replace(param.split("=")[0].toString() + "=", "");
				map.put(key, val);
			}
		}
		return map;
    }

}
