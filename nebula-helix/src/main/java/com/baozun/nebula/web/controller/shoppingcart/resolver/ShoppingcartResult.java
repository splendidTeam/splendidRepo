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

/**
 * 购物车操作的结果.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public enum ShoppingcartResult{

    /** 成功. */
    SUCCESS,

    //---------------------------------------------------------------------

    /** sku不存在. */
    SKU_NOT_EXIST,

    /** sku不可用. */
    SKU_NOT_ENABLE,

    /** 商品状态不可用. */
    ITEM_STATUS_NOT_ENABLE,

    /** 没到可以购买的时间. */
    ITEM_NOT_ACTIVE_TIME,

    /** 商品是 gift 不可以操作. */
    ITEM_IS_GIFT,

    /** 购物车主卖行超过大小. */
    MAIN_LINE_MAX_THAN_COUNT,

    /** 单商品超过最大数量. */
    ONE_LINE_MAX_THAN_COUNT,

    /**
     * 合并购物车行之后,单商品超过最大数量.
     * 
     * @since 5.3.2.3
     */
    ONE_LINE_MAX_THAN_COUNT_AFTER_MERGED,

    /** 超过库存数. */
    MAX_THAN_INVENTORY,
    
    /** 购买总商品数量超过最大限制.
     * 
     * @since 5.3.2.22
     *  */
    TOTAL_MAX_THAN_QUANTITY,

    // ******************删除*************************************************

    /** 基于 shoppingcart lines 查找SHOPPING_CART_LINE_COMMANDs 的大小和预期不匹配 可能已经被删掉了 （比如 打开了双窗口）. */
    SHOPPING_CART_LINE_SIZE_NOT_EXPECT,

    // ******************修改*************************************************

    /** 基于 shoppingcart line 查找SHOPPING_CART_LINE_COMMAND 可能已经被删掉了 （比如 打开了双窗口）. */
    SHOPPING_CART_LINE_COMMAND_NOT_FOUND,

    //---------------------------------------------------------------------

    /** 数据库层操作失败. */
    OPERATE_ERROR,

    //-------------------自定义校验-----------------------------------------

    /**
     * 自定义校验时候的失败.
     * 
     * @since 5.3.2.20
     */
    CUSTOM_FAIL;

}
