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
package com.baozun.nebula.web.controller.order.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.baozun.nebula.manager.salesorder.OrderLineManager;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.baozun.nebula.web.controller.order.viewcommand.ConsigneeSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.CouponInfoSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.InvoiceInfoSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.LogisticsInfoBarRecordSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.LogisticsInfoSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.OrderBaseInfoSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.OrderLineSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.OrderViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.PaymentInfoSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderLineSubViewCommand;
import com.feilong.core.DatePattern;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.date.DateUtil;
import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Component("orderViewCommandBuilder")
public class DefaultOrderViewCommandBuilder implements OrderViewCommandBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderViewCommandBuilder.class);

    /** The logistics manager. */
    @Autowired
    @Qualifier("logisticsManager")
    private LogisticsManager logisticsManager;

    /** The order line manager. */
    @Autowired
    @Qualifier("OrderLineManager")
    private OrderLineManager orderLineManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.OrderViewCommandBuilder#build(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public OrderViewCommand build(SalesOrderCommand salesOrderCommand){
        OrderBaseInfoSubViewCommand orderBaseInfoSubViewCommand = buildOrderBaseInfoSubViewCommand(salesOrderCommand);

        OrderViewCommand orderViewCommand = new OrderViewCommand();
        orderViewCommand.setConsigneeSubViewCommand(buildConsigneeSubViewCommand(salesOrderCommand));
        orderViewCommand.setCouponInfoSubViewCommand(buildCouponInfoSubViewCommand(salesOrderCommand));
        orderViewCommand.setInvoiceInfoSubViewCommand(buildInvoiceInfoSubViewCommand(salesOrderCommand));
        orderViewCommand.setLogisticsInfoSubViewCommand(buildLogisticsInfoSubViewCommand(salesOrderCommand));
        orderViewCommand.setOrderBaseInfoSubViewCommand(orderBaseInfoSubViewCommand);
        orderViewCommand.setPaymentInfoSubViewCommand(buildPaymentInfoSubViewCommand(salesOrderCommand, orderBaseInfoSubViewCommand));
        orderViewCommand.setOrderLineSubViewCommandList(buildOrderLineSubViewCommandlist(salesOrderCommand));
        return orderViewCommand;
    }

    /**
     * @param salesOrderCommand
     * @return
     */
    protected List<OrderLineSubViewCommand> buildOrderLineSubViewCommandlist(SalesOrderCommand salesOrderCommand){
        // orderline信息
        List<SimpleOrderLineSubViewCommand> simpleOrderLineSubViewCommand = orderLineManager.findByOrderID(salesOrderCommand.getId());
        return CollectionsUtil.collect(simpleOrderLineSubViewCommand, transformer(OrderLineSubViewCommand.class));
    }

    private static <I, O> Transformer<I, O> transformer(final Class<O> type,final String...includePropertyNames){
        return new Transformer<I, O>(){

            @Override
            public O transform(I inputBean){
                Validate.notNull(inputBean, "inputBean can't be null!");

                O outBean = ConstructorUtil.newInstance(type);

                PropertyUtil.copyProperties(outBean, inputBean, includePropertyNames);
                return outBean;
            }
        };
    }

    /**
     * @param salesOrderCommand
     * @return
     */
    protected LogisticsInfoSubViewCommand buildLogisticsInfoSubViewCommand(SalesOrderCommand salesOrderCommand){
        // 物流信息
        LogisticsInfoSubViewCommand logisticsInfoSubViewCommand = new LogisticsInfoSubViewCommand();
        LogisticsCommand logisticsCommand = logisticsManager.findLogisticsByOrderId(salesOrderCommand.getId());
        PropertyUtil.copyProperties(logisticsInfoSubViewCommand, salesOrderCommand, "transCode", "logisticsProviderName");
        if (null != logisticsCommand){
            String trackingDescription = logisticsCommand.getTrackingDescription();
            logisticsInfoSubViewCommand.setLogisticsInfoBarRecordSubViewCommandList(transformTrackingDescription(trackingDescription));
        }
        return logisticsInfoSubViewCommand;
    }

    /**
     * @param salesOrderCommand
     * @return
     */
    protected OrderBaseInfoSubViewCommand buildOrderBaseInfoSubViewCommand(SalesOrderCommand salesOrderCommand){
        // 订单信息
        OrderBaseInfoSubViewCommand orderBaseInfoSubViewCommand = new OrderBaseInfoSubViewCommand();

        PropertyUtil.copyProperties(orderBaseInfoSubViewCommand, salesOrderCommand, "createTime", "logisticsStatus", "financialStatus", "total", "discount", "actualFreight");

        orderBaseInfoSubViewCommand.setOrderId(salesOrderCommand.getId());
        orderBaseInfoSubViewCommand.setOrderCode(salesOrderCommand.getCode());
        return orderBaseInfoSubViewCommand;
    }

    /**
     * @param salesOrderCommand
     * @return
     */
    protected InvoiceInfoSubViewCommand buildInvoiceInfoSubViewCommand(SalesOrderCommand salesOrderCommand){
        // 发票信息
        InvoiceInfoSubViewCommand invoiceInfoSubViewCommand = new InvoiceInfoSubViewCommand();
        PropertyUtil.copyProperties(invoiceInfoSubViewCommand, salesOrderCommand, "receiptType", "receiptTitle", "receiptContent", "receiptCode", "receiptConsignee", "receiptTelphone", "receiptAddress");
        return invoiceInfoSubViewCommand;
    }

    /**
     * @param salesOrderCommand
     * @return
     */
    protected CouponInfoSubViewCommand buildCouponInfoSubViewCommand(SalesOrderCommand salesOrderCommand){
        // 优惠券信息
        CouponInfoSubViewCommand couponInfoSubViewCommand = new CouponInfoSubViewCommand();
        List<OrderPromotionCommand> orderPromotions = salesOrderCommand.getOrderPromotions();
        if (null != orderPromotions && orderPromotions.size() != 0){
            String coupon = orderPromotions.get(0).getCoupon();
            if (Validator.isNotNullOrEmpty(coupon)){
                coupon = coupon.replace("[", "").replace("]", "");
                couponInfoSubViewCommand.setCouponCode(coupon);
            }
        }
        return couponInfoSubViewCommand;
    }

    /**
     * @param salesOrderCommand
     * @param orderBaseInfoSubViewCommand
     * @return
     */
    protected PaymentInfoSubViewCommand buildPaymentInfoSubViewCommand(SalesOrderCommand salesOrderCommand,OrderBaseInfoSubViewCommand orderBaseInfoSubViewCommand){
        // 支付信息
        PaymentInfoSubViewCommand paymentInfoSubViewCommand = new PaymentInfoSubViewCommand();
        PropertyUtil.copyProperties(paymentInfoSubViewCommand, salesOrderCommand, "payment");
        if (orderBaseInfoSubViewCommand.getFinancialStatus() == 1){
            paymentInfoSubViewCommand.setSubOrdinate(salesOrderCommand.getPayInfo().get(0).getSubOrdinate());
        }
        return paymentInfoSubViewCommand;
    }

    /**
     * @param salesOrderCommand
     * @return
     */
    protected ConsigneeSubViewCommand buildConsigneeSubViewCommand(SalesOrderCommand salesOrderCommand){
        // 收货地址信息
        ConsigneeSubViewCommand consigneeSubViewCommand = new ConsigneeSubViewCommand();
        PropertyUtil.copyProperties(consigneeSubViewCommand, salesOrderCommand, "name", "address", "mobile", "tel", "email", "postcode", "buyerTel", "buyerName");

        Address country = AddressUtil.getAddressById(salesOrderCommand.getCountryId());
        Address province = AddressUtil.getAddressById(salesOrderCommand.getProvinceId());
        Address city = AddressUtil.getAddressById(salesOrderCommand.getCityId());
        Address area = AddressUtil.getAddressById(salesOrderCommand.getAreaId());

        consigneeSubViewCommand.setCountry(country == null ? null : country.getName());
        consigneeSubViewCommand.setProvince(province == null ? null : province.getName());
        consigneeSubViewCommand.setCity(city == null ? null : city.getName());
        consigneeSubViewCommand.setArea(area == null ? null : area.getName());
        return consigneeSubViewCommand;
    }

    /**
     * 说明：String物流信息转换为 List<LogisticsInfoBarRecordSubViewCommand>.
     *
     * @author 张乃骐
     * @param TrackingDescription
     *            the Tracking description
     * @return the list< logistics info bar record sub view command>
     * @time：2016年5月12日 下午8:28:56
     */
    private static List<LogisticsInfoBarRecordSubViewCommand> transformTrackingDescription(String TrackingDescription){
        if (Validator.isNullOrEmpty(TrackingDescription)){
            return null;
        }
        List<LogisticsInfoBarRecordSubViewCommand> list = new ArrayList<>();
        // String[] split = StringUtils.split(TrackingDescription, "<br/>");
        String[] split = TrackingDescription.split("<br/>");
        for (String string : split){
            LogisticsInfoBarRecordSubViewCommand logisticsInfoBarRecordSubViewCommand = new LogisticsInfoBarRecordSubViewCommand();
            //String date =  StringUtils.substring(string, 0, 17) ;
            String date = StringUtils.trimToEmpty(StringUtils.substring(string, 0, 17));
            logisticsInfoBarRecordSubViewCommand.setBarScanDate(DateUtil.toDate(date, DatePattern.COMMON_DATE_AND_TIME_WITHOUT_SECOND));
            logisticsInfoBarRecordSubViewCommand.setRemark(StringUtils.substring(string, 17));
            list.add(logisticsInfoBarRecordSubViewCommand);
        }
        Collections.reverse(list);
        return list;
    }
}
