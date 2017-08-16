package com.baozun.nebula.utilities.integration.payment.adaptor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 标准值适配器
 * 
 * @author jumbo
 *
 */
public interface BasePayParamCommandAdaptor{

    /**
     * 银行简码
     */
    String getDefault_bank();

    /**
     * 订单号
     */
    String getOrderNo();

    /**
     * 总金额
     */
    BigDecimal getTotalFee();

    /**
     * 是否是国际卡
     */
    boolean isInternationalCard();

    /**
     * 商品描述
     */
    String getBody();

    /**
     * 取消订单角色
     */
    String getTrade_role();

    /**
     * 交易号
     */
    String getTrade_no();

    /**
     * 支付支付类型
     */
    String getPaymentType();

    /**
     * 交易时间
     */
    String getPaymentTime();

    /**
     * 持卡人真实IP地址
     */
    String getCustomerIp();

    /**
     * 过期时间
     */
    String getIt_b_pay();

    /**
     * 扫码支付方式
     */
    String getQrPayMode();

    /**
     * 
     * @Description 扩展如Unionpay等其他不在上列参数之内的参数
     * @return
     * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
     * @version 2016-11-16
     */
    Map<String, Object> getRequestParams();

    /**
     * @Description
     * @param requestParams
     * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
     * @version 2016-11-16
     */
    void setRequestParams(Map<String, Object> requestParams);

}
