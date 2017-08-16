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
package com.baozun.nebula.web.controller.shoppingcart.builder;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartCommandBuilder;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.builder.CalcFreightCommandBuilder;
import com.baozun.nebula.web.controller.order.form.CouponInfoSubForm;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.form.ShippingInfoSubForm;
import com.baozun.nebula.web.controller.shoppingcart.factory.ShoppingcartFactory;
import com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingCartCommandCheckedBuilderHandler;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartBatchOptions;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartBatchResult;
import com.baozun.nebula.web.controller.shoppingcart.validator.CheckStatusShoppingCartLineListValidator;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月24日 下午4:59:37
 * @since 5.3.1
 */
@Component("shoppingCartCommandBuilder")
public class ShoppingCartCommandBuilderImpl implements ShoppingCartCommandBuilder{

    /** The shoppingcart factory. */
    @Autowired
    private ShoppingcartFactory shoppingcartFactory;

    @Autowired
    private CalcFreightCommandBuilder calcFreightCommandBuilder;

    @Autowired
    private SdkShoppingCartCommandBuilder sdkShoppingCartCommandBuilder;

    /**
     * 被选中的购物车行校验.
     * 
     * @since 5.3.2.20
     */
    @Autowired
    private CheckStatusShoppingCartLineListValidator checkStatusShoppingCartLineListValidator;

    @Autowired(required = false)
    private ShoppingCartCommandCheckedBuilderHandler shoppingCartCommandCheckedBuilderHandler;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder#buildShoppingCartCommandWithCheckStatus(com.baozun.nebula.web.MemberDetails, java.lang.String, com.baozun.nebula.web.controller.order.form.OrderForm,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ShoppingCartCommand buildShoppingCartCommandWithCheckStatus(MemberDetails memberDetails,String key,OrderForm orderForm,HttpServletRequest request){
        CouponInfoSubForm couponInfoSubForm = orderForm.getCouponInfoSubForm();
        List<String> couponList = toList(couponInfoSubForm.getCouponCode());

        //地址
        ShippingInfoSubForm shippingInfoSubForm = orderForm.getShippingInfoSubForm();
        CalcFreightCommand calcFreightCommand = calcFreightCommandBuilder.build(shippingInfoSubForm);
        return buildShoppingCartCommandWithCheckStatus(memberDetails, key, calcFreightCommand, couponList, request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder#buildShoppingCartCommandWithCheckStatus(com.baozun.nebula.web.MemberDetails, java.lang.String, com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand,
     * java.util.List, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ShoppingCartCommand buildShoppingCartCommandWithCheckStatus(MemberDetails memberDetails,String key,CalcFreightCommand calcFreightCommand,List<String> couponList,HttpServletRequest request){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingcartFactory.getShoppingCartLineCommandList(memberDetails, key, request);
        Validate.notEmpty(shoppingCartLineCommandList, "shoppingCartLineCommandList can't be null/empty!");

        //-----取选中状态的购物车行-------------------------------------------------------
        List<ShoppingCartLineCommand> shoppingCartLineCommandListWithCheckStatus = ShoppingCartUtil.getMainShoppingCartLineCommandListWithCheckStatus(shoppingCartLineCommandList, true);
        Validate.notEmpty(shoppingCartLineCommandListWithCheckStatus, "shoppingCartLineCommandList [is not] null/empty,but checkStatus shoppingCartLineCommandList is null/empty");

        //--------------------------------since 5.3.2.20-------------------------------------
        //校验被选中状态的购物车行.
        ShoppingcartBatchResult shoppingcartBatchResult = checkStatusShoppingCartLineListValidator.validate(memberDetails, shoppingCartLineCommandListWithCheckStatus, ShoppingcartBatchOptions.DEFAULT);
        Validate.notNull(shoppingcartBatchResult, "shoppingcartBatchResult can't be null!");

        //FIXME 抽取
        if (!shoppingcartBatchResult.getIsSuccess()){
            throw new RuntimeException("validate shoppingCartLineCommandListWithCheckStatus error," + JsonUtil.format(shoppingcartBatchResult));
        }

        //---------------------------------------------------------------
        if (null != shoppingCartCommandCheckedBuilderHandler){
            shoppingCartCommandCheckedBuilderHandler.preHandle(memberDetails, shoppingCartLineCommandListWithCheckStatus, calcFreightCommand, couponList, request);
        }

        //---------------------------------------------------------------
        ShoppingCartCommand shoppingCartCommand = buildShoppingCartCommand(memberDetails, shoppingCartLineCommandListWithCheckStatus, calcFreightCommand, couponList);
        Validate.notNull(shoppingCartCommand, "shoppingCartCommand can't be null");

        //---------------------------------------------------------------
        if (null != shoppingCartCommandCheckedBuilderHandler){
            shoppingCartCommandCheckedBuilderHandler.postHandle(memberDetails, shoppingCartLineCommandListWithCheckStatus, calcFreightCommand, couponList, request);
        }
        return shoppingCartCommand;
    }

    //------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder#buildShoppingCartCommand(com.baozun.nebula.web.
     * MemberDetails, java.util.List)
     */
    @Override
    public ShoppingCartCommand buildShoppingCartCommand(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLines){
        return buildShoppingCartCommand(memberDetails, shoppingCartLines, null, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder#buildShoppingCartCommand(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand, java.util.List)
     */
    @Override
    public ShoppingCartCommand buildShoppingCartCommand(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLines,CalcFreightCommand calcFreightCommand,List<String> coupons){
        if (isNullOrEmpty(shoppingCartLines)){
            return null;
        }

        Long groupId = null == memberDetails ? null : memberDetails.getGroupId();
        Set<String> memComboList = null == memberDetails ? null : memberDetails.getMemComboList();
        return sdkShoppingCartCommandBuilder.buildShoppingCartCommand(groupId, shoppingCartLines, calcFreightCommand, coupons, memComboList);
    }
}
