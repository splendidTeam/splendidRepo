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
package com.baozun.nebula.web.controller.order.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.constants.MetaInfoConstants;
import com.baozun.nebula.manager.salesorder.SalesOrderManager;
import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.utils.BankCodeConvertUtil;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.InvoiceInfoSubForm;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.form.PaymentInfoSubForm;
import com.baozun.nebula.web.controller.order.form.ShippingInfoSubForm;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class AbstractShoppingcartResolver.
 *
 * @author weihui.tang
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月3日 下午1:35:48
 * @since 5.3.1
 */
@Component
public class SalesOrderResolverImpl implements SalesOrderResolver{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesOrderResolverImpl.class);

    @Autowired
    private MataInfoManager     mataInfoManager;

    @Autowired
    private SalesOrderManager   salesOrderManager;

    @Autowired
    private SdkPaymentManager   sdkPaymentManager;

    @Autowired
    private OrderManager        orderManager;

    @Override
    public SalesOrderCommand buildSalesOrderCommand(MemberDetails memberDetails,OrderForm orderForm,HttpServletRequest request){
        // 需要封装的对象
        SalesOrderCommand salesOrderCommand = new SalesOrderCommand();
        // 设置收货地址信息
        ShippingInfoSubForm shippingInfoSubForm = orderForm.getShippingInfoSubForm();
        PropertyUtil.copyProperties(
                        salesOrderCommand,
                        shippingInfoSubForm,
                        "countryId",
                        "provinceId",
                        "cityId",
                        "areaId",
                        "townId",
                        "address",
                        "postcode",
                        "mobile",
                        "tel",
                        "email");

        //地址名称
        Address country = AddressUtil.getAddressById(shippingInfoSubForm.getCountryId());
        Address province = AddressUtil.getAddressById(shippingInfoSubForm.getProvinceId());
        Address city = AddressUtil.getAddressById(shippingInfoSubForm.getCityId());
        Address area = AddressUtil.getAddressById(shippingInfoSubForm.getAreaId());
        Address town = AddressUtil.getAddressById(shippingInfoSubForm.getTownId());
        salesOrderCommand.setCountry(country == null ? "" : country.getName());
        salesOrderCommand.setProvince(province == null ? "" : province.getName());
        salesOrderCommand.setCity(city == null ? "" : city.getName());
        salesOrderCommand.setArea(area == null ? "" : area.getName());
        salesOrderCommand.setTown(town == null ? "" : town.getName());

        // 用户信息
        boolean isGuest = Validator.isNullOrEmpty(memberDetails);
        //        salesOrderCommand.setName(isGuest ? "" : memberDetails.getNickName());
        salesOrderCommand.setName(shippingInfoSubForm.getName());
        salesOrderCommand.setMemberName(isGuest ? "" : memberDetails.getLoginName());
        salesOrderCommand.setIp(RequestUtil.getClientIp(request));
        salesOrderCommand.setMemberId(isGuest ? null : memberDetails.getGroupId());
        salesOrderCommand.setBuyerName(shippingInfoSubForm.getBuyerName());
        salesOrderCommand.setBuyerTel(shippingInfoSubForm.getBuyerTel());
        // 设置支付信息
        PaymentInfoSubForm paymentInfoSubForm = orderForm.getPaymentInfoSubForm();
        String paymentType = paymentInfoSubForm.getPaymentType();
        salesOrderCommand.setPayment(Integer.parseInt(paymentType));
        salesOrderCommand.setPaymentStr(
                        BankCodeConvertUtil.getPayTypeDetail(paymentInfoSubForm.getBankcode(), Integer.parseInt(paymentType)));
        // 设置运费
        setFreghtCommand(salesOrderCommand);
        // 设置优惠券信息
        setCoupon(salesOrderCommand, orderForm.getCouponInfoSubForm().getCouponCode());

        // 发票信息
        InvoiceInfoSubForm invoiceInfoSubForm = orderForm.getInvoiceInfoSubForm();
        if (Validator.isNotNullOrEmpty(invoiceInfoSubForm)){
            if (invoiceInfoSubForm.getIsNeedInvoice()){
                salesOrderCommand.setReceiptTitle(invoiceInfoSubForm.getInvoiceTitle());
                salesOrderCommand.setReceiptContent(invoiceInfoSubForm.getInvoiceContent());
                salesOrderCommand.setReceiptType(invoiceInfoSubForm.getInvoiceType());
                salesOrderCommand.setReceiptConsignee(invoiceInfoSubForm.getConsignee());
                salesOrderCommand.setReceiptAddress(invoiceInfoSubForm.getAddress());
                salesOrderCommand.setReceiptTelphone(invoiceInfoSubForm.getTelphone());
            }
        }

        // 订单来源
        salesOrderCommand.setSource(SalesOrder.SO_SOURCE_NORMAL);
        return salesOrderCommand;
    }

    /**
     * 设置优惠券
     * 
     * @param salesOrderCommand
     * @param coupon
     */
    private void setCoupon(SalesOrderCommand salesOrderCommand,String coupon){
        if (Validator.isNotNullOrEmpty(coupon)){
            // 校验优惠券
            PromotionCouponCode promotionCouponCode = salesOrderManager.validCoupon(coupon);
            if (Validator.isNotNullOrEmpty(promotionCouponCode)){
                List<CouponCodeCommand> coupons = new ArrayList<CouponCodeCommand>();
                CouponCodeCommand couponCode = new CouponCodeCommand();
                couponCode.setCouponCode(coupon);
                coupons.add(couponCode);
                salesOrderCommand.setCouponCodes(coupons);
            }
        }
    }

    /**
     * 设置运费
     * 
     * @param salesOrderCommand
     */
    private void setFreghtCommand(SalesOrderCommand salesOrderCommand){
        CalcFreightCommand calcFreightCommand = new CalcFreightCommand();
        calcFreightCommand.setProvienceId(salesOrderCommand.getProvinceId());
        calcFreightCommand.setCityId(salesOrderCommand.getCityId());
        calcFreightCommand.setCountyId(salesOrderCommand.getAreaId());
        calcFreightCommand.setTownId(salesOrderCommand.getTownId());
        // 设置默认的物流方式
        String modeId = mataInfoManager.findValue(MetaInfoConstants.DISTRIBUTION_MODE_ID);
        if (modeId != null){
            calcFreightCommand.setDistributionModeId(Long.parseLong(modeId));
        }

        LOGGER.info("calcFreightCommand is {}", JsonUtil.format(calcFreightCommand));

        salesOrderCommand.setCalcFreightCommand(calcFreightCommand);
    }

    /**
     * 通過支付流水號查詢訂單
     * 
     * @param subOrdinate
     * @return
     */
    @Override
    public SalesOrderCommand getSalesOrderCommand(String subOrdinate){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subOrdinate", subOrdinate);
        List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

        return orderManager.findOrderById(payInfoLogs.get(0).getOrderId(), SalesOrder.SALES_ORDER_STATUS_NEW);
    }
}
