package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.command.OrderStatusLogCommand;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.wormhole.constants.OrderStatusV5Constants;
import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;
import com.baozun.nebula.wormhole.mq.entity.order.SalesOrderV5;
import com.baozun.nebula.wormhole.mq.entity.pay.PaymentInfoV5;
import com.baozun.nebula.wormhole.scm.handler.PropellingSalesOrderHandler;
import com.feilong.core.Validator;
import com.feilong.core.bean.ConvertUtil;

@Transactional
@Service("propellingSalesOrderManager")
public class PropellingSalesOrderManagerImpl implements PropellingSalesOrderManager{

    @Autowired
    private OrderManager sdkOrderService;

    @Autowired
    private PropellingCommonManager propellingCommonManager;

    @Autowired
    private SdkPaymentManager paymentManager;

    @Autowired
    private OrderLineV5ListBuilder orderLineV5ListBuilder;

    @Autowired
    private DeliveryInfoV5Builder deliveryInfoV5Builder;

    @Autowired
    private OrderMemberV5Builder orderMemberV5Builder;

    @Autowired
    private OrderPaymentV5ListBuilder orderPaymentV5ListBuilder;

    @Autowired
    private OrderPromotionV5ListBuilder orderPromotionV5ListBuilder;

    @Autowired(required = false)
    private PropellingSalesOrderHandler propellingSalesOrderHandler;

    @Override
    public MsgSendContent propellingSalesOrder(MsgSendRecord msgSendRecord){
        Long orderId = msgSendRecord.getTargetId();
        SalesOrderCommand salesOrderCommand = sdkOrderService.findOrderById(orderId, 1);
        Validate.notNull(salesOrderCommand, "salesOrderCommand can't be null!");

        SalesOrderV5 salesOrderV5 = new SalesOrderV5();

        salesOrderV5.setOrderType(salesOrderCommand.getOrderType());
        salesOrderV5.setBsOrderCode(salesOrderCommand.getCode());
        salesOrderV5.setCreateTime(salesOrderCommand.getCreateTime());
        //是否开发票看是否有发票抬头
        salesOrderV5.setIsNeededInvoice(salesOrderCommand.getReceiptTitle() != null);

        salesOrderV5.setInvoiceTitle(salesOrderCommand.getReceiptTitle());
        salesOrderV5.setInvoiceContent(salesOrderCommand.getReceiptContent());

        //商品总金额该金额为整单最终实际货款.(不包含运费且未扣减虚拟货币[实际支付金额])不含运费的客户端显示最终金额
        salesOrderV5.setTotalActual(salesOrderCommand.getTotal());
        //实际运费
        salesOrderV5.setAcutalTransFee(salesOrderCommand.getActualFreight());

        //订单整单的折扣，含基于整单促销形成的折扣和基于行的促销形成的折扣
        salesOrderV5.setTotalDiscount(salesOrderCommand.getDiscount());

        //支付产生的折扣：由于预付卡或其他支付方式带来的金额折扣   
        salesOrderV5.setPayDiscount(getPayDiscount(salesOrderCommand));

        //卖家备注
        salesOrderV5.setSellerMemo(null);
        //买家家备注
        salesOrderV5.setBuyerMemo(salesOrderCommand.getRemark());

        //订单行
        salesOrderV5.setOrderLines(orderLineV5ListBuilder.buildOrderLineV5List(salesOrderCommand));

        //订单促销
        salesOrderV5.setPromotions(orderPromotionV5ListBuilder.buildOrderPromotionV5List(salesOrderCommand));

        //订单支付明细
        salesOrderV5.setSoPayments(orderPaymentV5ListBuilder.buildOrderPaymentV5List(salesOrderCommand));

        //单商城会员
        salesOrderV5.setOrdeMember(orderMemberV5Builder.buildOrderMemberV5(salesOrderCommand));

        //收货人
        salesOrderV5.setDeliveryInfo(deliveryInfoV5Builder.buildDeliveryInfoV5(salesOrderCommand));

        //订单包装信息 
        salesOrderV5.setProductPackages(null);
        if (propellingSalesOrderHandler != null){
            salesOrderV5 = propellingSalesOrderHandler.propellingSalesOrder(salesOrderV5, salesOrderCommand);
        }

        return propellingCommonManager.saveMsgBody(ConvertUtil.toList(salesOrderV5), msgSendRecord.getId());
    }

    private static BigDecimal getPayDiscount(SalesOrderCommand salesOrderCommand){
        //不是支付宝和货到付款的金额总和
        BigDecimal payDiscount = BigDecimal.ZERO;
        List<PayInfoCommand> payInfoCommandList = salesOrderCommand.getPayInfo();
        if (payInfoCommandList != null){
            for (PayInfoCommand payInfoCommand : payInfoCommandList){
                if (payInfoCommand.getPayType() != payInfoCommand.getMainPayType()){
                    payDiscount = payDiscount.add(payInfoCommand.getPayMoney());
                }
            }
        }
        return payDiscount;
    }

    @Override
    public MsgSendContent propellingPayment(MsgSendRecord msgSendRecord){
        List<PaymentInfoV5> list = new ArrayList<>();
        Long orderId = msgSendRecord.getTargetId();

        List<PayInfoCommand> payInfoCommandList = paymentManager.findPayInfoCommandByOrderId(orderId);
        SalesOrderCommand soCommand = sdkOrderService.findOrderById(orderId, 0);
        if (payInfoCommandList != null){
            for (PayInfoCommand payInfoCommand : payInfoCommandList){
                PaymentInfoV5 paymentInfoV5 = new PaymentInfoV5();
                //需要关联订单表
                paymentInfoV5.setBsOrderCode(payInfoCommand.getOrderCode());
                paymentInfoV5.setPaymentType(payInfoCommand.getPayType().toString());
                paymentInfoV5.setPaymentBank(null);
                paymentInfoV5.setPayTotal(payInfoCommand.getPayMoney());
                paymentInfoV5.setPayNo(payInfoCommand.getSubOrdinate());
                //since 5.3.2.10
                paymentInfoV5.setPaymentAccount(payInfoCommand.getThirdPayAccount());
                paymentInfoV5.setPaymentTime(payInfoCommand.getModifyTime());
                if (payInfoCommand.getPayType() == payInfoCommand.getMainPayType() || Objects.equals(SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT, soCommand.getFinancialStatus())){
                    paymentInfoV5.setAllComplete(true);
                }else{
                    paymentInfoV5.setAllComplete(false);
                }

                Map<String, String> remarkMap = new HashMap<>();
                if (Validator.isNotNullOrEmpty(payInfoCommand.getPayInfo())){
                    remarkMap.put("paydetail", payInfoCommand.getPayInfo());
                }

                if (remarkMap.size() > 0){
                    paymentInfoV5.setRemark(JSON.toJSONString(remarkMap));
                }else{
                    paymentInfoV5.setRemark(null);
                }
                list.add(paymentInfoV5);
            }
        }
        return propellingCommonManager.saveMsgBody(list, msgSendRecord.getId());
    }

    @Override
    public MsgSendContent propellingSoStatus(MsgSendRecord msgSendRecord){
        List<OrderStatusV5> orderStatusV5List = new ArrayList<OrderStatusV5>();
        OrderStatusV5 orderStatusV5 = new OrderStatusV5();
        Long orderStatusLogId = msgSendRecord.getTargetId();

        OrderStatusLogCommand orderStatusLogCommand = sdkOrderService.findOrderStatusLogById(orderStatusLogId);
        orderStatusV5.setBsOrderCode(orderStatusLogCommand.getOrderCode());
        //目前 2:订单取消  后续加
        if (orderStatusLogCommand.getAfterStatus().equals(SalesOrder.SALES_ORDER_STATUS_CANCELED) || orderStatusLogCommand.getAfterStatus().equals(SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED)){
            orderStatusV5.setOpType(OrderStatusV5Constants.ORDER_CANCEL);
        }
        orderStatusV5.setOpTime(orderStatusLogCommand.getCreateTime());

        orderStatusV5List.add(orderStatusV5);
        return propellingCommonManager.saveMsgBody(orderStatusV5List, msgSendRecord.getId());
    }

}
