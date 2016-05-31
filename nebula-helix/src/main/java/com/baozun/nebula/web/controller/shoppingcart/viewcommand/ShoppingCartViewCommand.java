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
package com.baozun.nebula.web.controller.shoppingcart.viewcommand;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 专门用来渲染购物车页面的.
 * 
 * <h3>购物车有什么:</h3>
 * <blockquote>
 * <ul>
 * <li>有基于店铺下面的购物车行list,{@link #getShopAndShoppingCartLineSubViewCommandListMap()},关于此对象核心操作点在于<b>构造</b>以及<b>排序 </b></li>
 * </ul>
 * </blockquote>
 * 
 * <h3>购物车没有什么:</h3>
 * <blockquote>
 * <ul>
 * <li>通常购物车是不含运费的,运费基于收获地址才有的,通常购物车和收获地址无关</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>咦,这个购物车viewcommand 就一个map元素啊,直接用map不就得了:</h3>
 * <blockquote>
 * 这个地方购物车的变化会比较复杂,封装一个对象,<b>扩展性</b>(比如extends)以及<b>理解性</b>会更好一点
 * </blockquote>
 * 
 * <h3>哎呀,这个购物车viewcommand怎么元素这么少啊,我页面还要显示优惠券,促销等等信息啊:</h3>
 * <blockquote>
 * 不好意思,优惠券等信息请独立构造,或者使用ajax形式来渲染<b>(推荐)</b>;这个对象关注的重点是 用户买了什么,而买了的东西享受何种促销或者活动请额外处理
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年4月22日 下午7:34:27
 * @since 5.3.1
 */
public class ShoppingCartViewCommand extends BaseViewCommand{

    /** The Constant serialVersionUID. */
    private static final long                                             serialVersionUID = 7847538393318014437L;

    /**
     * 基于店铺的购物车行列表map,不同的店铺下面挑选了那些东西.
     */
    private Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> shopAndShoppingCartLineSubViewCommandListMap;

    //*******************************************************************************************************
    /**
     * 获得 基于店铺的购物车行列表map,不同的店铺下面挑选了那些东西.
     *
     * @return the shopAndShoppingCartLineSubViewCommandListMap
     */
    public Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> getShopAndShoppingCartLineSubViewCommandListMap(){
        return shopAndShoppingCartLineSubViewCommandListMap;
    }

    /**
     * 设置 基于店铺的购物车行列表map,不同的店铺下面挑选了那些东西.
     *
     * @param shopAndShoppingCartLineSubViewCommandListMap
     *            the shopAndShoppingCartLineSubViewCommandListMap to set
     */
    public void setShopAndShoppingCartLineSubViewCommandListMap(
                    Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> shopAndShoppingCartLineSubViewCommandListMap){
        this.shopAndShoppingCartLineSubViewCommandListMap = shopAndShoppingCartLineSubViewCommandListMap;
    }
}
