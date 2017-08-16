package com.baozun.nebula.utils.convert;

import com.baozun.nebula.payment.manager.impl.PaymentManagerImpl;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;

/**
 * 
 * @Description 建议使用 {@link PaymentManagerImpl#getPayType(Integer payType)}
 * @see com.baozun.nebula.payment.manager.impl.PaymentManagerImpl#getPayType(Integer payType)
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-29
 */
@Deprecated
public class PayTypeConvertUtil{

    @Deprecated
    public static String getPayType(Integer payType){
        String type = PaymentFactory.PAY_TYPE_ALIPAY;
        switch (payType) {
            case 1:
                type = PaymentFactory.PAY_TYPE_ALIPAY;
                break;
            case 3:
                type = PaymentFactory.PAY_TYPE_ALIPAY_BANK;
                break;
            case 4:
                type = PaymentFactory.PAY_TYPE_WECHAT;
                break;
            case 14:
                type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT;
                break;
            case 131:
                type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;
                break;
            case 141:
                type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;
                break;
            case 161:
                type = PaymentFactory.PAY_TYPE_UNIONPAY;
                break;
        }
        return type;
    }
}
