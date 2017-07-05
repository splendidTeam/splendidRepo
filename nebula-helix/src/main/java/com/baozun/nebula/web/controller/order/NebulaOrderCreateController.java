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

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.manager.order.SdkOrderCreateManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResolver;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResult;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderReturnObject;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderTypeResolver;
import com.baozun.nebula.web.controller.order.validator.OrderFormValidator;
import com.baozun.nebula.web.controller.order.validator.SalesOrderCreateValidator;
import com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingCartOrderCreateBeforeHandler;
import com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingcartOrderCreateSuccessHandler;
import com.feilong.accessor.AutoKeyAccessor;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 * 订单确认控制器.
 * 
 * <h3>订单确认页面有的操作:</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>显示订单确认页面</td>
 * <td>参见
 * {@link #showTransactionCheck(MemberDetails, String, HttpServletRequest, HttpServletResponse, Model)}
 * </td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>创建订单</td>
 * <td>
 * 参见 {@link #createOrder(MemberDetails, String, OrderForm, BindingResult, HttpServletRequest, HttpServletResponse, Model)}
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>新增收获地址</td>
 * <td>请调用
 * {@link com.baozun.nebula.web.controller.member.NebulaMemberAddressController#addMemberAddress(MemberDetails, com.baozun.nebula.web.controller.member.form.MemberAddressForm, org.springframework.validation.BindingResult, HttpServletRequest, HttpServletResponse, Model)
 * NebulaMemberAddressController#addMemberAddress}</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td></td>
 * <td></td>
 * </tr>
 * <tr valign="top">
 * <td>选择优惠券</td>
 * <td>请调用 coupon 相关请求 (注：目前speedo写在自己商城里面)</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>校验优惠码</td>
 * <td>请调用 coupon 相关请求 (注：目前speedo写在自己商城里面)</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * 
 * <h3>关于confirm method:</h3>
 * <blockquote> 业界有两种方式,天猫是post请求,京东是get请求 </blockquote>
 * 
 * <h3>关于confirm 页面刷新:</h3>
 * 
 * <blockquote>
 * <p>
 * 业界也有两种方式,天猫刷新页面每次都只加载从购物车选中过来的lines,不关怎么调整(即使原line删除)<br>
 * 京东刷新页面,每次加载最终的选择项内容
 * </p>
 * </blockquote>
 * 
 * <h3>关于购物车选中状态:</h3>
 * 
 * <blockquote>
 * <p>
 * 天猫不会保存选中状态,天猫的购物车就是让用户挑选去结算;而京东每次操作都会保存状态,而订单确认行每次都拿用户选中的状态
 * </p>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年4月28日 上午11:42:30
 * @since 5.3.1
 */
public class NebulaOrderCreateController extends NebulaAbstractTransactionController{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaOrderCreateController.class);
    
    /**
     * 5.3.2.18 客户端识别码规定绑定名称
     * @since 5.3.2.18
     */
    public static final String CLIENT_IDENTIFICATION_MECHANISMS = NebulaOrderCreateController.class.getName()+"clientIdentificationMechanisms";

    /** 订单提交的form 校验，需要在商城 spring 相关xml 里面进行配置. */
    @Autowired
    @Qualifier("orderFormValidator")
    private OrderFormValidator orderFormValidator;

    @Autowired
    @Qualifier("immediatelyBuyAutoKeyAccessor")
    private AutoKeyAccessor autoKeyAccessor;

    @Autowired
    private SalesOrderResolver salesOrderResolver;

    @Autowired
    private SdkOrderCreateManager sdkOrderCreateManager;

    @Autowired
    private ShoppingcartOrderCreateSuccessHandler shoppingcartOrderCreateSuccessHandler;

    @Autowired(required = false)
    private ShoppingCartOrderCreateBeforeHandler shoppingCartOrderCreateBeforeHandler;

    @Autowired
    private SalesOrderCreateValidator salesOrderCreateValidator;
    /**
     * 订单类型处理器
     * @since 5.3.2.20
     */
    @Autowired(required = false)
    private SalesOrderTypeResolver salesOrderTypeResolver;

    /**
     * @Description 创建订单.
     *              <ol>
     *              <li>订单物流配送方式（当日达，次日达等）放在OrderForm.ShippingInfoSubForm.AppointType字段上</li>
     *              </ol>
     *
     * @param memberDetails
     *            the member details
     * @param orderForm
     *            the order form
     * @param key
     *            购买的商品list的钥匙,如果没有传入key 那么就是普通的购物车购买情况
     * @param bindingResult
     *            the binding result
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the string
     * @NeedLogin (guest=true)
     * @RequestMapping(value = "/transaction/create", method =
     *                       RequestMethod.POST)
     */
    public NebulaReturnResult createOrder(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "key",required = false) String key, // 隐藏域中传递
                    @ModelAttribute("orderForm") OrderForm orderForm,
                    BindingResult bindingResult,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @SuppressWarnings("unused") Model model){
        // 校验表单数据
        orderFormValidator.validate(orderForm, bindingResult);
        // 如果校验失败，返回错误
        if (bindingResult.hasErrors()){
            LOGGER.error("[ORDER_CREATEORDER] {} [{}] orderForm validation error. \"\"", memberDetails == null ? "Gueset" : memberDetails.getGroupId().toString(), new Date());
            return getResultFromBindingResult(bindingResult);
        }

        //---------------------------------------------------------------------------------
        ShoppingCartCommand shoppingCartCommand = shoppingCartCommandBuilder.buildShoppingCartCommandWithCheckStatus(memberDetails, key, orderForm, request);
        //---------------------------------------------------------------------------------
        // 校验购物车信息和促销
        //TODO feilong 多张优惠券
        String couponCode = orderForm.getCouponInfoSubForm().getCouponCode();
        SalesOrderResult salesorderResult = salesOrderCreateValidator.validate(shoppingCartCommand, couponCode);

        // 如果校验失败，返回错误
        if (salesorderResult != SalesOrderResult.SUCCESS){
            LOGGER.error("[ORDER_CREATEORDER] {} [{}] orderForm coupon [{}] validation error. \"\"", memberDetails == null ? "Guest" : memberDetails.getGroupId().toString(), new Date(), couponCode);
            return toNebulaReturnResult(salesorderResult);
        }

        //---------------------------------------------------------------------------------

        if (null != shoppingCartOrderCreateBeforeHandler){
            shoppingCartOrderCreateBeforeHandler.beforeCreateSalesOrder(shoppingCartCommand, orderForm, memberDetails, request, key);
        }

        //---------------------------------------------------------------------------------
        SalesOrderCreateOptions salesOrderCreateOptions = buildSalesOrderCreateOptions(key);
        Set<String> memCombos = null == memberDetails ? null : memberDetails.getMemComboList();

        //----------------------新建订单-----------------------------------------------------
        // 封装订单信息
        SalesOrderCommand salesOrderCommand = salesOrderResolver.toSalesOrderCommand(memberDetails, orderForm, request);
        //是否需要判断订单类型  @since 5.3.2.20
        if(null != salesOrderTypeResolver){
            salesOrderCommand.setOrderType(salesOrderTypeResolver.resolverOrderType(salesOrderCommand,shoppingCartCommand));
        }
        String subOrdinate = sdkOrderCreateManager.saveOrder(shoppingCartCommand, salesOrderCommand, memCombos, salesOrderCreateOptions);

        //---------------------------------------------------------------------------------
        //购物车信息重置
        if (isNotNullOrEmpty(key)){
            //  清空立即购买信息
            autoKeyAccessor.remove(key, request);
        }else{
            shoppingcartOrderCreateSuccessHandler.onOrderCreateSuccess(memberDetails, request, response);
        }

        //---------------------------------------------------------------------------------
        return toNebulaReturnResult(subOrdinate);
    }

    /**
     * @param key
     * @return
     */
    private static SalesOrderCreateOptions buildSalesOrderCreateOptions(String key){
        SalesOrderCreateOptions salesOrderCreateOptions = new SalesOrderCreateOptions();

        //设置立即购买标志
        if (isNotNullOrEmpty(key)){
            salesOrderCreateOptions.setIsImmediatelyBuy(true);
        }
        return salesOrderCreateOptions;
    }

    /**
     * @param subOrdinate
     * @return
     */
    private NebulaReturnResult toNebulaReturnResult(String subOrdinate){
        // 将订单创建成功后的信息返回给前端，创建支付链接用
        SalesOrderReturnObject salesOrderReturnObject = createReturnObject(subOrdinate);
        DefaultReturnResult result = new DefaultReturnResult();
        result.setResult(true);
        result.setReturnObject(salesOrderReturnObject);
        return result;
    }

    /**
     * 返回结果填充
     * 
     * @param shoppingcartResult
     * @return
     */
    private NebulaReturnResult toNebulaReturnResult(SalesOrderResult salesorderResult){
        if (SalesOrderResult.SUCCESS != salesorderResult){
            DefaultReturnResult result = new DefaultReturnResult();
            result.setResult(false);

            String messageStr = getMessage(salesorderResult.toString());

            DefaultResultMessage message = new DefaultResultMessage();
            message.setMessage(messageStr);
            result.setResultMessage(message);

            LOGGER.error(messageStr);
            return result;
        }
        return DefaultReturnResult.SUCCESS;
    }

    protected SalesOrderReturnObject createReturnObject(String subOrdinate){
        // 通過支付流水號查詢訂單
        SalesOrderCommand salesOrderCommand = salesOrderResolver.getSalesOrderCommand(subOrdinate);
        SalesOrderReturnObject salesOrderReturnObject = new SalesOrderReturnObject();
        salesOrderReturnObject.setCode(salesOrderCommand.getCode());
        salesOrderReturnObject.setId(salesOrderCommand.getId());
        salesOrderReturnObject.setSubOrdinate(subOrdinate);

        return salesOrderReturnObject;
    }

}
