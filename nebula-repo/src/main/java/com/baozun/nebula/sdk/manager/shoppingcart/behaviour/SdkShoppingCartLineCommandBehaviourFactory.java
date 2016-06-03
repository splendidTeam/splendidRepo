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

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.proxy.ShoppingCartLineCommandBehaviour;

/**
 * The Interface SdkShoppingCartLineCommandBehaviourFactory.
 * 
 * <h3>定义为factory的优点:</h3>
 * <blockquote>
 * 
 * <dl>
 * <dt>1.可以使代码结构清晰，有效地封装变化</dt>
 * <dd>通过工厂模式，将产品的实例化封装起来，使得调用者根本无需关心产品的实例化过程，只需依赖工厂即可得到自己想要的产品。</dd>
 * 
 * <dt>2.对调用者屏蔽具体的产品类</dt>
 * <dd>如果使用工厂模式，调用者只关心产品的接口就可以了，至于具体的实现，调用者根本无需关心。</dd>
 * 
 * <dt>3.降低耦合度</dt>
 * <dd>产品类的实例化通常来说是很复杂的，它需要依赖很多的类，而这些类对于调用者来说根本无需知道，如果使用了工厂方法，我们需要做的仅仅是实例化好产品类，然后交给调用者使用。对调用者来说，产品所依赖的类都是透明的。</dd>
 * </dl>
 * 
 * </blockquote>
 * 
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public interface SdkShoppingCartLineCommandBehaviourFactory{

    /**
     * 创建.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @return the sdk shopping cart line command behaviour
     */
    ShoppingCartLineCommandBehaviour getShoppingCartLineCommandBehaviour(ShoppingCartLineCommand shoppingCartLineCommand);

}
