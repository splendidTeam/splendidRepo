package com.baozun.nebula.payment.manager.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baozun.nebula.payment.convert.PayParamCommandAdaptor;
import com.baozun.nebula.payment.convert.PaymentConvertFactory;
import com.baozun.nebula.payment.convert.SalesOrderCommandToPaymentParamsConverter;
import com.baozun.nebula.payment.manager.PaymentManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.ReflectionUtil;
import com.baozun.nebula.utilities.common.WechatUtil;
import com.baozun.nebula.utilities.common.command.WechatPayParamCommand;
import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.convertor.PayParamConvertorAdaptor;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;
import com.baozun.nebula.utilities.integration.payment.unionpay.acp.sdk.UnionPayBase;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatConfig;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatResponseKeyConstants;
import com.feilong.core.Validator;

@Service("PaymentManager")
public class PaymentManagerImpl implements PaymentManager {
	
	private static final Logger log = LoggerFactory
			.getLogger(PaymentManagerImpl.class);
	
	@Override
	public PaymentRequest createPayment(SalesOrderCommand order) {
		PaymentRequest paymentRequest = null;
		try {
			PaymentFactory paymentFactory = PaymentFactory.getInstance();
			PaymentConvertFactory paymentConvertFactory = PaymentConvertFactory.getInstance();
			PayParamCommandAdaptor payParamCommandAdaptor = paymentConvertFactory.getConvertAdaptor(paymentFactory.getPayType(order.getOnLinePaymentCommand().getPayType()));
			payParamCommandAdaptor.setSalesOrderCommand(order);
            payParamCommandAdaptor.setRequestParams(SalesOrderCommandToPaymentParamsConverter.convert(order));
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());//获得支付适配器
			PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());//获得对应的参数转换器
			Map<String,String> addition = payParamConvertorAdaptor.commandConvertorToMapForCreatUrl(payParamCommandAdaptor);
			paymentRequest = paymentAdaptor.newPaymentRequest(RequestParam.HTTP_TYPE_GET, addition);//获得对应的结果类
		} catch (Exception ex){
			log.error("CreatePayment error: {}", ex);
			return null;
		}
		return paymentRequest;
	}
	
    @Override
    public PaymentRequest createPayment(Map<String, Object> additionParams, String payType) {
        PaymentRequest paymentRequest = null;
        try {
            PayParamCommandAdaptor payParamCommandAdaptor = PaymentConvertFactory.getInstance().getConvertAdaptor(payType);
            PaymentFactory paymentFactory = PaymentFactory.getInstance();
            //获得支付适配器
            PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payType);
            //获得对应的参数转换器
            PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payType);
            Map<String,String> addition = payParamConvertorAdaptor.commandConvertorToMapForCreatUrl(payParamCommandAdaptor);         
            // 將支付所需的定制参数赋值给addition
            if (null != additionParams) {
                addition.putAll(convertToStringParamsMap(additionParams));
            }          
            paymentRequest = paymentAdaptor.newPaymentRequest(RequestParam.HTTP_TYPE_GET, addition);
        } catch (Exception ex){
            log.error("CreatePayment error: "+ex.toString(), ex);
            return null;
        }
        return paymentRequest;
    }
    

    
	@Override
	public PaymentResult getPaymentResultForSyn(HttpServletRequest request,String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);//获得支付适配器
		PaymentResult result = new PaymentResult();
		result = paymentAdaptor.getPaymentResult(request);
		return result;
	}

	@Override
	public PaymentResult getPaymentResultForAsy(HttpServletRequest request,String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);//获得支付适配器
		PaymentResult result = new PaymentResult();
		result = paymentAdaptor.getPaymentResultFromNotification(request);
		return result;
	}
	
	@Override
	public PaymentResult cancelPayment(SalesOrderCommand order) {
		PaymentResult result = new PaymentResult();
		try {
			PaymentFactory paymentFactory = PaymentFactory.getInstance();
			PaymentConvertFactory paymentConvertFactory = PaymentConvertFactory.getInstance();
			PayParamCommandAdaptor payParamCommandAdaptor = paymentConvertFactory.getConvertAdaptor(paymentFactory.getPayType(order.getOnLinePaymentCancelCommand().getPayType()));
			payParamCommandAdaptor.setSalesOrderCommand(order);
			//获得支付适配器
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());
			if(paymentAdaptor.isSupportClosePaymentRequest()){
				//获得对应的参数转换器
				PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());//获得对应的参数转换器
				Map<String,String> addition = payParamConvertorAdaptor.commandConvertorToMapForCaneclOrder(payParamCommandAdaptor);
				result = paymentAdaptor.closePaymentRequest(addition);
			}else{
				result.setMessage("not support");
				result.setPaymentServiceSatus(PaymentServiceStatus.NOT_SUPPORT);
			}
		} catch (Exception ex){
			log.error("cancelPayment error: {}", ex);
			result.setMessage(ex.toString());
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
		}
		return result;
	}
	
	@Override
	public PaymentResult getOrderInfo(SalesOrderCommand order) {
		PaymentResult result = new PaymentResult();
		try {
			PaymentFactory paymentFactory = PaymentFactory.getInstance();
			PaymentConvertFactory paymentConvertFactory = PaymentConvertFactory.getInstance();
			PayParamCommandAdaptor payParamCommandAdaptor = paymentConvertFactory.getConvertAdaptor(paymentFactory.getPayType(order.getOnLinePaymentCancelCommand().getPayType()));
			payParamCommandAdaptor.setSalesOrderCommand(order);
            Map<String, Object> params = new HashMap<String, Object>(); 
            for (int i = 0; i < SalesOrderCommand.class.getFields().length; i++) { 
                Field field = SalesOrderCommand.class.getFields()[i]; 
                params.put(field.getName(), ReflectionUtil.getFieldValue(order, field.getName())); 
            }
            payParamCommandAdaptor.setRequestParams(params);
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());// 获得支付适配器
			PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());//获得对应的参数转换器
			Map<String, String> addition = payParamConvertorAdaptor.commandConvertorToMapForOrderInfo(payParamCommandAdaptor);
			result = paymentAdaptor.getOrderInfo(addition);
			
		} catch (Exception ex) {
			log.error("getOrderInfo error: " + ex.toString(), ex);
			result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			result.setMessage(ex.toString());
		}
		return result;
	}

	@Override
	public PaymentRequest createPaymentForWap(SalesOrderCommand order) {
		PaymentRequest paymentRequest = null;
		try {
			PaymentFactory paymentFactory = PaymentFactory.getInstance();
			PaymentConvertFactory paymentConvertFactory = PaymentConvertFactory.getInstance();
			PayParamCommandAdaptor payParamCommandAdaptor = paymentConvertFactory.getConvertAdaptor(paymentFactory.getPayType(order.getOnLinePaymentCommand().getPayType()));
			payParamCommandAdaptor.setSalesOrderCommand(order);
			// 获得支付适配器
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());
            Map<String, Object> salesOrderParams = new HashMap<String, Object>(); 
            for (int i = 0; i < SalesOrderCommand.class.getFields().length; i++) { 
                Field field = SalesOrderCommand.class.getFields()[i]; 
                salesOrderParams.put(field.getName(), ReflectionUtil.getFieldValue(order, field.getName())); 
            } 
            payParamCommandAdaptor.setRequestParams(salesOrderParams);
            //获得对应的参数转换器
            PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());
			Map<String, String> addition = payParamConvertorAdaptor.commandConvertorToMapForMobileCreatUrl(payParamCommandAdaptor);
			//创建一个新的MOBILE端授权请求
			paymentRequest = paymentAdaptor.newPaymentRequestForMobileCreateDirect(addition);
			log.info("RequestForMobileCreateDirect:{}",paymentRequest.getRequestURL());
			
			//Alipay WAP 支付需要拼接完参数之后，再获取一次token
			paymentRequest = paymentAdaptor.getCreateResponseToken(paymentRequest);

		}catch(Exception ex){
			log.error("CreatePayment error: "+ex.toString(), ex);
			return null;
		}
		//返回交易请求url
		return paymentRequest;
	}

	@Deprecated
	private void getParaMap(SalesOrderCommand order,
			PayParamCommandAdaptor payParamCommandAdaptor,
			Map<String, String> addition) throws PaymentParamErrorException {
		if(Validator.isNotNullOrEmpty(order.getCode())){
			addition.put("out_trade_no", order.getCode());
		}else{
			throw new PaymentParamErrorException("out_trade_no can't be null/empty!");
		}
		
		if(Validator.isNotNullOrEmpty(order.getOmsCode())){
			addition.put("req_id", order.getOmsCode());
		}else{
			throw new PaymentParamErrorException("req_id can't be null/empty!");
		}
		
		BigDecimal minPay = new BigDecimal(0.01f);
		BigDecimal maxPay = new BigDecimal(100000000);
		
		if (Validator.isNotNullOrEmpty(order.getTotal())) {
			addition.put("total_fee", String.valueOf(order.getTotal()));
		} else if (order.getTotal().compareTo(minPay) == -1
				|| order.getTotal().compareTo(maxPay) == 1) {
			throw new PaymentParamErrorException("total_fee:" + order.getTotal()
					+ " can't < " + minPay + " or > " + maxPay);
		}else {
			throw new PaymentParamErrorException("total_fee can't be null/empty!");
		}

		//可为空，但项目不允许传空的超时时间
		if(Validator.isNotNullOrEmpty(payParamCommandAdaptor.getIt_b_pay())){
			addition.put("pay_expire", payParamCommandAdaptor.getIt_b_pay());
		}else {
			throw new PaymentParamErrorException(
					"it_b_pay can't be null/empty!");
		}
		
		if(Validator.isNotNullOrEmpty(order.getName())){
			addition.put("out_user", order.getName());
		}
	}

	@Override
	public PaymentResult getPaymentResultForSynOfWap(
			HttpServletRequest request, String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		//获得支付适配器
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);
		PaymentResult result = new PaymentResult();
		result = paymentAdaptor.getPaymentResultForMobileAuthAndExecuteSYN(request);
		return result;
	}

	@Override
	public PaymentResult getPaymentResultForAsyOfWap(
			HttpServletRequest request, String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		//获得支付适配器
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);
		PaymentResult result = new PaymentResult();
		result = paymentAdaptor.getPaymentResultForMobileAuthAndExecuteASY(request);
		return result;
	}
	
	@Override
	public PaymentResult unifiedOrder(WechatPayParamCommand wechatPayParamCommand,String paymentType) {
		PaymentResult paymentResult = new PaymentResult();
		try {
			PaymentFactory paymentFactory = PaymentFactory.getInstance();
			//获得支付适配器
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentFactory.getPayType(Integer.valueOf(paymentType)));
			Map<String, String> addition = new HashMap<String, String>();
			getUnifiedOrderParaMap(wechatPayParamCommand, addition);;
			paymentResult = paymentAdaptor.unifiedOrder(addition);
		} catch (PaymentParamErrorException e) {
			log.error("unifiedOrder error: "+e.toString());
			paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			paymentResult.setMessage(e.toString());
		} catch (Exception ex){
			log.error("unifiedOrder error: "+ex.toString());
			paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			paymentResult.setMessage(ex.toString());
		}
		return paymentResult;
	}
	
	@Deprecated
	private void getCloseOrderParaMap(SalesOrderCommand order,Map<String, String> addition)
			throws PaymentParamErrorException {	
		addition.put("appid", WechatConfig.APP_ID);
		addition.put("mch_id", WechatConfig.PARTNER_ID);
		addition.put("nonce_str", WechatUtil.generateRandomString());
		
		if(Validator.isNotNullOrEmpty(order.getCode())){
			addition.put("out_trade_no", order.getCode());
 		}
	}
	
	@Deprecated
	private void getOrderQueryParaMapOfWechat(SalesOrderCommand order,Map<String, String> addition)
			throws PaymentParamErrorException {	
		addition.put("appid", WechatConfig.APP_ID);
		addition.put("mch_id", WechatConfig.PARTNER_ID);
		addition.put("nonce_str", WechatUtil.generateRandomString());

		if(Validator.isNotNullOrEmpty(order.getCode())){
			addition.put("out_trade_no", order.getCode());
 		}
		
		if(Validator.isNotNullOrEmpty(order.getOnLinePaymentCancelCommand().getTrade_no())){
			addition.put("transaction_id", order.getOnLinePaymentCancelCommand().getTrade_no());
 		}
	}
	
	@Deprecated
	private void getOrderQueryParmMapOfUnion(SalesOrderCommand order,Map<String, String> addition){
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		java.util.Date currentTime = new java.util.Date();// 得到当前系统时间
		String pDate = formatter.format(currentTime);

		// 固定填写
		addition.put("version", UnionPayBase.version);// M

		// 默认取值：UTF-8
		addition.put("encoding", UnionPayBase.encoding_UTF8);// M

		addition.put("signMethod", "01");// M

		// 取值：00
		addition.put("txnType", "00");// M

		// 01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付）03：分期付款
		addition.put("txnSubType", "00");// M

		//产品类型
		addition.put("bizType", "000000");// M
		
		// 0：普通商户直连接入2：平台类商户接入
		addition.put("accessType", "0");// M

		Properties properties = ProfileConfigUtil.findPro("config/acp_sdk.properties");

		addition.put("merId", properties.getProperty("merchantId"));

		// 商户端生成
		addition.put("orderId", order.getCode());// M

		// 商户发送交易时间
		addition.put("txnTime", pDate);// M
	
	}
	
	private void getUnifiedOrderParaMap(WechatPayParamCommand wechatPayParamCommand,Map<String, String> addition)
			throws PaymentParamErrorException {	
		addition.put("appid", WechatConfig.APP_ID);
		addition.put("mch_id", WechatConfig.PARTNER_ID);
		addition.put("nonce_str", WechatUtil.generateRandomString());
		addition.put("notify_url", WechatConfig.JS_API_PAYMENT_CALLBACK_URL);
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getDevice_info())){
			addition.put("device_info", wechatPayParamCommand.getDevice_info());
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getBody())){
			addition.put("body", wechatPayParamCommand.getBody());
 		} else {
 			throw new PaymentParamErrorException("unifiedOrder parameter error: body can't be null/empty!");
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getDetail())){
			addition.put("detail", wechatPayParamCommand.getDetail());
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getOut_trade_no())){
			addition.put("out_trade_no", wechatPayParamCommand.getOut_trade_no());
 		} else {
 			throw new PaymentParamErrorException("unifiedOrder parameter error: out_trade_no can't be null/empty!");
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getFee_type())){
			addition.put("fee_type", wechatPayParamCommand.getFee_type());
 		}
		
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getAttach())){
			addition.put("attach", wechatPayParamCommand.getAttach());
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getTotal_fee())){
			addition.put("total_fee", wechatPayParamCommand.getTotal_fee().toString());
 		} else {
 			throw new PaymentParamErrorException("unifiedOrder parameter error: total_fee can't be null/empty!");
 		}
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getSpbill_create_ip())){
			addition.put("spbill_create_ip", wechatPayParamCommand.getSpbill_create_ip());
 		} else {
 			throw new PaymentParamErrorException("unifiedOrder parameter error: spbill_create_ip can't be null/empty!");
 		}
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getTime_start())){
			addition.put("time_start", wechatPayParamCommand.getTime_start());
 		} 
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getTime_expire())){
			addition.put("time_expire", wechatPayParamCommand.getTime_expire());
 		} 
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getTrade_type())){
			addition.put("trade_type", wechatPayParamCommand.getTrade_type());
 		} else {
 			throw new PaymentParamErrorException("unifiedOrder parameter error: trade_type can't be null/empty!");
 		}
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getGoods_tag())){
			addition.put("goods_tag", wechatPayParamCommand.getGoods_tag());
 		} 
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getProduct_id())){
			addition.put("product_id", wechatPayParamCommand.getProduct_id());
 		} else {
 			if(wechatPayParamCommand.getTrade_type().equals(WechatResponseKeyConstants.TRADE_TYPE_NATIVE)){
 				throw new PaymentParamErrorException("unifiedOrder parameter error: when the trade_type value is 'NATIVE' product_id can't be null/empty!");
 			}
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getOpenid())){
			addition.put("openid", wechatPayParamCommand.getOpenid());
 		} else {
 			if(wechatPayParamCommand.getTrade_type().equals(WechatResponseKeyConstants.TRADE_TYPE_JSAPI)){
 				throw new PaymentParamErrorException("unifiedOrder parameter error: when the trade_type value is 'JSAPI' openid can't be null/empty!");
 			}
 		}
		
	}
	
	private Map<String, String> convertToStringParamsMap(Map<String, Object> params) {
		Map<String, String> res = new HashMap<String, String>();
		Set<String> keySet = params.keySet();
		for(String key : keySet){
			Object val = params.get(key);
			if(null!=val){
				String paramVal = val+"";
				res.put(key, paramVal);
			}
		}
		return res;
	}
}
