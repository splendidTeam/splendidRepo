/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.payment.manager.impl;

import static com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.dao.payment.PayInfoDao;
import com.baozun.nebula.dao.payment.PayInfoLogDao;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.event.PaymentSuccessEvent;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.payment.manager.PaymentResultSuccessUpdateManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoQueryManager;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Service("paymentResultSuccessUpdateManager")
@Transactional
public class PaymentResultSuccessUpdateManagerImpl implements PaymentResultSuccessUpdateManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentResultSuccessUpdateManagerImpl.class);

    @Autowired
    private SdkPaymentManager sdkPaymentManager;

    @Autowired
    private OrderManager sdkOrderService;

    @Autowired
    private SdkMemberManager sdkMemberManager;

    @Autowired
    private SdkMsgManager sdkMsgManager;

    @Autowired
    private PayInfoLogDao payInfoLogDao;

    @Autowired
    private PayInfoDao payInfoDao;

    @Autowired
    private SdkPayInfoQueryManager sdkPayInfoQueryManager;

    @Autowired
    private EventPublisher eventPublisher;

    //---------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.payment.manager.PaymentResultSuccessUpdateManager#updatePayInfos(com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand, java.lang.Integer)
     */
    @Override
    public void updateSuccess(PaymentServiceReturnCommand paymentServiceReturnCommand,Integer payType){

        Long memberId = null;
        String subOrdinate = paymentServiceReturnCommand.getOrderNo();//支付流水号
        String tradeNo = paymentServiceReturnCommand.getTradeNo();// 支付宝交易号
        String thirdPayAccount = paymentServiceReturnCommand.getBuyer();//买家账号

        //查询订单的需要支付的payInfolog
        List<PayInfoLog> payInfoLogList = sdkPayInfoQueryManager.findPayInfoLogListBySubOrdinate(subOrdinate, false);

        //---------------------------------------------------------------------

        if (isNotNullOrEmpty(payInfoLogList)){
            for (PayInfoLog payInfoLog : payInfoLogList){
                //  set THIRD_PAY_NO = :thirdPayNo,MODIFY_TIME= :modifyTime,THIRD_PAY_ACCOUNT = :thirdPayAccount,PAY_SUCCESS_STATUS = :paySuccessStatus
                payInfoLog.setThirdPayAccount(thirdPayAccount);
                payInfoLog.setThirdPayNo(tradeNo);
                payInfoLog.setPaySuccessStatus(true);
                payInfoLog.setModifyTime(new Date());
                payInfoLogDao.save(payInfoLog);

                PayInfo payInfo = payInfoDao.getByPrimaryKey(payInfoLog.getPayInfoId());
                payInfo.setThirdPayAccount(thirdPayAccount);
                payInfo.setThirdPayNo(tradeNo);
                payInfo.setPaySuccessStatus(true);
                payInfo.setModifyTime(new Date());
                payInfoDao.save(payInfo);

                SalesOrderCommand salesOrderCommand = sdkOrderService.findOrderById(payInfo.getOrderId(), 1);

                memberId = salesOrderCommand.getMemberId();
                // 更改订单的财务状态
                if (salesOrderCommand.getFinancialStatus() != SALES_ORDER_FISTATUS_FULL_PAYMENT){

                    //发邮件
                    sdkOrderService.sendEmailOfOrder(salesOrderCommand.getCode(), EmailConstants.PAY_ORDER_SUCCESS);

                    sdkOrderService.updateOrderFinancialStatus(salesOrderCommand.getCode(), SALES_ORDER_FISTATUS_FULL_PAYMENT);

                    //保存OMS消息发送记录(当订单有付款发生时，推送消息给到SCM) 
                    sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_PAY_SEND, salesOrderCommand.getId(), null);

                    //---------------------------------------------------------------------
                    salesOrderCommand.setFinancialStatus(SALES_ORDER_FISTATUS_FULL_PAYMENT);
                    //since 5.3.2.22
                    ApplicationEvent event = new PaymentSuccessEvent(this, subOrdinate, salesOrderCommand);
                    eventPublisher.publish(event);
                }
            }
        }

        //---------------------------------------------------------------------

        sdkPaymentManager.updatePayCodePayStatus(subOrdinate, new Date(), true);

        //---------------------------------------------------------------------

        //更改用户行为
        if (memberId != null){
            MemberConductCommand memberConductCommand = sdkMemberManager.findMemberConductCommandById(memberId);
            memberConductCommand.setPayTime(new Date());
            sdkMemberManager.saveMemberConduct(memberConductCommand);
        }

    }
}
