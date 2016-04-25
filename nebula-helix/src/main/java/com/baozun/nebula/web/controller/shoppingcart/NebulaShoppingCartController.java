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
package com.baozun.nebula.web.controller.shoppingcart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand;

/**
 * 购物车控制器.
 * 
 * <p>
 * 主要由以下方法组成:
 * </p>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>{@link #showShoppingCart(MemberDetails, HttpServletRequest, HttpServletResponse, Model) showShoppingCart}</td>
 * <td>显示购物车页面</td>
 * </tr>
 * <tr valign="top">
 * <td>{@link #addShoppingCart(MemberDetails, Long, Long, HttpServletRequest, HttpServletResponse, Model) addShoppingCart}</td>
 * <td>加入购物车</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #deleteShoppingCartLine(MemberDetails, Long, HttpServletRequest, HttpServletResponse, Model) deleteShoppingCartLine}</td>
 * <td>删除购物车某个商品</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #updateShoppingCartCount(MemberDetails, Long, Long, HttpServletRequest, HttpServletResponse, Model) updateShoppingCartCount}
 * </td>
 * <td>修改购物车数量</td>
 * </tr>
 * <tr valign="top">
 * <td>//TODO</td>
 * <td>修改商品销售属性</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * <h3>关于购物车数量:</h3>
 * <blockquote>
 * 
 * <p>
 * 通常每个页面的头部都会显示购物车数量,此处数量直接从Cookie中获取,比如页面使用
 * ${cookie.shoppingcartcount}来获取,不用每次都从后台获取;如果页面将来做静态化,可以使用javascript来赋值,比如$.cookie('shoppingcartcount');
 * </p>
 * 
 * <p>
 * 注意:该cookie的设置是 非httponly的,以便js获取
 * </p>
 * 
 * <h4>那么如何保证这个cookie的安全:</h4>
 * <blockquote>
 * 没有保证这个cookie安全的必要,理由有2,
 * <ol>
 * <li>这个cookie仅仅用作显示,用户修改这个cookie就和用户直接使用firedebug修改元素值是一样的</li>
 * <li>此cookie即使被捕获了,也没有什么影响,只是个数量</li>
 * </ol>
 * </blockquote>
 * 
 * <h4>该cookie的更新机制是什么:</h4>
 * <blockquote>
 * <ol>
 * <li>当购物车有任何变更,在merge的同时,都需要更新这个Cookie</li>
 * </ol>
 * </blockquote>
 * </blockquote>
 * 
 * <p>
 * 所有的请求游客都可以操作,所以不需要加 NeedLogin
 * </p>
 *
 * @author feilong
 * @version 5.3.1 2016年4月21日 下午6:18:53
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand
 * @see com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand
 * @since 5.3.1
 */
public class NebulaShoppingCartController extends BaseController{

    /** The Constant log. */
    private static final Logger LOGGER                   = LoggerFactory.getLogger(NebulaShoppingCartController.class);

    /** The Constant MODEL_KEY_MEMBER_ADDRESS. */
    private static final String MODEL_KEY_MEMBER_ADDRESS = "shoppingCartViewCommand";

    /** The Constant VIEW_MEMBER_ADDRESS_LIST. */
    private static final String VIEW_MEMBER_ADDRESS_LIST = "shoppingcart.list";

    /**
     * 显示用户的购物车.
     *
     * @param memberDetails
     *            the member details
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the string
     * @RequestMapping(value = "/shoppingcart", method = RequestMethod.GET)
     */
    public String showShoppingCart(
                    @LoginMember MemberDetails memberDetails,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        ShoppingCartViewCommand shoppingCartViewCommand = null;

        //TODO convert

        model.addAttribute(MODEL_KEY_MEMBER_ADDRESS, shoppingCartViewCommand);
        return VIEW_MEMBER_ADDRESS_LIST;
    }

    /**
     * 添加购物车.
     * 
     * <p>
     * 用户购买选定的sku,指定数量加入到购物车
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param skuId
     *            the sku id
     * @param count
     *            the count
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/shoppingcart/add", method = RequestMethod.POST)
     */
    public NebulaReturnResult addShoppingCart(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "skuId",required = true) Long skuId,
                    @RequestParam(value = "count",required = true) Long count,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        Validate.isTrue(count >= 1, "count cannot less than 1");
        //TODO
        return null;
    }

    /**
     * 删除购物车行.
     * <p>
     * 注意,此处参数设计为shoppingcartLineId 而不是 skuid,因为将来会出现 一个用户购物车里面会出现相同的sku,
     * 
     * <br>
     * 比如一个属于bundle 一个属于单买的;或者 一个是购买的, 一个属于赠品;将来需要区分
     * 
     * <br>
     * <span style="color:red">服务端必须同时拿shoppingcartLineId和memberId做参数,否则可能会出现安全漏洞</span>
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            the shoppingcartline id
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/shoppingcart/delete", method = RequestMethod.POST)
     */
    public NebulaReturnResult deleteShoppingCartLine(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "shoppingcartLineId",required = true) Long shoppingcartLineId,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){
        //TODO
        return null;
    }

    /**
     * 修改用户的购物车数量.
     * 
     * <p>
     * 注意,此处参数设计为shoppingcartLineId 而不是 skuid,因为将来会出现 一个用户购物车里面会出现相同的sku,
     * 
     * <br>
     * 比如一个属于bundle 一个属于单买的;或者 一个是购买的, 一个属于赠品;将来需要区分
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            the shoppingcartline id
     * @param count
     *            最终数量值,而非 incr值
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/shoppingcart/update", method = RequestMethod.POST)
     */
    public NebulaReturnResult updateShoppingCartCount(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "shoppingcartLineId",required = true) Long shoppingcartLineId,
                    @RequestParam(value = "count",required = true) Long count,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        Validate.isTrue(count >= 1, "count cannot less than 1");
        //TODO
        return null;
    }

    //TODO 修改销售属性 
    //TODO 删除bundle

    //TODO 选中

    /**
     * merge购物车之后做的事情.通常 {@link #addShoppingCart(MemberDetails, Long, Long, HttpServletRequest, HttpServletResponse, Model)},
     * {@link #updateShoppingCartCount(MemberDetails, Long, Long, HttpServletRequest, HttpServletResponse, Model)},
     * {@link #deleteShoppingCartLine(MemberDetails, Long, HttpServletRequest, HttpServletResponse, Model)} 都需要调用他
     *
     * @since 5.3.1
     */
    private void afterMergeShoppingCart(){
        //将购物车数量塞到Cookie 里面去
    }
}
