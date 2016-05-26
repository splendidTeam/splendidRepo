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
package com.baozun.nebula.sdk.manager.order;

import java.util.Set;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

/**
 * 提取出来,专注于创建订单.
 * 
 * <h3>说点题外话:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 在OOD设计原则里面,有两个重要的原则,
 * </p>
 * 
 * <p>
 * 第一,就是<b>单一职责原则 (Single Responsibility Principle)</b>,当需要修改某个类的时候原因有且只有一个。<br>
 * 换句话说就是让一个类只做一种类型责任，当这个类需要承当其他类型的责任的时候，就需要分解这个类。 类被修改的几率很大，因此应该专注于单一的功能。<br>
 * 如果你把多个功能放在同一个类中，功能之间就形成了关联，改变其中一个功能 ，有可能中止另一个功能，这时就需要新一轮的测试来避免可能出现的问题，非常耗时耗力。
 * </p>
 * 
 * <p>
 * 第二,就是 <b>接口分离原则 (Interface Segregation Principle)</b>,接口包含太多的方法也使其可用性降低,像这种包含了无用方法的"胖接口"会增加类之间的耦合.<br>
 * 接口隔离原则确保实现的接口有他们共同的职责,它们是明确的,易理解的,可复用的.
 * </p>
 * </blockquote>
 * 
 * @author feilong
 * @version 5.3.1 2016年5月14日 下午7:01:38
 * @since 5.3.1
 */
public interface SdkOrderCreateManager extends BaseManager{

    /**
     * 通过商品清单下订单.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param salesOrderCommand
     *            the sales order command
     * @param memCombos
     *            the mem combos
     * @return the string
     */
    //TODO 参数支持memberId 不支持memCombos
    //创建订单的核心思想是  ,某某人 要买什么什么东西, 收货地址 支付方式 等等是什么
    String saveOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand,Set<String> memCombos);

    /**
     * 手工下订单.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param salesOrderCommand
     *            the sales order command
     * @return the string
     * @deprecated 感觉应该可以和 {@link #saveOrder(ShoppingCartCommand, SalesOrderCommand, Set)}进行某种合并,暂时时间关系不动
     */
    @Deprecated
    String saveManualOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand);
}
