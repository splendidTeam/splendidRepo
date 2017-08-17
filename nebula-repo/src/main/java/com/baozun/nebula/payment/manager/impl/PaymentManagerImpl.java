package com.baozun.nebula.payment.manager.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.OnLinePaymentCommand;
import com.baozun.nebula.payment.convert.PayParamCommandAdaptor;
import com.baozun.nebula.payment.convert.PaymentConvertFactory;
import com.baozun.nebula.payment.manager.PaymentManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
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
import com.baozun.nebula.utilities.integration.payment.wechat.WechatUtil;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;

@Service("PaymentManager")
public class PaymentManagerImpl implements PaymentManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentManagerImpl.class);

    //---------------------------------------------------------------------

    @Deprecated
    @Override
    public PaymentRequest createPayment(SalesOrderCommand salesOrderCommand){
        Map<String, Object> additionParams = PropertyUtil.describe(salesOrderCommand);
        additionParams.putAll(PropertyUtil.describe(salesOrderCommand.getOnLinePaymentCommand()));

        PaymentRequest paymentRequest = createPayment(additionParams, salesOrderCommand.getOnLinePaymentCommand().getPayType());
        Validate.notNull(paymentRequest, "paymentRequest can't be null!");

        return paymentRequest;
    }

    /**
     * <h3>
     * 建议用于通用支付接口调用，启用原createPayment(SalesOrderCommand order)方法。</br>
     * SalesOrderCommand对象具有不易于扩展性，主要耦合基于原始商城订单支付，难以兼容shopdog或同一支付接口，不同形式调用（比如支付宝PC支付，还可以直接二维码支付）</br>
     * 如需扩展参数，可以直接注入additionParams中，拼接paymentURL时会将MAP中所有参数带上
     * </h3>
     * 
     * @param orderParams
     *            调用前可将SalesOrderCommand使用SalesOrderCommandToPaymentParamsConverter.convert(salesOrderCommand)方法转亦成Map
     * @param payType
     *            即SalesOrderCommand.getOnLinePaymentCommand().getPayType()</br>
     * @return PaymentRequest
     * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
     * @version 2016-11-29
     */
    @Override
    public PaymentRequest createPayment(Map<String, Object> orderParams,Integer payType){
        Validate.notEmpty(orderParams, "orderParams can't be null/empty!");
        Validate.notNull(payType, "payType can't be null!");

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("orderParams:{}", JsonUtil.format(orderParams));
        }

        //---------------------------------------------------------------------

        PaymentFactory paymentFactory = PaymentFactory.getInstance();
        String type = paymentFactory.getPayType(payType);

        PayParamCommandAdaptor payParamCommandAdaptor = PaymentConvertFactory.getInstance().getConvertAdaptor(type);
        payParamCommandAdaptor.setSalesOrderCommand(null);
        payParamCommandAdaptor.setRequestParams(orderParams);

        // 获得对应的参数转换器
        PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(type);

        try{

            Map<String, String> params = payParamConvertorAdaptor.commandConvertorToMapForCreatUrl(payParamCommandAdaptor);
            // 將支付所需的定制参数赋值给addition
            payParamConvertorAdaptor.extendCommandConvertorMap(params, orderParams);

            // 获得支付适配器
            PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(type);
            return paymentAdaptor.newPaymentRequest(RequestParam.HTTP_TYPE_GET, params);

        }catch (Exception ex){
            LOGGER.error("CreatePayment error: " + ex.toString(), ex);
            return null;
        }
    }

    @Override
    public PaymentResult getPaymentResultForSyn(HttpServletRequest request,String paymentType){
        PaymentFactory paymentFactory = PaymentFactory.getInstance();
        PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);// 获得支付适配器
        return paymentAdaptor.getPaymentResult(request);
    }

    @Override
    public PaymentResult getPaymentResultForAsy(HttpServletRequest request,String paymentType){
        PaymentFactory paymentFactory = PaymentFactory.getInstance();
        PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);// 获得支付适配器
        return paymentAdaptor.getPaymentResultFromNotification(request);
    }

    @Override
    public PaymentResult cancelPayment(SalesOrderCommand order){
        PaymentResult paymentResult = new PaymentResult();
        try{
            PaymentFactory paymentFactory = PaymentFactory.getInstance();
            PaymentConvertFactory paymentConvertFactory = PaymentConvertFactory.getInstance();
            PayParamCommandAdaptor payParamCommandAdaptor = paymentConvertFactory.getConvertAdaptor(paymentFactory.getPayType(order.getOnLinePaymentCancelCommand().getPayType()));
            payParamCommandAdaptor.setSalesOrderCommand(order);
            Map<String, Object> orderParams = PropertyUtil.describe(order);
            orderParams.putAll(PropertyUtil.describe(order.getOnLinePaymentCancelCommand()));
            payParamCommandAdaptor.setRequestParams(orderParams);

            // 获得支付适配器
            PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());
            if (paymentAdaptor.isSupportClosePaymentRequest()){

                // 获得对应的参数转换器
                PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());// 获得对应的参数转换器
                Map<String, String> params = payParamConvertorAdaptor.commandConvertorToMapForCaneclOrder(payParamCommandAdaptor);
                // 將支付所需的定制参数赋值给addition
                payParamConvertorAdaptor.extendCommandConvertorMap(params, orderParams);
                paymentResult = paymentAdaptor.closePaymentRequest(params);
            }else{
                paymentResult.setMessage("not support");
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.NOT_SUPPORT);
            }
        }catch (Exception ex){
            LOGGER.error("cancelPayment error: {}", ex);
            paymentResult.setMessage(ex.toString());
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
        }

        if (LOGGER.isInfoEnabled()){
            LOGGER.info(JsonUtil.format(paymentResult));
        }

        return paymentResult;
    }

    /**
     * <h2>订单状态查询</h2>
     * 
     * <p>注：wechatpay调用该方法查询微信支付状态时，可以按API中transaction_id或者out_trade_no维度查询订单状态。（当两个值都赋值transaction_id优先于out_trade_no）<br>
     * 但该方法实现调用中，payParamConvertorAdaptor.commandConvertorToMapForOrderInfo(payParamCommandAdaptor)的实现类PayParamConvertorForWechatAdaptor
     * 将payParamCommand.getOrderNo()(即同payParamCommand.getRequestParams().get("code")，其实二者为一个值)，同时赋值给了transaction_id和out_trade_no的value。<br>
     * 导致该方法实现查询wechatpay支付状态只能如上所述优先按transaction_id，查询微信支付状态。不能用于按out_trade_no查询微信支付状态。
     * 因无法兼容升级，故在此JavaDoc说明。</p>
     * 
     * @param order
     * @return
     */
    @Override
    public PaymentResult getOrderInfo(SalesOrderCommand salesOrderCommand){
        PaymentResult result = new PaymentResult();
        try{
            PaymentFactory paymentFactory = PaymentFactory.getInstance();
            PaymentConvertFactory paymentConvertFactory = PaymentConvertFactory.getInstance();
            PayParamCommandAdaptor payParamCommandAdaptor = paymentConvertFactory.getConvertAdaptor(paymentFactory.getPayType(salesOrderCommand.getOnLinePaymentCancelCommand().getPayType()));
            payParamCommandAdaptor.setSalesOrderCommand(salesOrderCommand);
            Map<String, Object> orderParams = PropertyUtil.describe(salesOrderCommand);
            orderParams.putAll(PropertyUtil.describe(salesOrderCommand.getOnLinePaymentCommand()));
            payParamCommandAdaptor.setRequestParams(orderParams);

            PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());// 获得支付适配器
            PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());// 获得对应的参数转换器
            Map<String, String> params = payParamConvertorAdaptor.commandConvertorToMapForOrderInfo(payParamCommandAdaptor);
            // 將支付所需的定制参数赋值给addition
            payParamConvertorAdaptor.extendCommandConvertorMap(params, orderParams);
            result = paymentAdaptor.getOrderInfo(params);
        }catch (Exception ex){
            LOGGER.error("getOrderInfo error: " + ex.toString(), ex);
            result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            result.setMessage(ex.toString());
        }
        return result;
    }

    //---------------------------------------------------------------------

    @Override
    public PaymentRequest createPaymentForWap(SalesOrderCommand salesOrderCommand){
        Validate.notNull(salesOrderCommand, "salesOrderCommand can't be null!");

        OnLinePaymentCommand onLinePaymentCommand = salesOrderCommand.getOnLinePaymentCommand();
        Validate.notNull(onLinePaymentCommand, "onLinePaymentCommand can't be null!");

        Integer payType = onLinePaymentCommand.getPayType();
        Validate.notNull(payType, "payType can't be null!");

        //---------------------------------------------------------------------

        PaymentFactory paymentFactory = PaymentFactory.getInstance();
        PaymentConvertFactory paymentConvertFactory = PaymentConvertFactory.getInstance();
        String payType2 = paymentFactory.getPayType(payType);

        PayParamCommandAdaptor payParamCommandAdaptor = paymentConvertFactory.getConvertAdaptor(payType2);
        payParamCommandAdaptor.setSalesOrderCommand(salesOrderCommand);

        //---------------------------------------------------------------------

        // 获得支付适配器
        PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(payParamCommandAdaptor.getPaymentType());
        payParamCommandAdaptor.setRequestParams(PropertyUtil.describe(salesOrderCommand));
        // 获得对应的参数转换器
        PayParamConvertorAdaptor payParamConvertorAdaptor = paymentFactory.getPaymentCommandToMapAdaptor(payParamCommandAdaptor.getPaymentType());

        //---------------------------------------------------------------------
        try{
            Map<String, String> addition = payParamConvertorAdaptor.commandConvertorToMapForMobileCreatUrl(payParamCommandAdaptor);
            // 创建一个新的MOBILE端授权请求
            PaymentRequest paymentRequest = paymentAdaptor.newPaymentRequestForMobileCreateDirect(addition);
            LOGGER.info("RequestForMobileCreateDirect:{}", paymentRequest.getRequestURL());

            // Alipay WAP 支付需要拼接完参数之后，再获取一次token
            return paymentAdaptor.getCreateResponseToken(paymentRequest);
        }catch (Exception ex){
            LOGGER.error("CreatePayment error: " + ex.toString(), ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * mobile同步回调获取返回支付结果<br>
     * 
     * <h3>
     * 注意事项：<br>
     * 该方法的AbstractAlipayPaymentAdaptor实现具有一个问题：<br>
     * 当支付成功后，PaymentResult.PaymentServiceSatus返回值为PaymentServiceStatus.SUCCESS<br>
     * 不同于支付宝同一类产品其他的接口成功返回值为PaymentServiceStatus.PAYMENT_SUCCESS<br>
     * 这样会导致外层判断无法一致！且需要另行单独判断Mobile 同步回调返回值！<br>
     * <h3>
     * 
     * @param request
     * @param paymentType
     * @return
     */
    @Override
    public PaymentResult getPaymentResultForSynOfWap(HttpServletRequest request,String paymentType){
        PaymentFactory paymentFactory = PaymentFactory.getInstance();
        // 获得支付适配器
        PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);
        PaymentResult result = new PaymentResult();
        result = paymentAdaptor.getPaymentResultForMobileAuthAndExecuteSYN(request);
        return result;
    }

    @Override
    public PaymentResult getPaymentResultForAsyOfWap(HttpServletRequest request,String paymentType){
        PaymentFactory paymentFactory = PaymentFactory.getInstance();
        // 获得支付适配器
        PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentType);
        PaymentResult result = new PaymentResult();
        result = paymentAdaptor.getPaymentResultForMobileAuthAndExecuteASY(request);
        return result;
    }

    @Override
    public PaymentResult unifiedOrder(WechatPayParamCommand wechatPayParamCommand,String paymentType){
        PaymentResult paymentResult = new PaymentResult();
        try{
            PaymentFactory paymentFactory = PaymentFactory.getInstance();
            // 获得支付适配器
            PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(paymentFactory.getPayType(Integer.valueOf(paymentType)));
            Map<String, String> addition = new HashMap<String, String>();
            getUnifiedOrderParaMap(wechatPayParamCommand, addition);
            return paymentAdaptor.unifiedOrder(addition);

        }catch (PaymentParamErrorException e){
            LOGGER.error("unifiedOrder error: " + e.toString());
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setMessage(e.toString());
        }catch (Exception ex){
            LOGGER.error("unifiedOrder error: " + ex.toString());
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setMessage(ex.toString());
        }
        return paymentResult;
    }

    private void getUnifiedOrderParaMap(WechatPayParamCommand wechatPayParamCommand,Map<String, String> addition) throws PaymentParamErrorException{
        addition.put("appid", WechatConfig.APP_ID);
        addition.put("mch_id", WechatConfig.PARTNER_ID);
        addition.put("nonce_str", WechatUtil.generateRandomString());
        addition.put("notify_url", WechatConfig.JS_API_PAYMENT_CALLBACK_URL);

        if (isNotNullOrEmpty(wechatPayParamCommand.getDevice_info())){
            addition.put("device_info", wechatPayParamCommand.getDevice_info());
        }

        if (isNotNullOrEmpty(wechatPayParamCommand.getBody())){
            addition.put("body", wechatPayParamCommand.getBody());
        }else{
            throw new PaymentParamErrorException("unifiedOrder parameter error: body can't be null/empty!");
        }

        if (isNotNullOrEmpty(wechatPayParamCommand.getDetail())){
            addition.put("detail", wechatPayParamCommand.getDetail());
        }

        if (isNotNullOrEmpty(wechatPayParamCommand.getOut_trade_no())){
            addition.put("out_trade_no", wechatPayParamCommand.getOut_trade_no());
        }else{
            throw new PaymentParamErrorException("unifiedOrder parameter error: out_trade_no can't be null/empty!");
        }

        if (isNotNullOrEmpty(wechatPayParamCommand.getFee_type())){
            addition.put("fee_type", wechatPayParamCommand.getFee_type());
        }

        if (isNotNullOrEmpty(wechatPayParamCommand.getAttach())){
            addition.put("attach", wechatPayParamCommand.getAttach());
        }

        if (isNotNullOrEmpty(wechatPayParamCommand.getTotal_fee())){
            addition.put("total_fee", wechatPayParamCommand.getTotal_fee().toString());
        }else{
            throw new PaymentParamErrorException("unifiedOrder parameter error: total_fee can't be null/empty!");
        }
        if (isNotNullOrEmpty(wechatPayParamCommand.getSpbill_create_ip())){
            addition.put("spbill_create_ip", wechatPayParamCommand.getSpbill_create_ip());
        }else{
            throw new PaymentParamErrorException("unifiedOrder parameter error: spbill_create_ip can't be null/empty!");
        }
        if (isNotNullOrEmpty(wechatPayParamCommand.getTime_start())){
            addition.put("time_start", wechatPayParamCommand.getTime_start());
        }
        if (isNotNullOrEmpty(wechatPayParamCommand.getTime_expire())){
            addition.put("time_expire", wechatPayParamCommand.getTime_expire());
        }
        if (isNotNullOrEmpty(wechatPayParamCommand.getTrade_type())){
            addition.put("trade_type", wechatPayParamCommand.getTrade_type());
        }else{
            throw new PaymentParamErrorException("unifiedOrder parameter error: trade_type can't be null/empty!");
        }
        if (isNotNullOrEmpty(wechatPayParamCommand.getGoods_tag())){
            addition.put("goods_tag", wechatPayParamCommand.getGoods_tag());
        }

        if (isNotNullOrEmpty(wechatPayParamCommand.getProduct_id())){
            addition.put("product_id", wechatPayParamCommand.getProduct_id());
        }else{
            if (wechatPayParamCommand.getTrade_type().equals(WechatResponseKeyConstants.TRADE_TYPE_NATIVE)){
                throw new PaymentParamErrorException("unifiedOrder parameter error: when the trade_type value is 'NATIVE' product_id can't be null/empty!");
            }
        }

        if (isNotNullOrEmpty(wechatPayParamCommand.getOpenid())){
            addition.put("openid", wechatPayParamCommand.getOpenid());
        }else{
            if (wechatPayParamCommand.getTrade_type().equals(WechatResponseKeyConstants.TRADE_TYPE_JSAPI)){
                throw new PaymentParamErrorException("unifiedOrder parameter error: when the trade_type value is 'JSAPI' openid can't be null/empty!");
            }
        }

    }

}
