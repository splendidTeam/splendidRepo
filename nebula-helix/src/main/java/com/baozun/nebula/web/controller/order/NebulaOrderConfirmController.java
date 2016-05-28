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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
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
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResult;
import com.baozun.nebula.web.controller.order.validator.OrderFormValidator;
import com.baozun.nebula.web.controller.order.viewcommand.OrderConfirmViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.ShoppingcartFactory;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder;
import com.baozun.nebula.web.controller.shoppingcart.converter.ShoppingcartViewCommandConverter;
import com.feilong.accessor.AutoKeyAccessor;
import com.feilong.core.Validator;

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
    private static final Logger              LOGGER = LoggerFactory.getLogger(NebulaOrderConfirmController.class);

    /** 订单提交的form 校验，需要在商城 spring 相关xml 里面进行配置. */
    @Autowired
    @Qualifier("orderFormValidator")
    private OrderFormValidator               orderFormValidator;

    /** The shoppingcart view command converter. */
    @Autowired
    @Qualifier("shoppingcartViewCommandConverter")
    private ShoppingcartViewCommandConverter shoppingcartViewCommandConverter;

    /** The auto key accessor. */
    @Autowired
    @Qualifier("immediatelyBuyAutoKeyAccessor")
    private AutoKeyAccessor                  autoKeyAccessor;

    /** The sdk member manager. */
    @Autowired
    private SdkMemberManager                 sdkMemberManager;

    /** The shoppingcart factory. */
    @Autowired
    private ShoppingcartFactory              shoppingcartFactory;

    /** The order manager. */
    @Autowired
    private OrderManager                     orderManager;

    /** The shopping cart command builder. */
    @Autowired
    private ShoppingCartCommandBuilder       shoppingCartCommandBuilder;

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

        // 获取购物车信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingcartFactory
                        .getShoppingCartLineCommandList(memberDetails, key, request);
        shoppingCartLineCommandList = ShoppingCartUtil.getMainShoppingCartLineCommandListWithCheckStatus(shoppingCartLineCommandList, true);

        Validate.notEmpty(shoppingCartLineCommandList, "shoppingCartLineCommandList can't be null/empty!");

        List<ContactCommand> contactCommandList = getContactCommandList(memberDetails);
        ShoppingCartCommand shoppingCartCommand = getChosenShoppingCartCommand(
                        memberDetails,
                        shoppingCartLineCommandList,
                        contactCommandList,
                        null);

        // 购物车为空
        if (shoppingCartCommand == null || shoppingCartCommand.getShoppingCartLineCommands() == null
                        || shoppingCartCommand.getShoppingCartLineCommands().isEmpty()){
            return "redirect:/index"; // 购物车为空返回的URL
        }

        // 封装viewCommand
        OrderConfirmViewCommand orderConfirmViewCommand = new OrderConfirmViewCommand();
        orderConfirmViewCommand.setShoppingCartCommand(shoppingCartCommand);
        // 收获地址信息
        orderConfirmViewCommand.setAddressList(contactCommandList);
        orderConfirmViewCommand.setKey(key);

        model.addAttribute("orderConfirmViewCommand", orderConfirmViewCommand);
        return "transaction.check";
    }

    /**
     * 获得用户的收货地址列表.
     *
     * @param memberDetails
     *            the member details
     * @return 如果 <code>memberDetails</code> 是null,返回null<br>
     *         否则调用 {@link com.baozun.nebula.sdk.manager.SdkMemberManager#findAllContactListByMemberId(Long)
     *         SdkMemberManager.findAllContactListByMemberId(Long)}
     */
    private List<ContactCommand> getContactCommandList(MemberDetails memberDetails){
        return null == memberDetails ? null : sdkMemberManager.findAllContactListByMemberId(memberDetails.getGroupId());
    }

    /**
     * 再次计算金额 使用情景：使用优惠券，切换地址(本方法并不对入参进行有效性校验，请在各商城端对其校验).
     *
     * @param memberDetails
     *            the member details
     * @param key
     *            the key
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
        if (orderForm.getCouponInfoSubForm() != null){
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
        if (orderForm.getShippingInfoSubForm() != null && orderForm.getShippingInfoSubForm().getProvinceId() != null
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

        // 获取购物车信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingcartFactory
                        .getShoppingCartLineCommandList(memberDetails, key, request);
        shoppingCartLineCommandList = ShoppingCartUtil.getMainShoppingCartLineCommandListWithCheckStatus(shoppingCartLineCommandList, true);

        ShoppingCartCommand shoppingCartCommand = getChosenShoppingCartCommand(
                        memberDetails,
                        shoppingCartLineCommandList,
                        addressList,
                        couponCode);

        result.setResult(true);
        result.setReturnObject(shoppingCartCommand);
        return result;

    }

    /**
     * 获取选中的购物车信息.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param addressList
     *            the address list
     * @param couponCode
     *            the coupon code
     * @return the cart info
     */
    private ShoppingCartCommand getChosenShoppingCartCommand(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    List<ContactCommand> addressList,
                    String couponCode){
        CalcFreightCommand calcFreightCommand = buildCalcFreightCommand(addressList);

        //优惠券
        List<String> coupons = null;
        if (Validator.isNotNullOrEmpty(couponCode)){
            coupons = new ArrayList<String>();
            coupons.add(couponCode);
        }
        return shoppingCartCommandBuilder.buildShoppingCartCommand(memberDetails, shoppingCartLineCommandList, calcFreightCommand, coupons);
    }

    /**
     * Builds the calc freight command.
     *
     * @param addressList
     *            the address list
     * @return the calc freight command
     */
    private CalcFreightCommand buildCalcFreightCommand(List<ContactCommand> addressList){
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
        return calcFreightCommand;
    }
}
