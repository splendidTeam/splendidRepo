package com.baozun.nebula.payment.convert;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.baozun.nebula.utilities.integration.payment.PaymentFactory;

public class PaymentConvertFactory{

    private static PaymentConvertFactory inst = new PaymentConvertFactory();

    private Map<String, PayParamCommandAdaptor> paymentConvertMap;

    //---------------------------------------------------------------------

    public static PaymentConvertFactory getInstance(){
        return inst;
    }

    public PaymentConvertFactory(){
        setConvertAdaptor();
    }

    //---------------------------------------------------------------------

    public PayParamCommandAdaptor getConvertAdaptor(String paymentType){
        Validate.notBlank(paymentType, "paymentType can't be blank!");
        return paymentConvertMap.get(paymentType);
    }

    private void setConvertAdaptor(){
        paymentConvertMap = new HashMap<>();
        paymentConvertMap.put(PaymentFactory.PAY_TYPE_ALIPAY, new OrderCommandParamConvertorAdaptor());
        paymentConvertMap.put(PaymentFactory.PAY_TYPE_ALIPAY_BANK, new OrderCommandParamConvertorAdaptor());
        paymentConvertMap.put(PaymentFactory.PAY_TYPE_ALIPAY_CREDIT, new OrderCommandParamConvertorAdaptor());
        paymentConvertMap.put(PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT, new OrderCommandParamConvertorAdaptor());
        paymentConvertMap.put(PaymentFactory.PAY_TYPE_UNIONPAY, new OrderCommandParamConvertorAdaptor());
        // 微信
        paymentConvertMap.put(PaymentFactory.PAY_TYPE_WECHAT, new OrderCommandParamConvertorAdaptor());
    }

}
