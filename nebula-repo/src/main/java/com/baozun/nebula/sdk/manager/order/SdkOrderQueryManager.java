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

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * 提取出来,专注于查询订单.
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
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
public interface SdkOrderQueryManager extends BaseManager{

    /**
     * 根据交易流水号查询订单信息.
     * 
     * <p>
     * 敏感信息解密处理
     * </p>
     *
     * @param subOrdinate
     *            the sub ordinate
     * @param paySuccessStatus
     *            true表示支付成功
     * @return 如果 <code>subOrdinate</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>subOrdinate</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>subOrdinate 和 paySuccessStatus</code> 是查询不到PayInfoLog list,抛出异常<br>
     * 
     */
    SalesOrderCommand findSalesOrderCommandBySubOrdinate(String subOrdinate,boolean paySuccessStatus);

    /**
     * 根据交易流水号查询订单信息.
     * 
     * <p>
     * 敏感信息解密处理
     * </p>
     *
     * @param subOrdinate
     *            the sub ordinate
     * @param paySuccessStatus
     *            true表示支付成功
     * @return 如果 <code>subOrdinate</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>subOrdinate</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>subOrdinate</code> 是查询不到PayInfoLog list,抛出异常<br>
     * 
     */
    SalesOrderCommand findSalesOrderCommandBySubOrdinate(String subOrdinate);

}
