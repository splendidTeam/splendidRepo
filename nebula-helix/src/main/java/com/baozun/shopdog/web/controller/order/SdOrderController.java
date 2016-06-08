/**
 * Copyright (c) 2015 Jumbomart All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Jumbomart. You shall not
 * disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Jumbo.
 * 
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY
 * DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 * 
 */
/*
 * Copyright by Deppon and the original author or authors.
 * 
 * This document only allow internal use ,Any of your behaviors using the file not internal will pay
 * legal responsibility.
 *
 * You may learn more information about Deppon from
 *
 * http://www.deppon.com
 *
 */
package com.baozun.shopdog.web.controller.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.constants.MetaInfoConstants;
import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.manager.order.SdkOrderCreateManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResolver;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder;
import com.baozun.shopdog.web.controller.order.viewcommand.ShopdogOrderCommand;
import com.baozun.shopdog.web.controller.order.viewcommand.ShopdogOrderParamCommand;
import com.baozun.shopdog.web.controller.order.viewcommand.ShopdogSettlementCommand;
import com.baozun.shopdog.web.controller.order.viewcommand.ShopdogSkusCommand;
import com.baozun.shopdog.web.controller.payment.AbstractSdPaymentController;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * shopdog订单预算信息、创建订单、查询支付状态方法的实现
 * 
 * @author 江家雷
 * @date 2016年5月24日 下午2:20:23
 * @since
 */
public class SdOrderController implements AbstractSdOrderController {

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdOrderController.class);

    @Autowired
    AbstractSdPaymentController sdPaymentController;

    @Autowired
    private MataInfoManager mataInfoManager;

    @Autowired
    private SdkOrderCreateManager sdkOrderCreateManager;

    @Autowired
    private SalesOrderResolver salesOrderResolver;

    @Autowired
    private ShoppingCartCommandBuilder shoppingCartCommandBuilder;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private SdkMemberManager sdkMemberManager;

    @Override
    public ShopdogSettlementCommand buildShopdogSettlementCommand(ShopdogOrderParamCommand shopdogOrderParamCommand) {
        return getShopdogSettlementCommand(getCalcFreightCommand(shopdogOrderParamCommand), shopdogOrderParamCommand);
    }

    @Override
    public ShopdogOrderCommand saveOrder(HttpServletRequest request, ShopdogOrderParamCommand shopdogOrderParamCommand) {
        // 封装订单信息
        SalesOrderCommand salesOrderCommand = new SalesOrderCommand();
        PropertyUtil.copyProperties(salesOrderCommand, shopdogOrderParamCommand, "countryId", "provinceId", "cityId", "areaId", "townId", "postcode", "mobile", "email");
        salesOrderCommand.setBuyerName(shopdogOrderParamCommand.getName());
        salesOrderCommand.setBuyerTel(shopdogOrderParamCommand.getMobile());
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("创建订单所需的参数：ShopdogOrderParamCommand["+JsonUtil.format(shopdogOrderParamCommand)+"]");
        }
        // 设置支付信息，1是支付宝支付，2微信支付
        if ("1".equals(shopdogOrderParamCommand.getPaymentType())) {
            salesOrderCommand.setPayment(Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_ALIPAY));
            salesOrderCommand.setPaymentStr("支付宝支付");
            salesOrderCommand.setPayType(Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_ALIPAY));
        } else if ("2".equals(shopdogOrderParamCommand.getPaymentType())) {
            salesOrderCommand.setPayment(Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_WECHAT));
            salesOrderCommand.setPaymentStr("微信支付");
            salesOrderCommand.setPayType(Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_WECHAT));
        }

        // 如果有memberId，根据memberId查询此会员的相关信息,初始化memberDetails用于下单使用
        MemberDetails memberDetails = null;
        if (null != shopdogOrderParamCommand.getMemberId()) {
            MemberCommand memberCommand = sdkMemberManager.findMemberById(shopdogOrderParamCommand.getMemberId());
            salesOrderCommand.setMemberName(null != memberCommand ? memberCommand.getRealName() : "");
            salesOrderCommand.setMemberId(null != memberCommand ? shopdogOrderParamCommand.getMemberId() : null);
            
            //用于会员下单使用
            memberDetails = new MemberDetails();
            memberDetails.setGroupId(null != memberCommand ? memberCommand.getGroupId() : null);
            memberDetails.setMemberId(null != memberCommand ? shopdogOrderParamCommand.getMemberId() : null);
            memberDetails.setLoginMobile(null != memberCommand ? memberCommand.getLoginMobile() : "");
            memberDetails.setLoginName(null != memberCommand ? memberCommand.getLoginName() : "");
            memberDetails.setLoginEmail(null != memberCommand ? memberCommand.getLoginEmail() : "");
            memberDetails.setRealName(null != memberCommand ? memberCommand.getRealName() : "");
        }
        
        salesOrderCommand.setIp(RequestUtil.getClientIp(request));
        // 订单来源
        salesOrderCommand.setSource(SalesOrder.SO_SOURCE_SHOPDOG_NORMAL);

        // 获取购物车信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();
        for (ShopdogSkusCommand shopdogSkusCommand : shopdogOrderParamCommand.getSkuList()) {
            ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();
            shoppingCartLineCommand.setSkuId(shopdogSkusCommand.getSkuId());
            shoppingCartLineCommand.setQuantity(shopdogSkusCommand.getCount());
            // 这个参数为模拟购物车选中结算
            shoppingCartLineCommand.setSettlementState(1);
            // 这个参数待确定 TODO
            shoppingCartLineCommand.setShopId(1L);
            shoppingCartLineCommandList.add(shoppingCartLineCommand);
        }
        ShoppingCartCommand shoppingCartCommand = shoppingCartCommandBuilder.buildShoppingCartCommand(memberDetails, shoppingCartLineCommandList, getCalcFreightCommand(shopdogOrderParamCommand), null);

        SalesOrderCreateOptions salesOrderCreateOptions = new SalesOrderCreateOptions();
        salesOrderCreateOptions.setIsImmediatelyBuy(true);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("shoppingCartCommand:" + JSON.toJSONString(shoppingCartCommand));
        }

        // 新建订单
        String subOrdinate = sdkOrderCreateManager.saveOrder(shoppingCartCommand, salesOrderCommand, null, salesOrderCreateOptions);

        return createShopdogOrderCommand(request, salesOrderCommand.getName(), subOrdinate, String.valueOf(salesOrderCommand.getPayType()));
    }

    /**
     * 根据订单号查询订单的支付状态
     * 
     * @author 江家雷
     * @date 2016年5月30日 下午3:11:22
     * @param request
     * @param shopdogOrderParamCommand
     * @return
     * @see com.baozun.shopdog.web.controller.order.AbstractSdOrderController#queryPaymentStatus(javax.servlet.http.HttpServletRequest,
     *      com.baozun.shopdog.web.controller.order.viewcommand.ShopdogOrderParamCommand)
     * @since
     */
    @Override
    public Boolean queryPaymentStatus(ShopdogOrderParamCommand shopdogOrderParamCommand) {
        SalesOrderCommand salesOrder = orderManager.findOrderByCode(shopdogOrderParamCommand.getOrderCode(), 1);
        return salesOrder.getPayInfo().get(0).getPaySuccessStatus();
    }



    /**
     * 预算信息
     * 
     * @author 江家雷
     * @param calcFreightCommand
     * @param shopdogOrderParamCommand
     * @return
     * @since
     */
    private ShopdogSettlementCommand getShopdogSettlementCommand(CalcFreightCommand calcFreightCommand, ShopdogOrderParamCommand shopdogOrderParamCommand) {
        ShopdogSettlementCommand shopdogSettlementCommand = new ShopdogSettlementCommand();
        ShoppingCartCommand shoppingCartCommand = buildShoppingCartCommand(calcFreightCommand, shopdogOrderParamCommand);
        shopdogSettlementCommand.setFreightCharge(shoppingCartCommand.getCurrentShippingFee());
        shopdogSettlementCommand.setOriginPayAmount(shoppingCartCommand.getOriginPayAmount());
        shopdogSettlementCommand.setTotalAmount(shoppingCartCommand.getCurrentPayAmount());
        return shopdogSettlementCommand;
    }

    /**
     * 生成计算运费的对象
     * 
     * @param salesOrderCommand
     */
    private CalcFreightCommand getCalcFreightCommand(ShopdogOrderParamCommand shopdogOrderParamCommand) {
        CalcFreightCommand calcFreightCommand = new CalcFreightCommand();
        calcFreightCommand.setProvienceId(shopdogOrderParamCommand.getProvinceId());
        calcFreightCommand.setCityId(shopdogOrderParamCommand.getCityId());
        calcFreightCommand.setCountyId(shopdogOrderParamCommand.getAreaId());
        calcFreightCommand.setTownId(shopdogOrderParamCommand.getTownId());
        // 设置默认的物流方式,目前使用的是默认的物流方式，如果是app端选择的话，未做处理 TODO
        String modeId = mataInfoManager.findValue(MetaInfoConstants.DISTRIBUTION_MODE_ID);
        if (modeId != null) {
            calcFreightCommand.setDistributionModeId(Long.parseLong(modeId));
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("calcFreightCommand is {}", JsonUtil.format(calcFreightCommand));
        }
        return calcFreightCommand;
    }

    // 获取购物车信息
    private ShoppingCartCommand buildShoppingCartCommand(CalcFreightCommand calcFreightCommand, ShopdogOrderParamCommand shopdogOrderParamCommand) {

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();
        // 循环获取ShopdogSkusCommand并构建shoppingCartLineCommand的LIST
        for (ShopdogSkusCommand shopdogSkusCommand : shopdogOrderParamCommand.getSkuList()) {
            ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();
            shoppingCartLineCommand.setSkuId(shopdogSkusCommand.getSkuId());
            shoppingCartLineCommand.setQuantity(shopdogSkusCommand.getCount());
            // 这个参数为模拟数据，模拟购物车行被选中
            shoppingCartLineCommand.setSettlementState(1);
            // 店铺id,商城的shopid是什么？待确定 TODO
            shoppingCartLineCommand.setShopId(1L);
            shoppingCartLineCommandList.add(shoppingCartLineCommand);
        }
        // 添加会员信息
        MemberDetails memberDetails = null;
        if (null != shopdogOrderParamCommand.getMemberId()) {
            MemberCommand memberCommand = sdkMemberManager.findMemberById(shopdogOrderParamCommand.getMemberId());
            memberDetails = new MemberDetails();
            memberDetails.setGroupId(null != memberCommand ? memberCommand.getGroupId() : null);
            memberDetails.setMemberId(null != memberCommand ? shopdogOrderParamCommand.getMemberId() : null);
            memberDetails.setLoginMobile(null != memberCommand ? memberCommand.getLoginMobile() : "");
            memberDetails.setLoginName(null != memberCommand ? memberCommand.getLoginName() : "");
            memberDetails.setLoginEmail(null != memberCommand ? memberCommand.getLoginEmail() : "");
            memberDetails.setRealName(null != memberCommand ? memberCommand.getRealName() : "");
        }
        // 调用nebula构建ShoppingCartCommand的方法
        ShoppingCartCommand shoppingCartCommand = shoppingCartCommandBuilder.buildShoppingCartCommand(memberDetails, shoppingCartLineCommandList, calcFreightCommand, null);
        return shoppingCartCommand;
    }

    // 构建返回给APP所需的command
    private ShopdogOrderCommand createShopdogOrderCommand(HttpServletRequest request, String userName, String subOrdinate, String payType) {
        // 通過支付流水號查詢訂單
        SalesOrderCommand newOrder = salesOrderResolver.getSalesOrderCommand(subOrdinate);
        ShopdogOrderCommand shopdogOrderCommand = new ShopdogOrderCommand();
        shopdogOrderCommand.setOrderCode(newOrder.getCode());
        // 支付宝支付
        if (SalesOrder.SO_PAYMENT_TYPE_ALIPAY.equals(payType)) {
            shopdogOrderCommand.setPayUrl(sdPaymentController.getPaymentUrl(request, userName, subOrdinate, payType));
        }

        // 微信支付
        if (SalesOrder.SO_PAYMENT_TYPE_WECHAT.equals(payType)) {
            shopdogOrderCommand.setPayCode(sdPaymentController.getPaymentUrl(request, userName, subOrdinate, payType));
        }
        shopdogOrderCommand.setFreightCharge(newOrder.getActualFreight());
        shopdogOrderCommand.setTotalAmount(newOrder.getTotal());

        return shopdogOrderCommand;

    }



}
