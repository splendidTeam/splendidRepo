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

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartCommandBuilder;
import com.baozun.nebula.web.MemberDetails;
import com.feilong.core.Validator;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月24日 下午4:59:37
 * @since 5.3.1
 */
@Component("shoppingCartCommandBuilder")
public class ShoppingCartCommandBuilderImpl implements ShoppingCartCommandBuilder{

    @Autowired
    private SdkShoppingCartCommandBuilder sdkShoppingCartCommandBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder#buildShoppingCartCommand(com.baozun.nebula.web.
     * MemberDetails, java.util.List)
     */
    @Override
    public ShoppingCartCommand buildShoppingCartCommand(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLines){
        return buildShoppingCartCommand(memberDetails, shoppingCartLines, null, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder#buildShoppingCartCommand(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand, java.util.List)
     */
    @Override
    public ShoppingCartCommand buildShoppingCartCommand(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    CalcFreightCommand calcFreightCommand,
                    List<String> coupons){
        if (Validator.isNullOrEmpty(shoppingCartLines)){
            return null;
        }

        Long groupId = null == memberDetails ? null : memberDetails.getGroupId();
        Set<String> memComboList = null == memberDetails ? null : memberDetails.getMemComboList();
        return sdkShoppingCartCommandBuilder
                        .buildShoppingCartCommand(groupId, shoppingCartLines, calcFreightCommand, coupons, memComboList);
    }

}
