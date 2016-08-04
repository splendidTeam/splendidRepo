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
package com.baozun.nebula.web.controller.shoppingcart.resolver;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineOperateCommonValidator;

/**
 * 购物车操作处理器.
 * 
 * <h3>主要定义以下接口或者操作:</h3>
 * 
 * <blockquote>
 * 
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #getShoppingCartLineCommandList(MemberDetails,HttpServletRequest ) getShoppingCartLineCommandList}</td>
 * <td>获得指定用户的购物车list(所有的包括选中的及没有选中的).</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #addShoppingCart(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse) addShoppingCart}</td>
 * <td>将特定的skuid,指定的数量count,加入到用户的购物车里面去.</td>
 * </tr>
 * 
 * 
 * <tr valign="top">
 * <td>{@link #updateShoppingCartCount(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse) updateShoppingCartCount}</td>
 * <td>更新指定的购物车行shoppingcartLineId的数量count.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #updateShoppingCartCount(MemberDetails, Map, HttpServletRequest, HttpServletResponse) updateShoppingCartCount with map}</td>
 * <td>批量更新购物车行shoppingcartLineId的数量count.(since 5.3.1.9)</td>
 * </tr>
 * 
 * 
 * <tr valign="top">
 * <td>{@link #toggleShoppingCartLineCheckStatus(MemberDetails, Long, boolean, HttpServletRequest, HttpServletResponse)
 * toggleShoppingCartLineCheckStatus}</td>
 * <td>切换指定购物车行的选中状态.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #toggleAllShoppingCartLineCheckStatus(MemberDetails, boolean, HttpServletRequest, HttpServletResponse)
 * toggleAllShoppingCartLineCheckStatus}</td>
 * <td>切换所有购物车行的选中状态.</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #deleteShoppingCartLine(MemberDetails, Long, HttpServletRequest, HttpServletResponse) deleteShoppingCartLine}</td>
 * <td>删除某个用户的某个特定 shoppingcartLineId 的购物车行.</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * 
 * <h3>缺少的功能:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 以不过多设计的原则,以下已知功能目前没有实现,将来视具体的项目需求,可以联系nebula产品组,再进行针对性开发,这样可以当时进行测试,效果更高
 * </p>
 * 
 * <ol>
 * <li>批量删除用户购物车行</li>
 * <li>清空用户购物车行</li>
 * </ol>
 * </blockquote>
 * 
 * @author weihui.tang
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月3日 下午1:35:48
 * @since 5.3.1
 */
public interface ShoppingcartResolver{

    /**
     * 获得指定用户的购物车list(所有的包括选中的及没有选中的).
     *
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param request
     *            the request
     * @return 如果指定的用户的购物车是空的,那么返回null
     */
    List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request);

    //***************************************************************************************
    /**
     * 将特定的<code>skuid</code>,指定的数量<code>count</code>,加入到用户的购物车里面去.
     * 
     * <h3>流程:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <img src="http://venusdrogon.github.io/feilong-platform/mysource/store/添加到购物车.png"/>
     * </p>
     * 
     * </blockquote>
     * 
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param skuId
     *            相关skuId
     * @param count
     *            数量
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
     * @throws NullPointerException
     *             如果 <code>skuId</code> 是null,或者 <code>count</code>是null
     */
    ShoppingcartResult addShoppingCart(MemberDetails memberDetails,Long skuId,Integer count,HttpServletRequest request,HttpServletResponse response);

    /**
     * 更新指定的购物车行<code>shoppingcartLineId</code>的数量<code>count</code>.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>参数count是全量数量</li>
     * <li>对于ajax批量修改不同购物车行数量的场景,请调用 {@link #updateShoppingCartCount(MemberDetails, Map, HttpServletRequest, HttpServletResponse)},<br>
     * 不要自行使用for循环来调用处理,可能会引起游客购物车结果数据不准确等问题</li>
     * </ol>
     * </blockquote>
     *
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param shoppingcartLineId
     *            指定的购物车行id,以前可能直接通过skuid来进行操作,现在用户的购物车可能相同的skuid存在不同的购物车行里面(比如bundle)
     * @param count
     *            全量数量
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
     */
    ShoppingcartResult updateShoppingCartCount(MemberDetails memberDetails,Long shoppingcartLineId,Integer count,HttpServletRequest request,HttpServletResponse response);

    /**
     * 支持批量更新购物车行<code>shoppingcartLineId</code>的数量<code>count</code>.
     * 
     * <h3>为什么需要这个批量操作的方法?</h3>
     * 
     * <blockquote>
     * <p>
     * Q:有了 {@link #updateShoppingCartCount(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse)},理论上不是可以通过 for 循环来操作的吗? 为什么要需要额外提供这个一个方法?
     * </p>
     * 
     * <p>
     * <b>A:原因有二:</b>
     * </p>
     * 
     * <ol>
     * <li>方便客户端直接调用,不需要些for 循环,额外的代码</li>
     * <li>关键的是,对于有些实现,比如 {@link GuestShoppingcartResolver},你如果用for 循环来操作,第二条获得的并不是第一条循环变更之后的数据,<br>
     * 原因在于,cookie获得的操作在同一个request请求里面都是取得 request header 里面的cookie字符串值,<br>
     * 再次说明了一点,cookie并不太适合存放这种业务敏感的数据,<br>
     * 
     * <p>
     * 参见图例:
     * </p>
     * 
     * <br>
     * <img src="http://venusdrogon.github.io/feilong-platform/mysource/store/批量修改购物车数量1.png"/>
     * 
     * <br>
     * <img src="http://venusdrogon.github.io/feilong-platform/mysource/store/批量修改购物车数量2.png"/>
     * </li>
     * </ol>
     * 
     * </blockquote>
     * 
     * <h3>关于 shoppingcartLineIdAndCountMap 参数:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 这是 购物车行id 和 修改之后全量数量的对照map
     * </p>
     * 
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * <tr style="background-color:#ccccff">
     * <th align="left">key和value</th>
     * <th align="left">说明</th>
     * </tr>
     * <tr valign="top">
     * <td>key 是指定的购物车行 id,即 shoppingcartLineId</td>
     * <td>指定的购物车行id,以前可能直接通过skuid来进行操作,现在用户的购物车可能相同的skuid存在不同的购物车行里面(比如bundle)</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>value是这行修改之后的数量</td>
     * <td>注意,参数count是全量数量</td>
     * </tr>
     * </table>
     * 
     * <h3>示例:</h3>
     * 
     * <p>
     * 如果要把 购物车行id 88 改成5个, 购物车行id 66 改成3个,你可以传入
     * </p>
     * 
     * <pre class="code">
     * Map{@code <Long, Integer>} shoppingcartLineIdAndCountMap = new HashMap{@code <>}();
     * shoppingcartLineIdAndCountMap.put(88L, 5);
     * shoppingcartLineIdAndCountMap.put(66L, 3);
     * </pre>
     * 
     * <p>
     * 对于每个count的 validate校验,参考通用校验 {@link ShoppingcartLineOperateCommonValidator#validate(Sku, Integer)}
     * </p>
     * 
     * </blockquote>
     * 
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param shoppingcartLineIdAndCountMap
     *            购物车行id 和 修改之后全量数量的对照map
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
     * @throws NullPointerException
     *             如果 <code>shoppingcartLineIdAndCountMap</code> 是null
     * @throws IllegalArgumentException
     *             如果 <code>shoppingcartLineIdAndCountMap</code> 是empty
     * @since 5.3.1.9
     */
    ShoppingcartResult updateShoppingCartCount(MemberDetails memberDetails,Map<Long, Integer> shoppingcartLineIdAndCountMap,HttpServletRequest request,HttpServletResponse response);

    /**
     * 删除某个用户的某个特定 <code>shoppingcartLineId</code> 的购物车行.
     *
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param shoppingcartLineId
     *            指定的购物车行id,以前可能直接通过skuid来进行操作,现在用户的购物车可能相同的skuid存在不同的购物车行里面(比如bundle)
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
     */
    ShoppingcartResult deleteShoppingCartLine(MemberDetails memberDetails,Long shoppingcartLineId,HttpServletRequest request,HttpServletResponse response);

    /**
     * 切换指定购物车行的选中状态.
     * 
     * <p>
     * 参数 <code>checkStatus</code>,用来标识选中还是不选中,true为将当前行选中,false为将当前行不选中
     * </p>
     *
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param shoppingcartLineId
     *            指定的购物车行
     * @param checkStatus
     *            true为将当前行选中,false为将当前行不选中
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
     */
    ShoppingcartResult toggleShoppingCartLineCheckStatus(MemberDetails memberDetails,Long shoppingcartLineId,boolean checkStatus,HttpServletRequest request,HttpServletResponse response);

    /**
     * 切换所有购物车行的选中状态.
     * 
     * <p>
     * 参数 <code>checkStatus</code>,用来标识选中还是不选中,true为将全部选中,false为全部不选中
     * </p>
     *
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param checkStatus
     *            true为将全部选中,false为全部不选中
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
     */
    ShoppingcartResult toggleAllShoppingCartLineCheckStatus(MemberDetails memberDetails,boolean checkStatus,HttpServletRequest request,HttpServletResponse response);
}
