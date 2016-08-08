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
package com.baozun.nebula.web.controller.shoppingcart.validator;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.Constants;

/**
 * 购物车单行最大数量校验器.
 * 
 * <p>
 * 如果这个接口没有实现类,或者没有配置,那么默认将使用 历史商城使用的 {@link Constants#SHOPPING_CART_SKU_ONE_LINE_COUNT} 做为默认值
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.9
 */
public interface ShoppingcartOneLineMaxQuantityValidator{

    /**
     * 校验指定用户购买的指定<code>skuId</code>买的总数量 <code>totalCount</code>是否超过单行最大数量 MaxQuantity.
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <p>
     * 传入的 totalCount 参数是<b>累计数量</b>, 即原来购物车已有的数量(如果没有,以0替代)+新加入的数量
     * </p>
     * </blockquote>
     * 
     * <h3>关于单行最大数量 MaxQuantity:</h3>
     * <blockquote>
     * <p>
     * 具体实现上,你可以将这个单行最大数量 MaxQuantity配置在数据库,配置在 properties文件,配置上你实际项目方便的地方
     * </p>
     * </blockquote>
     * 
     * <h3>关于特殊校验:</h3>
     * <blockquote>
     * <p>
     * 另外,<code>memberDetails</code>,<code>skuId</code> 两个参数,支持自定义实现,比如特殊商品单款单件的功能
     * </p>
     * </blockquote>
     *
     * @param memberDetails
     *            the member details
     * @param skuId
     *            the sku id
     * @param totalQuantity
     *            购物车行这个skuid <b>累计数量</b>
     * @return 如果大于单行最大数量 MaxQuantity,那么返回true,否则返回false
     * @since 5.3.1.9
     */
    boolean isGreaterThanMaxQuantity(MemberDetails memberDetails,Long skuId,Integer totalQuantity);

}
