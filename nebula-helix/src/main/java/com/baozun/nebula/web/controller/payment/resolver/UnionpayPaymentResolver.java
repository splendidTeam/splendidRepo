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
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.payment.manager.PayManager;
import com.baozun.nebula.payment.manager.ReservedPaymentType;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.utils.MapConvertUtils;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utils.convert.PayTypeConvertUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.Constants;
import com.baozun.nebula.web.controller.payment.NebulaPaymentController;
import com.baozun.nebula.web.controller.payment.service.common.command.CommonPayParamCommand;
import com.baozun.nebula.web.controller.payment.service.unionpay.UnionpayService;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

public class UnionpayPaymentResolver extends BasePaymentResolver implements PaymentResolver{

    /** The Constant LOGGER. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(NebulaPaymentController.class);
    
	/**
	 * 返回服务器信息
	 */
	public static final String UNIONSUCCESS = "200";

	public static final String UNIONFAIL = "000";

    @Autowired
    private SdkPaymentManager   sdkPaymentManager;

    @Autowired
    private PayManager          payManager;

    @Autowired
    private OrderManager        sdkOrderManager;

    @Autowired
    private EventPublisher      eventPublisher;
    
    @Autowired
	private MataInfoManager     mataInfoManager;
    
    @Autowired
    private UnionpayService     unionpayService;
    
    
    @Override
    public String buildPayUrl(
                    SalesOrderCommand originalSalesOrder,
                    PayInfoLog payInfoLog,
                    MemberDetails memberDetails,
                    Device device,
                    Map<String, String> extra,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model) throws IllegalPaymentStateException{
  
        //构造支付参数
    	CommonPayParamCommand payParamCommand = buildPayParams(originalSalesOrder, payInfoLog, request);
		
		//获取支付请求( url)链接对象
		PaymentRequest paymentRequest = unionpayService.createPayment(payParamCommand, extra);
		
		if (null == paymentRequest){
		    throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_GETURL_ERROR, "获取跳转地址失败");
		}
		
		if (StringUtils.isNotBlank(paymentRequest.getRequestHtml())){
		    try{
		        //记录日志和跳转到支付宝支付页面
		        payManager.savePayInfos(buildSavePayLogParams(originalSalesOrder, payInfoLog), paymentRequest, memberDetails.getLoginName());
		        response.getWriter().write(paymentRequest.getRequestHtml());
		    } catch (IOException e) {
		    	LOGGER.error("[BUILD_PAY_URL] build alipay url error. subOrdinate:[" + payInfoLog.getSubOrdinate() + "]", e);
		    }
		}
		
		return null;

    }

    @Override
    public String doPayReturn(String payType, Device device, String paySuccessRedirect, String payFailureRedirect, 
    		HttpServletRequest request, HttpServletResponse response) throws IllegalPaymentStateException{
    	throw new IllegalAccessError("union pay donot support return invoke.");
    }

    @Override
    public void doPayNotify(String payType, HttpServletRequest request, 
    		HttpServletResponse response) throws IllegalPaymentStateException,IOException, DocumentException{
        String subOrdinate = getSubOrdinate(request);

        // 判断交易是否已有成功 防止重复调用 。
        PayCode payCode = sdkPaymentManager.findPayCodeByCodeAndPayTypeAndPayStatus(subOrdinate, Integer.valueOf(payType), true);

        if (Validator.isNotNullOrEmpty(payCode)){
        	response.getWriter().write(UNIONSUCCESS);
        }else{
            PaymentResult paymentResult = getPaymentResult(request);

            if (null == paymentResult){
                //log
                sdkPaymentManager.savePaymentLog(
                                new Date(),
                                com.baozun.nebula.sdk.constants.Constants.PAY_LOG_NOTIFY_AFTER_MESSAGE,
                                null,
                                request.getRequestURL().toString());
                // 返回失败
                response.getWriter().write(UNIONFAIL);
            }else{
                // 获取支付状态
                String responseStatus = paymentResult.getPaymentServiceSatus().toString();

                SalesOrderCommand salesOrderCommand = getSalesOrderCommand(subOrdinate);

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

                response.getWriter().write(UNIONSUCCESS);
            }
        }
    }

	private String getSubOrdinate(HttpServletRequest request) {
		String subOrdinate = request.getParameter("orderId");

        LOGGER.info("[DO_PAY_NOTIFY] get sync notifications before , subOrdinate: {}", subOrdinate);
		return subOrdinate;
	}

	private SalesOrderCommand getSalesOrderCommand(String subOrdinate) {
		//添加订单查询：支付状态为1 物流状态为1||3
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("subOrdinate", subOrdinate);
		List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

		SalesOrderCommand salesOrderCommand = null;
		if (Validator.isNotNullOrEmpty(payInfoLogs)){
		    salesOrderCommand = sdkOrderManager.findOrderById(payInfoLogs.get(0).getOrderId(), 1);
		}
		return salesOrderCommand;
	}

	private PaymentResult getPaymentResult(HttpServletRequest request) {
		LOGGER.debug("[DO_PAY_NOTIFY] RequestInfoMapForLog:{}", JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));

		String paymentType = PayTypeConvertUtil.getPayType(ReservedPaymentType.UNIONPAY);
		
		// 获取异步通知
		PaymentResult paymentResult =  unionpayService.getPaymentResultForAsy(request, paymentType);
		
		LOGGER.info("[DO_PAY_NOTIFY] async notifications return value: " + MapConvertUtils.transPaymentResultToString(paymentResult));
		return paymentResult;
	}
    
    /**
	 * 获取支付信息
	 * @param bankCode
	 * @param payType
	 * @return
	 */
	protected OnLinePaymentCommand getOnLinePaymentCommand(String bankCode, Integer payType, String itBPay, String qrPayMode, HttpServletRequest request) {
		OnLinePaymentCommand onLinePaymentCommand = new OnLinePaymentCommand();
		onLinePaymentCommand.setBankCode(bankCode);
		onLinePaymentCommand.setCustomerIp(RequestUtil.getClientIp(request));
		onLinePaymentCommand.setPayTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		onLinePaymentCommand.setItBPay(itBPay);
		if (payType == ReservedPaymentType.ALIPAY_CREDIT_INT_M || payType == ReservedPaymentType.ALIPAY_CREDIT_INT_V) {
			onLinePaymentCommand.setIsInternationalCard(true);
		} else {
			onLinePaymentCommand.setIsInternationalCard(false);
		}
		onLinePaymentCommand.setPayType(payType);
		//onLinePaymentCommand.setQrPayMode(qrPayMode);
		return onLinePaymentCommand;
	}

    /**
	 * 获取过期时间
	 * @param orderCreateDate
	 * @return
	 * @throws IllegalPaymentStateException
	 */
	private String getItBPay(Date orderCreateDate) throws IllegalPaymentStateException {
        String payExpiryTime = mataInfoManager.findValue(MataInfo.PAYMENT_EXPIRY_TIME);
		
    	if(null == payExpiryTime){
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAYMENT_EXPIRYTIME_ISNULL);
		}
        
		Date now = new Date();
		long minutes = (now.getTime() - orderCreateDate.getTime()) / 1000 / 60;  
		
		Long itBPay = Long.valueOf(payExpiryTime) - minutes;
		
		if (itBPay <= 0L) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAYMENT_OVERTIME);
		}
		
		return itBPay.toString();
	}


	
	/**
	 * 为保存paylog构造对象
	 * @param salesOrder
	 * @param payInfoLog
	 * @return
	 */
	private SalesOrderCommand buildSavePayLogParams(SalesOrderCommand salesOrder, PayInfoLog payInfoLog) {
    	
        SalesOrderCommand so = new SalesOrderCommand();
        so.setCode(payInfoLog.getSubOrdinate());
        
        OnLinePaymentCommand onLinePaymentCommand = new OnLinePaymentCommand();
		onLinePaymentCommand.setPayType(payInfoLog.getPayType());
        so.setOnLinePaymentCommand(onLinePaymentCommand);
        
        return so;
    }
	
    /**
     * 构造支付参数
     * @param salesOrder
     * @param payInfoLog
     * @param request
     * @return
     * @throws IllegalPaymentStateException
     */
    private CommonPayParamCommand buildPayParams(SalesOrderCommand salesOrder, PayInfoLog payInfoLog,
            HttpServletRequest request) throws IllegalPaymentStateException {
    	CommonPayParamCommand payParam = new CommonPayParamCommand();
    	payParam.setOrderNo(payInfoLog.getSubOrdinate());
    	payParam.setTotalFee(payInfoLog.getPayMoney());
    	payParam.setPaymentType(payInfoLog.getPayType().toString());
        return payParam;
    }

}
