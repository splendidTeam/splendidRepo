package com.baozun.nebula.payment.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.common.command.WechatPayParamCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;

public interface PaymentManager{

    /**
     * 创建支付链接
     */
    PaymentRequest createPayment(SalesOrderCommand order);

    //-------------------------------------------------------------------

    /**
     * 同步方式获取返回结果
     */
    PaymentResult getPaymentResultForSyn(HttpServletRequest request,String paymentType);

    /**
     * 异步方式获取返回结果
     */
    PaymentResult getPaymentResultForAsy(HttpServletRequest request,String paymentType);

    /**
     * 创建手机支付链接
     * 
     * @param salesOrderCommand
     *            参数取值对应说明（支付宝参数名--实体类属性）:
     *            out_trade_no -- salesOrderCommand的code
     *            req_id -- salesOrderCommand的omsCode
     *            total_fee --salesOrderCommand的total
     *            out_user --salesOrderCommand的name
     *            pay_expire --salesOrderCommand中对象OnLinePaymentCommand的ItBPay
     * @return
     */
    PaymentRequest createPaymentForWap(SalesOrderCommand salesOrderCommand);

    /**
     * 同步方式获取返回结果
     */
    PaymentResult getPaymentResultForSynOfWap(HttpServletRequest request,String paymentType);

    /**
     * 异步方式获取返回结果
     */
    PaymentResult getPaymentResultForAsyOfWap(HttpServletRequest request,String paymentType);

    //-------------------------------------------------------------------

    /**
     * 取消订单
     */
    PaymentResult cancelPayment(SalesOrderCommand order);

    /**
     * 订单状态查询
     */
    PaymentResult getOrderInfo(SalesOrderCommand order);

    /**
     * 微信统一下单
     * 
     * @param order
     * @return
     */
    PaymentResult unifiedOrder(WechatPayParamCommand wechatPayParamCommand,String paymentType);

    /**
     * 
     * @Description
     *              <p>
     *              建议用于通用支付接口调用，启用原createPayment(SalesOrderCommand order)方法。</br>
     *              SalesOrderCommand对象具有不易于扩展性，主要耦合基于原始商城订单支付，难以兼容shopdog或同一支付接口，不同形式调用（比如支付宝PC支付，还可以直接二维码支付）</br>
     *              如需扩展参数，可以直接注入additionParams中，拼接paymentURL时会将MAP中所有参数带上
     *              </p>
     * @param additionParams
     *            调用前可将SalesOrderCommand使用SalesOrderCommandToPaymentParamsConverter.convert(salesOrderCommand)方法转亦成Map
     * @param payType
     *            即SalesOrderCommand.getOnLinePaymentCommand().getPayType()</br>
     * @return PaymentRequest
     * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
     * @version 2016-11-29
     */
    PaymentRequest createPayment(Map<String, Object> orderParams,Integer payType);

}
