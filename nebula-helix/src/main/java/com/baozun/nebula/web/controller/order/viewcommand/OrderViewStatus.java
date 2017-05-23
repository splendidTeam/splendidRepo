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
package com.baozun.nebula.web.controller.order.viewcommand;

/**
 * 订单的显示状态.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>每个商城的订单显示状态内部逻辑可能不一样,支持扩展</li>
 * <li>如果商城有特殊显示状态,可以联系nebula 组扩展</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="http://jira.baozun.cn/browse/NB-565">扩展Command类</a>
 * @since 5.3.2.16
 */
public enum OrderViewStatus{

    /** 待支付. */
    TO_BE_PAY,

    /** 待发货. */
    TO_BE_DELIVERED,

    /** 待收货. */
    TO_BE_RECEIVED,

    /** 待评价. */
    TO_BE_RATE,

    /** 已完成. */
    COMPLETED,

    /** 已取消. */
    CANCEL

}
