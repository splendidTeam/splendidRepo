package com.baozun.nebula.web.controller.payment.service.alipay;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.payment.convert.PayParamCommandAdaptor;
import com.baozun.nebula.payment.convert.PaymentConvertFactory;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.common.convertor.MapAndStringConvertor;
import com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;
import com.baozun.nebula.utilities.io.http.HttpClientUtil;
import com.baozun.nebula.utilities.io.http.HttpMethodType;
import com.feilong.core.Validator;

@Service
public class AlipayServiceImpl implements AlipayService {
	
	/** The Constant LOGGER. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(AlipayServiceImpl.class);
	
	private static final String INPUT_CHARSET = "utf-8";

	@Override
	public PaymentRequest createPayment(SalesOrderCommand order, Map<String, String> additionParams, Device device) {
		// TODO Auto-generated method stub
		return null;
	}

	private PaymentRequest createPayment(SalesOrderCommand order, Map<String, String> additionParams) {
        try {
            PaymentConvertFactory paymentConvertFactory = PaymentConvertFactory.getInstance();
            PayParamCommandAdaptor payParamCommandAdaptor = paymentConvertFactory.getConvertAdaptor(getPayType(order.getOnLinePaymentCommand().getPayType()));
            payParamCommandAdaptor.setSalesOrderCommand(order);
            PaymentFactory paymentFactory = PaymentFactory.getInstance();
            PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());//获得支付适配器
            
            PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());//获得对应的参数转换器
            Map<String,String> addition = payParamConvertorAdaptor.commandConvertorToMapForCreatUrl(payParamCommandAdaptor);         
            // 將支付所需的定制参数赋值给addition，例如定制化参数 qr_pay_mode
            if (null != additionParams) {
                addition.putAll(additionParams);
            }          
            return paymentAdaptor.newPaymentRequest(RequestParam.HTTP_TYPE_GET, addition);
        } catch (PaymentParamErrorException e) {
        	LOGGER.error("CreatePayment error: "+e.toString(), e);
            return null;
        } catch(Exception ex) {
        	LOGGER.error("CreatePayment error: "+ex.toString(), ex);
            return null;
        }
    }
	
	Map<String, String> buildRequestMapForWap(SalesOrderCommand order, PaymentAdaptor paymentAdaptor) {
		Map<String, String> createDirectParaMap = getCreateDirectParaMap(order);
		
		//创建一个新的MOBILE端授权请求
		PaymentRequest paymentRequest = paymentAdaptor.newPaymentRequestForMobileCreateDirect(createDirectParaMap);
		
		String result = HttpClientUtil.getHttpMethodResponseBodyAsStringIgnoreCharSet(paymentRequest.getRequestURL(), HttpMethodType.GET, INPUT_CHARSET);
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			if(Validator.isNotNullOrEmpty(result)){
				result = java.net.URLDecoder.decode(result, INPUT_CHARSET);
				String[] params = result.split("&");
				if(Validator.isNotNullOrEmpty(params)){
					for(String param:params){
						String key = param.split("=")[0].toString();
						String val = param.replace(param.split("=")[0].toString()+"=", "");
						map.put(key, val);
					}
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("newPaymentRequestForMobileCreateDirect error: "+e.toString(), e);
			return null;
		}
		
		return null;
	}
    
	private PaymentRequest createPaymentForWap(SalesOrderCommand order) {
		PaymentRequest paymentRequest = null;
		try {
			PaymentConvertFactory paymentConvertFactory = PaymentConvertFactory.getInstance();
			PayParamCommandAdaptor payParamCommandAdaptor = paymentConvertFactory.getConvertAdaptor(getPayType(order.getOnLinePaymentCommand().getPayType()));
			payParamCommandAdaptor.setSalesOrderCommand(order);
			PaymentFactory paymentFactory = PaymentFactory.getInstance();
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());// 获得支付适配器
		
			Map<String, String> addition = new HashMap<String, String>();
			
			//getParaMap(order, payParamCommandAdaptor, addition);
			
			//创建一个新的MOBILE端授权请求
			paymentRequest = paymentAdaptor.newPaymentRequestForMobileCreateDirect(addition);
			
			String result = HttpClientUtil.getHttpMethodResponseBodyAsStringIgnoreCharSet(paymentRequest.getRequestURL(),HttpMethodType.GET, INPUT_CHARSET);
			
			Map<String,String> map = new HashMap<String,String>();
			try {
				if(Validator.isNotNullOrEmpty(result)){
					result = java.net.URLDecoder.decode(result, INPUT_CHARSET);
					String[] params = result.split("&");
					if(Validator.isNotNullOrEmpty(params)){
						for(String param:params){
							String key = param.split("=")[0].toString();
							String val = param.replace(param.split("=")[0].toString()+"=", "");
							map.put(key, val);
						}
					}
				}
				
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("newPaymentRequestForMobileCreateDirect error: "+e.toString(), e);
				return null;
			}
			
			if(Validator.isNotNullOrEmpty(map)){
				//手机WAP端授权结果
				PaymentResult paymentResult = paymentAdaptor.getPaymentResultForMobileCreateDirect(map);
				if(paymentResult.getPaymentServiceSatus().equals(PaymentServiceStatus.SUCCESS)){
					Map<String, String> resultMap;
					try {
						resultMap = MapAndStringConvertor.convertResultToMap(map.get("res_data").toString());
						//创建一个新的MOBILE端交易请求
						paymentRequest = paymentAdaptor.newPaymentRequestForMobileAuthAndExecute(resultMap);
						LOGGER.info(paymentRequest.getRequestURL());
					} catch (DocumentException e) {
						LOGGER.error("newPaymentRequestForMobileAuthAndExecute error: "+e.toString(), e);
						return null;
					}
				}else{
					LOGGER.error("newPaymentRequestForMobileAuthAndExecute error: "+paymentResult.getMessage()+paymentResult.getPaymentServiceSatus());
					return null;
				}
			}
		}catch(Exception ex){
			LOGGER.error("CreatePayment error: "+ex.toString(), ex);
			return null;
		}
		//返回交易请求url
		return paymentRequest;
	}
    
    private Map<String, String> getCreateDirectParaMap(SalesOrderCommand order) {
    	Map<String, String> paraMap = new HashMap<String, String>();
    	paraMap.put("out_trade_no", order.getCode());
    	paraMap.put("req_id", String.valueOf(System.currentTimeMillis()));
    	paraMap.put("total_fee", String.valueOf(order.getTotal()));
    	paraMap.put("pay_expire", order.getOnLinePaymentCommand().getItBPay());
		
		if(Validator.isNotNullOrEmpty(order.getName())){
			paraMap.put("out_user", order.getName());
		}
		return paraMap;
	}
    
    private String getPayType(Integer payType) {
    	if(SalesOrder.SO_PAYMENT_TYPE_ALIPAY.equals(payType.toString())) {
    		return PaymentFactory.PAY_TYPE_ALIPAY;
    	} else if(SalesOrder.SO_PAYMENT_TYPE_NETPAY.equals(payType.toString())) {
    		return PaymentFactory.PAY_TYPE_ALIPAY_BANK;
    	} else if(SalesOrder.SO_PAYMENT_TYPE_ALIPAY_CREDIT.equals(payType.toString())) {
    		return PaymentFactory.PAY_TYPE_ALIPAY_CREDIT;
    	} else if(SalesOrder.SO_PAYMENT_TYPE_INTERNATIONALCARD.equals(payType.toString())) {
    		return PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;
    	} else {
    		throw new IllegalArgumentException("not an valide alipay type. payType:[" + payType + "]");
    	}
	}

}
