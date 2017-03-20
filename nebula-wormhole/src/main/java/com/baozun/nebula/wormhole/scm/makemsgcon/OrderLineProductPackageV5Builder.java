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
package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.util.List;

import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.wormhole.mq.entity.order.ProductPackageV5;

/**
 * 订单行包装信息构造器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public interface OrderLineProductPackageV5Builder{

    /**
     * 构造包装信息.
     * 
     * @param orderLineCommand
     * @return 如果 <code>orderLineCommand</code> 没有包装信息,返回 null<br>
     */
    List<ProductPackageV5> buildProductPackageV5List(OrderLineCommand orderLineCommand);
}
