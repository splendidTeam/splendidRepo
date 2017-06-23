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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toLong;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

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

    /** The mata info manager. */
    @Autowired
    private MataInfoManager mataInfoManager;

    /** The sales order manager. */
    @Autowired
    private SalesOrderManager salesOrderManager;

    /** The sdk payment manager. */
    @Autowired
    private SdkPaymentManager sdkPaymentManager;

    /** The order manager. */
    @Autowired
    private OrderManager orderManager;

    /** The sales order source resolver. */
    @Autowired
    private SalesOrderSourceResolver salesOrderSourceResolver;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.resolver.SalesOrderResolver#toSalesOrderCommand(com.baozun.nebula.web.MemberDetails, com.baozun.nebula.web.controller.order.form.OrderForm, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public SalesOrderCommand toSalesOrderCommand(MemberDetails memberDetails,OrderForm orderForm,HttpServletRequest request){
        SalesOrderCommand salesOrderCommand = new SalesOrderCommand();

        // 1.设置收货地址信息
        ShippingInfoSubForm shippingInfoSubForm = orderForm.getShippingInfoSubForm();
        setShippingInfo(salesOrderCommand, shippingInfoSubForm);

        // 2. 设置买家信息
        setBuyerInfo(memberDetails, request, salesOrderCommand, shippingInfoSubForm);

        PaymentInfoSubForm paymentInfoSubForm = orderForm.getPaymentInfoSubForm();
        // 3. 设置支付信息
        setPayinfo(salesOrderCommand, paymentInfoSubForm);

        // 设置运费
        setFreghtCommand(salesOrderCommand);

        // 设置优惠券信息
        setCoupon(salesOrderCommand, orderForm.getCouponInfoSubForm().getCouponCode());

        // 发票信息
        InvoiceInfoSubForm invoiceInfoSubForm = orderForm.getInvoiceInfoSubForm();
        // 设置 发票信息
        setInvoiceInfo(salesOrderCommand, invoiceInfoSubForm);

        // 订单来源
        salesOrderCommand.setSource(salesOrderSourceResolver.resolveOrderSource(memberDetails, orderForm, request));
        return salesOrderCommand;
    }

    /**
     * 设置 发票信息.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param invoiceInfoSubForm
     *            the invoice info sub form
     * @since 5.3.1.9
     */
    private void setInvoiceInfo(SalesOrderCommand salesOrderCommand,InvoiceInfoSubForm invoiceInfoSubForm){
        if (isNotNullOrEmpty(invoiceInfoSubForm)){
            if (invoiceInfoSubForm.getIsNeedInvoice()){
                salesOrderCommand.setReceiptTitle(invoiceInfoSubForm.getInvoiceTitle());
                salesOrderCommand.setReceiptContent(invoiceInfoSubForm.getInvoiceContent());
                salesOrderCommand.setReceiptType(invoiceInfoSubForm.getInvoiceType());
                salesOrderCommand.setReceiptConsignee(invoiceInfoSubForm.getConsignee());
                salesOrderCommand.setReceiptAddress(invoiceInfoSubForm.getAddress());
                salesOrderCommand.setReceiptTelphone(invoiceInfoSubForm.getTelphone());
                //5.3.2.18 增加对taxPayerId ：纳税人识别码的转换
                salesOrderCommand.setTaxPayerId(invoiceInfoSubForm.getTaxPayerId());
                //5.3.2.18 增加对companyAddress ：公司地址的转换
                salesOrderCommand.setCompanyAddress(invoiceInfoSubForm.getCompanyAddress());
                //5.3.2.18 增加对companyPhone ：公司电话的转换
                salesOrderCommand.setCompanyPhone(invoiceInfoSubForm.getCompanyPhone());
                //5.3.2.18 增加对accountBankName ：开户银行名称的转换
                salesOrderCommand.setAccountBankName(invoiceInfoSubForm.getAccountBankName());
                //5.3.2.18 增加对accountBankNumber ：开户银行账号的转换
                salesOrderCommand.setAccountBankNumber(invoiceInfoSubForm.getAccountBankNumber());
            }
        }
    }

    /**
     * 设置 支付信息.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param paymentInfoSubForm
     *            the payment info sub form
     * @since 5.3.1.9
     */
    private void setPayinfo(SalesOrderCommand salesOrderCommand,PaymentInfoSubForm paymentInfoSubForm){
        String paymentType = paymentInfoSubForm.getPaymentType();
        salesOrderCommand.setPayment(Integer.parseInt(paymentType));
        salesOrderCommand.setPaymentStr(BankCodeConvertUtil.getPayTypeDetail(paymentInfoSubForm.getBankcode(), paymentType));
    }

    /**
     * 设置买家信息.
     *
     * @param memberDetails
     *            the member details
     * @param request
     *            the request
     * @param salesOrderCommand
     *            the sales order command
     * @param shippingInfoSubForm
     *            the shipping info sub form
     * @since 5.3.1.9
     */
    private void setBuyerInfo(MemberDetails memberDetails,HttpServletRequest request,SalesOrderCommand salesOrderCommand,ShippingInfoSubForm shippingInfoSubForm){
        // 用户信息
        boolean isGuest = isNullOrEmpty(memberDetails);

        salesOrderCommand.setIp(RequestUtil.getClientIp(request));
        salesOrderCommand.setMemberId(isGuest ? null : memberDetails.getGroupId());
        salesOrderCommand.setMemberName(isGuest ? EMPTY : defaultIfNullOrEmpty(memberDetails.getLoginEmail(), memberDetails.getLoginMobile()));
        salesOrderCommand.setBuyerName(shippingInfoSubForm.getBuyerName());
        salesOrderCommand.setBuyerTel(shippingInfoSubForm.getBuyerTel());
        //5.3.2.18增加对客户端识别码属性设置
        salesOrderCommand.setClientIdentificationMechanisms((String)request.getAttribute("clientIdentificationMechanisms"));
    }

    /**
     * 设置收获信息.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param shippingInfoSubForm
     *            the shipping info sub form
     * @since 5.3.1.9
     */
    private void setShippingInfo(SalesOrderCommand salesOrderCommand,ShippingInfoSubForm shippingInfoSubForm){
        PropertyUtil.copyProperties(
                        salesOrderCommand,
                        shippingInfoSubForm, //
                        "countryId",
                        "provinceId",
                        "cityId",
                        "areaId",
                        "townId",
                        "address",
                        "postcode",
                        "mobile",
                        "tel",
                        "email",
                        "name",
                        "appointType",
                        "buyerName",
                        "buyerTel");

        //地址名称
        Address country = AddressUtil.getAddressById(shippingInfoSubForm.getCountryId());
        Address province = AddressUtil.getAddressById(shippingInfoSubForm.getProvinceId());
        Address city = AddressUtil.getAddressById(shippingInfoSubForm.getCityId());
        Address area = AddressUtil.getAddressById(shippingInfoSubForm.getAreaId());
        Address town = AddressUtil.getAddressById(shippingInfoSubForm.getTownId());

        salesOrderCommand.setCountry(country == null ? EMPTY : country.getName());
        salesOrderCommand.setProvince(province == null ? EMPTY : province.getName());
        salesOrderCommand.setCity(city == null ? EMPTY : city.getName());
        salesOrderCommand.setArea(area == null ? EMPTY : area.getName());
        salesOrderCommand.setTown(town == null ? EMPTY : town.getName());
    }

    /**
     * 设置优惠券.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param coupon
     *            the coupon
     */
    private void setCoupon(SalesOrderCommand salesOrderCommand,String coupon){
        if (isNotNullOrEmpty(coupon)){
            // 校验优惠券
            PromotionCouponCode promotionCouponCode = salesOrderManager.validCoupon(coupon);
            if (isNotNullOrEmpty(promotionCouponCode)){
                List<CouponCodeCommand> coupons = new ArrayList<CouponCodeCommand>();
                CouponCodeCommand couponCode = new CouponCodeCommand();
                couponCode.setCouponCode(coupon);
                coupons.add(couponCode);
                salesOrderCommand.setCouponCodes(coupons);
            }
        }
    }

    /**
     * 设置运费.
     *
     * @param salesOrderCommand
     *            the freght command
     */
    private void setFreghtCommand(SalesOrderCommand salesOrderCommand){
        CalcFreightCommand calcFreightCommand = new CalcFreightCommand();
        calcFreightCommand.setProvienceId(salesOrderCommand.getProvinceId());
        calcFreightCommand.setCityId(salesOrderCommand.getCityId());
        calcFreightCommand.setCountyId(salesOrderCommand.getAreaId());
        calcFreightCommand.setTownId(salesOrderCommand.getTownId());

        calcFreightCommand.setDistributionModeId(buildDistributionModeId(salesOrderCommand));

        LOGGER.debug("calcFreightCommand is {}", JsonUtil.format(calcFreightCommand));
        salesOrderCommand.setCalcFreightCommand(calcFreightCommand);
    }

    /**
     * 解析生成 物流方式.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @return 如果有设置参数 {@link com.baozun.nebula.sdk.command.SalesOrderCommand#getAppointType()}, 那么直接转成 long<br>
     *         如果没有传递参数,那么读取db(cache) 默认配置 {@link com.baozun.nebula.constants.MetaInfoConstants#DISTRIBUTION_MODE_ID}<br>
     *         如果还没有,那么返回null
     * @see <a href="http://jira.baozun.cn/browse/NB-377">NB-377</a>
     * @since 5.3.2.3
     */
    private Long buildDistributionModeId(SalesOrderCommand salesOrderCommand){
        //如果有设置参数, 那么直接转成 long
        String appointType = salesOrderCommand.getAppointType();
        if (isNotNullOrEmpty(appointType)){
            return toLong(appointType);
        }

        //如果没有传递参数,那么读取db(cache) 默认配置
        String modeId = mataInfoManager.findValue(MetaInfoConstants.DISTRIBUTION_MODE_ID);// 设置默认的物流方式
        if (isNotNullOrEmpty(modeId)){
            return toLong(modeId);
        }

        //如果还没有,那么返回null
        return null;
    }

    /**
     * 通過支付流水號查詢訂單.
     *
     * @param subOrdinate
     *            the sub ordinate
     * @return the sales order command
     */
    @Override
    public SalesOrderCommand getSalesOrderCommand(String subOrdinate){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subOrdinate", subOrdinate);
        List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

        return orderManager.findOrderById(payInfoLogs.get(0).getOrderId(), SalesOrder.SALES_ORDER_STATUS_NEW);
    }
}
