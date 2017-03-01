package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;
import com.baozun.nebula.wormhole.mq.entity.order.SalesOrderV5;
import com.baozun.nebula.wormhole.mq.entity.pay.PaymentInfoV5;
import com.baozun.nebula.wormhole.scm.handler.PropellingSalesOrderHandler;
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

    @Autowired
    private OrderStatusV5ListBuilder orderStatusV5ListBuilder;

    @Autowired
    private PaymentInfoV5Builder paymentInfoV5Builder;

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
        Long orderId = msgSendRecord.getTargetId();

        List<PayInfoCommand> payInfoCommandList = paymentManager.findPayInfoCommandByOrderId(orderId);
        Validate.notEmpty(payInfoCommandList, "payInfoCommandList can't be null/empty!");

        SalesOrderCommand salesOrderCommand = sdkOrderService.findOrderById(orderId, 0);
        Validate.notNull(salesOrderCommand, "salesOrderCommand can't be null!");

        List<PaymentInfoV5> paymentInfoV5LList = new ArrayList<>();
        for (PayInfoCommand payInfoCommand : payInfoCommandList){
            paymentInfoV5LList.add(paymentInfoV5Builder.buildPaymentInfoV5(payInfoCommand, salesOrderCommand));
        }
        return propellingCommonManager.saveMsgBody(paymentInfoV5LList, msgSendRecord.getId());
    }

    @Override
    public MsgSendContent propellingSoStatus(MsgSendRecord msgSendRecord){
        Long orderStatusLogId = msgSendRecord.getTargetId();
        List<OrderStatusV5> orderStatusV5List = orderStatusV5ListBuilder.buildOrderStatusV5List(orderStatusLogId);
        return propellingCommonManager.saveMsgBody(orderStatusV5List, msgSendRecord.getId());
    }

}
