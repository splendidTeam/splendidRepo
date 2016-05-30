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
 * This document only allow internal use ,Any of your behaviors using the file
 * not internal will pay legal responsibility.
 *
 * You may learn more information about Deppon from
 *
 *      http://www.deppon.com
 *
 */ 
package com.baozun.shopdog.web.controller.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.constants.MetaInfoConstants;
import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkFreightFeeManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.SdkOrderCreateManager;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResolver;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder;
import com.baozun.shopdog.web.controller.order.viewcommand.ShopdogOrderCommand;
import com.baozun.shopdog.web.controller.order.viewcommand.ShopdogOrderParamCommand;
import com.baozun.shopdog.web.controller.order.viewcommand.ShopdogSettlementCommand;
import com.baozun.shopdog.web.controller.order.viewcommand.ShopdogSkusCommand;
import com.baozun.shopdog.web.controller.payment.AbstractSdPaymentController;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.lang.NumberUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * @author 江家雷
 * @date 2016年5月24日 下午2:20:23
 * @since
 */
public class SdOrderController implements AbstractSdOrderController{

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
    private SdkFreightFeeManager sdkFreightFeeManager;
    
    @Autowired
    private SdkSkuManager sdkSkuManager;
    
    @Autowired
    private ShoppingCartCommandBuilder            shoppingCartCommandBuilder;
    /**
     * @author 江家雷
     * @date 2016年5月25日 上午11:04:27
     * @param shopdogOrderParamCommand
     * @return
     * @see com.baozun.shopdog.web.controller.order.AbstractSdOrderController#buildShopdogSettlementCommand(com.baozun.shopdog.web.controller.order.viewcommand.ShopdogOrderParamCommand)
     * @since
     */
    @Override
    public ShopdogSettlementCommand buildShopdogSettlementCommand(ShopdogOrderParamCommand shopdogOrderParamCommand) {
        return getShopdogSettlementCommand(getCalcFreightCommand(shopdogOrderParamCommand), shopdogOrderParamCommand);
    }
    
    
    /**
     * @author 江家雷
     * @date 2016年5月24日 下午2:21:35
     * @param shopdogOrderParamCommand
     * @return
     * @see com.baozun.shopdog.web.controller.order.AbstractSdOrderController#saveOrder(com.baozun.shopdog.web.controller.order.viewcommand.ShopdogOrderParamCommand)
     * @since
     */
    @Override
    public ShopdogOrderCommand saveOrder(HttpServletRequest request, ShopdogOrderParamCommand shopdogOrderParamCommand) {
        // 封装订单信息
        SalesOrderCommand salesOrderCommand = new SalesOrderCommand();
        PropertyUtil.copyProperties(salesOrderCommand, 
            shopdogOrderParamCommand, 
            "countryId", 
            "provinceId",
            "cityId", 
            "areaId", 
            "townId",
            "postcode",
            "mobile",
            "email");
        salesOrderCommand.setBuyerName(shopdogOrderParamCommand.getName());
        salesOrderCommand.setBuyerTel(shopdogOrderParamCommand.getMobile());
        // 设置支付信息
        salesOrderCommand.setPayment(Integer.parseInt(shopdogOrderParamCommand.getPaymentType()));
        salesOrderCommand.setPaymentStr("支付宝支付");
        salesOrderCommand.setPayType(Integer.parseInt(shopdogOrderParamCommand.getPaymentType()));
        //此会员名称是否需要从数据库中查询 TODO
        salesOrderCommand.setMemberName("");
        salesOrderCommand.setMemberId(shopdogOrderParamCommand.getMemberId());
        salesOrderCommand.setIp(RequestUtil.getClientIp(request));

        // 订单来源,暂时缺少驻店宝下单 TODO
        salesOrderCommand.setSource(SalesOrder.SO_SOURCE_NORMAL);

        // 获取购物车信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();
        for(ShopdogSkusCommand shopdogSkusCommand : shopdogOrderParamCommand.getSkuList()){
            ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();
            shoppingCartLineCommand.setSkuId(shopdogSkusCommand.getSkuId());
            shoppingCartLineCommand.setQuantity(shopdogSkusCommand.getCount());
            //这个参数为模拟数据，后续写死
            shoppingCartLineCommand.setSettlementState(1);
            //这个参数待确定 TODO
            shoppingCartLineCommand.setShopId(1L);
            shoppingCartLineCommandList.add(shoppingCartLineCommand);
        }
        ShoppingCartCommand shoppingCartCommand = shoppingCartCommandBuilder
                .buildShoppingCartCommand(null, shoppingCartLineCommandList, getCalcFreightCommand(shopdogOrderParamCommand), null);

        SalesOrderCreateOptions salesOrderCreateOptions = new SalesOrderCreateOptions();
        salesOrderCreateOptions.setIsImmediatelyBuy(true);
        LOGGER.info("shoppingCartCommand:"+JSON.toJSONString(shoppingCartCommand));
        // 新建订单
        String subOrdinate = sdkOrderCreateManager.saveOrder(
                        shoppingCartCommand,
                        salesOrderCommand,
                        null,
                        salesOrderCreateOptions);
      
        return createShopdogOrderCommand(request, salesOrderCommand.getName(), subOrdinate, String.valueOf(salesOrderCommand.getPayType()));
    }
    
    /**
     * 预算信息
     * @author 江家雷
     * @param calcFreightCommand
     * @param shopdogOrderParamCommand
     * @return
     * @since
     */
    private ShopdogSettlementCommand getShopdogSettlementCommand(CalcFreightCommand calcFreightCommand,ShopdogOrderParamCommand shopdogOrderParamCommand){
        
        ShopdogSettlementCommand shopdogSettlementCommand = new ShopdogSettlementCommand();
        
        List<ShoppingCartLineCommand> validLines = new ArrayList<>();
        for(ShopdogSkusCommand shopdogSkusCommand:shopdogOrderParamCommand.getSkuList()){
            ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();
            shoppingCartLineCommand.setSkuId(shopdogSkusCommand.getSkuId());
            shoppingCartLineCommand.setQuantity(shopdogSkusCommand.getCount());            
            Sku sku = sdkSkuManager.findSkuById(shopdogSkusCommand.getSkuId());
            shoppingCartLineCommand.setSalePrice(sku.getSalePrice());
            validLines.add(shoppingCartLineCommand);
        }
        shopdogSettlementCommand.setFreightCharge(sdkFreightFeeManager.getFreightFee(1L, calcFreightCommand, validLines));
        shopdogSettlementCommand.setTotalAmount(getOriginPayAmount(validLines));
        return shopdogSettlementCommand;
    }
    
    /**
     * 计算运费的对象
     * 
     * @param salesOrderCommand
     */
    private CalcFreightCommand getCalcFreightCommand(ShopdogOrderParamCommand shopdogOrderParamCommand) {
        CalcFreightCommand calcFreightCommand = new CalcFreightCommand();
        calcFreightCommand.setProvienceId(shopdogOrderParamCommand.getProvinceId());
        calcFreightCommand.setCityId(shopdogOrderParamCommand.getCityId());
        calcFreightCommand.setCountyId(shopdogOrderParamCommand.getAreaId());
        calcFreightCommand.setTownId(shopdogOrderParamCommand.getTownId());
        // 设置默认的物流方式
        String modeId = mataInfoManager.findValue(MetaInfoConstants.DISTRIBUTION_MODE_ID);
        if (modeId != null) {
            calcFreightCommand.setDistributionModeId(Long.parseLong(modeId));
        }
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("calcFreightCommand is {}", JsonUtil.format(calcFreightCommand));
        }
        return calcFreightCommand;
    }
    /**
     * 计算应付金额.
     *
     * @param shoppingCartLines
     *            the shopping cart lines
     * @return the origin pay amount
     */
    private BigDecimal getOriginPayAmount(List<ShoppingCartLineCommand> shoppingCartLines){
        BigDecimal originPayAmount = new BigDecimal(0);
        for (ShoppingCartLineCommand cartLine : shoppingCartLines){
            if (cartLine.isGift() || cartLine.isCaptionLine())
                continue;
            originPayAmount = originPayAmount.add(NumberUtil.getMultiplyValue(cartLine.getQuantity(), cartLine.getSalePrice()));
        }
        return originPayAmount = originPayAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private ShopdogOrderCommand createShopdogOrderCommand(HttpServletRequest request, String userName, String subOrdinate,String payType) {
        // 通過支付流水號查詢訂單
        SalesOrderCommand newOrder = salesOrderResolver.getSalesOrderCommand(subOrdinate);
        ShopdogOrderCommand shopdogOrderCommand = new ShopdogOrderCommand();
        shopdogOrderCommand.setOrderCode(newOrder.getCode());
        if(SalesOrder.SO_PAYMENT_TYPE_ALIPAY.equals(payType)){
            shopdogOrderCommand.setPayUrl(sdPaymentController.getPaymentUrl(request, userName, subOrdinate, payType));
        }        
        if(SalesOrder.SO_PAYMENT_TYPE_WECHAT.equals(payType)){
            shopdogOrderCommand.setPayCode(sdPaymentController.getPaymentUrl(request, userName, subOrdinate, payType));
        }       
        shopdogOrderCommand.setFreightCharge(newOrder.getActualFreight());
        shopdogOrderCommand.setTotalAmount(newOrder.getTotal());
        
        return shopdogOrderCommand;
        
    }

}
