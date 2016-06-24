package com.baozun.nebula.web.controller.payment.resolver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

import com.baozun.nebula.command.OnLinePaymentCommand;
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
import com.baozun.nebula.payment.manager.ReservedPaymentType;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.utils.MapConvertUtils;
import com.baozun.nebula.utilities.common.RequestMapUtil;
import com.baozun.nebula.utilities.common.convertor.MapAndStringConvertor;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utils.convert.PayTypeConvertUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.command.PaymentResultType;
import com.baozun.nebula.web.constants.Constants;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

public class AlipayPaymentResolver extends BasePaymentResolver implements PaymentResolver{

    /** The Constant LOGGER. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(AlipayPaymentResolver.class);
    
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
    public String buildPayUrl(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog,
            MemberDetails memberDetails, Device device, Map<String, String> extra, HttpServletRequest request, 
            HttpServletResponse response, Model model) throws IllegalPaymentStateException{

		//构造支付参数
		SalesOrderCommand payParams = buildPayParams(originalSalesOrder, payInfoLog, request);
		
		//获取支付请求( url)链接对象
		PaymentRequest paymentRequest = null;
		
		if (device.isMobile()){
		    paymentRequest = paymentManager.createPaymentForWap(payParams);
		}else{
		    paymentRequest = paymentManager.createPayment(payParams, extra);
		}
		
		if (null == paymentRequest){
		    throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_GETURL_ERROR, "获取跳转地址失败");
		}
		
		if (StringUtils.isNotBlank(paymentRequest.getRequestURL())){
		    try{
		        //记录日志和跳转到支付宝支付页面
		        payManager.savePayInfos(payParams, paymentRequest, memberDetails.getLoginName());
		        response.sendRedirect(paymentRequest.getRequestURL());
		    } catch (IOException e) {
		    	LOGGER.error("[BUILD_PAY_URL] build alipay url error. subOrdinate:[" + payInfoLog.getSubOrdinate() + "]", e);
		    }
		}
		
		return null;
	}

    @Override
    public String doPayReturn(String payType, Device device, String paySuccessRedirect, String payFailureRedirect, 
    		HttpServletRequest request, HttpServletResponse response) throws IllegalPaymentStateException{
        String subOrdinate = request.getParameter("out_trade_no");

        LOGGER.info("[DO_PAY_RETURN] get sync notifications before , subOrdinate: {}", subOrdinate);
        
    	// 查询支付订单号信息
		PayCode pc = sdkPaymentManager.findPayCodeBySubOrdinate(subOrdinate);
		
		if (Validator.isNullOrEmpty(pc)) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS, subOrdinate, "交易流水不存在");
		}
        
        // 判断交易是否已有成功 防止重复调用 。
        PayCode payCode = sdkPaymentManager.findPayCodeByCodeAndPayTypeAndPayStatus(subOrdinate, Integer.valueOf(payType), true);

        if (Validator.isNotNullOrEmpty(payCode)){
        	return "redirect:" + paySuccessRedirect + "?subOrdinate=" + subOrdinate;
        }

        LOGGER.debug("[DO_PAY_RETURN] RequestInfoMapForLog:{}", JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));

        // 获取同步付款通知
        PaymentResult paymentResult = null;

        String paymentType = PayTypeConvertUtil.getPayType(ReservedPaymentType.ALIPAY);

        if (device.isMobile()){
            paymentResult = paymentManager.getPaymentResultForSynOfWap(request, paymentType);
        }else{
            paymentResult = paymentManager.getPaymentResultForSyn(request, paymentType);
        }
        if (null == paymentResult){
            // 返回失败
            return "redirect:" + payFailureRedirect + "?subOrdinate=" + subOrdinate;
        }
        LOGGER.info("[DO_PAY_RETURN] sync notifications return value: " + MapConvertUtils.transPaymentResultToString(paymentResult));

      	if(device.isMobile() && PaymentServiceStatus.SUCCESS.equals(paymentResult.getPaymentServiceSatus())){
    		// 因为同步通知手机端返回的是SUCCESS,所以此处需要将alipay pc端返回码设进去
    		paymentResult.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
    	}
        
        // 获取支付状态
        String payStatus = paymentResult.getPaymentServiceSatus().toString();

        if (PAYMENT_SUCCESS.equals(payStatus)){
            // 获取通知成功，修改支付及订单信息
            payManager.updatePayInfos(paymentResult, null, Integer.valueOf(payType), true, request);
        }else{
            // 获取通知失败或其他情况
            payManager.savePaymentResultPaymentLog(paymentResult, null, Constants.DO_RETURN_AFTER_TYPE);
            return "redirect:" + payFailureRedirect + "?subOrdinate=" + subOrdinate;
        }

        return "redirect:" + paySuccessRedirect + "?subOrdinate=" + subOrdinate;
    }

    @Override
    public void doPayNotify(String payType, HttpServletRequest request, 
    		HttpServletResponse response) throws IllegalPaymentStateException,IOException, DocumentException{
    	boolean isMobile = false;
    	
		String aliPayMobileReturnData = request.getParameter("notify_data");
		
		if (Validator.isNotNullOrEmpty(aliPayMobileReturnData)) {
			isMobile = true;
		}
		
        String subOrdinate = getSubOrdinate(request, isMobile);

        LOGGER.info("[DO_PAY_NOTIFY] get sync notifications before , subOrdinate: {}", subOrdinate);

        // 判断交易是否已有成功 防止重复调用 。
        PayCode payCode = sdkPaymentManager.findPayCodeByCodeAndPayTypeAndPayStatus(subOrdinate, Integer.valueOf(payType), true);

        if (Validator.isNotNullOrEmpty(payCode)){
            response.getWriter().write(PaymentResultType.SUCCESS);
        }else{
            LOGGER.debug("[DO_PAY_NOTIFY] RequestInfoMapForLog:{}", JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));

            // 获取异步通知
            PaymentResult paymentResult = null;

            String paymentType = PayTypeConvertUtil.getPayType(ReservedPaymentType.ALIPAY);

			if (isMobile) {
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
	 * 从通知请求中解析交易流水号（支付宝异步通知用）
	 * 
	 * @param request
	 * @param isMobile
	 * @return
	 * @throws DocumentException
	 */
	private String getSubOrdinate(HttpServletRequest request, boolean isMobile) throws DocumentException {
		if (isMobile) {
			Map<String, String> responseMap = new HashMap<String, String>();
			RequestMapUtil.requestConvert(request, responseMap);
			LOGGER.info("[DO_PAY_NOTIFY]request notify_data is [{}]", responseMap.get("notify_data"));
			Map<String, String> resultMap = MapAndStringConvertor
					.convertResultToMap(responseMap.get("notify_data"));
			return resultMap.get("out_trade_no");
		}
		return request.getParameter("out_trade_no");
	}
	
    /**
     * 构造支付参数
     * @param salesOrder
     * @param payInfoLog
     * @param request
     * @return
     * @throws IllegalPaymentStateException
     */
    private SalesOrderCommand buildPayParams(SalesOrderCommand salesOrder, PayInfoLog payInfoLog,
            HttpServletRequest request) throws IllegalPaymentStateException {
    	
        SalesOrderCommand so = new SalesOrderCommand();
        so.setCode(payInfoLog.getSubOrdinate());
        so.setTotal(payInfoLog.getPayMoney());
        
        OnLinePaymentCommand onLinePaymentCommand = new OnLinePaymentCommand();
		onLinePaymentCommand.setBankCode(payInfoLog.getBankCode());
		onLinePaymentCommand.setCustomerIp(RequestUtil.getClientIp(request));
		onLinePaymentCommand.setPayTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		onLinePaymentCommand.setItBPay(getItBPay(salesOrder.getCreateTime()));
		
		onLinePaymentCommand.setPayType(payInfoLog.getPayType());
		if(SalesOrder.SO_PAYMENT_TYPE_INTERNATIONALCARD.equals(payInfoLog.getPayType().toString())) {
			onLinePaymentCommand.setIsInternationalCard(true);
		} else {
			onLinePaymentCommand.setIsInternationalCard(false);
		}
        
        so.setOnLinePaymentCommand(onLinePaymentCommand);
        
        return so;
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
		
		Long itBPay = Long.valueOf(payExpiryTime) - minutes;
		
		if (itBPay <= 0L) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAYMENT_OVERTIME);
		}
		
		return itBPay.toString() + "m";
	}
	
}
