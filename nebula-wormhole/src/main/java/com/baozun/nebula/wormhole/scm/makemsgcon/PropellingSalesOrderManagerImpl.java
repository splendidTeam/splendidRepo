package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.OrderStatusLogCommand;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.baozun.nebula.wormhole.constants.OrderStatusV5Constants;
import com.baozun.nebula.wormhole.constants.PromotionTypeConstants;
import com.baozun.nebula.wormhole.mq.entity.order.DeliveryInfoV5;
import com.baozun.nebula.wormhole.mq.entity.order.OrderLineV5;
import com.baozun.nebula.wormhole.mq.entity.order.OrderMemberV5;
import com.baozun.nebula.wormhole.mq.entity.order.OrderPaymentV5;
import com.baozun.nebula.wormhole.mq.entity.order.OrderPromotionV5;
import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;
import com.baozun.nebula.wormhole.mq.entity.order.SalesOrderV5;
import com.baozun.nebula.wormhole.mq.entity.pay.PaymentInfoV5;
import com.baozun.nebula.wormhole.scm.handler.PropellingSalesOrderHandler;
import com.feilong.core.Validator;

@Service("propellingSalesOrderManager")
@Transactional
public class PropellingSalesOrderManagerImpl implements PropellingSalesOrderManager{

    @Autowired
    private OrderManager                sdkOrderService;

    @Autowired
    private PropellingCommonManager     propellingCommonManager;

    @Autowired
    private SdkPaymentManager           paymentManager;

    @Autowired(required = false)
    private PropellingSalesOrderHandler propellingSalesOrderHandler;

    /**
     * 上传图片的域名
     */
    @Value("#{meta['upload.img.domain.base']}")
    private String                      uploadImgDomain;

    @Override
    public MsgSendContent propellingSalesOrder(MsgSendRecord msr){
        List<SalesOrderV5> list = new ArrayList<SalesOrderV5>();
        Long orderId = msr.getTargetId();
        SalesOrderCommand salesOrderCommand = sdkOrderService.findOrderById(orderId, 1);
        SalesOrderV5 salesOrderV5 = new SalesOrderV5();

        salesOrderV5.setOrderType(salesOrderCommand.getOrderType());
        salesOrderV5.setBsOrderCode(salesOrderCommand.getCode());
        salesOrderV5.setCreateTime(salesOrderCommand.getCreateTime());
        //是否开发票看是否有发票抬头
        if (salesOrderCommand.getReceiptTitle() == null){
            salesOrderV5.setIsNeededInvoice(false);
        }else{
            salesOrderV5.setIsNeededInvoice(true);
        }
        salesOrderV5.setInvoiceTitle(salesOrderCommand.getReceiptTitle());
        salesOrderV5.setInvoiceContent(salesOrderCommand.getReceiptContent());
        //商品总金额该金额为整单最终实际货款.(不包含运费且未扣减虚拟货币[实际支付金额])不含运费的客户端显示最终金额
        salesOrderV5.setTotalActual(salesOrderCommand.getTotal());
        //实际运费
        salesOrderV5.setAcutalTransFee(salesOrderCommand.getActualFreight());
        //订单整单的折扣，含基于整单促销形成的折扣和基于行的促销形成的折扣
        salesOrderV5.setTotalDiscount(salesOrderCommand.getDiscount());
        //支付产生的折扣：由于预付卡或其他支付方式带来的金额折扣   
        BigDecimal payDiscount = getPayDiscount(salesOrderCommand);
        salesOrderV5.setPayDiscount(payDiscount);
        //卖家备注
        salesOrderV5.setSellerMemo(null);
        //买家家备注
        salesOrderV5.setBuyerMemo(salesOrderCommand.getRemark());
        //订单行
        List<OrderLineV5> orderLines = getOrderLines(salesOrderCommand);
        salesOrderV5.setOrderLines(orderLines);
        //订单促销
        List<OrderPromotionV5> promotions = getOrderPromotion(salesOrderCommand);
        salesOrderV5.setPromotions(promotions);
        //订单支付明细
        List<OrderPaymentV5> soPayMents = getSoPayMents(salesOrderCommand);
        salesOrderV5.setSoPayments(soPayMents);
        //订单包装信息 
        salesOrderV5.setProductPackages(null);
        //单商城会员
        OrderMemberV5 orderMemberV5 = getOrderMemberV5(salesOrderCommand);
        salesOrderV5.setOrdeMember(orderMemberV5);
        //收货人
        DeliveryInfoV5 deliveryInfoV5 = getDeliveryInfoV5(salesOrderCommand);
        salesOrderV5.setDeliveryInfo(deliveryInfoV5);
        if (propellingSalesOrderHandler != null)
            salesOrderV5 = propellingSalesOrderHandler.propellingSalesOrder(salesOrderV5, salesOrderCommand);

        list.add(salesOrderV5);
        return propellingCommonManager.saveMsgBody(list, msr.getId());

    }

    private BigDecimal getPayDiscount(SalesOrderCommand salesOrderCommand){
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

    private List<OrderPromotionV5> getOrderPromotion(SalesOrderCommand salesOrderCommand){
        //订单促销 
        List<OrderPromotionV5> promotions = new ArrayList<OrderPromotionV5>();

        Map<Long, OrderPromotionCommand> map = getOrderPromotionCommandMap(salesOrderCommand);

        Iterator it = map.keySet().iterator();
        while (it.hasNext()){
            Long key = (Long) it.next();
            OrderPromotionCommand obj = map.get(key);
            if (obj.getBaseOrder()){
                OrderPromotionV5 orderPromotionV5 = new OrderPromotionV5();
                //促销类型值域由商城前端预定义(商城共用一套),维护进omsChooseOption
                orderPromotionV5.setType(PromotionTypeConstants.PROMOTION_ALL);
                //促销编码该字段目前在oms中无需备案,oms中仅记录该信息商城定义的活动编号
                orderPromotionV5.setCode(obj.getActivityId().toString());
                orderPromotionV5.setDescription(obj.getDescribe());
                orderPromotionV5.setDiscountFee(obj.getDiscountAmount());
                orderPromotionV5.setCouponCode(obj.getCoupon());
                orderPromotionV5.setRemark(null);
                promotions.add(orderPromotionV5);
            }
        }

        return promotions;
    }

    private Map<Long, OrderPromotionCommand> getOrderPromotionCommandMap(SalesOrderCommand salesOrderCommand){
        Map<Long, OrderPromotionCommand> map = new HashMap<Long, OrderPromotionCommand>();
        if (salesOrderCommand.getOrderPromotions() != null){
            for (OrderPromotionCommand orderPromotionCommand : salesOrderCommand.getOrderPromotions()){
                if (!orderPromotionCommand.getIsShipDiscount()){
                    OrderPromotionCommand obj = map.get(orderPromotionCommand.getActivityId());
                    if (obj == null){
                        map.put(orderPromotionCommand.getActivityId(), orderPromotionCommand);
                    }else{
                        orderPromotionCommand.setDiscountAmount(orderPromotionCommand.getDiscountAmount().add(obj.getDiscountAmount()));
                        map.put(orderPromotionCommand.getActivityId(), orderPromotionCommand);
                    }
                }
            }
        }
        return map;
    }

    private List<OrderPaymentV5> getSoPayMents(SalesOrderCommand salesOrderCommand){
        List<OrderPaymentV5> soPayments = new ArrayList<OrderPaymentV5>();
        if (salesOrderCommand.getPayInfo() != null){
            for (PayInfoCommand payInfoCommand : salesOrderCommand.getPayInfo()){
                OrderPaymentV5 orderPaymentV5 = new OrderPaymentV5();
                //目前是取的订单表里面的payment
                orderPaymentV5.setPaymentType(payInfoCommand.getPayType().toString());
                orderPaymentV5.setPayActual(payInfoCommand.getPayMoney());
                //附加价值说明：对于某些支付方式，可以使用此信息记录额外内容，如外部积分可以使用此信息来记录积分分值，预付卡可以用此来记录实际价值
                orderPaymentV5.setAdditionalWorth(null);
                soPayments.add(orderPaymentV5);
            }
        }
        return soPayments;
    }

    private DeliveryInfoV5 getDeliveryInfoV5(SalesOrderCommand salesOrderCommand){
        DeliveryInfoV5 deliveryInfoV5 = new DeliveryInfoV5();
        deliveryInfoV5.setCountry(null);
        
//        deliveryInfoV5.setProvince(salesOrderCommand.getProvince());
//        deliveryInfoV5.setCity(salesOrderCommand.getCity());
//        deliveryInfoV5.setDistrict(salesOrderCommand.getArea());
        // 订单表不存省市区文字，只存ID 
    	Address province=AddressUtil.getAddressById(salesOrderCommand.getProvinceId());
    	Address city=AddressUtil.getAddressById(salesOrderCommand.getCityId());
    	Address area=AddressUtil.getAddressById(salesOrderCommand.getAreaId());
        deliveryInfoV5.setProvince(province.getName());
        deliveryInfoV5.setCity(city.getName());
        deliveryInfoV5.setDistrict(area.getName());
        
        deliveryInfoV5.setTown(null);
        deliveryInfoV5.setAddress(salesOrderCommand.getAddress());
        deliveryInfoV5.setZipCode(salesOrderCommand.getPostcode());
        deliveryInfoV5.setReceiver(salesOrderCommand.getName());
        deliveryInfoV5.setReceiverMobile(salesOrderCommand.getMobile());
        deliveryInfoV5.setReceiverPhone(salesOrderCommand.getTel());
        deliveryInfoV5.setAppointShipDate(salesOrderCommand.getAppointShipDate());

        //派件时间类型（快递附加服务）【logisticsServiceType】：1：普通5：当日6：次日
        deliveryInfoV5.setLogisticsServiceType(salesOrderCommand.getAppointType());
        deliveryInfoV5.setRemark(null);
        return deliveryInfoV5;
    }

    private OrderMemberV5 getOrderMemberV5(SalesOrderCommand salesOrderCommand){
        OrderMemberV5 orderMemberV5 = new OrderMemberV5();
        orderMemberV5.setEmail(salesOrderCommand.getEmail());
        orderMemberV5.setMemberId(salesOrderCommand.getMemberId());
        orderMemberV5.setLoginName(salesOrderCommand.getMemberName());
        return orderMemberV5;
    }

    private List<OrderLineV5> getOrderLines(SalesOrderCommand salesOrderCommand){
        List<OrderLineV5> orderLines = new ArrayList<OrderLineV5>();
        if (salesOrderCommand.getOrderLines() != null){
            for (OrderLineCommand orderLine : salesOrderCommand.getOrderLines()){
                OrderLineV5 orderLineV5 = new OrderLineV5();
                orderLineV5.setBsOrderLineId(orderLine.getId());
                orderLineV5.setExtentionCode(orderLine.getExtentionCode());
                orderLineV5.setBsSkuName(orderLine.getItemName());
                orderLineV5.setQty(orderLine.getCount());
                //吊牌价,参考数据，可以为空
                orderLineV5.setListPrice(orderLine.getMSRP());
                //销售价(折前单价)
                orderLineV5.setUnitPrice(orderLine.getSalePrice());
                //行总计(扣减所有活动优惠且未扣减积分抵扣)sum(sl.totalActual)=so.totalActualtotalActual+discountFee=unitPrice×qty最终货款=销售价X数量-折扣
                orderLineV5.setTotalActual(orderLine.getSubtotal());
                //行优惠总金额(不包含积分抵扣)
                BigDecimal orderLineDiscount = getOrderLineDiscount(salesOrderCommand, orderLine);
                orderLineV5.setDiscountFee(orderLineDiscount);
                //是否赠品 
                if (orderLine.getType() == Constants.ITEM_TYPE_SALE){
                    orderLineV5.setIsPrezzie(false);
                }else{
                    orderLineV5.setIsPrezzie(true);
                }
                //保修时长(按月计)
                orderLineV5.setWarrantyMonths(null);
                //行优惠	
                List<OrderPromotionV5> promotions = getOrderLinePromotion(salesOrderCommand, orderLine);

                orderLineV5.setPromotions(promotions);
                //订单行包装信息
                orderLineV5.setProductPackages(null);
                //设置商品图片
                orderLineV5.setItemPic(uploadImgDomain + orderLine.getItemPic());
                orderLines.add(orderLineV5);
            }
        }
        return orderLines;
    }

    private BigDecimal getOrderLineDiscount(SalesOrderCommand salesOrderCommand,OrderLineCommand orderLine){
        BigDecimal orderLineDiscount = BigDecimal.ZERO;

        if (salesOrderCommand.getOrderPromotions() != null){
            for (OrderPromotionCommand obj : salesOrderCommand.getOrderPromotions()){
                if (orderLine.getId().equals(obj.getOrderLineId()) && !obj.getBaseOrder()){
                    orderLineDiscount = orderLineDiscount.add(obj.getDiscountAmount());
                }
            }
        }
        return orderLineDiscount;
    }

    private List<OrderPromotionV5> getOrderLinePromotion(SalesOrderCommand salesOrderCommand,OrderLineCommand orderLine){
        List<OrderPromotionV5> promotions = new ArrayList<OrderPromotionV5>();
        if (salesOrderCommand.getOrderPromotions() != null){
            //主商品才有优惠
            if (orderLine.getType().equals(1)){
                for (OrderPromotionCommand obj : salesOrderCommand.getOrderPromotions()){
                    //不是运费优惠
                    if (!obj.getIsShipDiscount() && orderLine.getId().equals(obj.getOrderLineId())){
                        OrderPromotionV5 orderPromotionV5 = new OrderPromotionV5();
                        //促销类型值域由商城前端预定义(商城共用一套),维护进omsChooseOption
                        orderPromotionV5.setType(
                                        obj.getBaseOrder() ? PromotionTypeConstants.PROMOTION_ALLONLINE
                                                        : PromotionTypeConstants.PROMOTION_LINE);
                        //促销编码该字段目前在oms中无需备案,oms中仅记录该信息商城定义的活动编号
                        orderPromotionV5.setCode(obj.getActivityId().toString());
                        orderPromotionV5.setDescription(obj.getDescribe());
                        orderPromotionV5.setDiscountFee(obj.getDiscountAmount());
                        orderPromotionV5.setCouponCode(obj.getCoupon());
                        orderPromotionV5.setRemark(null);
                        promotions.add(orderPromotionV5);
                    }
                }
            }
        }
        return promotions;
    }

    @Override
    public MsgSendContent propellingPayment(MsgSendRecord msr){
        List<PaymentInfoV5> list = new ArrayList<PaymentInfoV5>();
        Long orderId = msr.getTargetId();

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
                paymentInfoV5.setPaymentTime(payInfoCommand.getModifyTime());
                if (payInfoCommand.getPayType() == payInfoCommand.getMainPayType()
                                || SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT.equals(soCommand.getFinancialStatus())){
                    paymentInfoV5.setAllComplete(true);
                }else{
                    paymentInfoV5.setAllComplete(false);
                }

                Map<String, String> remarkMap = new HashMap<String, String>();
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
        return propellingCommonManager.saveMsgBody(list, msr.getId());
    }

    @Override
    public MsgSendContent propellingSoStatus(MsgSendRecord msr){
        List<OrderStatusV5> list = new ArrayList<OrderStatusV5>();
        OrderStatusV5 orderStatusV5 = new OrderStatusV5();
        Long orderStatusLogId = msr.getTargetId();
        OrderStatusLogCommand orderStatusLogCommand = sdkOrderService.findOrderStatusLogById(orderStatusLogId);
        orderStatusV5.setBsOrderCode(orderStatusLogCommand.getOrderCode());
        //目前 2:订单取消  后续加
        if (orderStatusLogCommand.getAfterStatus().equals(SalesOrder.SALES_ORDER_STATUS_CANCELED)
                        || orderStatusLogCommand.getAfterStatus().equals(SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED)){
            orderStatusV5.setOpType(OrderStatusV5Constants.ORDER_CANCEL);
        }
        orderStatusV5.setOpTime(orderStatusLogCommand.getCreateTime());

        list.add(orderStatusV5);
        return propellingCommonManager.saveMsgBody(list, msr.getId());
    }

}
