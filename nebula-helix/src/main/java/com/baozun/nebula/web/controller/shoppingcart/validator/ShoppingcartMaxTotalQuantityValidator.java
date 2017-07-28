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

/**
 * 购物车购买商品总数校验器
 * @author bowen.dai
 * 
 * @since 5.3.2.22
 *
 */
public interface ShoppingcartMaxTotalQuantityValidator{

    /**
     * 校验指定用户购物车中商品总数量是否超过总行最大数量 MaxCount(购买商品最大总数量MaxCount自己实现).
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <p>
     * 传入的 <code>totalCount</code> 参数是<b>购物车中商品总数量</b>(已经计算新增加的商品数量), 即原来购物车已有的商品总数量+新加入的商品总数量
     * </p>
     * </blockquote>
     * 
     * <h3>关于购物车中商品最大数量 MaxCount:</h3>
     * <blockquote>
     * <p>
     * 具体实现上,你可以将这个最大数量 MaxCount配置在数据库,配置在 properties文件,配置上你实际项目方便的地方
     * </p>
     * </blockquote>
     * 
     * <h3>关于特殊校验:</h3>
     * <blockquote>
     * <p>
     * 另外,<code>memberDetails</code>参数支持自定义实现,比如游客最多10个,会员最多99个诸如此类的
     * </p>
     * </blockquote>
     *
     * @param memberDetails
     *            the member details
     * @param totalCount
     *            购物车中商品总数量(add操作后)
     * @return 如果大于商品最大数量 MaxCount,那么返回true,否则返回false
     * @since 5.3.2.22
     */
    boolean isGreaterThanMaxQuantity(MemberDetails memberDetails,Integer totalCount);

}
