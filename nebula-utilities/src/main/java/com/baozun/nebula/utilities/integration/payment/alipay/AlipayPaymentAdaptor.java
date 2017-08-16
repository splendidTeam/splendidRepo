package com.baozun.nebula.utilities.integration.payment.alipay;

import java.util.Properties;

import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;

public class AlipayPaymentAdaptor extends AbstractAlipayPaymentAdaptor{

    public static final String PLATFORM_ALIPAY = "PaymentServiceProvider:Alipay";

    public AlipayPaymentAdaptor(Properties InComingConf){
        super.configs = InComingConf;
        super.setPayMethod(RequestParam.PAYMETHOE_D);
        super.getAddress(PaymentFactory.PAY_TYPE_ALIPAY);
    }

    @Override
    public String getServiceProvider(){
        return PLATFORM_ALIPAY;
    }

}
