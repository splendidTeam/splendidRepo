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
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.validator.OrderFormValidator;
import com.baozun.nebula.web.controller.order.viewcommand.OrderConfirmViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.converter.ShoppingcartViewCommandConverter;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver;
import com.baozun.nebula.web.controller.shoppingcart.resolver.predicate.MainLinesPredicate;
import com.feilong.core.util.CollectionsUtil;

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
 * <td>参见 {@link #showTransactionCheck(MemberDetails, String, HttpServletRequest, HttpServletResponse, Model)}</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>创建订单</td>
 * <td>参见 {@link #createOrder(MemberDetails, OrderForm, String, BindingResult, HttpServletRequest, HttpServletResponse, Model)}</td>
 * </tr>
 * <tr valign="top">
 * <td>新增收获地址</td>
 * <td>请调用
 * {@link com.baozun.nebula.web.controller.member.NebulaMemberAddressController#addMemberAddress(MemberDetails, com.baozun.nebula.web.controller.member.form.MemberAddressForm, org.springframework.validation.BindingResult, HttpServletRequest, HttpServletResponse, Model)
 * NebulaMemberAddressController#addMemberAddress}
 * </td>
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
 * <blockquote>
 * 业界有两种方式,天猫是post请求,京东是get请求
 * </blockquote>
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
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaOrderConfirmController.class);

    /** 订单提交的form 校验，需要在商城 spring 相关xml 里面进行配置. */
    @Autowired
    @Qualifier("orderFormValidator")
    private OrderFormValidator  orderFormValidator;

    
	@Autowired
	private SdkShoppingCartManager	sdkShoppingCartManager;

	@Autowired
	private SdkMemberManager		sdkMemberManager;

	/** The guest shoppingcart resolver. */
	@Autowired
	@Qualifier("guestShoppingcartResolver")
	private ShoppingcartResolver	guestShoppingcartResolver;

	/** The member shoppingcart resolver. */
	@Autowired
	@Qualifier("memberShoppingcartResolver")
	private ShoppingcartResolver	memberShoppingcartResolver;
	
    @Autowired
    @Qualifier("shoppingcartViewCommandConverter")
    private ShoppingcartViewCommandConverter shoppingcartViewCommandConverter;
    

	private static final String		MODEL_KEY_ORDER_CONFIRM	= "orderConfirmViewCommand";
	
	
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

        //TODO 获得购物车数据 (如果没有传入key 那么就是普通的购物车购买情况)

        //收获地址信息 
		List<ContactCommand>  addressList = null;
        if(memberDetails!=null){
        	 addressList =  sdkMemberManager.findAllContactListByMemberId(memberDetails.getMemberId());
        }
        // 获取购物车信息
        ShoppingCartCommand cartCommand = getChosenShoppingCartCommand(request, memberDetails);

        // 封装viewCommand
        OrderConfirmViewCommand OrderViewCommand = new OrderConfirmViewCommand();
        OrderViewCommand.setShoppingCartViewCommand(shoppingcartViewCommandConverter.convert(cartCommand));
        OrderViewCommand.setAddressList(addressList);

        model.addAttribute(MODEL_KEY_ORDER_CONFIRM, OrderViewCommand);

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
     * @RequestMapping(value = "/transaction/create", method = RequestMethod.POST)
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveOrder(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     *      com.baozun.nebula.sdk.command.SalesOrderCommand, java.util.Set)
     * @see com.baozun.nebula.sdk.manager.impl.OrderManagerImpl#saveOrderInfo(SalesOrderCommand, ShoppingCartCommand)
     * @see com.baozun.nebula.sdk.manager.impl.OrderManagerImpl#liquidateSkuInventory(List<ShoppingCartLineCommand>)
     * @see com.baozun.nebula.sdk.manager.impl.OrderManagerImpl#saveOrderLine(Long, ShoppingCartLineCommand)
     */
    public NebulaReturnResult createOrder(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "key",required = false) String key,//隐藏域中传递
                    @ModelAttribute("orderForm") OrderForm orderForm,
                    BindingResult bindingResult,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        //validator

        //dosome logic

        //取到支付地址
        //TODO
        return null;
    }
    
    
	/**
	 * Detect shoppingcart resolver.
	 *
	 * @param memberDetails
	 *            the member details
	 * @return the shoppingcart resolver
	 */
	private ShoppingcartResolver detectShoppingcartResolver(MemberDetails memberDetails){
		return null == memberDetails ? guestShoppingcartResolver : memberShoppingcartResolver;
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
	protected ShoppingCartCommand getChosenShoppingCartCommand(HttpServletRequest request,MemberDetails memberDetails){
		ShoppingcartResolver shoppingcartResolver = detectShoppingcartResolver(memberDetails);
		List<ShoppingCartLineCommand> cartLines = shoppingcartResolver.getShoppingCartLineCommandList(memberDetails, request);
		
		//过虑未选中的购物车行  过滤之后，金额是否需要重新计算？
		cartLines = CollectionsUtil.select(cartLines, new MainLinesPredicate()) ;
		if (null == cartLines) {
			return null;
		}
		Long memberId = null == memberDetails ? null : memberDetails.getMemberId();
		Set<String> memComboList = null == memberDetails ? null : memberDetails.getMemComboList();
		return sdkShoppingCartManager.findShoppingCart(memberId, memComboList, null, null, cartLines);
	}
}
