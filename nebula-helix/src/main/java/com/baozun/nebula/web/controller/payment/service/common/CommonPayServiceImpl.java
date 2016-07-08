package com.baozun.nebula.web.controller.payment.service.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.web.controller.payment.service.common.command.CommonPayParamCommand;

@Service
public class CommonPayServiceImpl implements CommonPayService {
	
	/** The Constant LOGGER. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(CommonPayServiceImpl.class);
	
	public static final String INPUT_CHARSET = "utf-8";

	@Override
	public PaymentRequest createPayment(CommonPayParamCommand payParamCommand, Map<String, String> extraParams, Device device) {
		try {
			return createPayment(payParamCommand, extraParams);
		} catch (Exception e) {
			LOGGER.error("[CREATEPAYMENT] create payment error.", e);
		}
		return null;
	}
	
	@Override
	public PaymentResult getPaymentResultForSyn(HttpServletRequest request, String paymentType) {
		//获得支付适配器
		PaymentAdaptor paymentAdaptor = PaymentFactory.getInstance().getPaymentAdaptor(paymentType);
		return paymentAdaptor.getPaymentResult(request);
	}

	@Override
	public PaymentResult getPaymentResultForAsy(HttpServletRequest request, String paymentType) {
		//获得支付适配器
		PaymentAdaptor paymentAdaptor = PaymentFactory.getInstance().getPaymentAdaptor(paymentType);
		return paymentAdaptor.getPaymentResultFromNotification(request);
	}
	
	protected PaymentRequest createPayment(CommonPayParamCommand payParamCommand, Map<String, String> additionParams) throws Exception {
		//获得支付适配器
        PaymentFactory paymentFactory = PaymentFactory.getInstance();
        PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommand.getPaymentType());
        LOGGER.info("[CREATE_PAYMENT_FORWAP] get reserved pay type. orderCode:[{}], storePayType:[{}], reservedPayType:[{}]", payParamCommand.getTrade_no(), payParamCommand.getPaymentType());
        
        //获得对应的参数转换器
        PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommand.getPaymentType());
        Map<String,String> addition = payParamConvertorAdaptor.commandConvertorToMapForCreatUrl(payParamCommand);
        
        //将支付所需的定制参数赋值给addition，例如定制化参数 qr_pay_mode
        if (null != additionParams) {
            addition.putAll(additionParams);
        }
        
        return paymentAdaptor.newPaymentRequest(RequestParam.HTTP_TYPE_GET, addition);
    }

}
