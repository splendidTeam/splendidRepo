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
package com.baozun.nebula.web.controller.shoppingcart.factory;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.baozun.nebula.web.controller.shoppingcart.form.CommonImmediatelyBuyForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ImmediatelyBuyForm;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLinePackageInfoFormListValidator;

import static com.feilong.core.util.CollectionsUtil.collect;

/**
 * The Class CommonImmediatelyBuyAdaptor.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public class CommonImmediatelyBuyAdaptor extends OneShoppingCartLineCommandImmediatelyBuyAdaptor{

    /**
     * 包装信息的校验.
     * 
     * @since 5.3.2.27
     */
    @Autowired
    private ShoppingcartLinePackageInfoFormListValidator shoppingcartLinePackageInfoFormListValidator;

    @Override
    protected ShoppingCartLineCommand buildShoppingCartLineCommand(ImmediatelyBuyForm immediatelyBuyForm,HttpServletRequest request){
        CommonImmediatelyBuyForm commonImmediatelyBuyForm = (CommonImmediatelyBuyForm) immediatelyBuyForm;

        shoppingcartLinePackageInfoFormListValidator.validator(commonImmediatelyBuyForm.getPackageInfoFormList());

        //TODO 校验

        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();

        shoppingCartLineCommand.setSkuId(commonImmediatelyBuyForm.getSkuId());
        shoppingCartLineCommand.setQuantity(commonImmediatelyBuyForm.getCount());
        //购物车行中放入misc字段的值  @since 5.3.2.18
        shoppingCartLineCommand.setMisc((String) request.getAttribute(ImmediatelyBuyShoppingCartLineCommandListFactory.REQUEST_ATTRIBUTE_MISC));
        //选中
        shoppingCartLineCommand.setSettlementState(1);

        //@since 5.3.2.27
        shoppingCartLineCommand.setShoppingCartLinePackageInfoCommandList(collect(commonImmediatelyBuyForm.getPackageInfoFormList(), ShoppingCartLinePackageInfoCommand.class));

        return shoppingCartLineCommand;
    }
}
