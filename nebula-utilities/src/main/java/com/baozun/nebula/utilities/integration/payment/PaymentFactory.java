package com.baozun.nebula.utilities.integration.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayBankPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayCreditCardPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayInternationalCreditCardPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.alipay.AlipayPaymentRequest;
import com.baozun.nebula.utilities.integration.payment.convertor.PayParamConvertorAdaptor;
import com.baozun.nebula.utilities.integration.payment.convertor.PayParamConvertorForAlipayAdaptor;
import com.baozun.nebula.utilities.integration.payment.convertor.PayParamConvertorForUnionPayAdaptor;
import com.baozun.nebula.utilities.integration.payment.convertor.PayParamConvertorForWechatAdaptor;
import com.baozun.nebula.utilities.integration.payment.unionpay.UnionPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.unionpay.UnionPaymentRequest;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatPaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatPaymentRequest;

public class PaymentFactory{

    private static final Logger logger = LoggerFactory.getLogger(PaymentFactory.class);

    //--------------------------------------------------------------------------------------------------------------
    /** 支付宝 */
    public static final int ALIPAY = 1;

    /** 支付宝网银 */
    public static final int ALIPAY_BANK = 3;

    /** 支付宝信用卡 */
    public static final int ALIPAY_CREDIT = 14;

    /** 支付宝国际卡 */
    public static final int ALIPAY_CREDIT_INT_V = 131;

    /** 支付宝国际卡 */
    public static final int ALIPAY_CREDIT_INT_M = 141;

    /** 微信支付 */
    public static final int WECHAT = 4;

    /** 银联支付 */
    public static final int UNIONPAY = 161;

    //--------------------------------------------------------------------------------------------------------------

    public static final String PAY_TYPE_ALIPAY = "Alipay";

    public static final String PAY_TYPE_ALIPAY_BANK = "Alipay_Bank";

    public static final String PAY_TYPE_ALIPAY_CREDIT = "Alipay_Credit";

    public static final String PAY_TYPE_ALIPAY_CREDIT_INT = "Alipay_Credit_Int";

    public static final String PAY_TYPE_UNIONPAY = "unionpay";

    public static final String PAY_TYPE_WECHAT = "Wechat";

    //--------------------------------------------------------------------------------------------------------------

    public static final String ALIPAYDEFAULTALIPAYCONFIG = "config/alipay.properties";

    public static final String ALIPAYINTERNATIONALCREDITCARDCONFIG = "config/payment/alipay/alipay_InternationalCreditCard.properties";

    public static final String ALIPAYADDRESS = "config/payment/alipay/alipayAddress.properties";

    /**
     * @deprecated 目前银联配置文件格式参见 com.baozun.nebula.utilities.integration.payment.unionpay.AbstractUnionPaymentAdaptor.AbstractUnionPaymentAdaptor()
     */
    @Deprecated
    public static final String UNIONPAYDEFAULTALIPAYCONFIG = "config/payment/unionpay/unionpay.properties";

    //--------------------------------------------------------------------------------------------------------------

    private static PaymentFactory inst = new PaymentFactory();

    //--------------------------------------------------------------------------------------------------------------

    private Map<String, String> paymentConfigMap;

    private Map<String, PaymentAdaptor> paymentAdaptorMap;

    private Map<String, PayParamConvertorAdaptor> payParamCommandToMapAdaptorMap;

    /**
     * @deprecated 没有被调用到 <br>
     *             add javadoc by jinxin (2017年6月1日 上午11:03:07)
     */
    @Deprecated
    private Map<String, PaymentRequest> paymentResultMap;

    //--------------------------------------------------------------------------------------------------------------

    public PaymentFactory(){
        initPaymentConfig();
        initPaymentAdaptorMap();
        initPayParamCommandToMapAdaptorMap();
        initPayParamPaymentResultMap();
    }

    public static PaymentFactory getInstance(){
        return inst;
    }

    //--------------------------------------------------------------------------------------------------------------

    /**
     * 获得支付适配器
     * 
     * @param payMentType
     * @return
     */
    public PaymentAdaptor getPaymentAdaptor(String payMentType){
        Validate.notBlank(payMentType, "payMentType can't be blank!");
        return paymentAdaptorMap.get(payMentType);

    }

    /**
     * 获得转换器
     * 
     * @param payMentType
     * @return
     */
    public PayParamConvertorAdaptor getPaymentCommandToMapAdaptor(String payMentType){
        Validate.notBlank(payMentType, "payMentType can't be blank!");
        return payParamCommandToMapAdaptorMap.get(payMentType);

    }

    /**
     * 获得支付结果
     * 
     * @param payMentType
     * @return
     * @deprecated 没有被调用到 <br>
     *             add javadoc by jinxin (2017年6月1日 上午11:03:07)
     */
    @Deprecated
    public PaymentRequest getPaymentResult(String payMentType){
        Validate.notBlank(payMentType, "payMentType can't be blank!");
        return paymentResultMap.get(payMentType);

    }

    //--------------------------------------------------------------------------------------------------------------

    /**
     * 初始化转换器
     */
    public void initPayParamCommandToMapAdaptorMap(){
        payParamCommandToMapAdaptorMap = new HashMap<>();
        payParamCommandToMapAdaptorMap.put(PAY_TYPE_ALIPAY, new PayParamConvertorForAlipayAdaptor());
        payParamCommandToMapAdaptorMap.put(PAY_TYPE_ALIPAY_BANK, new PayParamConvertorForAlipayAdaptor());
        payParamCommandToMapAdaptorMap.put(PAY_TYPE_ALIPAY_CREDIT, new PayParamConvertorForAlipayAdaptor());
        payParamCommandToMapAdaptorMap.put(PAY_TYPE_ALIPAY_CREDIT_INT, new PayParamConvertorForAlipayAdaptor());
        payParamCommandToMapAdaptorMap.put(PAY_TYPE_UNIONPAY, new PayParamConvertorForUnionPayAdaptor());
        payParamCommandToMapAdaptorMap.put(PAY_TYPE_WECHAT, new PayParamConvertorForWechatAdaptor());
    }

    /**
     * 支付结果列表
     * 
     * @deprecated 没有被调用到 <br>
     *             add javadoc by jinxin (2017年6月1日 上午11:03:07)
     */
    @Deprecated
    public void initPayParamPaymentResultMap(){
        paymentResultMap = new HashMap<>();
        paymentResultMap.put(PAY_TYPE_ALIPAY, new AlipayPaymentRequest());
        paymentResultMap.put(PAY_TYPE_ALIPAY_BANK, new AlipayPaymentRequest());
        paymentResultMap.put(PAY_TYPE_ALIPAY_CREDIT, new AlipayPaymentRequest());
        paymentResultMap.put(PAY_TYPE_ALIPAY_CREDIT_INT, new AlipayPaymentRequest());
        paymentResultMap.put(PAY_TYPE_UNIONPAY, new UnionPaymentRequest());
        paymentResultMap.put(PAY_TYPE_WECHAT, new WechatPaymentRequest());
    }

    /*
     * 初始化配置文件
     */
    private void initPaymentConfig(){
        paymentConfigMap = new HashMap<>();
        paymentConfigMap.put(PAY_TYPE_ALIPAY, ProfileConfigUtil.getProfilePath(ALIPAYDEFAULTALIPAYCONFIG));
        paymentConfigMap.put(PAY_TYPE_ALIPAY_BANK, ALIPAYDEFAULTALIPAYCONFIG);
        paymentConfigMap.put(PAY_TYPE_ALIPAY_CREDIT, ALIPAYDEFAULTALIPAYCONFIG);
        paymentConfigMap.put(PAY_TYPE_ALIPAY_CREDIT_INT, ALIPAYINTERNATIONALCREDITCARDCONFIG);
        paymentConfigMap.put(PAY_TYPE_UNIONPAY, UNIONPAYDEFAULTALIPAYCONFIG);
    }

    //--------------------------------------------------------------------------------------------------------------
    /*
     * 初始化配置参数
     */
    public Properties initConfig(String payMentType){
        String configFile = paymentConfigMap.get(payMentType);
        return ProfileConfigUtil.findCommonPro(configFile);
    }

    //--------------------------------------------------------------------------------------------------------------

    /*
     * 初始化适配器Map
     */
    private Map<String, PaymentAdaptor> initPaymentAdaptorMap(){
        paymentAdaptorMap = new HashMap<>();
        paymentAdaptorMap.put(PAY_TYPE_ALIPAY, initAlipayPaymentAdaptor());
        paymentAdaptorMap.put(PAY_TYPE_ALIPAY_BANK, initAlipayBankPaymentAdaptor());
        paymentAdaptorMap.put(PAY_TYPE_ALIPAY_CREDIT, initAlipayCreditCardPaymentAdaptor());
        paymentAdaptorMap.put(PAY_TYPE_ALIPAY_CREDIT_INT, initAlipayInternationalCreditCardPaymentAdaptor());
        paymentAdaptorMap.put(PAY_TYPE_UNIONPAY, initUnionPaymentAdaptor());
        paymentAdaptorMap.put(PAY_TYPE_WECHAT, initWechatPaymentAdaptor());
        return paymentAdaptorMap;
    }

    //--------------------------------------------------------------------------------------------------------------

    /*
     * 初始化Alipay适配器
     */
    private PaymentAdaptor initAlipayPaymentAdaptor(){
        return new AlipayPaymentAdaptor(initConfig(PAY_TYPE_ALIPAY));
    }

    /*
     * 初始化Alipay银行卡适配器
     */
    private PaymentAdaptor initAlipayBankPaymentAdaptor(){
        return new AlipayBankPaymentAdaptor(initConfig(PAY_TYPE_ALIPAY));
    }

    /*
     * 初始化Alipay国内信用卡适配器
     */
    private PaymentAdaptor initAlipayCreditCardPaymentAdaptor(){
        return new AlipayCreditCardPaymentAdaptor(initConfig(PAY_TYPE_ALIPAY));
    }

    /*
     * 初始化Alipay国外信用卡适配器
     */
    private PaymentAdaptor initAlipayInternationalCreditCardPaymentAdaptor(){
        return new AlipayInternationalCreditCardPaymentAdaptor(initConfig(PAY_TYPE_ALIPAY_CREDIT_INT));
    }

    /*
     * 初始化银联适配器
     */
    private PaymentAdaptor initUnionPaymentAdaptor(){
        //不需要传配置参数, 内部已经实现了
        return new UnionPaymentAdaptor();
    }

    /*
     * 初始化Wechat适配器
     */
    private PaymentAdaptor initWechatPaymentAdaptor(){
        return new WechatPaymentAdaptor();
    }

    //--------------------------------------------------------------------------------------------------------------

    public String getPayType(Integer payType){
        String type = PaymentFactory.PAY_TYPE_ALIPAY;
        switch (payType) {
            case ALIPAY:
                type = PaymentFactory.PAY_TYPE_ALIPAY;
                break;
            case ALIPAY_BANK:
                type = PaymentFactory.PAY_TYPE_ALIPAY_BANK;
                break;
            case ALIPAY_CREDIT:
                type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT;
                break;
            case ALIPAY_CREDIT_INT_V:
                type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;
                break;
            case ALIPAY_CREDIT_INT_M:
                type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;
                break;
            case WECHAT:
                type = PaymentFactory.PAY_TYPE_WECHAT;
                break;
            case UNIONPAY:
                type = PaymentFactory.PAY_TYPE_UNIONPAY;
                break;
        }
        return type;
    }
}
