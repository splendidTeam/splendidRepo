package com.baozun.nebula.utilities.integration.payment.alipay;

import java.util.Properties;

import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;

/**
 * @deprecated 支付宝已经停止该产品申请 @since 5.3.2.17 <br>
 *             add javadoc by jinxin (2017年6月1日 下午1:40:49)
 */
@Deprecated
public class AlipayBankPaymentAdaptor extends AbstractAlipayPaymentAdaptor{

    public static final String PLATFORM_ALIPAY_BANK = "PaymentServiceProvider:AlipayBank";

    public AlipayBankPaymentAdaptor(Properties InComingConf){
        super.configs = InComingConf;
        super.setPayMethod(RequestParam.PAYMETHOE_BANK);
        super.setIsDefault_login("Y");
        super.getAddress(PaymentFactory.PAY_TYPE_ALIPAY_BANK);
    }

    @Override
    public String getServiceProvider(){
        return PLATFORM_ALIPAY_BANK;
    }

}
