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
package com.baozun.nebula.sdk.command;

import com.baozun.nebula.model.BaseModel;

/**
 * 订单创建的选项设置.
 * 
 * <p>
 * 从 {@link SalesOrderCommand}中剥离出来,职责分离,也便于扩展
 * </p>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>{@link SalesOrderCommand}</td>
 * <td>包含 收获地址,支付方式等信息</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link SalesOrderCreateOptions}</td>
 * <td>包含这次购买的一些特性, 比如 是否是立即购买,是否是QS订单等等</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月26日 下午12:26:50
 * @since 5.3.1
 */
public class SalesOrderCreateOptions extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2790187147863846666L;

    /** 是否是立即购买的订单 *. */
    private boolean           isImmediatelyBuy;

    /** 是否是后台下单 *. */
    private boolean           isBackCreateOrder;

    /**
     * 是否qs.
     * 
     * @deprecated 没有用到
     */
    @Deprecated
    private boolean           isQs;

    /**
     * 获得 是否是立即购买的订单 *.
     *
     * @return the isImmediatelyBuy
     */
    public boolean getIsImmediatelyBuy(){
        return isImmediatelyBuy;
    }

    /**
     * 设置 是否是立即购买的订单 *.
     *
     * @param isImmediatelyBuy
     *            the isImmediatelyBuy to set
     */
    public void setIsImmediatelyBuy(boolean isImmediatelyBuy){
        this.isImmediatelyBuy = isImmediatelyBuy;
    }

    /**
     * 获得 是否是后台下单 *.
     *
     * @return the isBackCreateOrder
     */
    public boolean getIsBackCreateOrder(){
        return isBackCreateOrder;
    }

    /**
     * 设置 是否是后台下单 *.
     *
     * @param isBackCreateOrder
     *            the isBackCreateOrder to set
     */
    public void setIsBackCreateOrder(boolean isBackCreateOrder){
        this.isBackCreateOrder = isBackCreateOrder;
    }

    /**
     * 获得 是否qs.
     *
     * @return the isQs
     */
    public boolean getIsQs(){
        return isQs;
    }

    /**
     * 设置 是否qs.
     *
     * @param isQs
     *            the isQs to set
     */
    public void setIsQs(boolean isQs){
        this.isQs = isQs;
    }

}
