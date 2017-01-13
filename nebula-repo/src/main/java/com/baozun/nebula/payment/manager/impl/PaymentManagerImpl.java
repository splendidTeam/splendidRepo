package com.baozun.nebula.payment.manager.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baozun.nebula.payment.convert.PayParamCommandAdaptor;
import com.baozun.nebula.payment.convert.PaymentConvertFactory;
import com.baozun.nebula.payment.manager.PaymentManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
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
import com.baozun.nebula.utilities.integration.payment.wechat.WechatConfig;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatResponseKeyConstants;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;

@Service("PaymentManager")
public class PaymentManagerImpl implements PaymentManager {

	private static final Logger log = LoggerFactory.getLogger(PaymentManagerImpl.class);

	@Deprecated
	@Override
	public PaymentRequest createPayment(SalesOrderCommand order) {
		Map<String,Object> additionParams = PropertyUtil.describe(order);
		additionParams.putAll(PropertyUtil.describe(order.getOnLinePaymentCommand()));
		PaymentRequest paymentRequest = createPayment(additionParams, order.getOnLinePaymentCommand().getPayType());
		return paymentRequest;
	}

	/**
	 * 
	 * @Description <p>
	 *              建议用于通用支付接口调用，启用原createPayment(SalesOrderCommand
	 *              order)方法。</br>
	 *              SalesOrderCommand对象具有不易于扩展性，主要耦合基于原始商城订单支付，难以兼容shopdog或同一支付接口
	 *              ，不同形式调用（比如支付宝PC支付，还可以直接二维码支付）</br>
	 *              如需扩展参数，可以直接注入additionParams中，拼接paymentURL时会将MAP中所有参数带上
	 *              </p>
	 * @param additionParams
	 *            调用前可将SalesOrderCommand使用PropertyUtil.describe()方法转亦成Map
	 * @param payType
	 *            即SalesOrderCommand.getOnLinePaymentCommand().getPayType()</br>
	 * @return PaymentRequest
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-29
	 */
	@Override
	public PaymentRequest createPayment(Map<String, Object> orderParams,
			Integer payType) {
		PaymentRequest paymentRequest = null;
		try {
			PaymentFactory paymentFactory = PaymentFactory.getInstance();
			String type = paymentFactory.getPayType(payType);
			PayParamCommandAdaptor payParamCommandAdaptor = PaymentConvertFactory.getInstance().getConvertAdaptor(type);
			payParamCommandAdaptor.setRequestParams(orderParams);
			// 获得对应的参数转换器
			PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(type);
			Map<String, String> params = payParamConvertorAdaptor.commandConvertorToMapForCreatUrl(payParamCommandAdaptor);
			// 將支付所需的定制参数赋值给addition
			payParamConvertorAdaptor.extendCommandConvertorMap(params,orderParams);
			log.info("RequestParams has : {}", orderParams);
			// 获得支付适配器
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(type);
			paymentRequest = paymentAdaptor.newPaymentRequest(
					RequestParam.HTTP_TYPE_GET, params);
		} catch (Exception ex) {
			log.error("CreatePayment error: " + ex.toString(), ex);
			return paymentRequest;
		}
		return paymentRequest;
	}

	@Override
	public PaymentResult getPaymentResultForSyn(HttpServletRequest request,
			String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);// 获得支付适配器
		PaymentResult result = new PaymentResult();
		result = paymentAdaptor.getPaymentResult(request);
		return result;
	}

	@Override
	public PaymentResult getPaymentResultForAsy(HttpServletRequest request,
			String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);// 获得支付适配器
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
			Map<String, Object> orderParams = PropertyUtil.describe(order);
			orderParams.putAll(PropertyUtil.describe(order.getOnLinePaymentCancelCommand()));
			payParamCommandAdaptor.setRequestParams(orderParams);

			// 获得支付适配器
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());
			if (paymentAdaptor.isSupportClosePaymentRequest()) {
				
				// 获得对应的参数转换器
				PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());// 获得对应的参数转换器
				Map<String, String> params = payParamConvertorAdaptor.commandConvertorToMapForCaneclOrder(payParamCommandAdaptor);
				// 將支付所需的定制参数赋值给addition
				payParamConvertorAdaptor.extendCommandConvertorMap(params,orderParams);
				result = paymentAdaptor.closePaymentRequest(params);
			} else {
				result.setMessage("not support");
				result.setPaymentServiceSatus(PaymentServiceStatus.NOT_SUPPORT);
			}
		} catch (Exception ex) {
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
			Map<String, Object> orderParams = PropertyUtil.describe(order);
			orderParams.putAll(PropertyUtil.describe(order.getOnLinePaymentCommand()));
			payParamCommandAdaptor.setRequestParams(orderParams);
			
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());// 获得支付适配器
			PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());// 获得对应的参数转换器
			Map<String, String> params = payParamConvertorAdaptor.commandConvertorToMapForOrderInfo(payParamCommandAdaptor);
			// 將支付所需的定制参数赋值给addition
			payParamConvertorAdaptor.extendCommandConvertorMap(params,orderParams);
			result = paymentAdaptor.getOrderInfo(params);
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
			payParamCommandAdaptor.setRequestParams(PropertyUtil.describe(order));
			// 获得对应的参数转换器
			PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());
			Map<String, String> addition = payParamConvertorAdaptor.commandConvertorToMapForMobileCreatUrl(payParamCommandAdaptor);
			// 创建一个新的MOBILE端授权请求
			paymentRequest = paymentAdaptor.newPaymentRequestForMobileCreateDirect(addition);
			log.info("RequestForMobileCreateDirect:{}",paymentRequest.getRequestURL());

			// Alipay WAP 支付需要拼接完参数之后，再获取一次token
			paymentRequest = paymentAdaptor.getCreateResponseToken(paymentRequest);

		} catch (Exception ex) {
			log.error("CreatePayment error: " + ex.toString(), ex);
			return null;
		}
		// 返回交易请求url
		return paymentRequest;
	}

	@Override
	public PaymentResult getPaymentResultForSynOfWap(
			HttpServletRequest request, String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		// 获得支付适配器
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);
		PaymentResult result = new PaymentResult();
		result = paymentAdaptor.getPaymentResultForMobileAuthAndExecuteSYN(request);
		return result;
	}

	@Override
	public PaymentResult getPaymentResultForAsyOfWap(
			HttpServletRequest request, String paymentType) {
		PaymentFactory paymentFactory = PaymentFactory.getInstance();
		// 获得支付适配器
		PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);
		PaymentResult result = new PaymentResult();
		result = paymentAdaptor.getPaymentResultForMobileAuthAndExecuteASY(request);
		return result;
	}

	@Override
	public PaymentResult unifiedOrder(WechatPayParamCommand wechatPayParamCommand, String paymentType) {
		PaymentResult paymentResult = new PaymentResult();
		try {
			PaymentFactory paymentFactory = PaymentFactory.getInstance();
			// 获得支付适配器
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentFactory.getPayType(Integer.valueOf(paymentType)));
			Map<String, String> addition = new HashMap<String, String>();
			getUnifiedOrderParaMap(wechatPayParamCommand, addition);
			paymentResult = paymentAdaptor.unifiedOrder(addition);
		} catch (PaymentParamErrorException e) {
			log.error("unifiedOrder error: " + e.toString());
			paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			paymentResult.setMessage(e.toString());
		} catch (Exception ex) {
			log.error("unifiedOrder error: " + ex.toString());
			paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			paymentResult.setMessage(ex.toString());
		}
		return paymentResult;
	}

	private void getUnifiedOrderParaMap(
			WechatPayParamCommand wechatPayParamCommand,
			Map<String, String> addition) throws PaymentParamErrorException {
		addition.put("appid", WechatConfig.APP_ID);
		addition.put("mch_id", WechatConfig.PARTNER_ID);
		addition.put("nonce_str", WechatUtil.generateRandomString());
		addition.put("notify_url", WechatConfig.JS_API_PAYMENT_CALLBACK_URL);

		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getDevice_info())) {
			addition.put("device_info", wechatPayParamCommand.getDevice_info());
		}

		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getBody())) {
			addition.put("body", wechatPayParamCommand.getBody());
		} else {
			throw new PaymentParamErrorException(
					"unifiedOrder parameter error: body can't be null/empty!");
		}

		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getDetail())) {
			addition.put("detail", wechatPayParamCommand.getDetail());
		}

		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getOut_trade_no())) {
			addition.put("out_trade_no",wechatPayParamCommand.getOut_trade_no());
		} else {
			throw new PaymentParamErrorException("unifiedOrder parameter error: out_trade_no can't be null/empty!");
		}

		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getFee_type())) {
			addition.put("fee_type", wechatPayParamCommand.getFee_type());
		}

		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getAttach())) {
			addition.put("attach", wechatPayParamCommand.getAttach());
		}

		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getTotal_fee())) {
			addition.put("total_fee", wechatPayParamCommand.getTotal_fee().toString());
		} else {
			throw new PaymentParamErrorException("unifiedOrder parameter error: total_fee can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getSpbill_create_ip())) {
			addition.put("spbill_create_ip",wechatPayParamCommand.getSpbill_create_ip());
		} else {
			throw new PaymentParamErrorException("unifiedOrder parameter error: spbill_create_ip can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getTime_start())) {
			addition.put("time_start", wechatPayParamCommand.getTime_start());
		}
		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getTime_expire())) {
			addition.put("time_expire", wechatPayParamCommand.getTime_expire());
		}
		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getTrade_type())) {
			addition.put("trade_type", wechatPayParamCommand.getTrade_type());
		} else {
			throw new PaymentParamErrorException("unifiedOrder parameter error: trade_type can't be null/empty!");
		}
		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getGoods_tag())) {
			addition.put("goods_tag", wechatPayParamCommand.getGoods_tag());
		}

		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getProduct_id())) {
			addition.put("product_id", wechatPayParamCommand.getProduct_id());
		} else {
			if (wechatPayParamCommand.getTrade_type().equals(WechatResponseKeyConstants.TRADE_TYPE_NATIVE)) {
				throw new PaymentParamErrorException("unifiedOrder parameter error: when the trade_type value is 'NATIVE' product_id can't be null/empty!");
			}
		}

		if (Validator.isNotNullOrEmpty(wechatPayParamCommand.getOpenid())) {
			addition.put("openid", wechatPayParamCommand.getOpenid());
		} else {
			if (wechatPayParamCommand.getTrade_type().equals(WechatResponseKeyConstants.TRADE_TYPE_JSAPI)) {
				throw new PaymentParamErrorException("unifiedOrder parameter error: when the trade_type value is 'JSAPI' openid can't be null/empty!");
			}
		}

	}

}
