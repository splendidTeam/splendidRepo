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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * The Class SdkShoppingCartLineCommandBehaviourFactoryImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Service("sdkShoppingCartLineCommandBehaviourFactory")
public class SdkShoppingCartLineCommandBehaviourFactoryImpl implements SdkShoppingCartLineCommandBehaviourFactory{

    @Autowired
    @Qualifier("sdkShoppingCartLineCommandBundleBehaviour")
    private SdkShoppingCartLineCommandBehaviour sdkShoppingCartLineCommandBundleBehaviour;

    @Autowired
    @Qualifier("sdkShoppingCartLineCommandCommonBehaviour")
    private SdkShoppingCartLineCommandBehaviour sdkShoppingCartLineCommandCommonBehaviour;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartLineCommandBehaviourFactory#create(com.baozun.nebula.sdk.command.
     * shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public SdkShoppingCartLineCommandBehaviour getShoppingCartLineCommandBehaviour(ShoppingCartLineCommand shoppingCartLineCommand){
        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
        if (null == relatedItemId){
            return sdkShoppingCartLineCommandCommonBehaviour;
        }
        return sdkShoppingCartLineCommandBundleBehaviour;
    }
}
