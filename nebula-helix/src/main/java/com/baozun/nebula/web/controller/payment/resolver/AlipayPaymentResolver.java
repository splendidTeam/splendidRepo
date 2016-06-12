package com.baozun.nebula.web.controller.payment.resolver;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.event.PayWarnningEvent;
import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.payment.manager.PayManager;
import com.baozun.nebula.payment.manager.PaymentManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.utils.MapConvertUtils;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utils.convert.PayTypeConvertUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.command.PaymentResultType;
import com.baozun.nebula.web.constants.Constants;
import com.baozun.nebula.web.controller.payment.NebulaPaymentController;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

public class AlipayPaymentResolver extends BasePaymentResolver implements PaymentResolver{

    /** The Constant LOGGER. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(NebulaPaymentController.class);

    private static final String PAYMENT_SUCCESS = "PAYMENT_SUCCESS";

    @Autowired
    private SdkPaymentManager   sdkPaymentManager;

    @Autowired
    private PaymentManager      paymentManager;

    @Autowired
    private PayManager          payManager;

    @Autowired
    private OrderManager        sdkOrderManager;

    @Autowired
    private EventPublisher      eventPublisher;
    
    @Autowired
	private MataInfoManager     mataInfoManager;

    @Override
    public String buildPayUrl(
                    SalesOrderCommand originalSalesOrder,
                    PayInfoLog payInfoLog,
                    MemberDetails memberDetails,
                    Device device,
                    Map<String, Object> extra,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model) throws IllegalPaymentStateException{

        String itBPay = getItBPay(originalSalesOrder.getCreateTime());

        // 超时校验
        if (itBPay.equals("0m")){
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAYMENT_OVERTIME);
        }

        // 获取各种参数
        String payInfo = payInfoLog.getPayInfo();

        Properties pro = ProfileConfigUtil.findCommonPro("config/payMentInfo.properties");
        String payTypeStr = pro.getProperty(payInfo + ".payType");
        String bankCode = pro.getProperty(payInfo + ".bankcode");

        Integer payType = null;

        if (Validator.isNotNullOrEmpty(payTypeStr)){
            payType = Integer.parseInt(payTypeStr.trim());
        }

        if (Validator.isNotNullOrEmpty(bankCode)){
            bankCode = bankCode.trim();
        }

        String subOrdinate = payInfoLog.getSubOrdinate();

        PayCode pc = sdkPaymentManager.findPayCodeBySubOrdinate(subOrdinate);

        String qrPayMode = "";

        if (extra.get("qrPayMode") != null){
            qrPayMode = extra.get("qrPayMode").toString();
        }

        // 参数封装
        SalesOrderCommand so = new SalesOrderCommand();
        so.setCode(subOrdinate);
        so.setTotal(pc.getPayMoney());
        so.setOnLinePaymentCommand(getOnLinePaymentCommand(bankCode, payType, itBPay, qrPayMode, request));

        // 获取支付请求( url)链接对象
        PaymentRequest paymentRequest = null;

        if (device.isMobile()){
            paymentRequest = paymentManager.createPaymentForWap(so);
        }else{
            paymentRequest = paymentManager.createPayment(so);
        }

        if (null == paymentRequest){
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_GETURL_ERROR, "获取跳转地址失败");
        }else{
            String url = paymentRequest.getRequestURL();
            if (StringUtils.isNotBlank(url)){
                try{
                    // 记录日志和跳转到支付宝支付页面
                    payManager.savePayInfos(so, paymentRequest, memberDetails.getLoginName());
                    response.sendRedirect(url);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    public String doPayReturn(HttpServletRequest request,HttpServletResponse response,String payType,Device device)
                    throws IllegalPaymentStateException{
        String subOrdinate = request.getParameter("out_trade_no");

        LOGGER.info("[DO_PAY_RETURN] get sync notifications before , subOrdinate: {}", subOrdinate);

        // 判断交易是否已有成功 防止重复调用 。
        PayCode payCode = sdkPaymentManager.findPayCodeByCodeAndPayTypeAndPayStatus(subOrdinate, Integer.valueOf(payType), true);

        if (Validator.isNotNullOrEmpty(payCode)){
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAID, subOrdinate, "订单已经被支付");
        }

        LOGGER.debug("[DO_PAY_RETURN] RequestInfoMapForLog:{}", JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));

        // 获取同步付款通知
        PaymentResult paymentResult = null;

        String paymentType = PayTypeConvertUtil.getPayType(Integer.valueOf(payType));

        if (device.isMobile()){
            paymentResult = paymentManager.getPaymentResultForSynOfWap(request, paymentType);
        }else{
            paymentResult = paymentManager.getPaymentResultForSyn(request, paymentType);
        }
        if (null == paymentResult){
            // 返回失败
            return "redirect:" + getPayFailurePageRedirect(subOrdinate);
        }
        LOGGER.info("[DO_PAY_RETURN] sync notifications return value: " + MapConvertUtils.transPaymentResultToString(paymentResult));

        // 获取支付状态
        String payStatus = paymentResult.getPaymentServiceSatus().toString();

        if (PAYMENT_SUCCESS.equals(payStatus)){
            // 获取通知成功，修改支付及订单信息
            payManager.updatePayInfos(paymentResult, null, Integer.valueOf(payType), true, request);
        }else{
            // 获取通知失败或其他情况
            payManager.savePaymentResultPaymentLog(paymentResult, null, Constants.DO_RETURN_AFTER_TYPE);
            return "redirect:" + getPayFailurePageRedirect(subOrdinate);
        }

        return "redirect:" + getPaySuccessPageRedirect(subOrdinate);
    }

    @Override
    public void doPayNotify(HttpServletRequest request,HttpServletResponse response,String payType,Device device)
                    throws IllegalPaymentStateException,IOException{
        String subOrdinate = request.getParameter("out_trade_no");

        LOGGER.info("[DO_PAY_NOTIFY] get sync notifications before , subOrdinate: {}", subOrdinate);

        // 判断交易是否已有成功 防止重复调用 。
        PayCode payCode = sdkPaymentManager.findPayCodeByCodeAndPayTypeAndPayStatus(subOrdinate, Integer.valueOf(payType), true);

        if (Validator.isNotNullOrEmpty(payCode)){
            response.getWriter().write(PaymentResultType.SUCCESS);
        }else{
            LOGGER.debug("[DO_PAY_NOTIFY] RequestInfoMapForLog:{}", JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));

            // 获取异步通知
            PaymentResult paymentResult = null;

            String paymentType = PayTypeConvertUtil.getPayType(Integer.valueOf(payType));

            if (device.isMobile()){
                paymentResult = paymentManager.getPaymentResultForAsyOfWap(request, paymentType);
            }else{
                paymentResult = paymentManager.getPaymentResultForAsy(request, paymentType);
            }

            LOGGER.info("[DO_PAY_NOTIFY] async notifications return value: " + MapConvertUtils.transPaymentResultToString(paymentResult));

            if (null == paymentResult){
                //log
                sdkPaymentManager.savePaymentLog(
                                new Date(),
                                com.baozun.nebula.sdk.constants.Constants.PAY_LOG_NOTIFY_AFTER_MESSAGE,
                                null,
                                request.getRequestURL().toString());
                // 返回失败
                response.getWriter().write(PaymentResultType.FAIL);
            }else{
                // 获取支付状态
                String responseStatus = paymentResult.getPaymentServiceSatus().toString();

                //添加订单查询：支付状态为1 物流状态为1||3
                Map<String, Object> paraMap = new HashMap<String, Object>();
                paraMap.put("subOrdinate", subOrdinate);
                List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

                SalesOrderCommand salesOrderCommand = null;
                if (Validator.isNotNullOrEmpty(payInfoLogs)){
                    salesOrderCommand = sdkOrderManager.findOrderById(payInfoLogs.get(0).getOrderId(), 1);
                }

                if (canUpdatePayInfos(responseStatus, salesOrderCommand)){
                    // 获取通知成功，修改支付及订单信息
                    payManager.updatePayInfos(paymentResult, null, Integer.valueOf(payType), false, request);
                }else{
                    if (Validator.isNotNullOrEmpty(salesOrderCommand)){
                        //log
                        String result = "FinancialStatus：" + salesOrderCommand.getFinancialStatus() + " LogisticsStatus:"
                                        + salesOrderCommand.getLogisticsStatus();

                        PayWarnningEvent payWarnningEvent = new PayWarnningEvent(
                                        this,
                                        salesOrderCommand.getCode(),
                                        null,
                                        new Date(),
                                        null,
                                        responseStatus,
                                        null,
                                        result);
                        eventPublisher.publish(payWarnningEvent);
                    }

                    // 返回失败记录日志
                    payManager.savePaymentResultPaymentLog(paymentResult, null, Constants.DO_NOTIFY_AFTER_TYPE);
                }

                response.getWriter().write(paymentResult.getResponseValue());
            }
        }
    }

    /**
     * @param responseStatus
     * @param salesOrderCommand
     * @return
     */
    private boolean canUpdatePayInfos(String responseStatus,SalesOrderCommand salesOrderCommand){
        if (null == salesOrderCommand){
            return false;
        }

        Integer logisticsStatus = salesOrderCommand.getLogisticsStatus();
        Integer financialStatus = salesOrderCommand.getFinancialStatus();
        return PAYMENT_SUCCESS.equals(responseStatus)
                        && (Objects.equals(SalesOrder.SALES_ORDER_STATUS_NEW, logisticsStatus)
                                        || Objects.equals(SalesOrder.SALES_ORDER_STATUS_TOOMS, logisticsStatus))
                        && Objects.equals(
                                        SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT,
                                        financialStatus);
    }
    
    /**
	 * 获取过期时间
	 * @param orderCreateDate
	 * @return
	 * @throws IllegalPaymentStateException
	 */
	private String getItBPay(Date orderCreateDate) throws IllegalPaymentStateException {
		String payExpiryTime = mataInfoManager.findValue(MataInfo.PAYMENT_EXPIRY_TIME);
		Date now = new Date();
		long minutes = (now.getTime() - orderCreateDate.getTime()) / 1000 / 60;  
		if (payExpiryTime == null) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAID);
		}
		Long itBPay = Long.valueOf(payExpiryTime) - minutes;
		if (itBPay <= 0L) {
			return "0m";
		} else {
			return itBPay.toString() + "m";
		}
	}
    

    //TODO
    //注意这里的paytype要设置成
    //order.getOnLinePaymentCancelCommand().getPayType()
    //OnLinePaymentCancelCommand的payType用ReservedPaymentType里的常量

}
