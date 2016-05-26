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
package com.baozun.nebula.sdk.manager.order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.event.EmailEvent;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.handler.SalesOrderHandler;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.feilong.core.Validator;

/**
 * The Class SdkOrderCreateEmailManagerImpl.
 *
 * @author feilong
 * @version 5.3.1 2016年5月13日 下午8:27:25
 * @since 5.3.1
 */
@Transactional
@Service("sdkOrderEmailManager")
public class SdkOrderEmailManagerImpl implements SdkOrderEmailManager{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkOrderEmailManagerImpl.class);

    /** The page url base. */
    @Value("#{meta['page.base']}")
    private String              pageUrlBase;

    /** The img domain url. */
    @Value("#{meta['upload.img.domain.base']}")
    private String              imgDomainUrl;

    /** The frontend base url. */
    @Value("#{meta['frontend.url']}")
    private String              frontendBaseUrl;

    /** The sdk mata info manager. */
    @Autowired
    private SdkMataInfoManager  sdkMataInfoManager;

    /** The sdk member manager. */
    @Autowired
    private SdkMemberManager    sdkMemberManager;

    /** The sales order handler. */
    @Autowired(required = false)
    private SalesOrderHandler   salesOrderHandler;

    /** The event publisher. */
    @Autowired
    private EventPublisher      eventPublisher;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkOrderCreateEmailManager#sendEmailOfCreateOrder(java.util.List)
     */
    @Override
    public void sendEmailOfCreateOrder(List<Map<String, Object>> dataMapList){
        if (Validator.isNotNullOrEmpty(dataMapList)){
            for (Map<String, Object> dataMap : dataMapList){
                String email = dataMap.get("email").toString();
                String createOrderSuccess = EmailConstants.CREATE_ORDER_SUCCESS;
                EmailEvent emailEvent = new EmailEvent(this, email, createOrderSuccess, dataMap);
                eventPublisher.publish(emailEvent);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkOrderCreateEmailManager#sendEmail(java.lang.String, java.lang.String, java.util.Map)
     */
    @Override
    public void sendEmail(String emailTemplete,String email,Map<String, Object> dataMap){
        EmailEvent emailEvent = new EmailEvent(this, email, emailTemplete, dataMap);
        eventPublisher.publish(emailEvent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkOrderCreateEmailManager#getDataMap(java.lang.String,
     * com.baozun.nebula.model.salesorder.SalesOrder, com.baozun.nebula.sdk.command.SalesOrderCommand, java.util.List,
     * com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop, java.util.List)
     */
    @Override
    public Map<String, Object> buildDataMapForCreateOrder(
                    String subOrdinate,
                    SalesOrder salesOrder,
                    SalesOrderCommand salesOrderCommand,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShopCartCommandByShop shopCartCommandByShop,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    SalesOrderCreateOptions salesOrderCreateOptions){

        // 后台下单 并且填写了 邮件地址
        Long memberId = salesOrderCommand.getMemberId();
        boolean isGuest = Validator.isNullOrEmpty(memberId);

        String email = salesOrderCommand.getEmail();
        String nickName = salesOrderCommand.getName();
        if (!isGuest){
            MemberPersonalData memberPersonalData = sdkMemberManager.findMemberPersonData(salesOrder.getMemberId());

            if (Validator.isNullOrEmpty(email)){
                email = memberPersonalData.getEmail();
            }

            nickName = memberPersonalData.getNickname();
            if (Validator.isNullOrEmpty(nickName)){
                nickName = salesOrderCommand.getName();
            }
        }

        if (Validator.isNullOrEmpty(email)){
            return null;
        }

        //***************************************************************************************

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("nickName", nickName);
        dataMap.put("email", email);

        // 获取付款地址
        if (salesOrderCreateOptions.getIsBackCreateOrder()
                        && !salesOrderCommand.getPayment().toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD)){
            String payUrlPrefix = sdkMataInfoManager.findValue(MataInfo.PAY_URL_PREFIX);
            String payUrl = frontendBaseUrl + payUrlPrefix + "?code=" + subOrdinate;
            dataMap.put("isShowPayButton", true);
            dataMap.put("payUrl", payUrl);
        }else{
            dataMap.put("isShowPayButton", false);
            dataMap.put("payUrl", "#");
        }
        Date createTime = salesOrder.getCreateTime();
        dataMap.put("createDateOfAll", sdf.format(createTime));
        dataMap.put("createDateOfSfm", sdf1.format(createTime));

        dataMap.put("receiveName", salesOrderCommand.getName());
        dataMap.put("orderCode", salesOrder.getCode());
        dataMap.put("ssqStr", salesOrderCommand.getProvince() + salesOrderCommand.getCity() + salesOrderCommand.getArea());

        dataMap.put("address", salesOrderCommand.getAddress());
        String mobile = salesOrderCommand.getMobile();
        dataMap.put(
                        "mobile",
                        Validator.isNotNullOrEmpty(mobile) ? mobile
                                        : salesOrderCommand.getTel());

        dataMap.put("payment", getPaymentName(salesOrderCommand.getPayment()));

        BigDecimal total = salesOrder.getTotal();
        dataMap.put("sumItemFee", total.add(salesOrder.getDiscount()));
        dataMap.put("sumPay", total.add(salesOrder.getActualFreight()));

        dataMap.put("shipFee", salesOrder.getActualFreight());
        dataMap.put("offersTotal", salesOrder.getDiscount());

        dataMap.put("itemLines", shoppingCartLineCommandList);
        dataMap.put("pageUrlBase", pageUrlBase);
        dataMap.put("imgDomainUrl", imgDomainUrl);

        /** 扩展点 如商城需要需要加入特殊字段 各自实现 **/
        if (null != salesOrderHandler){
            dataMap = salesOrderHandler.getEmailDataOfCreateOrder(
                            subOrdinate,
                            salesOrder,
                            salesOrderCommand,
                            shoppingCartLineCommandList,
                            shopCartCommandByShop,
                            promotionSKUDiscAMTBySettingList,
                            dataMap);
        }
        return dataMap;
    }

    /**
     * 返回支付方式.
     *
     * @param payment
     *            the payment
     * @return the payment name
     */
    private String getPaymentName(Integer payment){
        String name = "支付宝";
        if (payment == Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_COD)){
            name = "货到付款";
        }else if (payment == Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_NETPAY)){
            name = "网银在线";
        }else if (payment == Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_ALIPAY)){
            name = "支付宝";
        }
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkOrderCreateEmailManager#getDataMap(java.lang.String,
     * com.baozun.nebula.sdk.command.SalesOrderCommand, java.lang.String)
     */
    @Override
    public Map<String, Object> buildDataMap(String emailTemplete,SalesOrderCommand salesOrderCommand,String nickName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("nickName", nickName);
        dataMap.put("createDateOfAll", sdf.format(salesOrderCommand.getCreateTime()));
        dataMap.put("createDateOfSfm", sdf1.format(salesOrderCommand.getCreateTime()));
        dataMap.put("orderCode", salesOrderCommand.getCode());
        dataMap.put("receiveName", salesOrderCommand.getName());
        dataMap.put("ssqStr", salesOrderCommand.getProvince() + salesOrderCommand.getCity() + salesOrderCommand.getArea());
        dataMap.put("address", salesOrderCommand.getAddress());

        dataMap.put(
                        "mobile",
                        Validator.isNotNullOrEmpty(salesOrderCommand.getMobile()) ? salesOrderCommand.getMobile()
                                        : salesOrderCommand.getTel());

        dataMap.put("sumItemFee", salesOrderCommand.getTotal().add(salesOrderCommand.getDiscount()));
        dataMap.put("shipFee", salesOrderCommand.getActualFreight());
        dataMap.put("offersTotal", salesOrderCommand.getDiscount());
        dataMap.put("sumPay", salesOrderCommand.getTotal().add(salesOrderCommand.getActualFreight()));
        dataMap.put("itemLines", salesOrderCommand.getOrderLines());
        dataMap.put("payment", getPaymentName(salesOrderCommand.getPayment()));

        dataMap.put("pageUrlBase", pageUrlBase);
        dataMap.put("imgDomainUrl", imgDomainUrl);
        dataMap.put("logisticsProvider", salesOrderCommand.getLogisticsProviderName());
        dataMap.put("transCode", salesOrderCommand.getTransCode());
        dataMap.put("nowDay", sdf1.format(calendar.getTime()));

        //扩展点 如 商城需要需要加入特殊字段 各自实现 **/
        if (null != salesOrderHandler){
            dataMap = salesOrderHandler.getEmailData(salesOrderCommand, dataMap, emailTemplete);
        }
        return dataMap;
    }

}
