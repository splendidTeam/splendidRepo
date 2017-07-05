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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder;
import com.baozun.nebula.web.controller.shoppingcart.converter.ShoppingcartViewCommandConverter;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm;
import com.baozun.nebula.web.controller.shoppingcart.handler.UncheckedInvalidStateShoppingCartLineHandler;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
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
 * 
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #showShoppingCart(MemberDetails, HttpServletRequest, Model) showShoppingCart}</td>
 * <td>显示购物车页面</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #addShoppingCart(MemberDetails, ShoppingCartLineAddForm, HttpServletRequest, HttpServletResponse, Model) addShoppingCart}</td>
 * <td>基于ShoppingCartLineAddForm 添加购物车</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #updateShoppingCartLine(MemberDetails, Long, ShoppingCartLineUpdateSkuForm, HttpServletRequest, HttpServletResponse, Model) updateShoppingCartSku}</td>
 * <td>基于ShoppingCartLineUpdateSkuForm 修改购物车行</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * 
 * <h3>关于购物车数量:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 通常每个页面的头部都会显示购物车数量,此处数量直接从Cookie中获取,比如页面使用${cookie.shoppingcartcount}来获取,不用每次都从后台获取;如果页面将来做静态化,可以使用javascript来赋值,比如$.cookie('shoppingcartcount');
 * </p>
 * 
 * <p>
 * 注意:该cookie的设置是 非httponly的,以便js获取
 * </p>
 * 
 * <h4>那么如何保证这个cookie的安全:</h4>
 * 
 * <blockquote> 没有保证这个cookie安全的必要,理由有2,
 * <ol>
 * <li>这个cookie仅仅用作显示,用户修改这个cookie就和用户直接使用firedebug修改元素值是一样的</li>
 * <li>此cookie即使被捕获了,也没有什么影响,只是个数量</li>
 * </ol>
 * </blockquote>
 * 
 * <h4>该cookie的更新机制是什么:</h4> <blockquote>
 * <ol>
 * <li>当购物车有任何变更,在merge的同时,都需要更新这个Cookie</li>
 * </ol>
 * </blockquote>
 * 
 * </blockquote>
 * 
 * <h3>关于 购物车 {@link com.baozun.nebula.web.NeedLogin}</h3>
 * 
 * <blockquote>
 * <p>
 * 所有的请求游客都可以操作,所以不需要加 NeedLogin
 * </p>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年4月21日 下午6:18:53
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand
 * @see com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand
 * @since 5.3.1
 */
public class NebulaShoppingCartController extends NebulaAbstractCommonShoppingCartController{

    /** The shopping cart command builder. */
    @Autowired
    private ShoppingCartCommandBuilder shoppingCartCommandBuilder;

    /** The shoppingcart view command converter. */
    @Autowired
    @Qualifier("shoppingcartViewCommandConverter")
    private ShoppingcartViewCommandConverter shoppingcartViewCommandConverter;

    /** The unchecked invalid state shopping cart line handler. */
    @Autowired
    private UncheckedInvalidStateShoppingCartLineHandler uncheckedInvalidStateShoppingCartLineHandler;

    //**********************************showShoppingCart************************************************

    /**
     * 显示用户的购物车.
     *
     * @param memberDetails
     *            the member details
     * @param request
     *            the request
     * @param response
     * @param model
     *            the model
     * @return the string
     * @RequestMapping(value = "/shoppingcart", method = RequestMethod.GET)
     */
    public String showShoppingCart(@LoginMember MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response,Model model){
        ShoppingCartViewCommand shoppingCartViewCommand = buildShoppingCartViewCommand(memberDetails, request);

        //将状态不对的 选中状态的订单行 变成不选中. 
        uncheckedInvalidStateShoppingCartLineHandler.uncheckedInvalidStateShoppingCartLine(memberDetails, shoppingCartViewCommand, request, response);

        model.addAttribute("shoppingCartViewCommand", shoppingCartViewCommand);
        return "shoppingcart.shoppingcart";
    }

    /**
     * 构造 ShoppingCartViewCommand.
     *
     * @param memberDetails
     *            the member details
     * @param request
     *            the request
     * @return the shopping cart view command
     * @since 5.3.2.6
     */
    protected ShoppingCartViewCommand buildShoppingCartViewCommand(MemberDetails memberDetails,HttpServletRequest request){
        // 获取购物车信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingcartFactory.getShoppingCartLineCommandList(memberDetails, request);
        ShoppingCartCommand shoppingCartCommand = shoppingCartCommandBuilder.buildShoppingCartCommand(memberDetails, shoppingCartLineCommandList);

        // 封装viewCommand
        return shoppingcartViewCommandConverter.convert(shoppingCartCommand);
    }

    //**********************************addShoppingCart************************************************

    /**
     * 使用添加表单来的添加到购物车.
     * 
     * <p>
     * 用户可以购买选定的sku,指定数量,以及包装信息等加入到购物车,相比较 {@link #addShoppingCart(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse, Model)} 更加高级
     * </p>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * 由于以下原因:
     * 
     * <ol>
     * <li>此功能并不是每个store 都需要</li>
     * <li>通常而言不同的商城其实包装信息不同</li>
     * <li>价格等因子理论上都是内部计算(不可以通过url传输)</li>
     * </ol>
     * 此方法一般不直接mapping url 地址, 而是url地址解析到mapping方法,再调用这个方法
     * </blockquote>
     *
     * @param memberDetails
     *            一个用户
     * @param shoppingCartLineAddForm
     *            添加购物车的表单
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return 如果操作成功返回 {@link DefaultReturnResult#SUCCESS},否则会基于{@link ShoppingcartResult} 构造 {@link DefaultReturnResult} 并返回
     * @see #addShoppingCart(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse, Model)
     * @see #addShoppingCartBatch(MemberDetails, Long[], Integer, HttpServletRequest, HttpServletResponse, Model)
     * @since 5.3.2.13
     * @RequestMapping(value = "/shoppingcart/addcart", method = RequestMethod.POST)
     */
    public NebulaReturnResult addShoppingCart(//
                    @LoginMember MemberDetails memberDetails,
                    @ModelAttribute("shoppingCartLineAddForm") ShoppingCartLineAddForm shoppingCartLineAddForm,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.addShoppingCart(memberDetails, shoppingCartLineAddForm, request, response);
        return toNebulaReturnResult(shoppingcartResult);
    }

    /**
     * 基于ShoppingCartLineUpdateSkuForm 修改购物车行(可以买新的SKU).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>注意,此处参数设计为shoppingcartLineId 而不是 skuid,因为将来会出现 一个用户购物车里面会出现相同的sku,
     * <br>
     * 比如一个属于bundle 一个属于单买的;或者 一个是购买的,一个属于赠品;将来需要区分</li>
     * <li>通常用来相同款商品修改销售属性,比如修改尺码;</li>
     * </ol>
     * </blockquote>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            the shoppingcartline id
     * @param shoppingCartLineUpdateSkuForm
     *            the shopping cart line update form
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return 如果操作成功返回 {@link DefaultReturnResult#SUCCESS},否则会基于{@link ShoppingcartResult} 构造 {@link DefaultReturnResult} 并返回
     * 
     * @see <a href="http://jira.baozun.cn/browse/NB-367">购物车添加修改商品尺码的功能</a>
     * @since 5.3.2.3
     * @RequestMapping(value = "/shoppingcart/updateline", method = RequestMethod.POST)
     */
    public NebulaReturnResult updateShoppingCartLine(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "shoppingcartLineId",required = true) Long shoppingcartLineId,
                    @ModelAttribute("shoppingCartLineUpdateSkuForm") ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @SuppressWarnings("unused") Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.updateShoppingCartLine(memberDetails, shoppingcartLineId, shoppingCartLineUpdateSkuForm, request, response);
        return toNebulaReturnResult(shoppingcartResult);
    }

}
