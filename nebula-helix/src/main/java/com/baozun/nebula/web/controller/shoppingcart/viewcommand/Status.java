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

/**
 * 购物车行状态.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.6
 */
public enum Status{

    /** 正常状态. */
    NORMAL,

    //*************************************************
//    /** 商品code 不存在. */
//    ITEM_CODE_NOT_EXIT,

    /** 商品下架. */
    ITEM_LIFECYCLE_OFF_SHELF,

//    /** 商品逻辑删除. */
//    ITEM_LIFECYCLE_LOGICAL_DELETION,

//    /** 商品新建状态. */
//    ITEM_LIFECYCLE_NEW,
//
//    /** 商品未到提前上架时间. */
//    ITEM_NOT_AHEAD_OF_TIME,

    //*************************************************
    /** 库存不足. */
    OUT_OF_STOCK;

}