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
package com.baozun.nebula.web.controller.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.manager.salesorder.OrderLineManager;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
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
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * myaccount Orderdetail 相关方法controller
 * 
 * <ol>
 * <li>{@link #showOrderDetails(MemberDetails, String, HttpServletRequest, Model)} 进入订单详情页</li>
 * </ol>
 * 
 * <h3>showOrderDetails方法,主要有以下几点:</h3> <blockquote>
 * <ol>
 * <li>当会员查询时传入MemberDetails MemberDetails必须有memberid</li>
 * <li>当游客查询时构造MemberDetails,并将收货人姓名setRealName()中便可进行查询</li>
 * </ol>
 * </blockquote>.
 *
 * @author 张乃骐
 * @version 1.0
 * @date 2016年5月10日
 */
public class NebulaOrderDetailsController extends BaseController {

    /** The order manager. */
    @Autowired
    private OrderManager orderManager;

    /** The order line manager. */
    @Autowired
    @Qualifier("OrderLineManager")
    private OrderLineManager orderLineManager;

    /** The logistics manager. */
    @Autowired
    @Qualifier("logisticsManager")
    private LogisticsManager logisticsManager;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaOrderDetailsController.class);

    /**
     * 显示订单明细.当memberid和后台查出的memberid相同或者收货人名字和查处的收货人名字相同才进行显示
     * 
     * 当会员查询时传入memberid
     * 
     * 当游客查询是构造MemberDetails,并将收货人姓名setRealName()中便可进行查询
     * 
     * 成功则构造返回订单详情页面
     * 
     * 失败返回空的字符串,商城端去进行判断
     *
     * @param memberDetails the member details
     * @param orderCode the order code
     * @param request the request
     * @param model the model
     * @return the string
     * @since 5.3.1
     * @NeedLogin (guest=true)
     * @RequestMapping(value = "order/{orderCode}", method = RequestMethod.GET)
     */
    public String showOrderDetails(@LoginMember MemberDetails memberDetails,
            @RequestParam(value = "orderCode", required = true) String orderCode, HttpServletRequest request,
            Model model) {
        Validate.notNull(memberDetails, "memberDetails can't be null!");

        // 通过orderCode查询 command
        SalesOrderCommand salesOrderCommand = orderManager.findOrderByCode(orderCode, 1);
        Validate.notNull(salesOrderCommand, "salesOrderCommand can't be null!");

        // 判断是否为本人进行操作
        boolean isSelfOrder = isSelfOrder(memberDetails, salesOrderCommand);
        Validate.isTrue(isSelfOrder, "order:%s,is not his self:[{}] order", orderCode,
                JsonUtil.format(memberDetails, 0, 0));

        OrderViewCommand orderViewCommand = toOrderViewCommand(salesOrderCommand);
        model.addAttribute("orderViewCommand", orderViewCommand);
        return "order.orderdetails";

    }

    /**
     * @param salesOrderCommand
     * @return
     * @since 5.3.1
     */
    private OrderViewCommand toOrderViewCommand(SalesOrderCommand salesOrderCommand) {
        // 订单信息
        OrderBaseInfoSubViewCommand orderBaseInfoSubViewCommand = new OrderBaseInfoSubViewCommand();
        PropertyUtil.copyProperties(orderBaseInfoSubViewCommand, salesOrderCommand, "createTime",
                "logisticsStatus", "financialStatus", "total", "discount", "actualFreight");
        orderBaseInfoSubViewCommand.setOrderId(salesOrderCommand.getId());
        orderBaseInfoSubViewCommand.setOrderCode(salesOrderCommand.getCode());
        // 收货地址信息
        ConsigneeSubViewCommand consigneeSubViewCommand = new ConsigneeSubViewCommand();
        PropertyUtil.copyProperties(consigneeSubViewCommand, salesOrderCommand, "name", "address", "mobile",
                "tel", "email", "postcode", "buyerTel", "buyerName");
        Address country = AddressUtil.getAddressById(salesOrderCommand.getCountryId());
        Address province = AddressUtil.getAddressById(salesOrderCommand.getProvinceId());
        Address city = AddressUtil.getAddressById(salesOrderCommand.getCityId());
        Address area = AddressUtil.getAddressById(salesOrderCommand.getAreaId());
        consigneeSubViewCommand.setCountry(country == null ? null : country.getName());
        consigneeSubViewCommand.setProvince(province == null ? null : province.getName());
        consigneeSubViewCommand.setCity(city == null ? null : city.getName());
        consigneeSubViewCommand.setArea(area == null ? null : area.getName());
        // 支付信息
        PaymentInfoSubViewCommand paymentInfoSubViewCommand = new PaymentInfoSubViewCommand();
        PropertyUtil.copyProperties(paymentInfoSubViewCommand, salesOrderCommand, "payment");
        if (orderBaseInfoSubViewCommand.getFinancialStatus() == 1) {
            paymentInfoSubViewCommand.setSubOrdinate(salesOrderCommand.getPayInfo().get(0).getSubOrdinate());
        }
        // 优惠券信息
        CouponInfoSubViewCommand couponInfoSubViewCommand = new CouponInfoSubViewCommand();
        List<OrderPromotionCommand> orderPromotions = salesOrderCommand.getOrderPromotions();
        if (null != orderPromotions && orderPromotions.size() != 0) {
            String coupon = orderPromotions.get(0).getCoupon();
            if(Validator.isNotNullOrEmpty(coupon)){
                coupon=coupon.replace("[", "").replace("]", "");
                couponInfoSubViewCommand.setCouponCode(coupon);
            }
        }
        // 发票信息
        InvoiceInfoSubViewCommand invoiceInfoSubViewCommand = new InvoiceInfoSubViewCommand();
        PropertyUtil.copyProperties(invoiceInfoSubViewCommand, salesOrderCommand, "receiptType",
                "receiptTitle", "receiptContent", "receiptCode", "receiptConsignee", "receiptTelphone",
                "receiptAddress");
        // ordline信息
        List<SimpleOrderLineSubViewCommand> simpleOrderLineSubViewCommand = orderLineManager
                .findByOrderID(salesOrderCommand.getId());
        List<OrderLineSubViewCommand> orderLineSubViewCommandlist = new ArrayList<OrderLineSubViewCommand>();
        for (SimpleOrderLineSubViewCommand simpleOrderLine : simpleOrderLineSubViewCommand) {
            OrderLineSubViewCommand orderLineSubViewCommand = new OrderLineSubViewCommand();
            PropertyUtil.copyProperties(orderLineSubViewCommand, simpleOrderLine, "id", "addTime", "itemId",
                    "itemCode", "itemName", "skuId", "extentionCode", "propertiesMap", "skuPropertys",
                    "quantity", "itemPic", "salePrice", "listPrice", "subTotalAmt");
            orderLineSubViewCommandlist.add(orderLineSubViewCommand);
        }
        // 物流信息
        LogisticsInfoSubViewCommand logisticsInfoSubViewCommand = new LogisticsInfoSubViewCommand();
        LogisticsCommand logisticsCommand = logisticsManager
                .findLogisticsByOrderId(salesOrderCommand.getId());
        PropertyUtil.copyProperties(logisticsInfoSubViewCommand, salesOrderCommand, "transCode",
                "logisticsProviderName");
        if (null != logisticsCommand) {
            String trackingDescription = logisticsCommand.getTrackingDescription();
            logisticsInfoSubViewCommand.setLogisticsInfoBarRecordSubViewCommandList(
                    transformTrackingDescription(trackingDescription));
        }
        OrderViewCommand orderViewCommand = new OrderViewCommand();
        orderViewCommand.setConsigneeSubViewCommand(consigneeSubViewCommand);
        orderViewCommand.setCouponInfoSubViewCommand(couponInfoSubViewCommand);
        orderViewCommand.setInvoiceInfoSubViewCommand(invoiceInfoSubViewCommand);
        orderViewCommand.setLogisticsInfoSubViewCommand(logisticsInfoSubViewCommand);
        orderViewCommand.setOrderBaseInfoSubViewCommand(orderBaseInfoSubViewCommand);
        orderViewCommand.setPaymentInfoSubViewCommand(paymentInfoSubViewCommand);
        orderViewCommand.setOrderLineSubViewCommandList(orderLineSubViewCommandlist);
        return orderViewCommand;
    }

    /**
     * 说明：判断是否可以查询订单
     * 
     * memberid相同 or 收货人姓名相同.
     *
     * @author 张乃骐
     * @param memberDetails the member details
     * @param salesOrderCommand the sales order command
     * @return true, if validate order<br>
     * @time：2016年5月23日 下午2:56:06
     */
    protected boolean isSelfOrder(MemberDetails memberDetails, SalesOrderCommand salesOrderCommand) {
        Long memberId = salesOrderCommand.getMemberId();
        String name = salesOrderCommand.getName();

        if (null != memberDetails.getMemberId()) {
            return memberDetails.getMemberId().longValue() == memberId.longValue();
        }
        if (Validator.isNotNullOrEmpty(memberDetails.getRealName())) {
            return memberDetails.getRealName().equals(name);
        }
        return false;
    }

    /**
     * 说明：String物流信息转换为 List<LogisticsInfoBarRecordSubViewCommand>.
     *
     * @author 张乃骐
     * @param TrackingDescription the Tracking description
     * @return the list< logistics info bar record sub view command>
     * @time：2016年5月12日 下午8:28:56
     */
    private List<LogisticsInfoBarRecordSubViewCommand> transformTrackingDescription(
            String TrackingDescription) {
        if (Validator.isNullOrEmpty(TrackingDescription)) {
            return null;
        }
        List<LogisticsInfoBarRecordSubViewCommand> list = new ArrayList<LogisticsInfoBarRecordSubViewCommand>();
        // String[] split = StringUtils.split(TrackingDescription, "<br/>");
        String[] split = TrackingDescription.split("<br/>");
        for (String string : split) {
            LogisticsInfoBarRecordSubViewCommand logisticsInfoBarRecordSubViewCommand = new LogisticsInfoBarRecordSubViewCommand();
            String date = StringUtils.substring(string, 0, 17);
            logisticsInfoBarRecordSubViewCommand.setBarScanDate(
                    DateUtil.string2Date(date, DatePattern.COMMON_DATE_AND_TIME_WITHOUT_SECOND));
            logisticsInfoBarRecordSubViewCommand.setRemark(StringUtils.substring(string, 17));
            list.add(logisticsInfoBarRecordSubViewCommand);
        }
        Collections.reverse(list);
        return list;
    }
}
