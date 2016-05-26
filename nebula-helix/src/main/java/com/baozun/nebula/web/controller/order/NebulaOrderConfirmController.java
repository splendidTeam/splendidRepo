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
import java.util.Date;
import java.util.List;
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

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.manager.order.SdkOrderCreateManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartCommandBuilder;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResolver;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResult;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderReturnObject;
import com.baozun.nebula.web.controller.order.validator.OrderFormValidator;
import com.baozun.nebula.web.controller.order.viewcommand.OrderConfirmViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.ShoppingcartFactory;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder;
import com.baozun.nebula.web.controller.shoppingcart.converter.ShoppingcartViewCommandConverter;
import com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingcartOrderCreateSuccessHandler;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.framework.accessor.AutoKeyAccessor;

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
 * @author feilong
 * @version 5.3.1 2016年4月28日 上午11:42:30
 * @since 5.3.1
 */
public class NebulaOrderConfirmController extends BaseController{

    /** The Constant log. */
    private static final Logger                   LOGGER                  = LoggerFactory.getLogger(NebulaOrderConfirmController.class);

    /** 订单提交的form 校验，需要在商城 spring 相关xml 里面进行配置. */
    @Autowired
    @Qualifier("orderFormValidator")
    private OrderFormValidator                    orderFormValidator;

    @Autowired
    private SdkMemberManager                      sdkMemberManager;

    @Autowired
    private ShoppingcartFactory                   shoppingcartFactory;

    @Autowired
    @Qualifier("shoppingcartViewCommandConverter")
    private ShoppingcartViewCommandConverter      shoppingcartViewCommandConverter;

    @Autowired
    private SalesOrderResolver                    salesOrderResolver;

    @Autowired
    private SdkOrderCreateManager                 sdkOrderCreateManager;

    @Autowired
    @Qualifier("immediatelyBuyAutoKeyAccessor")
    protected AutoKeyAccessor                     autoKeyAccessor;

    @Autowired
    private OrderManager                          orderManager;

    private static final String                   MODEL_KEY_ORDER_CONFIRM = "orderConfirmViewCommand";

    /** 购物车为空返回的URL */
    public static final String                    CART_NULL_BACK_URL      = "/index";

    @Autowired
    private SdkShoppingCartCommandBuilder         sdkShoppingCartCommandBuilder;

    @Autowired
    private ShoppingCartCommandBuilder            shoppingCartCommandBuilder;

    @Autowired
    private ShoppingcartOrderCreateSuccessHandler shoppingcartOrderCreateSuccessHandler;

    /**
     * 显示订单结算页面.
     *
     * @param memberDetails
     *            the member details
     * @param key
     *            购买的商品list的钥匙,如果没有传入key 那么就是普通的购物车购买情况
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the string
     * @NeedLogin (guest=true)
     * @RequestMapping(value = "/transaction/check", method = RequestMethod.GET)
     */
    public String showTransactionCheck(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "key",required = false) String key,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        // TODO feilong 获得购物车数据 (如果没有传入key 那么就是普通的购物车购买情况)
        List<ContactCommand> addressList = null;
        if (memberDetails != null){
            addressList = sdkMemberManager.findAllContactListByMemberId(memberDetails.getGroupId());
        }

        // 获取购物车信息
        List<ShoppingCartLineCommand> cartLines = getCartLines(request, memberDetails, key);
        ShoppingCartCommand shoppingCartCommand = getChosenShoppingCartCommand(cartLines, memberDetails, addressList, null);

        // 购物车为空
        if (shoppingCartCommand == null || shoppingCartCommand.getShoppingCartLineCommands() == null
                        || shoppingCartCommand.getShoppingCartLineCommands().isEmpty()){
            return "redirect:" + CART_NULL_BACK_URL;
        }

        // 封装viewCommand
        OrderConfirmViewCommand orderConfirmViewCommand = new OrderConfirmViewCommand();
        orderConfirmViewCommand.setShoppingCartCommand(shoppingCartCommand);
        // 收获地址信息
        orderConfirmViewCommand.setAddressList(addressList);

        orderConfirmViewCommand.setKey(key);

        model.addAttribute(MODEL_KEY_ORDER_CONFIRM, orderConfirmViewCommand);

        return "transaction.check";
    }

    /**
     * 创建订单.
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
                    Model model){
        // 校验表单数据
        orderFormValidator.validate(orderForm, bindingResult);
        // 如果校验失败，返回错误
        if (bindingResult.hasErrors()){
            LOGGER.error(
                            "[ORDER_CREATEORDER] {} [{}] orderForm validation error. \"\"",
                            memberDetails == null ? "Gueset" : memberDetails.getGroupId().toString(),
                            new Date());
            return getResultFromBindingResult(bindingResult);
        }

        // 封装订单信息
        SalesOrderCommand salesOrderCommand = salesOrderResolver.buildSalesOrderCommand(memberDetails, orderForm, request);

        // 获取购物车信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getCartLines(request, memberDetails, key);

        // 获取购物车行信息
        List<String> couponList = CollectionsUtil.getPropertyValueList(salesOrderCommand.getCouponCodes(), "couponCode");

        CalcFreightCommand calcFreightCommand = salesOrderCommand.getCalcFreightCommand();

        ShoppingCartCommand shoppingCartCommand = shoppingCartCommandBuilder
                        .buildShoppingCartCommand(memberDetails, shoppingCartLineCommandList, calcFreightCommand, couponList);

        // 校验购物车信息和促销
        String couponCode = orderForm.getCouponInfoSubForm().getCouponCode();

        SalesOrderResult salesorderResult = validateWithShoppingCart(shoppingCartCommand, couponCode);
        // 如果校验失败，返回错误
        if (salesorderResult != SalesOrderResult.SUCCESS){
            LOGGER.error(
                            "[ORDER_CREATEORDER] {} [{}] orderForm coupon [{}] validation error. \"\"",
                            memberDetails == null ? "Gueset" : memberDetails.getGroupId().toString(),
                            new Date(),
                            couponCode);
            return toNebulaReturnResult(salesorderResult);
        }

        SalesOrderCreateOptions salesOrderCreateOptions = new SalesOrderCreateOptions();

        //设置立即购买标志
        if (Validator.isNotNullOrEmpty(key)){
            salesOrderCreateOptions.setIsImmediatelyBuy(true);
        }

        // 新建订单
        String subOrdinate = sdkOrderCreateManager.saveOrder(
                        shoppingCartCommand,
                        salesOrderCommand,
                        null == memberDetails ? null : memberDetails.getMemComboList(),
                        salesOrderCreateOptions);

        //购物车信息重置
        if (Validator.isNotNullOrEmpty(key)){
            //  清空立即购买信息
            autoKeyAccessor.remove(key, request);
        }else{
            shoppingcartOrderCreateSuccessHandler.onOrderCreateSuccess(memberDetails, request, response);
        }

        return toNebulaReturnResult(subOrdinate);
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
     * 再次计算金额 使用情景：使用优惠券，切换地址(本方法并不对入参进行有效性校验，请在各商城端对其校验)
     * 
     * @param memberDetails
     *            the member details
     * @param orderForm
     *            the order form
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
     * @RequestMapping(value = "/transaction/recalc", method = RequestMethod.POST)
     */
    public NebulaReturnResult recalc(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "key",required = false) String key, // 隐藏域中传递
                    @ModelAttribute("orderForm") OrderForm orderForm,
                    BindingResult bindingResult,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        DefaultReturnResult result = new DefaultReturnResult();

        // 优惠券
        String couponCode = null;
        if (orderForm != null && orderForm.getCouponInfoSubForm() != null){
            couponCode = orderForm.getCouponInfoSubForm().getCouponCode();
            //非用户绑定优惠券
            if (orderForm.getCouponInfoSubForm().getId() == null){
                PromotionCouponCode promotionCouponCode = orderManager.validCoupon(orderForm.getCouponInfoSubForm().getCouponCode());
                if (promotionCouponCode == null){
                    result.setResult(false);
                    result.setReturnObject(getMessage(SalesOrderResult.ORDER_COUPON_NOT_AVALIBLE.toString()));
                    return result;
                }
            }
        }

        //地址
        List<ContactCommand> addressList = null;
        if (orderForm != null && orderForm.getShippingInfoSubForm() != null && orderForm.getShippingInfoSubForm().getProvinceId() != null
                        && orderForm.getShippingInfoSubForm().getCityId() != null
                        && orderForm.getShippingInfoSubForm().getAreaId() != null){
            ContactCommand contactCommand = new ContactCommand();
            contactCommand.setProvinceId(orderForm.getShippingInfoSubForm().getProvinceId());
            contactCommand.setCityId(orderForm.getShippingInfoSubForm().getCityId());
            contactCommand.setAreaId(orderForm.getShippingInfoSubForm().getAreaId());
            contactCommand.setIsDefault(true);

            addressList = new ArrayList<ContactCommand>();
            addressList.add(contactCommand);
        }

        List<ShoppingCartLineCommand> cartLines = getCartLines(request, memberDetails, key);

        ShoppingCartCommand shoppingCartCommand = getChosenShoppingCartCommand(cartLines, memberDetails, addressList, couponCode);

        result.setResult(true);
        result.setReturnObject(shoppingCartCommand);
        return result;

    }

    protected List<ShoppingCartLineCommand> getCartLines(HttpServletRequest request,MemberDetails memberDetails,String key){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingcartFactory
                        .getShoppingCartLineCommandList(memberDetails, key, request);
        return ShoppingCartUtil.getMainShoppingCartLineCommandListWithCheckStatus(shoppingCartLineCommandList, true);
    }

    /**
     * 获取选中的购物车信息.
     * 
     * @param request
     *            the request
     * @param memberDetails
     *            the member details
     * @return the cart info
     */
    protected ShoppingCartCommand getChosenShoppingCartCommand(
                    List<ShoppingCartLineCommand> cartLines,
                    MemberDetails memberDetails,
                    List<ContactCommand> addressList,
                    String couponCode){
        if (cartLines == null || cartLines.isEmpty()){
            return null;
        }
        Long groupId = null == memberDetails ? null : memberDetails.getGroupId();
        Set<String> memComboList = null == memberDetails ? null : memberDetails.getMemComboList();

        //地址
        CalcFreightCommand calcFreightCommand = null;
        if (addressList != null && !addressList.isEmpty()){
            ContactCommand contactCommand = null;

            //默认地址
            for (ContactCommand curContactCommand : addressList){
                if (curContactCommand.getIsDefault()){
                    contactCommand = curContactCommand;
                    break;
                }
            }
            //无默认，取第一条
            if (contactCommand == null){
                contactCommand = addressList.get(0);
            }
            calcFreightCommand = new CalcFreightCommand();
            calcFreightCommand.setProvienceId(contactCommand.getProvinceId());
            calcFreightCommand.setCityId(contactCommand.getCityId());
            calcFreightCommand.setCountyId(contactCommand.getAreaId());
        }else{
            //在未选择地址的情况下   为了显示默认运费，初始设置一个临时地址，上海市黄浦区()
            calcFreightCommand = new CalcFreightCommand();
            calcFreightCommand.setProvienceId(310000L);
            calcFreightCommand.setCityId(310100L);
            calcFreightCommand.setCountyId(310101L);
        }

        //优惠券
        List<String> coupons = null;
        if (Validator.isNotNullOrEmpty(couponCode)){
            coupons = new ArrayList<String>();
            coupons.add(couponCode);
        }

        return sdkShoppingCartCommandBuilder.buildShoppingCartCommand(groupId, cartLines, calcFreightCommand, coupons, memComboList);
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

    /**
     * 购物车和优惠券促销验证
     * 
     * @param shoppingCartCommand
     * @return
     */
    public SalesOrderResult validateWithShoppingCart(ShoppingCartCommand shoppingCartCommand,String coupon){
        /** 校驗購物車 */
        SalesOrderResult salesOrderResult = checkShoppingCart(shoppingCartCommand);
        if (null != salesOrderResult){
            return salesOrderResult;
        }

        /** 如果输入了优惠券则要进行优惠券验证 */
        if (Validator.isNotNullOrEmpty(coupon)){
            /** 校驗优惠券促销 */
            salesOrderResult = checkCoupon(coupon, shoppingCartCommand);
            if (null != salesOrderResult){
                return salesOrderResult;
            }
        }

        return SalesOrderResult.SUCCESS;
    }

    /**
     * 校驗購物車 失败返回错误，正确返回null
     **/
    private SalesOrderResult checkShoppingCart(ShoppingCartCommand shoppingCartCommand){
        if (Validator.isNullOrEmpty(shoppingCartCommand)
                        || com.feilong.core.Validator.isNullOrEmpty(shoppingCartCommand.getShoppingCartLineCommands())){// 購物車不能為空
            return SalesOrderResult.ORDER_SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }
        return null;
    }

    /**
     * 校驗優惠券是否有效 success 有效，其他 無效 失败返回错误，正确返回null
     **/
    private SalesOrderResult checkCoupon(String coupon,ShoppingCartCommand cartCommand){
        if (Validator.isNullOrEmpty(cartCommand.getCartPromotionBriefList())){// 無效
            return SalesOrderResult.ORDER_COUPON_NOT_AVALIBLE;
        }
        for (PromotionBrief pro : cartCommand.getCartPromotionBriefList()){// 從活動中取記錄校驗
            if (Validator.isNotNullOrEmpty(pro.getDetails())){
                for (PromotionSettingDetail settingDetail : pro.getDetails()){// 遍曆活動詳情
                    if (Validator.isNullOrEmpty(settingDetail.getCouponCodes())){// 先校驗整單優惠券有沒有
                        if (Validator.isNotNullOrEmpty(settingDetail.getAffectSKUDiscountAMTList())){
                            for (PromotionSKUDiscAMTBySetting skuSetting : settingDetail.getAffectSKUDiscountAMTList()){// 遍曆商品行優惠記錄
                                if (Validator.isNullOrEmpty(skuSetting.getCouponCodes())){// 如果整單優惠券沒有，校驗商品行優惠券
                                    continue;
                                }
                                if (skuSetting.getCouponCodes().contains(coupon)){
                                    return null;
                                }
                            }
                        }
                        continue;
                    }
                    if (settingDetail.getCouponCodes().contains(coupon)){
                        return null;
                    }
                }
            }
        }
        return SalesOrderResult.ORDER_COUPON_NOT_AVALIBLE;

    }

    private SalesOrderReturnObject createReturnObject(String subOrdinate){
        // 通過支付流水號查詢訂單
        SalesOrderCommand newOrder = salesOrderResolver.getSalesOrderCommand(subOrdinate);
        SalesOrderReturnObject salesOrderReturnObject = new SalesOrderReturnObject();
        salesOrderReturnObject.setCode(newOrder.getCode());
        salesOrderReturnObject.setId(newOrder.getId());
        salesOrderReturnObject.setSubOrdinate(subOrdinate);

        return salesOrderReturnObject;
    }

}
