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

import java.util.Date;

import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.sdk.command.shoppingcart.CookieShoppingCartLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.date.DateUtil;
import com.feilong.core.util.RandomUtil;

/**
 * The Class ToCookieShoppingCartLineTransformer.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see ToShoppingCartLineCommandTransformer
 * @since 5.3.1
 */
//default Access Modifiers
class ToCookieShoppingCartLineTransformer implements Transformer<ShoppingCartLineCommand, CookieShoppingCartLine>{

    /** The Constant LOGGER. */
    private static final Logger   LOGGER              = LoggerFactory.getLogger(ToCookieShoppingCartLineTransformer.class);

    /** The Constant COPY_PROPERTY_NAMES. */
    private static final String[] COPY_PROPERTY_NAMES = {
                                                          "skuId",
                                                          "extentionCode",
                                                          "quantity",
                                                          "createTime",
                                                          "settlementState",
                                                          "lineGroup" };

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections4.Transformer#transform(java.lang.Object)
     */
    @Override
    public CookieShoppingCartLine transform(ShoppingCartLineCommand shoppingCartLineCommand){
        CookieShoppingCartLine cookieShoppingCartLine = new CookieShoppingCartLine();
        PropertyUtil.copyProperties(cookieShoppingCartLine, shoppingCartLineCommand, COPY_PROPERTY_NAMES);

        cookieShoppingCartLine.setIsGift(shoppingCartLineCommand.isGift());
        // XXX feilong bundle 以后再考虑 id
        cookieShoppingCartLine.setId(buildId(shoppingCartLineCommand));
        return cookieShoppingCartLine;
    }

    /**
     * Builds the id.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @return the long
     */
    private long buildId(ShoppingCartLineCommand shoppingCartLineCommand){
        Long id = shoppingCartLineCommand.getId();
        return null == id ? createId() : id;

    }

    /**
     * 创建 id.
     *
     * @return the long
     */
    private long createId(){
        Date now = new Date();
        //shoppingCartLines.size() 不能解决删除的问题
        return DateUtil.getTime(now) + RandomUtil.createRandomWithLength(2);
    }
}
