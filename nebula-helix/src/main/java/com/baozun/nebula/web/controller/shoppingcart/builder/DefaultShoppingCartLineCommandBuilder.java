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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;

import static com.feilong.core.util.CollectionsUtil.collect;

/**
 * 默认的 {@link ShoppingCartLineCommand} 构造器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
@Component("shoppingCartLineCommandBuilder")
public class DefaultShoppingCartLineCommandBuilder implements ShoppingCartLineCommandBuilder{

    @Autowired
    private ShoppingCartLineSettlementStateBuilder shoppingCartLineSettlementStateBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartLineCommandBuilder#build(com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm, java.lang.String)
     */
    @Override
    public ShoppingCartLineCommand build(ShoppingCartLineAddForm shoppingCartLineAddForm,String extentionCode){
        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();
        shoppingCartLineCommand.setSkuId(shoppingCartLineAddForm.getSkuId());
        shoppingCartLineCommand.setExtentionCode(extentionCode);
        shoppingCartLineCommand.setQuantity(shoppingCartLineAddForm.getCount());

        shoppingCartLineCommand.setShoppingCartLinePackageInfoCommandList(collect(shoppingCartLineAddForm.getPackageInfoFormList(), ShoppingCartLinePackageInfoCommand.class));

        shoppingCartLineCommand.setSettlementState(shoppingCartLineSettlementStateBuilder.build(shoppingCartLineAddForm, extentionCode));

        shoppingCartLineCommand.setCreateTime(new Date());
        return shoppingCartLineCommand;
    }

}
