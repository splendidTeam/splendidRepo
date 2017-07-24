package com.baozun.nebula.payment.manager.impl;

import static com.baozun.nebula.sdk.constants.Constants.PAY_LOG_NOTIFY_AFTER_MESSAGE;
import static com.baozun.nebula.sdk.constants.Constants.PAY_LOG_RETURN_AFTER_MESSAGE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.BeforePaymentCancelOrderCommand;
import com.baozun.nebula.command.OnLinePaymentCancelCommand;
import com.baozun.nebula.command.OnLinePaymentCommand;
import com.baozun.nebula.command.wechat.WechatJsApiConfigCommand;
import com.baozun.nebula.command.wechat.WechatJsApiPayCommand;
import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.dao.payment.PayInfoLogDao;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.event.PayWarnningEvent;
import com.baozun.nebula.event.PaymentEvent;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.payment.PayWarnningLog;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.payment.manager.PayManager;
import com.baozun.nebula.payment.manager.PaymentManager;
import com.baozun.nebula.payment.manager.PaymentResultSuccessUpdateManager;
import com.baozun.nebula.payment.manager.ReservedPaymentType;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.handler.SalesOrderHandler;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoQueryManager;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCouponManager;
import com.baozun.nebula.sdk.utils.MapConvertUtils;
import com.baozun.nebula.utilities.common.WechatUtil;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatConfig;
import com.feilong.core.Validator;

import static com.feilong.core.Validator.isNotNullOrEmpty;

@Transactional
@Service("paymentManager")
public class PayManagerImpl implements PayManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(PayManagerImpl.class);

    @Value("#{meta['page.base']}")
    private String pageUrlBase = "";

    @Value("#{meta['upload.img.domain.base']}")
    private String imgDomainUrl = "";

    public static final String MEMEBER_CANCEL = "buy";

    public static final String SHOP_CANCEL = "seller";

    public static final String SIGNTYPE = "MD5";

    private static final String SHA_1 = "SHA-1";

    public static final String TRADE_NOT_EXIST = "TRADE_NOT_EXIST";

    public static final String TRADE_STATUS_NOT_AVAILD = "TRADE_STATUS_NOT_AVAILD";

    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    public static final String TRADE_REFUSE = "TRADE_REFUSE";//立即支付交易拒绝

    public static final String TRADE_CANCEL = "TRADE_CANCEL";//交易取消

    public static final String TRADE_CLOSED = "TRADE_CLOSED";//交易中途取消

    public static final String DAEMON_CONFIRM_CLOSE = "DAEMON_CONFIRM_CLOSE"; //超时程序因买家不付款关闭交易

    private static final String USERPAYING = "USERPAYING";

    private static final String REVOKED = "REVOKED";

    private static final String SUCCESS = "SUCCESS";

    private static final String ORDERNOTEXIST = "ORDERNOTEXIST";

    //---------------------------------------------------------------------

    @Autowired
    private SdkPaymentManager sdkPaymentManager;

    @Autowired
    private OrderManager sdkOrderService;

    @Autowired
    private SdkPromotionCouponManager sdkPromotionCouponManager;

    @Autowired
    private SdkSkuManager sdkSkuManager;

    @Autowired
    private PayInfoLogDao payInfoLogDao;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private PaymentManager paymentManager;

    @Autowired
    private SdkOrderDao sdkOrderDao;

    @Autowired
    private PaymentResultSuccessUpdateManager paymentResultSuccessUpdateManager;

    @Autowired
    private SdkMataInfoManager sdkMataInfoManager;

    @Autowired
    private SdkPayInfoQueryManager sdkPayInfoQueryManager;

    @Autowired(required = false)
    private SalesOrderHandler salesOrderHandler;

    //---------------------------------------------------------------------

    @Override
    public void savePayInfos(SalesOrderCommand so,PaymentRequest paymentRequest,String operator){
        Validate.notNull(so, "so can't be null!");
        Validate.notNull(paymentRequest, "paymentRequest can't be null!");

        //---------------------------------------------------------------------

        savePayLog(new Date(), Constants.PAY_LOG_CALL_AFTER_MESSAGE, operator, MapConvertUtils.transPaymentRequestToString(paymentRequest));

        //---------------------------------------------------------------------
        //更新支付相关
        //更新t_so_payinfo
        String subOrdinate = so.getCode();

        //---------------------------------------------------------------------

        //查询订单的需要支付的payInfolog
        List<PayInfoLog> payInfoLogList = sdkPayInfoQueryManager.findPayInfoLogListBySubOrdinate(subOrdinate, false);

        if (isNotNullOrEmpty(payInfoLogList)){
            for (PayInfoLog payInfoLog : payInfoLogList){
                payInfoLog.setPaymentPeople(operator);
                payInfoLogDao.save(payInfoLog);
            }
        }

        //---------------------------------------------------------------------

        //更新t_so_paycode
        sdkPaymentManager.updatePayCodeBySubOrdinate(subOrdinate, so.getOnLinePaymentCommand().getPayType());
    }

    /**
     * 保存支付日志
     * 
     * @param createTime
     * @param message
     * @param operator
     * @param returnVal
     */
    private void savePayLog(Date createTime,String message,String operator,String returnVal){
        sdkPaymentManager.savePaymentLog(createTime, message, operator, returnVal);
    }

    @Override
    public void updatePayInfos(PaymentResult paymentResult,String operator,Integer payType,boolean flag,HttpServletRequest request){
        String transPaymentResultToString = MapConvertUtils.transPaymentResultToString(paymentResult);
        String message = flag ? PAY_LOG_RETURN_AFTER_MESSAGE : PAY_LOG_NOTIFY_AFTER_MESSAGE;

        savePayLog(new Date(), message, operator, transPaymentResultToString);

        //---------------------------------------------------------------------
        PaymentServiceStatus paymentServiceStatus = paymentResult.getPaymentServiceSatus();
        if (paymentServiceStatus == PaymentServiceStatus.PAYMENT_SUCCESS){
            //更新支付信息
            paymentResultSuccessUpdateManager.updateSuccess(paymentResult.getPaymentStatusInformation(), payType);
        }
    }

    @Override
    public void savePaymentResultPaymentLog(PaymentResult paymentResult,String operator,Integer type){
        //TODO 不支持订单拆分
        String desc = StringUtils.EMPTY;
        switch (type) {
            case Constants.GET_URL_AFTER_TYPE:
                desc = Constants.PAY_LOG_CALL_AFTER_MESSAGE;
                break;
            case Constants.DO_RETURN_AFTER_TYPE:
                desc = Constants.PAY_LOG_RETURN_AFTER_MESSAGE;
                break;
            case Constants.DO_NOTIFY_AFTER_TYPE:
                desc = Constants.PAY_LOG_NOTIFY_AFTER_MESSAGE;
                break;
            case Constants.CLOSE_AFTER_TYPE:
                desc = Constants.PAY_LOG_CLOSE_PAYMENT_AFTER_MESSAGE;
                break;
            default:
                break;
        }
        savePayLog(new Date(), desc, operator, MapConvertUtils.transPaymentResultToString(paymentResult));
    }

    @Override
    public boolean isSupportClosePaymentRequest(){
        PaymentFactory payFactory = PaymentFactory.getInstance();
        PaymentAdaptor paymentAdaptor = payFactory.getPaymentAdaptor(PaymentFactory.PAY_TYPE_ALIPAY);
        return paymentAdaptor.isSupportClosePaymentRequest();
    }

    /*
     * 此方法不再建议使用,请使用:含oms参数的closeTradeUpdateOrderStatus() 因关系到库存释放
     * 
     * @see com.baozun.nebula.payment.manager.PayManager#closeTradeUpdateOrderStatus(java.lang.Long, com.baozun.nebula.utilities.integration.payment.PaymentResult, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void closeTradeUpdateOrderStatus(Long orderId,PaymentResult paymentResult,String operator,String orderNo,String userType,String payCode){
        /** 扩展点 修改外部code **/
        if (null != salesOrderHandler){
            salesOrderHandler.updateOutCoupon(orderNo, PromotionCouponCode.NOT_USED);
        }
        if (MEMEBER_CANCEL.equals(userType) || SHOP_CANCEL.equals(userType)){
            savePaymentResultPaymentLog(paymentResult, operator, Constants.CLOSE_AFTER_TYPE);
            Integer logisticsStatus = SalesOrder.SALES_ORDER_STATUS_CANCELED;//用户取消
            if (SHOP_CANCEL.equals(userType)){
                logisticsStatus = SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED;//商城取消
            }
            //修改该订单的callCloseStatus的状态
            sdkPaymentManager.upPayInfoCallCloseStaBySubOrdinate(orderId, true, new Date());

            //修改订单的物流状态、修改优惠券的使用状态、加库存
            cancalOrderUpdateInfo(orderId, orderNo, logisticsStatus);
        }else{
            throw new BusinessException(ErrorCodes.transaction_cancel_usertype_error);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * 多了个是否oms取消，若oms：将不释放库存
     * 
     * @see com.baozun.nebula.payment.manager.PayManager#closeTradeUpdateOrderStatusNew(java.lang.Long, com.baozun.nebula.utilities.integration.payment.PaymentResult, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.Boolean)
     */
    @Override
    public void closeTradeUpdateOrderStatus(Long orderId,PaymentResult paymentResult,String operator,String orderNo,String userType,String payCode,Boolean isOms){
        /** 扩展点 修改外部code **/
        if (null != salesOrderHandler){
            salesOrderHandler.updateOutCoupon(orderNo, PromotionCouponCode.NOT_USED);
        }
        if (MEMEBER_CANCEL.equals(userType) || SHOP_CANCEL.equals(userType)){
            savePaymentResultPaymentLog(paymentResult, operator, Constants.CLOSE_AFTER_TYPE);
            Integer logisticsStatus = SalesOrder.SALES_ORDER_STATUS_CANCELED;//用户取消
            if (SHOP_CANCEL.equals(userType)){
                logisticsStatus = SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED;//商城取消
            }
            //修改该订单的callCloseStatus的状态
            sdkPaymentManager.upPayInfoCallCloseStaBySubOrdinate(orderId, true, new Date());

            //修改订单的物流状态、修改优惠券的使用状态、加库存
            cancelOrderUpdateInfo(orderId, orderNo, logisticsStatus, isOms);
        }else{
            throw new BusinessException(ErrorCodes.transaction_cancel_usertype_error);
        }

    }

    @Override
    public void savePaymentRequestPaymentLog(PaymentRequest paymentRequest,String operator){
        savePayLog(new Date(), Constants.PAY_LOG_CALL_AFTER_MESSAGE, operator, MapConvertUtils.transPaymentRequestToString(paymentRequest));
    }

    /**
     * 此方法不再建议使用
     * (1)更新订单状态
     * (2)如果使用了优惠券则退回优惠券
     * (3)增加库存
     * 
     * @param orderId
     * @param code
     */
    @Override
    @Deprecated
    public void cancalOrderUpdateInfo(Long orderId,String code,Integer logisticsStatus){
        /** 扩展点 修改外部code **/
        if (null != salesOrderHandler){
            salesOrderHandler.updateOutCoupon(code, PromotionCouponCode.NOT_USED);
        }
        //修改订单的物流状态
        sdkOrderService.cancelOrderLogisticsStatus(code, logisticsStatus);
        //修改优惠券的使用状态
        sdkPromotionCouponManager.updatePromCouponUsedStatusByOrderCode(code, PromotionCouponCode.NOT_USED);

        //根据订单id查询出该订单的明细
        List<OrderLineCommand> orderLines = sdkOrderService.findOrderDetailList(orderId);
        if (null != orderLines && orderLines.size() > 0){
            for (OrderLineCommand orderLine : orderLines){
                //加库存
                if (orderLine.getExtentionCode() != null && orderLine.getCount() > 0){
                    sdkSkuManager.addSkuInventory(orderLine.getExtentionCode(), orderLine.getCount());
                }
            }
        }
        //发送邮件
        sdkOrderService.sendEmailOfOrder(code, EmailConstants.CANCEL_ORDER_SUCCESS);
    }

    @Override
    public void cancelOrderUpdateInfo(Long orderId,String code,Integer logisticsStatus,Boolean isOms){
        /** 扩展点 修改外部code **/
        if (null != salesOrderHandler){
            salesOrderHandler.updateOutCoupon(code, PromotionCouponCode.NOT_USED);
        }
        //修改订单的物流状态
        sdkOrderService.cancelOrderLogisticsStatus(code, logisticsStatus, isOms);
        //修改优惠券的使用状态
        sdkPromotionCouponManager.updatePromCouponUsedStatusByOrderCode(code, PromotionCouponCode.NOT_USED);

        //根据订单id查询出该订单的明细
        List<OrderLineCommand> orderLines = sdkOrderService.findOrderDetailList(orderId);
        if (isOms == null || !isOms){
            if (null != orderLines && orderLines.size() > 0){
                for (OrderLineCommand orderLine : orderLines){
                    //加库存
                    if (orderLine.getExtentionCode() != null && orderLine.getCount() > 0){
                        sdkSkuManager.addSkuInventory(orderLine.getExtentionCode(), orderLine.getCount());
                    }
                }
            }
        }
        //发送邮件
        sdkOrderService.sendEmailOfOrder(code, EmailConstants.CANCEL_ORDER_SUCCESS);

    }

    @Override
    public void cancelOrder(BeforePaymentCancelOrderCommand beforePaymentCancelOrderCommand){
        String type = "";
        if (Validator.isNotNullOrEmpty(beforePaymentCancelOrderCommand) && beforePaymentCancelOrderCommand.getCancelType() != null){
            if (beforePaymentCancelOrderCommand.getCancelType().equals(SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED)){
                type = SHOP_CANCEL;
            }else if (beforePaymentCancelOrderCommand.getCancelType().equals(SalesOrder.SALES_ORDER_STATUS_CANCELED)){
                type = MEMEBER_CANCEL;
            }else{
                throw new BusinessException(ErrorCodes.transaction_cancel_usertype_error);
            }
        }
        String orderCode = beforePaymentCancelOrderCommand.getCode();
        Long orderId = beforePaymentCancelOrderCommand.getOrderId();
        String ip = beforePaymentCancelOrderCommand.getClientIp();
        String operatorName = beforePaymentCancelOrderCommand.getOperatorName();

        //查询订单的需要支付的payInfolog
        List<PayInfoLog> payInfoLogs = sdkPayInfoQueryManager.findPayInfoLogListByOrderId(orderId, false);
        PayInfoLog payInfoLog = null;
        if (Validator.isNotNullOrEmpty(payInfoLogs)){
            payInfoLog = payInfoLogs.get(0);
        }

        SalesOrderCommand salesOrderCommand = sdkOrderDao.findOrderById(orderId, 1);

        //如果在取消之前发现已经是取消状态,则抛出异常
        if (salesOrderCommand.getLogisticsStatus().equals(SalesOrder.SALES_ORDER_STATUS_CANCELED) || salesOrderCommand.getLogisticsStatus().equals(SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED)){

            throw new BusinessException(ErrorCodes.ORDER_ITERATIVE_CANCEL);
        }

        /**
         * 直接取消场景
         * 1 非cod 但是已经成功调用取消支付宝交易接口
         * 2 OMS取消
         */
        if (null == payInfoLog || (payInfoLog.getCallCloseStatus() != null && payInfoLog.getCallCloseStatus())
                        || (beforePaymentCancelOrderCommand.getCancelType().equals(SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED) && beforePaymentCancelOrderCommand.getIsOMS())){
            //cancalOrderUpdateInfo(orderId,orderCode, beforePaymentCancelOrderCommand.getCancelType());
            cancelOrderUpdateInfo(orderId, orderCode, beforePaymentCancelOrderCommand.getCancelType(), beforePaymentCancelOrderCommand.getIsOMS());
        }else{

            String thirdPayNo = payInfoLog.getThirdPayNo();
            Date payTime = payInfoLog.getCreateTime();
            Integer payType = payInfoLog.getThirdPayType();
            String payCode = payInfoLog.getSubOrdinate();

            SalesOrderCommand so = new SalesOrderCommand();
            so.setCode(payCode);
            so.setOnLinePaymentCancelCommand(new OnLinePaymentCancelCommand(payType, thirdPayNo, type));
            so.setOnLinePaymentCommand(new OnLinePaymentCommand(payType, null, null, new SimpleDateFormat("yyyyMMddHHmmss").format(payTime), ip));

            String paymenInfo = sdkMataInfoManager.findValue(MataInfo.PAYMENT_INFO);

            //是否需要去第三方支付平台关闭交易开关
            boolean isNeedClosePay = true;

            if (Validator.isNotNullOrEmpty(paymenInfo) && Boolean.parseBoolean(paymenInfo)){
                //交易查询
                PaymentResult res = paymentManager.getOrderInfo(so);
                if (payType.equals(ReservedPaymentType.WECHAT) || payType.equals(ReservedPaymentType.UNIONPAY)){
                    /**
                     * 微信 银联
                     * 如果交易成功,用户支付中，记一下log；
                     * 如果支付不存在/已撤销 直接取消订单;
                     * 
                     */
                    if (SUCCESS.equals(res.getMessage()) || USERPAYING.equals(res.getMessage())){
                        isNeedClosePay = tradeSuccessHandler(orderCode, salesOrderCommand, isNeedClosePay, res);
                    }else if (ORDERNOTEXIST.equals(res.getMessage()) || REVOKED.equals(res.getMessage())){
                        isNeedClosePay = tradeOtherStatusHandler(beforePaymentCancelOrderCommand, orderCode, orderId);
                    }

                }else if (payType.equals(ReservedPaymentType.ALIPAY) || payType.equals(ReservedPaymentType.ALIPAY_BANK) || payType.equals(ReservedPaymentType.ALIPAY_CREDIT)){
                    /**
                     * 支付宝
                     * 如果交易成功，记一下log；
                     * 如果立即支付交易拒绝/关闭/中途取消/买家超时未付款，取消订单;
                     */
                    if (TRADE_SUCCESS.equals(res.getMessage())){

                        isNeedClosePay = tradeSuccessHandler(orderCode, salesOrderCommand, isNeedClosePay, res);

                    }else if (TRADE_REFUSE.equals(res.getMessage()) || TRADE_CLOSED.equals(res.getMessage()) || TRADE_CANCEL.equals(res.getMessage()) || DAEMON_CONFIRM_CLOSE.equals(res.getMessage())){
                        isNeedClosePay = tradeOtherStatusHandler(beforePaymentCancelOrderCommand, orderCode, orderId);
                    }
                }
            }

            if (isNeedClosePay){
                PaymentResult paymentResult = paymentManager.cancelPayment(so);
                LOGGER.info("close payment request return value: " + MapConvertUtils.transPaymentResultToString(paymentResult));

                if (payType.equals(ReservedPaymentType.WECHAT) || payType.equals(ReservedPaymentType.UNIONPAY)){
                    if (ORDERNOTEXIST.equals(paymentResult.getMessage())){

                        tradeOtherStatusHandler(beforePaymentCancelOrderCommand, orderCode, orderId);
                    }else if (SUCCESS.equals(paymentResult.getMessage())){

                        closeTradeUpdateOrderStatus(orderId, paymentResult, operatorName, orderCode, type, payCode, beforePaymentCancelOrderCommand.getIsOMS());
                    }else{
                        // 关闭交易失败
                        //savePaymentResultPaymentLog(paymentResult, operatorName,Constants.CLOSE_AFTER_TYPE);
                        PaymentEvent paymentEvent = new PaymentEvent(this, paymentResult, operatorName, Constants.CLOSE_AFTER_TYPE);
                        eventPublisher.publish(paymentEvent);
                        throw new BusinessException(ErrorCodes.transaction_closed);
                    }

                }else if (payType.equals(ReservedPaymentType.ALIPAY) || payType.equals(ReservedPaymentType.ALIPAY_BANK) || payType.equals(ReservedPaymentType.ALIPAY_CREDIT)){
                    if (TRADE_NOT_EXIST.equals(paymentResult.getMessage())){
                        tradeOtherStatusHandler(beforePaymentCancelOrderCommand, orderCode, orderId);
                    }else{
                        PaymentServiceStatus closePayStatus = paymentResult.getPaymentServiceSatus();
                        if (PaymentServiceStatus.SUCCESS == closePayStatus){
                            // 关闭交易成功
                            closeTradeUpdateOrderStatus(orderId, paymentResult, operatorName, orderCode, type, payCode, beforePaymentCancelOrderCommand.getIsOMS());
                        }else{
                            // 关闭交易失败
                            //savePaymentResultPaymentLog(paymentResult, operatorName,Constants.CLOSE_AFTER_TYPE);
                            PaymentEvent paymentEvent = new PaymentEvent(this, paymentResult, operatorName, Constants.CLOSE_AFTER_TYPE);
                            eventPublisher.publish(paymentEvent);
                            throw new BusinessException(ErrorCodes.transaction_closed);
                        }
                    }
                }
            }
        }
    }

    private boolean tradeOtherStatusHandler(BeforePaymentCancelOrderCommand beforePaymentCancelOrderCommand,String orderCode,Long orderId){
        //cancalOrderUpdateInfo(orderId,orderCode, beforePaymentCancelOrderCommand.getCancelType());
        cancelOrderUpdateInfo(orderId, orderCode, beforePaymentCancelOrderCommand.getCancelType(), beforePaymentCancelOrderCommand.getIsOMS());
        return false;
    }

    private boolean tradeSuccessHandler(String orderCode,SalesOrderCommand salesOrderCommand,boolean isPaySuccess,PaymentResult res){
        String result = "";
        if (!salesOrderCommand.getFinancialStatus().equals(SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT)){
            //		                          // 查询订单状态 如果是已支付完成
            //		                         PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
            //		                          // subordinate
            //		                         paymentServiceReturnCommand.setOrderNo(payCode);
            //		                          // 第三方流水号、账号
            //		                         paymentServiceReturnCommand.setTradeNo(res.getPaymentStatusInformation().getTradeNo());
            //		                         paymentServiceReturnCommand.setSeller(res.getPaymentStatusInformation().getBuyer());
            //
            //		                         updatePayInfos(paymentServiceReturnCommand, null );

            //1.更改订单状态为已支付 2.抛异常说明：该订单已付款，不可取消，建议先退款?
            //orderManager.updateOrderFinancialStatus(salesOrderCommand.getCode(),SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT);

            result = PayWarnningLog.RESULT_DISCARD;
        }else{
            result = PayWarnningLog.RESULT_SAME;
            isPaySuccess = false;
        }

        // log
        PayWarnningEvent payWarnningEvent = new PayWarnningEvent(
                        this,
                        orderCode,
                        res.getPaymentStatusInformation().getTradeNo(),
                        new Date(),
                        salesOrderCommand.getFinancialStatus().toString(),
                        res.getPaymentStatusInformation().getTradeStatus(),
                        true,
                        result);
        eventPublisher.publish(payWarnningEvent);
        return isPaySuccess;
    }

    @Override
    public WechatJsApiConfigCommand getWechatJsApiConfigCommand(String url,String jsapiTicket){
        String nonceStr = WechatUtil.getNonceStr();
        String timeStamp = WechatUtil.getTimeStamp();

        WechatJsApiConfigCommand WechatJsApiConfigCommand = new WechatJsApiConfigCommand();
        WechatJsApiConfigCommand.setAppId(WechatConfig.APP_ID);
        WechatJsApiConfigCommand.setNonceStr(nonceStr);
        WechatJsApiConfigCommand.setTimeStamp(timeStamp);

        Map<String, String> params = new TreeMap<String, String>();
        params.put("jsapi_ticket", jsapiTicket);
        params.put("noncestr", nonceStr);
        params.put("timestamp", timeStamp);
        params.put("url", url);
        String signature = WechatUtil.getSignature(params, SHA_1);
        WechatJsApiConfigCommand.setSignature(signature);

        return WechatJsApiConfigCommand;
    }

    @Override
    public WechatJsApiPayCommand getWechatJsApiPayCommand(String prepayId){
        String nonceStr = WechatUtil.getNonceStr();
        String timeStamp = WechatUtil.getTimeStamp();
        String mPackage = "prepay_id=" + prepayId;

        WechatJsApiPayCommand WechatJsApiPayCommand = new WechatJsApiPayCommand();
        WechatJsApiPayCommand.setNonceStr(nonceStr);
        WechatJsApiPayCommand.setSignType(SIGNTYPE);
        WechatJsApiPayCommand.setTimeStamp(timeStamp);
        WechatJsApiPayCommand.setPackAge(mPackage);
        WechatJsApiPayCommand.setPaySign(generatePaySign(nonceStr, mPackage, timeStamp));

        return WechatJsApiPayCommand;
    }

    /**
     * 获得签名的sign
     * 
     * @param nonceStr
     * @param packageStr
     * @param timeStampStr
     * @return
     * @throws WechatException
     */
    private String generatePaySign(String nonceStr,String packageStr,String timeStampStr){
        Map<String, String> srcMap = new HashMap<String, String>();
        srcMap.put("appId", WechatConfig.APP_ID);
        srcMap.put("timeStamp", timeStampStr);
        srcMap.put("nonceStr", nonceStr);
        srcMap.put("package", packageStr);
        srcMap.put("signType", SIGNTYPE);
        return WechatUtil.makeSign(srcMap, null, WechatConfig.PAY_SIGN_KEY);
    }

}
