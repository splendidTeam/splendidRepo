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
package com.baozun.nebula.web.controller.shoppingcart.persister;

import org.apache.commons.collections4.Transformer;

import com.baozun.nebula.sdk.command.shoppingcart.CookieShoppingCartLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.feilong.core.bean.PropertyUtil;

/**
 * 将{@link CookieShoppingCartLine} 转换成{@link ShoppingCartLineCommand} 转换器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see ToCookieShoppingCartLineTransformer
 * @since 5.3.1
 */
//default Access Modifiers
class ToShoppingCartLineCommandTransformer implements Transformer<CookieShoppingCartLine, ShoppingCartLineCommand>{

    /** The Constant COPY_PROPERTY_NAMES. */
    private static final String[] COPY_PROPERTY_NAMES = { //
                                                          "skuId",
                                                          "extentionCode",
                                                          "quantity",
                                                          "createTime",
                                                          "settlementState",
                                                          "lineGroup",
                                                          "shoppingCartLinePackageInfoCommandList" };

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections4.Transformer#transform(java.lang.Object)
     */
    @Override
    public ShoppingCartLineCommand transform(CookieShoppingCartLine cookieShoppingCartLine){
        // 将cookie中的购物车 转换为 shoppingCartLineCommand
        ShoppingCartLineCommand shoppingLineCommand = new ShoppingCartLineCommand();
        PropertyUtil.copyProperties(shoppingLineCommand, cookieShoppingCartLine, COPY_PROPERTY_NAMES);

        shoppingLineCommand.setId(cookieShoppingCartLine.getId());
        shoppingLineCommand.setGift(null == cookieShoppingCartLine.getIsGift() ? false : cookieShoppingCartLine.getIsGift());

        return shoppingLineCommand;
    }
}
