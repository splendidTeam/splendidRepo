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
package com.baozun.nebula.web.controller.shoppingcart.builder;

/**
 * 单行可购买最大值构造器
 * @author bowen.dai
 * @since 5.3.2.22
 */
public interface ShoppingcartOneLineMaxQuantityBuilder{
    /**
     * 构建单行可购买最大值
     * 
     * @param memberId 此处参数不使用MemberDetails的原因是因为在同步游客和会员购物车进行合并时传入参数限制，故此处参数使用memberID
     * @param skuId
     * @return
     */
    int build(Long memberId,Long skuId);

}
